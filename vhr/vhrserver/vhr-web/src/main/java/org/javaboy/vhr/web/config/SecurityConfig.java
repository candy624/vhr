package org.javaboy.vhr.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by candy on 2020/10/26.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private HrService hrService;
    @Resource
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Resource
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    LoginFilter loginFilter() throws Exception {
        LoginFilter filter = new LoginFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            Hr hr = (Hr) authentication.getPrincipal();
            hr.setPassword(null);
            RespBean ok = RespBean.ok("登录成功", hr);
            String s = new ObjectMapper().writeValueAsString(ok);
            out.write(s);
            out.flush();
            out.close();
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            RespBean error = RespBean.error(exception.getMessage());
            if (exception instanceof LockedException) {
                error.setMsg("账户被锁定,请联系管理员!");
            } else if (exception instanceof CredentialsExpiredException) {
                error.setMsg("密码过期,请联系管理员!");
            } else if (exception instanceof AccountExpiredException) {
                error.setMsg("账户过期,请联系管理员!");
            } else if (exception instanceof DisabledException) {
                error.setMsg("账户被禁用,请联系管理员!");
            } else if (exception instanceof BadCredentialsException) {
                error.setMsg("用户名或密码输入错误,请重新输入!");
            }
        });
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setFilterProcessesUrl("/doLogin");
        ConcurrentSessionControlAuthenticationStrategy strategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        strategy.setMaximumSessions(1);
        filter.setSessionAuthenticationStrategy(strategy);
        return filter;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setAccessDecisionManager(customUrlDecisionManager);
                object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                return object;
            }
        }).and().logout().logoutSuccessHandler((request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功!")));
            out.flush();
            out.close();
        }).permitAll().and().csrf().disable().exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(401);
            PrintWriter out = response.getWriter();
            RespBean respBean = RespBean.error("访问失败!");
            if (authenticationException instanceof InsufficientAuthenticationException) respBean.setMsg("请求失败,请联系管理员!");
            out.write(new ObjectMapper().writeValueAsString(respBean));
            out.flush();
            out.close();
        });
        security.addFilterAfter(new ConcurrentSessionFilter(sessionRegistry(), (event) -> {
            HttpServletResponse response = event.getResponse();
            response.setContentType("");
            response.setStatus(401);
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(RespBean.error("您已在另一台设备登录,本次登录已下线!")));
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class);
        security.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
