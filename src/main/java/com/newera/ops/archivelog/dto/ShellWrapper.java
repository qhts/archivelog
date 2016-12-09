package com.newera.ops.archivelog.dto;

/**
 * shell封装类
 * @author qiantao
 *
 */
public class ShellWrapper {

	/**执行状态**/
	private Integer stateCode;
	/**执行消息**/
	private String result;
	/**命令**/
	private Commond commond;
	/**下一步命令的结果**/
	private ShellWrapper nextWrapper;
	/**
	 * @return code
	 */
	public Integer getStateCode() {
		return stateCode;
	}
	/**
	 * @param stateCode stateCode
	 */
	public void setStateCode(Integer stateCode) {
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
	 * @return commond
	 */
	public Commond getCommond() {
		return commond;
	}
	/**
	 * @param commond commond
	 */
	public void setCommond(Commond commond) {
		this.commond = commond;
	}
	/**
	 * @return nextWrapper
	 */
	public ShellWrapper getNextWrapper() {
		return nextWrapper;
	}
	/**
	 * @param nextWrapper nextWrapper
	 */
	public void setNextWrapper(ShellWrapper nextWrapper) {
		this.nextWrapper = nextWrapper;
	}
	
}
