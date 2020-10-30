package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.SalaryMapper;
import org.javaboy.vhr.model.Salary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
@Service
public class SalaryService {
    @Resource
    SalaryMapper salaryMapper;

    public List<Salary> getAllSalaries() {
        return salaryMapper.getAllSalaries();
    }

    public Integer addSalary(Salary salary) {
        salary.setCreateDate(new Date());
        return salaryMapper.insertSelective(salary);
    }

    public Integer deleteSalaryById(Integer id) {
        return salaryMapper.deleteByPrimaryKey(id);
    }

    public Integer updateSalaryById(Salary salary) {
        return salaryMapper.updateByPrimaryKeySelective(salary);
    }
}
