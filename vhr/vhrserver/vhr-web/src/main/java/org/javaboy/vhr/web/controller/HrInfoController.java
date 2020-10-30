package org.javaboy.vhr.web.controller;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by candy on 2020/10/30.
 */
@RestController
public class HrInfoController {
    @Resource
    HrService hrService;

    @GetMapping("/hr/info")
    public Hr getCurrentHr(Authentication authentication) {
        return (Hr) authentication.getPrincipal();
    }

    @PostMapping("/hr/info")
    public RespBean updateHr(@RequestBody Hr hr, Authentication authentication) {
        if (hrService.updateHr(hr) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/hr/pass")
    public RespBean updateHrPasswd(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldpass");
        String pass = (String) info.get("pass");
        Integer hrid = (Integer) info.get("hrid");
        if (hrService.updateHrPasswd(oldPass, pass, hrid)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

//    @PostMapping("/hr/userface")
//    public RespBean updateHrUserface(MultipartFile file, Integer id,Authentication authentication) {
//        String fileId = FastDFSUtils.upload(file);
//        String url = nginxHost + fileId;
//        if (hrService.updateUserface(url, id) == 1) {
//            Hr hr = (Hr) authentication.getPrincipal();
//            hr.setUserface(url);
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
//            return RespBean.ok("更新成功!", url);
//        }
//        return RespBean.error("更新失败!");
//    }
}
