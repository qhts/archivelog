package com.newera.ops.archivelog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * historyr页面
 * @author qiantao
 *
 */
@Controller
@RequestMapping("/history")
public class HistoryController {

	/**  
     * 列出所有服务器.  
     * @param model model
     * @return html
     */  
    @RequestMapping("/list")  
    public String listServers(Model model){  
    	return "history";  
    }  
}
