package org.javaboy.vhr.web.controller.system.basic;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.enums.BusinessType;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/11/1.
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Resource
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartment() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    public RespBean addDep(@RequestBody Department department) {
        departmentService.addDep(department);
        if (department.getResult() == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @DeleteMapping("/{id}")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    public RespBean deleteDepById(@PathVariable Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentService.deleteDepById(department);
        if (department.getResult() == -2) {
            return RespBean.error("该部门下有子部门,删除失败!");
        } else if (department.getResult() == -1) {
            return RespBean.error("该部门下有员工,删除失败!");
        } else if (department.getResult() == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
