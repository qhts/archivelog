package com.newera.ops.archivelog.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.newera.ops.archivelog.dto.Result;
import com.newera.ops.archivelog.dto.ScpCommond;
import com.newera.ops.archivelog.dto.ShellCommond;


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
	 * @param commond shell命令
	 * @return 结果
	 * @throws Exception 异常
	 */
	public Result execshell(ShellCommond commond) throws Exception{
		logger.info("执行机器："+commond.getIP()+",同步执行脚本："+commond.getShell());
		Connection conn = null;
		Session sess = null;
		BufferedReader br = null;
		BufferedReader ebr = null;
		Result r = new Result();
		StringBuffer sb = new StringBuffer();
		Integer sig = null;
		try{
			conn = getOpenedConnection(commond.getIP(), commond.getSshPort());
			sess = conn.openSession();
			sess.execCommand(commond.getShell());
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
			 r.setResult(sb.toString());
			 r.setStateCode(sig);
		}
		return r;
		
	}
	
	/**
	 * 执行多条shell命令
	 * @param commonds shell命令
	 * @param parallel 是否并行
	 * @return 结果
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	public List<Result> batchExecShell(List<ShellCommond> commonds,Boolean parallel) throws Exception{
		List<Result> rs = new ArrayList<Result>();
		if(commonds!=null && commonds.size()>0){
			if(parallel){
				Map<String,Connection> cons = new HashMap<String,Connection>();
				Future<Result>[] fs = new Future[commonds.size()];
				try{
					//根据IP建立连接
					for(int i=0;i<commonds.size();i++){
						if(cons.get(commonds.get(i).getIP())==null){
							Connection con = getOpenedConnection(commonds.get(i).getIP(), commonds.get(i).getSshPort());
							cons.put(commonds.get(i).getIP(), con);
						}
						logger.info("执行机器："+commonds.get(i).getIP()+",异步执行脚本："+commonds.get(i).getShell());
						fs[i] = pool.submit(new ExecShellThread(cons.get(commonds.get(i).getIP()),commonds.get(i)));
					}
					for(int i=0;i<commonds.size();i++){
						try {
							Result r = fs[i].get();
							rs.add(i, r);
						} catch (Exception e) {
							Result r = new Result();
							r.setShellCommond(commonds.get(i));
							r.setStateCode(1);
							r.setResult(e.toString());
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
				for(ShellCommond s:commonds){
					rs.add(execshell(s));
				}
			}
		}
		return rs;
	}
	
	/**
	 * scp命令
	 * @param scp scp命令
	 * @throws Exception 异常
	 */
	public void exeScpPut(ScpCommond scp) throws Exception{
		String lp = "";
		for(int i = 0;i<scp.getLocalPath().length;i++){
			lp += scp.getLocalPath()[i]+",";
		}
		logger.info("远端机器："+scp.getIP()+",复制文件【"+lp+"】到【"+scp.getRemotePath()[0]+"】");
		Connection conn = null;
		SCPClient scpClient = null;
		try{
			conn = getOpenedConnection(scp.getIP(), scp.getSshPort());
			scpClient = conn.createSCPClient();
			scpClient.put(scp.getLocalPath(), scp.getRemotePath()[0]);
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	/**
	 * 执行远程下载命令
	 * @param scp scp命令
	 * @throws Exception 异常
	 */
	public void exeScpGet(ScpCommond scp) throws Exception{
		String rp = "";
		for(int i = 0;i<scp.getRemotePath().length;i++){
			rp += scp.getRemotePath()[i]+",";
		}
		logger.info("远端机器："+scp.getIP()+",从远端拉取【"+rp+"】到【"+scp.getLocalPath()[0]+"】");
		Connection conn = null;
		SCPClient scpClient = null;
		try{
			conn = getOpenedConnection(scp.getIP(), scp.getSshPort());
			scpClient = conn.createSCPClient();
			scpClient.get(scp.getRemotePath(), scp.getLocalPath()[0]);
		}finally{
			if(conn!=null){
				conn.close();
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
					sb.append(System.getProperty("line.separator"));
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
class ExecShellThread implements Callable<Result> {
	/**log**/
	Logger logger = Logger.getLogger(ExecShellThread.class);
	/**connection**/
	private Connection connection;
	/**commond**/
	private ShellCommond commond;
	/**
	 * @param con 连接
	 * @param cmd 命令
	 */
	ExecShellThread(Connection con,ShellCommond cmd){
		this.connection = con;
		this.commond = cmd;
	}
	@Override
	public Result call() throws Exception {
		Session sess = null;
		BufferedReader br = null;
		BufferedReader ebr = null;
		Result r = new Result();
		StringBuffer sb = new StringBuffer();
		Integer sig = null;
		try{
			sess = connection.openSession();
			sess.execCommand(commond.getShell());
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
			 r.setResult(sb.toString());
			 r.setStateCode(sig);
			 r.setShellCommond(commond);
		}
		return r;
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