package info.biyesheji.reptile.util;

public class ErrorResultMessage {

    public static final ErrorMessage 请求频繁 = new ErrorMessage(1,"请求过于频繁,请稍后再试!");
    protected static final Integer 自定义异常提示code = 99;



    public static class ErrorMessage{
        private Integer errorCode;
        private String Message;

        public ErrorMessage(Integer errorCode, String message) {
            this.errorCode = errorCode;
            Message = message;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public ErrorMessage format(Object... args) {
            return new ErrorMessage(100, String.format("业务异常信息: %s", args));
        }

    }
}
