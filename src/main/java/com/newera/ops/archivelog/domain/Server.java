package com.newera.ops.archivelog.domain;

/**
 * 服务器
 * @author qiantao
 *
 */
public class Server {
	/**id**/
	private Long id;
	/**IP**/
	private String IP;
	/**remark**/
	private String remark;
	/**settingsId**/
	private Long settingsId;
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
	 * @return IP
	 */
	public String getIP() {
		return IP;
	}
	/**
	 * @param iP ip
	 */
	public void setIP(String iP) {
		IP = iP;
	}
	/**
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}
	/** 
	 * @param remark remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return settingsId
	 */
	public Long getSettingsId() {
		return settingsId;
	}
	/**
	 * @param settingsId settingsId
	 */
	public void setSettingsId(Long settingsId) {
		this.settingsId = settingsId;
	}

	
}
