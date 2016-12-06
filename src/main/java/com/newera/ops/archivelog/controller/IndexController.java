package com.newera.ops.archivelog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页
 * @author qiantao
 *
 */
@Controller
public class IndexController {

	/**  
     * 返回html模板.  
     * @return html
     */  
    @RequestMapping("/")  
    public String helloHtml(){  
    	return "redirect:/server/list";  
    }  
}
