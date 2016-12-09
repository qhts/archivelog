package com.newera.ops.archivelog.dto;

/**
 * 命令封装类
 * @author qiantao
 *
 */
public class ScpCommond extends Commond{
	/**远程地址**/
	private String remotePath;
	/**本地地址**/
	private String localPath;
	/**
	 * @return 远程地址
	 */
	public String getRemotePath() {
		return remotePath;
	}
	/**
	 * @param remotePath 远程地址
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	/**
	 * @return 本地地址
	 */
	public String getLocalPath() {
		return localPath;
	}
	/**
	 * @param localPath 本地地址
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
}
