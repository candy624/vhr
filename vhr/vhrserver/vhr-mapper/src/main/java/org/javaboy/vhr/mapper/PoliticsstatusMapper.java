package org.javaboy.vhr.mapper;

import org.javaboy.vhr.model.Politicsstatus;

import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
public interface PoliticsstatusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Politicsstatus record);

    int insertSelective(Politicsstatus record);

    Politicsstatus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Politicsstatus record);

    int updateByPrimaryKey(Politicsstatus record);

    List<Politicsstatus> getAllPoliticsstatus();
}
