package org.javaboy.vhr.mapper;

import org.javaboy.vhr.model.Role;

import java.util.List;

/**
 * Created by candy on 2020/10/25.
 */
public interface RoleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getAllRoles();
}
