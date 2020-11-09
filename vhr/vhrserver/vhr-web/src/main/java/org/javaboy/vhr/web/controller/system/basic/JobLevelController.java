package org.javaboy.vhr.web.controller.system.basic;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.enums.BusinessType;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.service.JobLevelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/11/1.
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JobLevelController {
    @Resource
    JobLevelService jobLevelService;

    @GetMapping("/")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @PostMapping("/")
    @Log(title = "职称管理", businessType = BusinessType.INSERT)
    public RespBean addJobLevel(@RequestBody JobLevel jobLevel) {
        if (jobLevelService.addJobLevel(jobLevel) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    @Log(title = "职称管理", businessType = BusinessType.UPDATE)
    public RespBean updateJobLevelById(@RequestBody JobLevel jobLevel) {
        if (jobLevelService.updateJobLevelById(jobLevel) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    @Log(title = "职称管理", businessType = BusinessType.DELETE)
    public RespBean deleteJobLevelById(@PathVariable Integer id) {
        if (jobLevelService.deleteJobLevelById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @DeleteMapping("/")
    @Log(title = "职称管理", businessType = BusinessType.DELETE)
    public RespBean deleteJobLevelByIds(Integer[] ids) {
        if (jobLevelService.deleteJobLevelsByIds(ids) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
