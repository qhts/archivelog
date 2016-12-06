package com.newera.ops.archivelog.dto;

/**
 * 命令封装类
 * @author qiantao
 *
 */
public class ShellCommond {

	/**命令**/
	private String shell;
	/**执行IP**/
	private String IP;
	/**ssh端口**/
	private int sshPort = 22;
	/**
	 * getshell
	 * @return shell
	 */
	public String getShell() {
		return shell;
	}
	/**
	 * 设置shell
	 * @param shell shell
	 */
	public void setShell(String shell) {
		this.shell = shell;
	}
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
	
	
}
