package org.javaboy.vhr.web.controller;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.service.HrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by candy on 2020/10/31.
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    HrService hrService;

    @GetMapping("/hrs")
    public List<Hr> getAllHrs() {
        return hrService.getAllHrsExceptCurrentHr();
    }
}
