package com.newera.ops.archivelog;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.newera.ops.archivelog.util.Config;

/**
 * 入口
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({Config.class})
@EnableScheduling
public class App  
{
	/**
	 * 主函数
	 * @param args 参数
	 * @throws Exception 
	 */
    public static void main( String[] args ) throws Exception 
    {
    	SpringApplication.run(App.class, args);
    }
    
	/**
	 * 
	 * 资源释放
	 *
	 */
	@PreDestroy
	public void destory() {
	}
}
