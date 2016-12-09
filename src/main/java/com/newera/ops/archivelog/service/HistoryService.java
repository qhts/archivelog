package com.newera.ops.archivelog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newera.ops.archivelog.domain.History;
import com.newera.ops.archivelog.mapper.HistoryMapper;

/**
 * 
 * @author qiantao
 *
 */
@Service
public class HistoryService {
	/****/
	@Autowired
	private HistoryMapper hsitoryMapper;

	/**
	 * @return list
	 */
	public List<History> getAll() {
		
		return hsitoryMapper.getAll();
	}

}
