package com.newera.ops.archivelog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newera.ops.archivelog.domain.Settings;
import com.newera.ops.archivelog.mapper.SettingsMapper;

/**
 * 
 * @author qiantao
 *
 */
@Service
public class SettingsService {
	
	/**mapper**/
	@Autowired
	private SettingsMapper mapper;
	
	/**
	 * 
	 * @return Settings
	 */
	public List<Settings> getAll() {
		return mapper.getAll();
	}

	/**
	 * 删除
	 * @param id id
	 */
	public void delete(long id) {
		mapper.deleteSettings(id);
	}

	/**
	 * 添加
	 * @param settings settings
	 */
	public void add(Settings settings) {
		mapper.addSettings(settings);
	}

	/**
	 * 更新
	 * @param settings settings
	 */
	public void update(Settings settings) {
		mapper.updateSettings(settings);
	}

}
