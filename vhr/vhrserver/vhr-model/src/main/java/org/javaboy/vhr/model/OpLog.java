package org.javaboy.vhr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.javaboy.vhr.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by candy on 2020/11/2.
 */
public class OpLog implements Serializable {
    @Excel(name = "操作序号", cellType = Excel.ColumnType.NUMERIC)
    private Integer id;
    @Excel(name = "操作模块")
    private String title;               //操作模块
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer businessType;       //操作类型
    @Excel(name = "请求方法")
    private String method;              //请求方法
    @Excel(name = "请求方式")
    private String requestMethod;       //请求方式
    @Excel(name = "操作人员")
    private String operName;            //操作人员
    @Excel(name = "请求地址")
    private String operUrl;             //请求地址
    @Excel(name = "操作地址")
    private String operIp;              //操作ip
    @Excel(name = "操作地点")
    private String operLocation;        //操作地点
    @Excel(name = "请求参数")
    private String operParam;           //请求参数
    @Excel(name = "返回参数")
    private String jsonResult;          //返回参数
    @Excel(name = "状态", readConverterExp = "0=正常,1=异常")
    private Integer status;             //操作状态
    @Excel(name = "错误消息")
    private String errorMsg;            //错误消息
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;              //操作时间

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public String getOperLocation() {
        return operLocation;
    }

    public void setOperLocation(String operLocation) {
        this.operLocation = operLocation;
    }

    public String getOperParam() {
        return operParam;
    }

    public void setOperParam(String operParam) {
        this.operParam = operParam;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
}
