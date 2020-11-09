package org.javaboy.vhr.web.controller.system;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.service.RoleService;
import org.javaboy.vhr.common.enums.BusinessType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/11/1.
 */
@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Resource
    HrService hrService;
    @Resource
    RoleService roleService;

    @GetMapping("/")
    @Log(title = "人事管理", businessType = BusinessType.OTHER)
    public List<Hr> getAllHrs(String keywords) {
        return hrService.getAllHrs(keywords);
    }

    @PutMapping("/")
    @Log(title = "修改人事信息")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/roles")
    @Log(title = "查询所有角色")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    @Log(title = "修改人事角色权限")
    public RespBean updateHrRole(Integer hrid, Integer[] rids) {
        if (hrService.updateHrRole(hrid, rids)){
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    @Log(title = "删除人事信息")
    public RespBean deleteHrById(@PathVariable Integer id) {
        if (hrService.deleteHrById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
