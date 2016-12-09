package com.newera.ops.archivelog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newera.ops.archivelog.domain.History;
import com.newera.ops.archivelog.domain.Server;
import com.newera.ops.archivelog.domain.Settings;
import com.newera.ops.archivelog.dto.ScpCommond;
import com.newera.ops.archivelog.dto.ShellCommond;
import com.newera.ops.archivelog.dto.ShellWrapper;
import com.newera.ops.archivelog.mapper.HistoryMapper;

/**
 * 
 * @author qiantao
 *
 */
@Service
public class ArchiveService extends BaseService{
	/**settingsService**/
	@Autowired
	private SettingsService settingsService;
	/**serverService**/
	@Autowired
	private ServerService serverService;
	/****/
	@Autowired
	private HistoryMapper mapper;
	/**
	 * 压缩日志
	 * @throws Exception Exception
	 */
	public void archivejob() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<Settings> list = settingsService.getAll();
		StringBuffer sb = new StringBuffer();
		History history = new History();
		history.setBeginTime(new Date());
		//遍历
		for(Settings settings : list){
			String zipName = settings.getZipPrefix() + sdf.format(new Date())+".zip";
			List<String> params = new ArrayList<String>();
			params.add(settings.getLogSourceDir());
			params.add(zipName);
			params.add(settings.getModifyTime() + "");
			String shell = shellUtil.praseShellToCmd("archive.sh", params);
			
			//获取服务器列表
			List<Server> servers = serverService.getServersBySettings(settings.getId());
			List<ShellWrapper> wrappers = new ArrayList<ShellWrapper>();
			if(servers==null || servers.size()==0){
				continue;
			}
			for(Server server : servers){
				//第一步操作，压缩
				ShellCommond cmd = new ShellCommond();
				cmd.setIP(server.getIP());
				cmd.setSshPort(22);
				cmd.setCommondName("压缩");
				cmd.setShell(shell);
				ShellWrapper first = new ShellWrapper();
				first.setCommond(cmd);
				//第二步操作，下载
				ScpCommond scp = new ScpCommond();
				scp.setIP(server.getIP());
				scp.setSshPort(22);
				scp.setCommondName("下载");
				scp.setLocalPath(settings.getStorageDir() + "/" + server.getIP());
				scp.setRemotePath(settings.getLogSourceDir() + "/" + zipName);
				ShellWrapper second = new ShellWrapper();
				second.setCommond(scp);
				first.setNextWrapper(second);
				//第三步操作，删除
				ShellCommond c = new ShellCommond();
				c.setIP(server.getIP());
				c.setSshPort(22);
				c.setCommondName("删除");
				c.setShell("rm -rf " + settings.getLogSourceDir() + "/" + zipName);
				ShellWrapper third = new ShellWrapper();
				third.setCommond(c);
				second.setNextWrapper(third);
				
				wrappers.add(first);
			}
			sb.append("*************************************************************************\r\n");
			sb.append("名称：【"+settings.getName()+"】\r\n");
			sb.append(execWrappersAndGetResult(wrappers));
		}
		history.setEndTime(new Date());
		history.setDetails(sb.toString().replace("\r\n", "<br>"));
		mapper.save(history);
		logger.info("日志归档结束，执行结果：\r\n" + sb.toString());
		
	}



}
