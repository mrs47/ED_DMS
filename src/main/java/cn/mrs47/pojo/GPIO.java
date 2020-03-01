package cn.mrs47.pojo;

import java.io.Serializable;

/**
 * @author mrs47
 */
public class GPIO implements Serializable {
    private Integer pin;
    private Integer inOrOut;
    private Integer highOrLow;


    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(Integer inOrOut) {
        this.inOrOut = inOrOut;
    }

    public Integer getHighOrLow() {
        return highOrLow;
    }

    public void setHighOrLow(Integer highOrLow) {
        this.highOrLow = highOrLow;
    }

    @Override
    public String toString() {
        return "GPIO{" +
                "pin=" + pin +
                ", inOrOut=" + inOrOut +
                ", highOrLow=" + highOrLow +
                '}';
    }
}
