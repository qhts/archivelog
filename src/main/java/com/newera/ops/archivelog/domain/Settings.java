package com.newera.ops.archivelog.domain;


/**
 * 配置
 * @author qiantao
 *
 */
public class Settings {
	/**settingId**/
	private Long id;
	/**name**/
	private String name;
	/**日志源目录**/
	private String logSourceDir;
	/**压缩后文件名**/
	private String zipPrefix;
	/**需要归档的文件距现在的天数**/
	private int modifyTime = 7;
	/**本地保存的地址**/
	private String storageDir;
	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return logSourceDir
	 */
	public String getLogSourceDir() {
		return logSourceDir;
	}
	/**
	 * @param logSourceDir logSourceDir
	 */
	public void setLogSourceDir(String logSourceDir) {
		this.logSourceDir = logSourceDir;
	}
	/**
	 * @return zipPrefix
	 */
	public String getZipPrefix() {
		return zipPrefix;
	}
	/**
	 * @param zipPrefix zipPrefix
	 */
	public void setZipPrefix(String zipPrefix) {
		this.zipPrefix = zipPrefix;
	}
	/**
	 * @return fileRegex
	 */
	public int getModifyTime() {
		return modifyTime;
	}
	/**
	 * @param modifyTime modifyTime
	 */
	public void setModifyTime(int modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * @return storageDir
	 */
	public String getStorageDir() {
		return storageDir;
	}
	/**
	 * @param storageDir storageDir
	 */
	public void setStorageDir(String storageDir) {
		this.storageDir = storageDir;
	}
	
}
