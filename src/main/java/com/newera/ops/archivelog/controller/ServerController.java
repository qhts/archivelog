package com.newera.ops.archivelog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.newera.ops.archivelog.domain.Server;
import com.newera.ops.archivelog.service.ServerService;
import com.newera.ops.archivelog.service.SettingsService;

/**
 * server页面
 * @author qiantao
 *
 */
@Controller
@RequestMapping("/server")
public class ServerController {
	/**service**/
	@Autowired
	private ServerService serverService;
	/**settingsService**/
	@Autowired
	private SettingsService settingsService;

	/**  
     * 列出所有服务器.  
     * @param model model
     * @return html
     */  
    @RequestMapping("/list")  
    public String listServers(Model model){  
    	model.addAttribute("servers",serverService.getAll());
    	model.addAttribute("set", settingsService.getAll());
    	return "server";  
    }  
    
	/**  
     * 添加服务器.  
     * @param server server
     * @return html
     */  
    @RequestMapping(value = "/add",method = RequestMethod.POST)  
    public String addServer(Server server){  
    	serverService.add(server);
    	return "redirect:/server/list";  
    }  
    
	/**  
     * 修改服务器.  
     * @param server server
     * @return html
     */  
    @RequestMapping(value = "/update",method = RequestMethod.POST)  
    public String updateServer(Server server){  
    	serverService.update(server);
    	return "redirect:/server/list";  
    }  
    
	/**  
     * 删除服务器.  
     * @param id id
     * @return html
     */  
    @RequestMapping("/{id}/delete")  
    public String deleteServer(@PathVariable long id){  
    	serverService.delete(id);
    	return "redirect:/server/list";  
    }  
    
    
}
