package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.OpLogMapper;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2020/11/2.
 */
@Service
public class OpLogService {
    @Resource
    OpLogMapper opLogMapper;

    /**
     * 新增操作日志
     * @param opLog 操作日志对象
     */
    public void insertOpLog(OpLog opLog) {
        opLogMapper.insertOperlog(opLog);
    }

    /**
     * 查询系统操作日志结合
     * @param opLog 操作日志对象
     * @return 操作日志集合
     */
//    public List<OpLog> selectOpLogList(OpLog opLog) {
//        return opLogMapper.selectOperLogList(opLog);
//    }

    public RespPageBean getOpLogByPage(Integer page, Integer size, OpLog opLog, Date [] beginDateScope) {
        if (page != null && size != null) {
            page = (page -1) * size;
        }
        List<OpLog> opLogs = opLogMapper.selectOperLogList(page, size, opLog, beginDateScope);
        Long total = opLogMapper.getTotal(opLog, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(opLogs);
        bean.setTotal(total);
        return bean;
    }


    /**
     * 批量删除系统操作日志
     * @param ids 需要删除的操作日志id
     * @return 结果
     */
    public int deleteOpLogByIds(Integer[] ids) {
        return opLogMapper.deleteOperLogByIds(ids);
    }

    /**
     * 查询操作日志详细
     * @param id 操作id
     * @return 操作日志对象
     */
    public OpLog selectOpLogById(Integer id) {
        return opLogMapper.selectOpLogById(id);
    }

    /**
     * 清空操作日志
     */
    public void cleanOpLog() {
        opLogMapper.cleanOpLog();
    }
}
