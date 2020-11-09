package org.javaboy.vhr.web.acpect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.javaboy.vhr.common.annotation.Log;
import org.javaboy.vhr.common.enums.BusinessStatus;
import org.javaboy.vhr.model.OpLog;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.service.OpLogService;
import org.javaboy.vhr.common.utils.IpInfoUtil;
import org.javaboy.vhr.common.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.*;

/**
 * Created by candy on 2020/11/2.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
//    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");

    @Resource
    private OpLogService opLogService;
    @Resource
    private HrService hrService;
    @Resource
    private IpInfoUtil ipInfoUtil;
    @Resource
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    @Pointcut("@annotation(org.javaboy.vhr.common.annotation.Log)")
    public void controllerAspect() {

    }

    /**
     * 前置通知(在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     * @param joinPoint 切点
     * @throws InterruptedException 异常
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
//        //线程绑定变量（该数据只有当前请求的线程可见）
//        Date beginTime = new Date();
//        beginTimeThreadLocal.set(beginTime);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("========请求========");
        log.info("浏览器输入的网址URL : " + request.getRequestURI());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("执行的业务方法名 = CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("业务方法获得的参数 = ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "controllerAspect()", returning = "objectResult")
    public void after(JoinPoint joinPoint, Object objectResult) {
        handleLog(joinPoint, null, objectResult);
    }

    @AfterThrowing(value = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    public void handleLog(final JoinPoint joinPoint, final Exception e, final Object jsonResult) {
        try {
            String title = getControllerMethodInfo(joinPoint).get("title").toString();
            Integer type = (Integer) getControllerMethodInfo(joinPoint).get("type");
            Map<String, String[]> requestParams = request.getParameterMap();

            OpLog opLog = new OpLog();
            // 请求用户
            Principal principal = request.getUserPrincipal();
            opLog.setOperName(principal.getName());
            // 日志标题
            opLog.setTitle(title);
            // 业务类型
            opLog.setBusinessType(type);
            // 方法名称
            opLog.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            // 请求方式
            opLog.setRequestMethod(request.getMethod());
            // 请求url
            opLog.setOperUrl(request.getRequestURI());
            // 请求参数
            opLog.setOperParam(ObjectUtil.mapToString(requestParams));
            // ip
            opLog.setOperIp(ipInfoUtil.getIpAddr(request));
            // 操作时间
            opLog.setOperTime(new Date());
            // 返回参数
            if (type == 5) {
                opLog.setJsonResult("导出成功");
            } else {
                opLog.setJsonResult(JSON.toJSONString(jsonResult));
            }
            if (e != null) {
                // 操作状态
                opLog.setStatus(BusinessStatus.FAIL.ordinal());
                // 错误消息
                opLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
            } else {
                opLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            }
            opLogService.insertOpLog(opLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception 异常
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>(16);
        // 获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取相关参数
        Object[] arguments = joinPoint.getArgs();
        // 生成类对象
        Class targetClass = Class.forName(targetName);
        // 获取该类中的方法
        Method[] methods = targetClass.getMethods();
        String title = "";
        Integer type = null;
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] classes = method.getParameterTypes();
            if (classes.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            title = method.getAnnotation(Log.class).title();
            type = method.getAnnotation(Log.class).businessType().ordinal();
            map.put("title", title);
            map.put("type", type);
        }
        return map;
    }
}
