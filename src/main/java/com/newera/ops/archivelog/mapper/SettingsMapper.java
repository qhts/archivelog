package com.newera.ops.archivelog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.newera.ops.archivelog.domain.Settings;

/**
 * SettingsMapper
 * @author qiantao
 *
 */
@Mapper
public interface SettingsMapper {
	
	/**
	 * 全部Settings
	 * @return settings
	 */
	@Select("select * from settings")
	List<Settings> getAll();
	
	/**
	 * 添加Settings
	 * @param settings settings
	 */
	@Insert("insert into settings (logSourceDir,zipPrefix,modifyTime,storageDir,name) values(#{logSourceDir},#{zipPrefix},#{modifyTime},#{storageDir},#{name})")
	void addSettings(Settings settings);
	
	/**
	 * 修改Settings
	 * @param settings settings
	 * @return st
	 */
	@Update("update settings set logSourceDir=#{logSourceDir},zipPrefix=#{zipPrefix},modifyTime=#{modifyTime},storageDir=#{storageDir},name=#{name} where id=#{id}")
	int updateSettings(Settings settings);
	
	/**
	 * 删除Settings
	 * @param id id
	 * @return st
	 */
	@Update("delete from settings where id=#{id}")
	int deleteSettings(@Param(value = "id") Long id);
	
}
