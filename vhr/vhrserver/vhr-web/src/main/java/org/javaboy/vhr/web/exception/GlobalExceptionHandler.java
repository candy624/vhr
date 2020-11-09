package org.javaboy.vhr.web.exception;

import org.javaboy.vhr.common.lang.RespBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by candy on 2020/10/26.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有关联数据，操作失败!");
        }
        return RespBean.error("数据库异常，操作失败!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public RespBean handler(RuntimeException e) throws IOException {
//        log.error("运行时异常:-------------->",e);
        return RespBean.error(e.getMessage());
    }
}
