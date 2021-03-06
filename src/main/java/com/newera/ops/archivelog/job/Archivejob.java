package com.newera.ops.archivelog.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.newera.ops.archivelog.service.ArchiveService;

/**
 * 归档job
 * @author qiantao
 *
 */
@Component
public class Archivejob {
	
	/**service**/
	@Autowired
	private ArchiveService archiveService;

	/**
	 * @throws Exception  Exception
	 * 每周日凌晨一点执行一次
	 */
	@Scheduled(cron = "0 0 1 ? * 7 ")
    public void reportCurrentByCron() throws Exception{
		archiveService.archivejob();
    }
}
