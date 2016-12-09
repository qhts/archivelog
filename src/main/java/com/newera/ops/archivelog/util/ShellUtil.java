package com.newera.ops.archivelog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.newera.ops.archivelog.dto.ScpCommond;
import com.newera.ops.archivelog.dto.ShellCommond;
import com.newera.ops.archivelog.dto.ShellWrapper;


/**
 * ssh操作工具类
 */
@Component
public final class ShellUtil {
	
	/**
	 * log
	 */
	Logger logger = Logger.getLogger(ShellUtil.class);
	
	/**
	 * shell执行池
	 */
	ExecutorService pool = Executors.newCachedThreadPool();
	
	/**
	 * 配置 
	 */
	@Autowired
	private Config config;
	
	/**
	 * 禁止实例化
	 */
	private ShellUtil(){}
	/**
	 * 执行一条shell命令 
	 * @param wrapper shell命令
	 * @throws Exception 异常
	 */
	public void execWrapper(ShellWrapper wrapper) throws Exception{
		if(wrapper.getCommond() instanceof ShellCommond){
			logger.info("执行机器："+wrapper.getCommond().getIP()+",同步执行脚本："+((ShellCommond)wrapper.getCommond()).getShell());
			Connection conn = null;
			Session sess = null;
			BufferedReader br = null;
			BufferedReader ebr = null;
			StringBuffer sb = new StringBuffer();
			Integer sig = null;
			try{
				conn = getOpenedConnection(wrapper.getCommond().getIP(), wrapper.getCommond().getSshPort());
				sess = conn.openSession();
				sess.execCommand(((ShellCommond)wrapper.getCommond()).getShell());
				br = new BufferedReader(new InputStreamReader(new StreamGobbler(sess.getStdout()),"utf8"));
				ebr = new BufferedReader(new InputStreamReader(new StreamGobbler(sess.getStderr()),"utf8"));
				while((sig=sess.getExitStatus()) == null){
					loadFromStream(br, sb);
					loadFromStream(ebr, sb);
					sig = sess.getExitStatus();
					Thread.sleep(1);
				}
				loadFromStream(br, sb);
				loadFromStream(ebr, sb);
			}finally{
				if(sess!=null){
					sess.close();
				}
				if(conn!=null){
					conn.close();
				}
				if(br != null){
					br.close();
				}
				if(ebr != null){
					ebr.close();
				}
				wrapper.setResult(sb.toString());
				wrapper.setStateCode(sig);
			}
		}else if(wrapper.getCommond() instanceof ScpCommond){
			logger.info("远端机器：" + wrapper.getCommond().getIP() + ",从远端拉取【" + ((ScpCommond)wrapper.getCommond()).getRemotePath() + 
					"】到【" + ((ScpCommond)wrapper.getCommond()).getLocalPath() + "】");
			Connection conn = null;
			SCPClient scpClient = null;
			try{
				conn = getOpenedConnection(wrapper.getCommond().getIP(), wrapper.getCommond().getSshPort());
				scpClient = conn.createSCPClient();
				File f = new File(((ScpCommond)wrapper.getCommond()).getLocalPath());
				if(!f.exists()){
					f.mkdirs();
				}
				scpClient.get(((ScpCommond)wrapper.getCommond()).getRemotePath(), ((ScpCommond)wrapper.getCommond()).getLocalPath());
				wrapper.setResult("文件下载成功");
				wrapper.setStateCode(0);
			}catch(Exception e){
				wrapper.setResult(e.toString());
				wrapper.setStateCode(1);
			}finally{
				if(conn!=null){
					conn.close();
				}
			}
		}
		
	}
	
	/**
	 * 执行多条shell命令
	 * @param wrappers wrappers
	 * @param parallel 是否并行
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	public void batchExecShell(List<ShellWrapper> wrappers,Boolean parallel) throws Exception{
		if(wrappers!=null && wrappers.size()>0){
			if(parallel){
				Map<String,Connection> cons = new HashMap<String,Connection>();
				Future<ShellWrapper>[] fs = new Future[wrappers.size()];
				try{
					//根据IP建立连接
					for(int i=0;i<wrappers.size();i++){
						if(cons.get(wrappers.get(i).getCommond().getIP())==null){
							Connection con = getOpenedConnection(wrappers.get(i).getCommond().getIP(), wrappers.get(i).getCommond().getSshPort());
							cons.put(wrappers.get(i).getCommond().getIP(), con);
						}
						fs[i] = pool.submit(new ExecShellThread(cons.get(wrappers.get(i).getCommond().getIP()),wrappers.get(i)));
					}
					for(int i=0;i<wrappers.size();i++){
						try {
							fs[i].get();
						} catch (Exception e) {
							wrappers.get(i).setResult(e.toString());
							wrappers.get(i).setStateCode(1);
							e.printStackTrace();
						}
					}
				}finally{
					for(String key:cons.keySet()){
						Connection c = cons.get(key);
						if(c!=null){
							c.close();
						}
					}
				}
			}else{
				for(ShellWrapper s:wrappers){
					execWrapper(s);
				}
			}
		}
	}
	
	 /**
	   * 获得ssh连接，最大重试10次
	   * @param host host
	   * @param port port
	   * @return con 
	   * @throws Exception 异常
	   */
	private Connection getOpenedConnection(String host, int port) throws Exception  {
		Connection conn = new Connection(host);
		ConnectionInfo info = null;
		int c = 0;
		while(info == null && c<10){
			try {
				c++;
				info = conn.connect();
			} catch (IOException e) {
				if(e.getMessage().contains("There was a problem while connecting to")){
					Thread.sleep(1000);
				}else{
					throw e;
				}
			} 
		}
		boolean isAuthenticated = conn.authenticateWithPassword(config.getUsername(), config.getPassword());
		if (!isAuthenticated){
			throw new IOException("Authentication failed.");
		}
		return conn;
	}
	
