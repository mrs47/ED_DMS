package cn.mrs47.vo;

import cn.mrs47.pojo.Device;

import java.io.Serializable;
import java.util.List;

/**
 * @author mrs47
 */
public class CenterInfo implements Serializable {

    private Integer totalDevice;
    private Integer inlineDevice;
    private Integer canDeploy;
    private Integer surplusDeploy;
    private Integer offline;
    private List<Device> errorDevice;


    public Integer getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(Integer totalDevice) {
        this.totalDevice = totalDevice;
    }

    public Integer getInlineDevice() {
        return inlineDevice;
    }

    public void setInlineDevice(Integer inlineDevice) {
        this.inlineDevice = inlineDevice;
    }

    public Integer getCanDeploy() {
        return canDeploy;
    }

    public void setCanDeploy(Integer canDeploy) {
        this.canDeploy = canDeploy;
    }

    public Integer getSurplusDeploy() {
        return surplusDeploy;
    }

    public void setSurplusDeploy(Integer surplusDeploy) {
        this.surplusDeploy = surplusDeploy;
    }

    public Integer getOffline() {
        return offline;
    }

    public void setOffline(Integer offline) {
        this.offline = offline;
    }

    public List<Device> getErrorDevice() {
        return errorDevice;
    }

    public void setErrorDevice(List<Device> errorDevice) {
        this.errorDevice = errorDevice;
    }

    @Override
    public String toString() {
        return "CenterInfo{" +
                "totalDevice=" + totalDevice +
                ", inlineDevice=" + inlineDevice +
                ", canDeploy=" + canDeploy +
                ", surplusDeploy=" + surplusDeploy +
                ", offline=" + offline +
                ", errorDevice=" + errorDevice +
                '}';
    }
}
