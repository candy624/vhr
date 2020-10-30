package org.javaboy.vhr.web.controller;

import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.web.config.VerificationCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by candy on 2020/10/25.
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login() {
        System.out.println("sss");
        return RespBean.error("尚未登录,请登录!");
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession();
        session.setAttribute("verify_code", text);
        VerificationCode.output(image, response.getOutputStream());
    }
}
