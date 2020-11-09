package org.javaboy.vhr.web.controller.system.basic;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.enums.BusinessType;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.Position;
import org.javaboy.vhr.service.PositionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/11/1.
 */
@RestController
@RequestMapping("/system/basic/position")
public class PositionController {
    @Resource
    PositionService positionService;

    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    @Log(title = "政治面貌管理", businessType = BusinessType.INSERT)
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.addPosition(position) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    @Log(title = "政治面貌管理", businessType = BusinessType.UPDATE)
    public RespBean updatePositions(@RequestBody Position position) {
        if (positionService.updatePositions(position) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    @Log(title = "政治面貌管理", businessType = BusinessType.DELETE)
    public RespBean deletePositionById(@PathVariable Integer id) {
        if (positionService.deletePositionById(id) == 1) {
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败!");
    }

    @DeleteMapping("/")
    @Log(title = "政治面貌管理", businessType = BusinessType.DELETE)
    public RespBean deletePositionByIds(Integer[] ids) {
        if (positionService.deletePositionsByIds(ids) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
