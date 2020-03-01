package cn.mrs47.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author mrs47
 */
public class DeviceInfo implements Serializable {
    //66:传递设备信息，50:上传GPIO信息 51：GPIO 控制信息 52: 初始化端口,98:心跳包，70：文件更新包括软件更新查询
    private Integer code;
    // 产品key
    private String productKey;
    // 设备key
    private String deviceKey;
    // 使用AES算法Token为密钥 Random为初始化向量
    private String random;

    private Info data;

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

    public Info getData() {
        return data;
    }

    public void setData(Info data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "code=" + code +
                ", productKey='" + productKey + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", random='" + random + '\'' +
                ", device=" + data +
                '}';
    }

    /**
     * 内部类说明
     *
     *
     */
    public class Info implements Serializable{
        private Integer   state;
        private CPUInfo   cpu;
        private Hd        hd;
        private MemInfo   mem;
        private NetInfo   net;
        private Processes processes;

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public CPUInfo getCpu() {
            return cpu;
        }

        public void setCpu(CPUInfo cpu) {
            this.cpu = cpu;
        }

        public Hd getHd() {
            return hd;
        }

        public void setHd(Hd hd) {
            this.hd = hd;
        }

        public MemInfo getMem() {
            return mem;
        }

        public void setMem(MemInfo mem) {
            this.mem = mem;
        }

        public NetInfo getNet() {
            return net;
        }

        public void setNet(NetInfo net) {
            this.net = net;
        }

        public Processes getProcesses() {
            return processes;
        }

        public void setProcesses(Processes processes) {
            this.processes = processes;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "state=" + state +
                    ", cpu=" + cpu +
                    ", hd=" + hd +
                    ", mem=" + mem +
                    ", net=" + net +
                    ", processes=" + processes +
                    '}';
        }
    }

    public class CPUInfo implements Serializable{

        private List<Float>  usage;
        private List<String> temp;
        private List<String> time;
        private String       cpuModel ;


        public List<Float> getUsage() {
            return usage;
        }

        public void setUsage(List<Float> usage) {
            this.usage = usage;
        }

        public List<String> getTemp() {
            return temp;
        }

        public void setTemp(List<String> temp) {
            this.temp = temp;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public String getCpuModel() {
            return cpuModel;
        }

        public void setCpuModel(String cpuModel) {
            this.cpuModel = cpuModel;
        }

        @Override
        public String toString() {
            return "CPUInfo{" +
                    "usage=" + usage +
                    ", temp=" + temp +
                    ", time=" + time +
                    ", cpuModel='" + cpuModel + '\'' +
                    '}';
        }
    }

    public class Hd implements Serializable{
        private String total;
        private String used;
        private String free;
        private String  time;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Hd{" +
                    "total=" + total +
                    ", used=" + used +
                    ", free=" + free +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    public class MemInfo implements Serializable{
        private String memTotal;
        private String swapTotal;
        private List<String> memFree;
        private List<String> swapFree;
        private List<String> time;

        public String getMemTotal() {
            return memTotal;
        }

        public void setMemTotal(String memTotal) {
            this.memTotal = memTotal;
        }

        public String getSwapTotal() {
            return swapTotal;
        }

        public void setSwapTotal(String swapTotal) {
            this.swapTotal = swapTotal;
        }

        public List<String> getMemFree() {
            return memFree;
        }

        public void setMemFree(List<String> memFree) {
            this.memFree = memFree;
        }

        public List<String> getSwapFree() {
            return swapFree;
        }

        public void setSwapFree(List<String> swapFree) {
            this.swapFree = swapFree;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "MemInfo{" +
                    "memTotal='" + memTotal + '\'' +
                    ", swapTotal='" + swapTotal + '\'' +
                    ", memFree=" + memFree +
                    ", swapFree=" + swapFree +
                    ", time=" + time +
                    '}';
        }
    }

    public class NetInfo implements Serializable{
        private Map<String,Integer>   netInterface;
        private Map<String,List<Integer>> netIn;
        private Map<String,List<Integer>> netOut;
        private List<String>          time;

        public Map<String, Integer> getNetInterface() {
            return netInterface;
        }

        public void setNetInterface(Map<String, Integer> netInterface) {
            this.netInterface = netInterface;
        }

        public Map<String, List<Integer>> getNetIn() {
            return netIn;
        }

        public void setNetIn(Map<String, List<Integer>> netIn) {
            this.netIn = netIn;
        }

        public Map<String, List<Integer>> getNetOut() {
            return netOut;
        }

        public void setNetOut(Map<String, List<Integer>> netOut) {
            this.netOut = netOut;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "NetInfo{" +
                    "netInterface=" + netInterface +
                    ", netIn=" + netIn +
                    ", netOut=" + netOut +
                    ", time=" + time +
                    '}';
        }
    }

   public class Processes implements Serializable{
        private List<Process> processList;

        public List<Process> getProcessList() {
            return processList;
        }

        public void setProcessList(List<Process> processList) {
            this.processList = processList;
        }

        @Override
        public String toString() {
            return "Processes{" +
                    "processList=" + processList +
                    '}';
        }
    }

    public class Process implements Serializable{
        private String pid;
        private String pName;
        private String user;
        private String status;
        private Float  cpuUsage;
        private Float  memUsage;
        private String time;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Float getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Float cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public Float getMemUsage() {
            return memUsage;
        }

        public void setMemUsage(Float memUsage) {
            this.memUsage = memUsage;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Process{" +
                    "pid='" + pid + '\'' +
                    ", pName='" + pName + '\'' +
                    ", user='" + user + '\'' +
                    ", status='" + status + '\'' +
                    ", cpuUsage=" + cpuUsage +
                    ", memUsage=" + memUsage +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
}

