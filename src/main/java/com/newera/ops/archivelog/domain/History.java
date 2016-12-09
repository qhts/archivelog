package com.newera.ops.archivelog.domain;

import java.util.Date;

/**
 * 
 * @author qiantao
 *
 */
public class History {
	/**id**/
	private Long id;
	/****/
	private Date beginTime;
	/****/
	private Date endTime;
	/****/
	private String details;
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
	 * @return beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime beginTime
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return details
	 */
	public String getDetails() {
		return details;
	}
	/**
	 * @param details details
	 */
	public void setDetails(String details) {
		this.details = details;
	}
	
}
