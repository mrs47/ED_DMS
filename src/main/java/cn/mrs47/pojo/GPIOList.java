package cn.mrs47.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author mrs47
 */
public class GPIOList implements Serializable {
    private List<GPIO> list;

    public List<GPIO> getList() {
        return list;
    }

    public void setList(List<GPIO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "GPIOList{" +
                "list=" + list +
                '}';
    }
}
