package cn.mrs47.service;

import cn.mrs47.common.ResponseData;

/**
 * @author mrs47
 */
public interface DeviceService {
    ResponseData addDevice(String userId, String productId, String deviceName, String address);

    ResponseData selectAll(String userId, String deviceName, Integer page, Integer many, Integer status);

    ResponseData selectAllByProduct(String userId, String productId, String deviceName, Integer page, Integer many, Integer status);

    ResponseData removeDevice(String userId, String deviceId);

    String uploadDeviceInfo(String info, String deviceKey, String productKey);

    String uploadGPIOInfo(String info, String productKey, String deviceKey);

    ResponseData selectCenterDevice(String userId);

    ResponseData getDeviceInfo(String userId, String deviceId);

    ResponseData getDeviceGPIOInfo(String userId, String deviceId);

    ResponseData selectSoftByDeviceId(String userId, String deviceId);

    ResponseData pressInIOControl(String email, String deviceId, Integer pin, Integer highOrLow, Integer inOrOut, String password);
}
