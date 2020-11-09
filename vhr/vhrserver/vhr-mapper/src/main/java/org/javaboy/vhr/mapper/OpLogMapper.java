package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.OpLog;

import java.util.Date;
import java.util.List;

/**
 * Created by candy on 2020/11/2.
 */
public interface OpLogMapper {
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(OpLog record);
//
//    int insertSelective(OpLog record);
//
//    OpLog selectByPrimaryKey(Integer id);
//
//    int updateByPrimaryKeySelective(OpLog record);
//
//    int updateByPrimaryKey(OpLog record);

    /**
     *  新增操作日志
     * @param opLog 操作日志对象
     */
    void insertOperlog(OpLog opLog);

    /**
     * 查询系统操作日志集合
     * @param opLog 操作日志对象
     * @return 操作日志集合
     */
    List<OpLog> selectOperLogList(@Param("page") Integer page, @Param("size") Integer size, @Param("oplog") OpLog opLog, @Param("beginDateScope") Date[] beginDateScope);

    /**
     * 查询总记录数
     * @param opLog 日志对象
     * @return 总记录数
     */
    Long getTotal(@Param("oplog") OpLog opLog, Date[] beginDateScope);

    /**
     * 批量删除系统操作日志
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Integer[] ids);

    /**
     * 查询操作日志详细
     * @param id 操作ID
     * @return 操作日志对象
     */
    OpLog selectOpLogById(Integer id);

    /**
     * 清空操作日志
     */
    void cleanOpLog();
}
