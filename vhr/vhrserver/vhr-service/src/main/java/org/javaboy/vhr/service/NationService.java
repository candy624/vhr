package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.NationMapper;
import org.javaboy.vhr.model.Nation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/10/30.
 */
@Service
public class NationService {
    @Resource
    NationMapper nationMapper;

    public List<Nation> getAllNations() {
        return nationMapper.getAllNations();
    }
}
