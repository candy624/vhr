package org.javaboy.vhr.mapper;

import org.javaboy.vhr.model.Nation;

import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
public interface NationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Nation record);

    int insertSelective(Nation record);

    Nation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Nation record);

    int updateByPrimaryKey(Nation record);

    List<Nation> getAllNations();
}
