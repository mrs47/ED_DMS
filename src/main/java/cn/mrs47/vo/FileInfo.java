package cn.mrs47.vo;

import cn.mrs47.pojo.FileControlInfo;

import java.io.Serializable;

/**
 * @author mrs47
 */
public class FileInfo implements Serializable {
    //66:传递设备信息，50:上传GPIO信息 51：GPIO 控制信息 52: 初始化端口,98:心跳包，70：文件更新包括软件更新查询
    private Integer code;
    // 产品key
    private String productKey;
    // 设备key
    private String deviceKey;
    // 使用AES算法Token为密钥 Random为初始化向量
    private String random;

    private FileControlInfo data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public FileControlInfo getData() {
        return data;
    }

    public void setData(FileControlInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "code=" + code +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", random='" + random + '\'' +
                ", file=" + data +
                '}';
    }
}