	/**
	 * 读取流中数据
	 * @param br br
	 * @param sb sb
	 * @throws Exception 异常
	 */
	private void loadFromStream(BufferedReader br,StringBuffer sb) throws Exception{
		int b;
		char[] c = new char[4096];
		while((b=br.read(c))!=-1){
			sb.append(c,0,b);
			logger.debug(new String(c,0,b));
		}
	}
	
	/**
	 * 将shell的参数注入生成一条长命令,注意''内的变量不替换
	 * @param shellName shellName
	 * @param params params
	 * @return 长命令
	 * @throws Exception 
	 */
	public String praseShellToCmd(String shellName,List<String> params) throws Exception{
		BufferedReader r = null;
		StringBuffer sb = new StringBuffer();
		String cmd = "";
		try{
			InputStream in = ShellUtil.class.getClassLoader().getResourceAsStream("shell/"+shellName);
			r = new BufferedReader(new InputStreamReader(in,"utf-8"));
			String s = null;
			while((s = r.readLine())!=null){
				if(!s.trim().startsWith("#") && !"".trim().equals(s)){
					sb.append(s);
					sb.append("\n");
				}
			}
			String shell = sb.toString();
			//对单引号就行分割，下标为双数的就行特殊字符替换
			String[] sps = shell.split("'");
			for(int j=0;j<sps.length;j++){
				if(params!=null){
					if(j%2==0){
						for(int i=0;i<params.size();i++){
							//替换参数
							sps[j] = sps[j].replace("$"+(i+1), params.get(i));
							//替换$#
							sps[j] = sps[j].replace("$#", params.size()+"");
						}
					}
					cmd += sps[j];
				}
			}
		}finally{
			if(r != null){
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return cmd;
	}
	
}
/**
 * 异步远程命令执行类
 * @author qiantao
 *
 */
class ExecShellThread implements Callable<ShellWrapper> {
	/**log**/
	Logger logger = Logger.getLogger(ExecShellThread.class);
	/**connection**/
	private Connection connection;
	/**wrapper**/
	private ShellWrapper wrapper;
	/**
	 * @param con 连接
	 * @param w 命令
	 */
	ExecShellThread(Connection con,ShellWrapper w){
		this.connection = con;
		this.wrapper = w;
	}
	@Override
	public ShellWrapper call() throws Exception {
		if(wrapper.getCommond() instanceof ShellCommond){
			logger.info("执行机器："+wrapper.getCommond().getIP()+",异步执行脚本："+((ShellCommond)wrapper.getCommond()).getShell());
			Session sess = null;
			BufferedReader br = null;
			BufferedReader ebr = null;
			StringBuffer sb = new StringBuffer();
			Integer sig = null;
			try{
				sess = connection.openSession();
				sess.execCommand(((ShellCommond)wrapper.getCommond()).getShell());
				br = new BufferedReader(new InputStreamReader(new StreamGobbler(sess.getStdout()),"utf8"));
				ebr = new BufferedReader(new InputStreamReader(new StreamGobbler(sess.getStderr()),"utf8"));
				while((sig=sess.getExitStatus()) == null){
					loadFromStream(br, sb);
					loadFromStream(ebr, sb);
					sig = sess.getExitStatus();
					Thread.sleep(1);
				}
				loadFromStream(br, sb);
				loadFromStream(ebr, sb);
			}finally{
				if(sess!=null){
					sess.close();
				}
				if(br != null){
					br.close();
				}
				if(ebr != null){
					ebr.close();
				}
				wrapper.setResult(sb.toString());
				wrapper.setStateCode(sig);
			}
		}else if(wrapper.getCommond() instanceof ScpCommond){
			logger.info("远端机器：" + wrapper.getCommond().getIP() + ",从远端拉取【" + ((ScpCommond)wrapper.getCommond()).getRemotePath() + 
					"】到【" + ((ScpCommond)wrapper.getCommond()).getLocalPath() + "】");
			SCPClient scpClient = null;
			try{
				scpClient = connection.createSCPClient();
				File f = new File(((ScpCommond)wrapper.getCommond()).getLocalPath());
				if(!f.exists()){
					f.mkdirs();
				}
				scpClient.get(((ScpCommond)wrapper.getCommond()).getRemotePath(), ((ScpCommond)wrapper.getCommond()).getLocalPath());
				wrapper.setResult("文件下载成功");
				wrapper.setStateCode(0);
			}catch(Exception e){
				wrapper.setResult(e.toString());
				wrapper.setStateCode(1);
			}
		}
		return wrapper;
	}
	
	/**
	 * 读取流中数据
	 * @param br br
	 * @param sb sb
	 * @throws Exception 异常
	 */
	private void loadFromStream(BufferedReader br,StringBuffer sb) throws Exception{
		int b;
		char[] c = new char[4096];
		while((b=br.read(c))!=-1){
			sb.append(c,0,b);
			logger.debug(new String(c,0,b));
		}
	}
}