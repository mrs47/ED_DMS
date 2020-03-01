package cn.mrs47.pojo;

import java.io.Serializable;

/**
 * @author mrs47
 */
public class Balance implements Serializable {
    private Integer id;
    private String userId;
    private Integer balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", UserId='" + userId + '\'' +
                ", balance=" + balance +
                '}';
    }
}