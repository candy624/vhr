package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.PoliticsstatusMapper;
import org.javaboy.vhr.model.Politicsstatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
@Service
public class PoliticsstatusService {
    @Resource
    PoliticsstatusMapper politicsstatusMapper;

    public List<Politicsstatus> getAllPoliticsstauts() {
        return politicsstatusMapper.getAllPoliticsstatus();
    }
}
