package cn.mrs47.pojo;

import cn.mrs47.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mrs47
 */
public class FileHub implements Serializable {

    private Integer id;
    private String  fileId;
    private String  userId;
    private String  fileName;
    private String  filePath;
    private String  host;
    private Date    createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCreateTime() {
        return TimeUtil.TimeStampToDate(this.createTime.getTime());
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FileHub{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                ", userId='" + userId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileRealPath='" + host + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}