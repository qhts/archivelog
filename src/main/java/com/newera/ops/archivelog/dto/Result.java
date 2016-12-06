package com.newera.ops.archivelog.dto;

/**
 * shell执行结果封装类
 * @author qiantao
 *
 */
public class Result {

	/**执行状态**/
	private int stateCode;
	/**执行消息**/
	private String result;
	/**命令**/
	private ShellCommond shellCommond;
	/**
	 * @return code
	 */
	public int getStateCode() {
		return stateCode;
	}
	/**
	 * @param stateCode stateCode
	 */
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
	/**
	 * @return result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result result
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return shellCommond
	 */
	public ShellCommond getShellCommond() {
		return shellCommond;
	}
	/**
	 * @param shellCommond shellCommond
	 */
	public void setShellCommond(ShellCommond shellCommond) {
		this.shellCommond = shellCommond;
	}
	
	
}
