package cn.mrs47.service.impl;

import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.dao.*;
import cn.mrs47.pojo.*;
import cn.mrs47.service.DeviceService;
import cn.mrs47.util.AESUtil;
import cn.mrs47.util.IdUtil;
import cn.mrs47.util.MD5Util;
import cn.mrs47.util.RedisUtil;
import cn.mrs47.vo.CenterInfo;
import cn.mrs47.vo.DeviceInfo;
import cn.mrs47.vo.GPIOInfo;
import cn.mrs47.vo.PageInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mrs47
 */
@Service("deviceService")
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SoftHubMapper softHubMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    @Transactional
    public ResponseData addDevice(String userId, String productId, String deviceName, String address) {
        boolean isEmpty = checkParamEmpty(productId, deviceName, address);
        if (isEmpty) {
            return ResponseData.createByErrorMessage("参数错误");
        }

        Integer balance = balanceMapper.selectBalanceByUserId(userId);
        if (balance<1){
            return ResponseData.createByErrorMessage("设备资源包用尽，请购买后添加");
        }

        int count = balanceMapper.updateBalance(userId, balance - 1);
        if (count < 1){
            return ResponseData.createByErrorMessage("添加失败");
        }
        String productName = productMapper.selectProductNameById(productId);
        Device device = new Device();
        device.setProductId(productId);
        device.setDeviceId(IdUtil.getDeviceId());
        device.setDeviceKey(IdUtil.getDeviceKey());
        device.setDeviceName(deviceName);
        device.setProductName(productName);
        device.setAddress(address);
        device.setStatus(0);
        int n = deviceMapper.insertDevice(device);
        if (n < 1) {
            return ResponseData.createByErrorMessage("新增失败");
        }
        return ResponseData.createBySuccessMessage("新增成功");
    }

    @Override
    public ResponseData selectAll(String userId, String deviceName, Integer page, Integer many, Integer status) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (many == null || many < 1) {
            many = 8;
        }
        int count = deviceMapper.selectCount(userId, deviceName, status);
        if (count < 1) {
            return ResponseData.createByErrorMessage("没有设备");
        }
        Integer totalPage = (count % many == 0) ? (count / many) : (count / many + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        Integer index = (page - 1) * many;
        List<Device> devices = deviceMapper.selectAll(userId, deviceName, status, index, many);
        PageInfo pageInfo = new PageInfo(count, many, page, devices);
        return ResponseData.createBySuccess(pageInfo);

    }

    @Override
    public ResponseData selectAllByProduct(String userId, String productId, String deviceName, Integer page, Integer many, Integer status) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (many == null || many < 1) {
            many = 8;
        }

        int count = deviceMapper.selectCountByProductId(userId, productId, deviceName, status);
        if (count < 1) {
            return ResponseData.createByErrorMessage("没有设备");
        }

        Integer totalPage = (count % many == 0) ? (count / many) : (count / many + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        Integer index = (page - 1) * many;
        List<Device> devices = deviceMapper.selectAllByProduct(userId, productId, deviceName, status, index, many);
        PageInfo pageInfo = new PageInfo(count, many, page, devices);
        return ResponseData.createBySuccess(pageInfo);

    }

    @Override
    public ResponseData removeDevice(String userId, String deviceId) {
        boolean isEmpty = checkParamEmpty(deviceId);
        if (isEmpty) {
            return ResponseData.createByErrorMessage("参数有误");
        }
        //已做级联删除
        int n = deviceMapper.deleteDevice(userId, deviceId);
        Jedis jedis = RedisUtil.getJedis();
        jedis.del(ServiceConstant.REDIS_INFO+deviceId);
        jedis.close();
        if (n < 1) {
            return ResponseData.createByErrorMessage("删除失败");
        }
        Integer balance = balanceMapper.selectBalanceByUserId(userId);
        balanceMapper.updateBalance(userId,balance+1);
        return ResponseData.createBySuccessMessage("删除成功");
    }

    @Override
    public String uploadDeviceInfo(String info, String deviceKey, String productKey) {
        Device device = deviceMapper.selectDeviceByProductKeyAndDeviceKey(productKey, deviceKey);
        if (device == null) {
            return "";
        }
        String token = productMapper.selectTokenByProductKey(productKey);

        String decrypt = AESUtil.decrypt(info, token, "0000000000000000");
        Jedis jedis = RedisUtil.getJedis();
        jedis.lpush(ServiceConstant.REDIS_INFO + device.getDeviceId(), decrypt);
        jedis.close();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCode(66);
        deviceInfo.setDeviceKey(deviceKey);
        deviceInfo.setProductKey(productKey);
        deviceInfo.setRandom("0000000000000000");
        return AESUtil.encrypt(new Gson().toJson(deviceInfo), token, deviceInfo.getRandom());
    }

    @Override
    public String uploadGPIOInfo(String info, String productKey, String deviceKey) {
        Device device = deviceMapper.selectDeviceByProductKeyAndDeviceKey(productKey, deviceKey);
        if (device == null) {
            return "";
        }
        String token = productMapper.selectTokenByProductKey(productKey);
        String decrypt = AESUtil.decrypt(info, token, "0000000000000000");
        Jedis jedis = RedisUtil.getJedis();
        jedis.hset(ServiceConstant.REDIS_GPIO + device.getDeviceId(), "info", decrypt);
        // 10分钟过期相当于 心跳数据
        jedis.expire(ServiceConstant.REDIS_GPIO + device.getDeviceId(), 600);

        // 在消息队列中查询是否有命令
        String ioCtrl = jedis.lpop(ServiceConstant.REDIS_MQ_GPIO + device.getDeviceId());
        jedis.close();
        if (ioCtrl == null) {
            GPIOInfo gpioInfo = new GPIOInfo();
            gpioInfo.setDeviceKey(deviceKey);
            gpioInfo.setProductKey(productKey);
            gpioInfo.setRandom("0000000000000000");
            gpioInfo.setCode(50);
            return AESUtil.encrypt(new Gson().toJson(gpioInfo), token, gpioInfo.getRandom());
        }
        return AESUtil.encrypt(ioCtrl, token, "0000000000000000");
    }

    @Override
    public ResponseData selectCenterDevice(String userId) {
        Integer total = deviceMapper.selectCount(userId, null, null);
        Integer offline = deviceMapper.selectCount(userId, null, 0);
        Integer inline = total - offline;
        List<Device> list = deviceMapper.selectErrorDevice(userId);
        Integer balance = balanceMapper.selectBalanceByUserId(userId);


        CenterInfo centerInfo = new CenterInfo();
        centerInfo.setTotalDevice(total);
        centerInfo.setInlineDevice(inline);
        centerInfo.setOffline(offline);
        centerInfo.setErrorDevice(list);
        centerInfo.setCanDeploy(balance);
        return ResponseData.createBySuccess(centerInfo);
    }

    @Override
    public ResponseData getDeviceInfo(String userId, String deviceId) {

        Jedis jedis = RedisUtil.getJedis();
        // 一组5分分钟 12组 1小 获取近1小时的数据
        // 一组有10条数据
        List<String> list = jedis.lrange(ServiceConstant.REDIS_INFO + deviceId, 0, 7);
        Gson gson = new Gson();
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (String value : list) {
            deviceInfos.add(gson.fromJson(value, DeviceInfo.class));
        }
        Collections.reverse(deviceInfos);
        return ResponseData.createBySuccess(deviceInfos);
    }

    @Override
    public ResponseData getDeviceGPIOInfo(String userId, String deviceId) {
        if ("".equals(deviceId) || deviceId == null) {
            return ResponseData.createByError();
        }

        Jedis jedis = RedisUtil.getJedis();
        String info = jedis.hget(ServiceConstant.REDIS_GPIO + deviceId, "info");
        jedis.close();
        if (info == null) {
            return ResponseData.createByError("空数据");
        }
        Gson gson = new Gson();
        GPIOInfo gpioInfo = gson.fromJson(info, GPIOInfo.class);
        return ResponseData.createBySuccess(gpioInfo);
    }

    @Override
    public ResponseData selectSoftByDeviceId(String userId, String deviceId) {
        String productId = deviceMapper.selectProductIdByDeviceId(deviceId);
        if (productId == null) {
            return ResponseData.createByErrorMessage("设备不存在");
        }
        List<SoftHub> softHubs = softHubMapper.selectSoftByProductId(productId);

        if (softHubs == null){
            return ResponseData.createByErrorMessage("软件不存在");
        }
        return ResponseData.createBySuccess(softHubs);
    }

    @Override
    public ResponseData pressInIOControl(String email, String deviceId, Integer pin, Integer highOrLow, Integer inOrOut, String password) {
        User user = userMapper.selectLogin(email, MD5Util.MD5EncodeUtf8(password));
        if (user == null){
            return ResponseData.createByErrorMessage("密码错误");
        }
        if (pin == null){
            pin = 0;
        }

        GPIO gpio=new GPIO();
        gpio.setPin(pin);
        gpio.setInOrOut(inOrOut);
        gpio.setHighOrLow(highOrLow);

        List<GPIO> list=new ArrayList<>();
        list.add(gpio);
        GPIOList gpioList=new GPIOList();
        gpioList.setList(list);

        String productId = deviceMapper.selectProductIdByDeviceId(deviceId);
        if (productId==null){
            return ResponseData.createBySuccessMessage("无此设备");
        }

        String deviceKey = deviceMapper.selectDeviceKeyByDeviceId(deviceId);
        String productKey = productMapper.selectProductKeyByProductId(user.getUserId(),productId);
        GPIOInfo gpioInfo=new GPIOInfo();

        if (pin==0){
            gpioInfo.setCode(52);
        }else {
            gpioInfo.setCode(51);
        }

        gpioInfo.setProductKey(productKey);
        gpioInfo.setDeviceKey(deviceKey);
        gpioInfo.setRandom("0000000000000000");
        gpioInfo.setData(gpioList);
        Jedis jedis = RedisUtil.getJedis();
        jedis.lpush(ServiceConstant.REDIS_MQ_GPIO+deviceId,new Gson().toJson(gpioInfo));
        jedis.close();
        return ResponseData.createBySuccessMessage("控制信息已入队列，大约5分钟后生效");
    }

    private boolean checkParamEmpty(Object... param) {
        for (Object value : param) {
            if (value == null || "".equals(value)) {
                return true;
            }
        }
        return false;
    }


}
