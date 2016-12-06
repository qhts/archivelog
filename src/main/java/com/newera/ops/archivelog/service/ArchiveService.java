package com.newera.ops.archivelog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newera.ops.archivelog.domain.Server;
import com.newera.ops.archivelog.domain.Settings;
import com.newera.ops.archivelog.dto.Result;
import com.newera.ops.archivelog.dto.ScpCommond;
import com.newera.ops.archivelog.dto.ShellCommond;
import com.newera.ops.archivelog.util.ShellUtil;

/**
 * 
 * @author qiantao
 *
 */
@Service
public class ArchiveService {
	/**settingsService**/
	@Autowired
	private SettingsService settingsService;
	/**shellUtil**/
	@Autowired
	private ShellUtil shellUtil;
	/**serverService**/
	@Autowired
	private ServerService serverService;

	/**
	 * 压缩日志
	 * @throws Exception Exception
	 */
	public void archivejob() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<Settings> list = settingsService.getAll();
		//遍历
		for(Settings settings : list){
			String zipName = settings.getZipPrefix() + sdf.format(new Date())+".zip";
			List<String> params = new ArrayList<String>();
			params.add(settings.getLogSourceDir());
			params.add(zipName);
			params.add(settings.getModifyTime() + "");
			String shell = shellUtil.praseShellToCmd("archive.sh", params);
			
			List<ShellCommond> cmds = new ArrayList<ShellCommond>();
			//获取服务器列表
			List<Server> servers = serverService.getServersBySettings(settings.getId());
			for(Server server : servers){
				ShellCommond cmd = new ShellCommond();
				cmd.setIP(server.getIP());
				cmd.setSshPort(22);
				cmd.setShell(shell);
				cmds.add(cmd);
			}
			//执行压缩命令
			List<Result> rs = shellUtil.batchExecShell(cmds, true);
			//对压缩成功的机器执行拉取命令并删除文件
			for(int i=0;i<rs.size();i++){
				if(rs.get(i).getStateCode()==0){
					ScpCommond scp = new ScpCommond();
					scp.setIP(rs.get(i).getShellCommond().getIP());
					scp.setSshPort(22);
					scp.setLocalPath(settings.getStorageDir());
					scp.setRemotePath(settings.getLogSourceDir() + "/" + zipName);
					shellUtil.exeScpGet(scp);
					
					ShellCommond cmd = new ShellCommond();
					cmd.setIP(rs.get(i).getShellCommond().getIP());
					cmd.setSshPort(22);
					cmd.setShell("rm -rf " + settings.getLogSourceDir() + "/" + zipName);
				}
			}
			
		}
	}

}
