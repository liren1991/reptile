package info.biyesheji.reptile.entity;

import java.io.Serializable;

public class ReptileParam implements Serializable {
    private static final long serialVersionUID = 4959620169199181669L;
    public static final String 分页页数 = "pageNum";

    private String cookie;
    private String url;


    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
