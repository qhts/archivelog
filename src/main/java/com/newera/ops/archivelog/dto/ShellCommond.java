package com.newera.ops.archivelog.dto;

/**
 * 命令封装类
 * @author qiantao
 *
 */
public class ShellCommond extends Commond{

	/**命令**/
	private String shell;
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
	
	
}
