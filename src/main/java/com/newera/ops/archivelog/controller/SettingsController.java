package com.newera.ops.archivelog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.newera.ops.archivelog.domain.Settings;
import com.newera.ops.archivelog.service.SettingsService;

/**
 * server页面
 * @author qiantao
 *
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {
	/**service**/
	@Autowired
	private SettingsService settingsService;

	/**  
     * 列出所有服务器.  
     * @param model model
     * @return html
     */  
    @RequestMapping("/list")  
    public String listSettings(Model model){  
    	model.addAttribute("settings",settingsService.getAll());
    	return "settings";  
    }  
    
    
	/**  
     * 修改配置.  
     * @param settings settings
     * @return html
     */  
    @RequestMapping(value = "/update",method = RequestMethod.POST)  
    public String updateSettings(Settings settings){  
    	settingsService.update(settings);
    	return "redirect:/settings/list";  
    }  
    
    /**  
     * 添加配置.  
     * @param settings settings
     * @return html
     */  
    @RequestMapping(value = "/add",method = RequestMethod.POST)  
    public String addSettings(Settings settings){  
    	settingsService.add(settings);
    	return "redirect:/settings/list";  
    }  
    
	/**  
     * 删除配置.  
     * @param id id
     * @return html
     */  
    @RequestMapping("/{id}/delete")  
    public String deleteSteeings(@PathVariable long id){  
    	settingsService.delete(id);
    	return "redirect:/settings/list";  
    }  
    
}
