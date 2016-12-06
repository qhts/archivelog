package com.newera.ops.archivelog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newera.ops.archivelog.domain.Server;
import com.newera.ops.archivelog.dto.ServerDto;
import com.newera.ops.archivelog.mapper.ServerMapper;

/**
 * 
 * @author qiantao
 *
 */
@Service
public class ServerService {
	
	/**mapper**/
	@Autowired
	private ServerMapper mapper;
	
	/**
	 * 
	 * @return servers
	 */
	public List<ServerDto> getAll() {
		return mapper.getAll();
	}

	/**
	 * 删除
	 * @param id id
	 */
	public void delete(long id) {
		mapper.deleteServer(id);
	}

	/**
	 * 添加
	 * @param server server
	 */
	public void add(Server server) {
		mapper.addServer(server);
	}

	/**
	 * 更新
	 * @param server server
	 */
	public void update(Server server) {
		mapper.updateServer(server);
	}

	/**
	 * 获得setting下的server
	 * @param id id
	 * @return servers
	 */
	public List<Server> getServersBySettings(Long id) {
		return mapper.getServersBySettings(id);
	}

}
