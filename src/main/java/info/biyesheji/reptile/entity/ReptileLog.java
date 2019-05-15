package info.biyesheji.reptile.entity;
import java.io.Serializable;

/**
* ReptileLog.java
* @version 1.0.0
*/
public class ReptileLog implements Serializable {
    public static final int 未处理 = 0;
    public static final int 已下载 = 1;
    public static final int 已处理 = 2;
    public static final int 不可用 = 3;
    private Integer id;
    private String url;  
    private Integer languageType;  //  1 java
    private String startNum;  
    private String remark;  
    private Integer type;  //  1 github 2 码云
    private Integer status;  //  0 未处理 1 已处理 2 不可用
    private String projectName;  
    private String gitUrl;  

    public void setId(Integer id) { this.id = id; }
    public Integer getId() { return this.id; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }
    public void setLanguageType(Integer languageType) { this.languageType = languageType; }
    public Integer getLanguageType() { return this.languageType; }
    public void setStartNum(String startNum) { this.startNum = startNum; }
    public String getStartNum() { return this.startNum; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getRemark() { return this.remark; }
    public void setType(Integer type) { this.type = type; }
    public Integer getType() { return this.type; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getStatus() { return this.status; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectName() { return this.projectName; }
    public void setGitUrl(String gitUrl) { this.gitUrl = gitUrl; }
    public String getGitUrl() { return this.gitUrl; }
}