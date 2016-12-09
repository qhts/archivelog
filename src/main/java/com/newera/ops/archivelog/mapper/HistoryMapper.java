package com.newera.ops.archivelog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.newera.ops.archivelog.domain.History;

/**
 * @author qiantao
 *
 */
@Mapper
public interface HistoryMapper {

	/**
	 * 全部history
	 * @return history
	 */
	@Select("select * from history order by beginTime desc limit 10")
	List<History> getAll();
	
	/**
	 * 保存history
	 * @param history hstory
	 */
	@Insert("insert into history (beginTime,endTime,details) values (#{beginTime},#{endTime},#{details})")
	void save(History history);
	
}
