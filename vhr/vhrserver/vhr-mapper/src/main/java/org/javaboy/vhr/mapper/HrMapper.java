package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.Role;

import java.util.List;

/**
 * Created by candy on 2020/10/25.
 */

public interface HrMapper {
    /**
     * 根据 主键 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(Hr record);

    int insertSelective(Hr record);

    /**
     * 根据 主键 查询 hr
     * @param id
     * @return
     */
    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    /**
     * 根据 主键 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Hr record);

    /**
     * 加载 hr 的 用户名
     * @param username
     * @return
     */
    Hr loadUserByUsername(String username);

    /**
     * 获取 hr 对应的角色
     * @return
     */
    List<Role> getHrRolesById(Integer id);

    /**
     * 获取对应 的 所有 hr
     * @param hrid
     * @param keywords
     * @return
     */
    List<Hr> getAllHrs(@Param("hrid") Integer hrid, @Param("keywords") String keywords);

    /**
     * 获取 当前异常的 hr
     * @param id
     * @return
     */
    List<Hr> getAllHrsExceptCurrentHr(Integer id);


    /**
     * 更新 hr password
     * @param hrid
     * @param encodePass
     * @return
     */
    Integer updatePasswd(@Param("hrid") Integer hrid, @Param("encodePass") String encodePass);


    /**
     * 更新 hr 头像
     * @param url
     * @param id
     * @return
     */
    Integer updateUserface(@Param("url") String url, @Param("id") Integer id);
}
