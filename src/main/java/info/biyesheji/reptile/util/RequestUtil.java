package info.biyesheji.reptile.util;

import java.io.Serializable;

import static info.biyesheji.reptile.util.ErrorResultMessage.自定义异常提示code;

public class RequestUtil implements Serializable {

    private static final int succStatus = 200;
    private static final int errorStatus = 500;
    private static final long serialVersionUID = -8090827123673073325L;


    private Object data;
    private Integer status;
    private String errorMessage;
    private Integer errorCode;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static final Object succ(Object data){
        RequestUtil requestUtil =  new RequestUtil();
        requestUtil.setData(data);
        requestUtil.setStatus(succStatus);
        return requestUtil;
    }
    public static final Object succ(){
        RequestUtil requestUtil =  new RequestUtil();
        requestUtil.setStatus(succStatus);
        return requestUtil;
    }

    public static final Object error(String errorMessage){
        RequestUtil requestUtil = new RequestUtil();
        requestUtil.setStatus(errorStatus);
        requestUtil.setErrorMessage(errorMessage);
        requestUtil.setErrorCode(自定义异常提示code);
        return requestUtil;
    }

    public static final Object error(ErrorResultMessage.ErrorMessage errorMessage){
        RequestUtil requestUtil = new RequestUtil();
        requestUtil.setStatus(errorStatus);
        requestUtil.setErrorMessage(errorMessage.getMessage());
        requestUtil.setErrorCode(errorMessage.getErrorCode());
        return requestUtil;
    }

}
