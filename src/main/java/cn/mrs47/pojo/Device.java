package cn.mrs47.pojo;

import cn.mrs47.util.TimeUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mrs47
 */
public class Device implements Serializable {
    private Integer id;
    private String  deviceId;
    private String  productId;
    private String  deviceName;
    private String  deviceKey;
    private Integer status;
    private String  productName;
    private String  address;
    private Date    updateTime;
    private Date    createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpdateTime() {
        return TimeUtil.TimeStampToDate(updateTime.getTime());
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return TimeUtil.TimeStampToDate(createTime.getTime());
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", productId='" + productId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", status=" + status +
                ", productName='" + productName + '\'' +
                ", address='" + address + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}