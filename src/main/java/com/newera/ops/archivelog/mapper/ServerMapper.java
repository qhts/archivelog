package com.newera.ops.archivelog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.newera.ops.archivelog.domain.Server;
import com.newera.ops.archivelog.dto.ServerDto;

/**
 * servermapper
 * @author qiantao
 *
 */
@Mapper
public interface ServerMapper {
	
	/**
	 * 全部server
	 * @return server
	 */
	@Select("select * from server s left join settings ss on s.settingsId=ss.id")
	List<ServerDto> getAll();
	
	/**
	 * 添加server
	 * @param server server
	 */
	@Insert("insert into server (IP,remark,settingsId) values(#{IP},#{remark},#{settingsId})")
	void addServer(Server server);
	
	/**
	 * 修改server
	 * @param server server
	 * @return st
	 */
	@Update("update server set IP=#{IP},remark=#{remark},settingsId=#{settingsId} where id=#{id}")
	int updateServer(Server server);
	
	/**
	 * 删除server
	 * @param id id
	 * @return st
	 */
	@Update("delete from server where id=#{id}")
	int deleteServer(@Param(value = "id") Long id);
	
	/**
	 * ip获得server
	 * @param id id
	 * @return st
	 */
	@Update("select * from server where id=#{id}")
	Server getServerById(@Param(value = "id") Long id);

	/**
	 * 获得setting下的server
	 * @param id id
	 * @return servers
	 */
	@Select("select * from server where settingsId=#{settingsId}")
	List<Server> getServersBySettings(@Param(value = "settingsId") Long id);
}
