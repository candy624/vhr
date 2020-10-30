package org.javaboy.vhr.web.controller.config;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/10/27.
 */
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Resource
    private MenuService menuService;

    @GetMapping("/menu")
    public List<Menu> getMenusByHrId() {
        return menuService.getMenusByHrId();
    }
}
