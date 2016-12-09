package com.newera.ops.archivelog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newera.ops.archivelog.service.ArchiveService;
import com.newera.ops.archivelog.service.HistoryService;

/**
 * historyr页面
 * @author qiantao
 *
 */
@Controller
@RequestMapping("/history")
public class HistoryController {
	/****/
	@Autowired
	private HistoryService historyService;
	/****/
	@Autowired
	private ArchiveService archiveService;

	/**  
     * 列出所有服务器.  
     * @param model model
     * @return html
     */  
    @RequestMapping("/list")  
    public String listServers(Model model){  
    	model.addAttribute("historys", historyService.getAll());
    	return "history";  
    }  
    
	/**  
     * 执行归档 
     * @param model model
     * @return html
	 * @throws Exception 
     */  
    @RequestMapping("/archiveNow")  
    public String archiveNow(Model model) throws Exception{  
    	archiveService.archivejob();
    	return "redirect:/history/list";  
    }  
}
