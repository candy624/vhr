package org.javaboy.vhr.web.controller.emp;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.*;
import org.javaboy.vhr.service.*;
import org.javaboy.vhr.utils.POIUtils;
import org.javaboy.vhr.common.enums.BusinessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2020/10/29.
 */
@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {
    @Resource
    EmployeeService employeeService;
    @Resource
    NationService nationService;
    @Resource
    PoliticsstatusService politicsstatusService;
    @Resource
    JobLevelService jobLevelService;
    @Resource
    PositionService positionService;
    @Resource
    DepartmentService departmentService;

    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          Employee employee,
                                          Date[] beginDateScope) {
        return employeeService.getEmployeeByPage(page, size, employee, beginDateScope);
    }

    @PostMapping("/")
    @Log(title = "员工资料", businessType = BusinessType.INSERT)
    public RespBean addEmp(@RequestBody Employee employee) {
        if (employeeService.addEmp(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{id}")
    @Log(title = "员工资料", businessType = BusinessType.DELETE)
    public RespBean deleteEmpById(@PathVariable Integer id) {
        if (employeeService.deleteEmpByEid(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    @Log(title = "员工资料", businessType = BusinessType.UPDATE)
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateEmp(employee) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusService.getAllPoliticsstauts();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        return RespBean.build().setStatus(200).setObj(String.format("%08d", employeeService.maxWorkID() + 1));
    }

    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/export")
    @Log(title = "员工资料", businessType = BusinessType.EXPORT)
    public ResponseEntity<byte[]> exportData() {
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(), null).getData();
        return POIUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    @Log(title = "员工资料", businessType = BusinessType.IMPORT)
    public RespBean importData(MultipartFile file) {
        List<Employee> list = POIUtils.excel2Employee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstauts(), departmentService.getAllDepartments(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());
        if (employeeService.addEmps(list) == 1) {
            return RespBean.ok("上传成功!");
        }
        return RespBean.error("上传失败!");
    }


}
