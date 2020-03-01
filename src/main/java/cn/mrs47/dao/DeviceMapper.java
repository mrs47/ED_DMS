package cn.mrs47.dao;

import cn.mrs47.pojo.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */

@Repository
public interface DeviceMapper {
    int insertDevice(Device device);

    List<Device> selectAll(@Param("userId") String userId, @Param("deviceName")String deviceName, @Param("status") Integer status, @Param("index") Integer index, @Param("many") Integer many);

    int selectCount(@Param("userId") String userId, @Param("deviceName") String deviceName, @Param("status") Integer status);

    int selectCountByProductId(@Param("userId") String userId, @Param("productId") String productId, @Param("deviceName")String deviceName, @Param("status") Integer status);

    List<Device> selectAllByProduct(@Param("userId") String userId, @Param("productId") String productId, @Param("deviceName")String deviceName, @Param("status") Integer status, @Param("index") Integer index, @Param("many") Integer many);

    int deleteDevice(@Param("userId") String userId, @Param("deviceId")String deviceId);

    Device selectDeviceByProductKeyAndDeviceKey(@Param("productKey")String productKey, @Param("deviceKey")String deviceKey);

    List<Device> selectErrorDevice(@Param("userId")String userId);

    List<Device> selectDeviceByProductId(@Param("productId")String productId);

    String selectDeviceKeyByDeviceId(@Param("deviceId")String deviceId);

    void setAlive(@Param("deviceId")String deviceId);

    void setDied(@Param("list")List<String> list);

    String selectProductIdByDeviceId(@Param("deviceId")String deviceId);
}
