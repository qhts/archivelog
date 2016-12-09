package com.newera.ops.archivelog.dto;

/**
 * 
 * @author qiantao
 *
 */
public class Commond {
	/**执行IP**/
	private String IP;
	/**ssh端口**/
	private int sshPort = 22;
	/**name**/
	private String commondName;
	/**
	 * getIP
	 * @return IP
	 */
	public String getIP() {
		return IP;
	}
	/**
	 * 设置IP
	 * @param iP IP
	 */
	public void setIP(String iP) {
		IP = iP;
	}
	/**
	 * getSshPort
	 * @return sshPort
	 */
	public int getSshPort() {
		return sshPort;
	}
	/**
	 * 设置sshPort
	 * @param sshPort sshPort
	 */
	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}
	/**
	 * @return commondName
	 */
	public String getCommondName() {
		return commondName;
	}
	/**
	 * @param commondName commondName
	 */
	public void setCommondName(String commondName) {
		this.commondName = commondName;
	}
	
}
