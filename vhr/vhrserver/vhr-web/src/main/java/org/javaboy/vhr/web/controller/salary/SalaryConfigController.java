package org.javaboy.vhr.web.controller.salary;

import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.model.Salary;
import org.javaboy.vhr.service.EmployeeService;
import org.javaboy.vhr.service.SalaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/10/31.
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalaryConfigController {
    @Resource
    EmployeeService employeeService;
    @Resource
    SalaryService salaryService;

    @GetMapping("/")
    public RespPageBean getEmployeeByPageWithSalary(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return employeeService.getEmployeeByPageWithSalary(page, size);
    }

    @GetMapping("/salaries")
    public List<Salary> getAllSalaries() {
        return salaryService.getAllSalaries();
    }

    @PutMapping("/")
    public RespBean updateEmployeeSalaryById(Integer eid, Integer sid) {
        Integer result = employeeService.updateEmployeeSalaryById(eid, sid);
        if (result == 1 || result == 2) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更细失败!");
    }
}
