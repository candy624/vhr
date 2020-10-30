package org.javaboy.vhr.mapper;

import org.javaboy.vhr.model.Salary;

import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
public interface SalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Salary record);

    int insertSelective(Salary record);

    Salary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Salary record);

    int updateByPrimaryKey(Salary record);

    List<Salary> getAllSalaries();
}
