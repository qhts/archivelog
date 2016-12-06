package com.newera.ops.archivelog.dto;

/**
 * 命令封装类
 * @author qiantao
 *
 */
public class ScpCommond {
	/**远程地址**/
	private String[] remotePath;
	/**本地地址**/
	private String[] localPath;
	/**执行IP**/
	private String IP;
	/**ssh端口**/
	private int sshPort = 22;
	/**
	 * @return 远程地址
	 */
	public String[] getRemotePath() {
		return remotePath;
	}
	/**
	 * @param remotePath 远程地址
	 */
	public void setRemotePath(String... remotePath) {
		this.remotePath = remotePath;
	}
	/**
	 * @return 本地地址
	 */
	public String[] getLocalPath() {
		return localPath;
	}
	/**
	 * @param localPath 本地地址
	 */
	public void setLocalPath(String... localPath) {
		this.localPath = localPath;
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
