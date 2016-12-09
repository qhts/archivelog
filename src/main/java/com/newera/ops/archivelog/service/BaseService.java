package com.newera.ops.archivelog.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.newera.ops.archivelog.dto.ShellWrapper;
import com.newera.ops.archivelog.util.ShellUtil;

/**
 * 
 * @author qiantao
 *
 */
public class BaseService {
	/**shellUtil**/
	@Autowired
	protected ShellUtil shellUtil;
	/**
	 * log
	 */
	Logger logger = Logger.getLogger(BaseService.class);
	
	/**
	 * 
	 * @param shellWrappers shellWrappers
	 * @return result
	 * @throws Exception Exception
	 */
	public String execWrappersAndGetResult(List<ShellWrapper> shellWrappers) throws Exception{
		StringBuffer sb = new StringBuffer();
		execWrappers(shellWrappers);
		for(int i=0;i<shellWrappers.size();i++){
			praseWrapper(shellWrappers.get(i),sb);
		}
		return sb.toString();
	}
	
	/**
	 * 递归输出日志
	 * @param shellWrapper shellWrapper
	 * @param sb sb
	 */
	private void praseWrapper(ShellWrapper shellWrapper, StringBuffer sb) {
		if(shellWrapper.getStateCode()!=null){
			if(shellWrapper.getStateCode()==0){
				sb.append("IP【"+shellWrapper.getCommond().getIP()+"】 ")
				.append(shellWrapper.getCommond().getCommondName()+"操作【成功】\r\n");
			}else{
				sb.append("IP【"+shellWrapper.getCommond().getIP()+"】 ")
				.append(shellWrapper.getCommond().getCommondName()+"操作【失败】 错误日志【\r\n")
				.append(shellWrapper.getResult()+"\r\n")
				.append("】\r\n");
			}
			if(shellWrapper.getNextWrapper()!=null){
				praseWrapper(shellWrapper.getNextWrapper(), sb);
			}else{
				sb.append("\r\n");
			}
		}
	}

	/**
	 * 递归执行
	 * @param shellWrappers shellWrappers
	 * @throws Exception Exception
	 */
	private void execWrappers(List<ShellWrapper> shellWrappers) throws Exception{
		shellUtil.batchExecShell(shellWrappers, true);
		List<ShellWrapper> nextWrapper = new ArrayList<ShellWrapper>();
		for(int i=0;i<shellWrappers.size();i++){
			if(shellWrappers.get(i).getStateCode()==0 && shellWrappers.get(i).getNextWrapper()!=null){
				nextWrapper.add(shellWrappers.get(i).getNextWrapper());
			}
		}
		if(nextWrapper.size()>0){
			execWrappers(nextWrapper);
		}
	}
}
