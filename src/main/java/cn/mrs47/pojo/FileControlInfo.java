package cn.mrs47.pojo;

import java.io.Serializable;

/**
 * @author mrs47
 */
public  class FileControlInfo  implements Serializable {
    private Integer tag;
    private String host;
    private String port;
    private String uri;
    private String name;
    private String version;
    private String userName;
    private String password;

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "FileControlInfo{" +
                "tag=" + tag +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}