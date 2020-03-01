package cn.mrs47.pojo;

import cn.mrs47.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mrs47
 */
public class SoftHub implements Serializable {
    private Integer id;
    private String  softId;
    private String  softName;
    private String  productId;
    private String  productName;
    private String  path;
    private String  host;
    private String  version;
    private Date    createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreateTime() {
        return TimeUtil.TimeStampToDate(this.createTime.getTime());
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SoftHub{" +
                "id=" + id +
                ", softId='" + softId + '\'' +
                ", softName='" + softName + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", path='" + path + '\'' +
                ", host='" + host + '\'' +
                ", version='" + version + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}