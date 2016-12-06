package com.newera.ops.archivelog.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置类
 * @author qiantao
 *
 */
@ConfigurationProperties(prefix="archivelog")
public class Config {
	/**用户名**/
	private String username;
	/**密码**/
	private String password;
	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
