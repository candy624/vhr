package org.javaboy.vhr.web.controller.system.basic;

import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.enums.BusinessType;
import org.javaboy.vhr.common.lang.RespBean;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.OpLogService;
import org.javaboy.vhr.utils.ExcelUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2020/11/2.
 */
@RestController
@RequestMapping("/system/basic/syslog")
public class SysLogController {
    @Resource
    OpLogService opLogService;

    @GetMapping("/")
    public RespPageBean getAllSysLog(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     OpLog opLog,
                                     Date[] beginDateScope) {
        return opLogService.getOpLogByPage(page, size, opLog, beginDateScope);
    }

    @GetMapping("/export")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    public ResponseEntity<byte[]> export(OpLog opLog) {
        List<OpLog> logs = (List<OpLog>) opLogService.getOpLogByPage(null, null, opLog, null).getData();
        ExcelUtils<OpLog> util = new ExcelUtils<>(OpLog.class);
        return util.exportExcel(logs, "操作日志");
    }

    @DeleteMapping("/{ids}")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    public RespBean remove(@PathVariable Integer[] ids) {
        if (opLogService.deleteOpLogByIds(ids) > 0) {
            return RespBean.ok("操作成功!");
        }
        return RespBean.error("操作失败!");
    }

    @DeleteMapping("/clean")
    public RespBean clean() {
        opLogService.cleanOpLog();
        return RespBean.ok("操作成功!");
    }
}
