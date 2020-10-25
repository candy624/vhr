package org.javaboy.vhr.web.controller;

import org.javaboy.vhr.model.ResqBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by candy on 2020/10/25.
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public ResqBean login() {
        return ResqBean.error("尚未登录,请登录!");
    }

    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }
}
