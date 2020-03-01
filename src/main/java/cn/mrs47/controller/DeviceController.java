package cn.mrs47.controller;

import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.User;
import cn.mrs47.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author mrs47
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;


    /**
     * 添加设备
     * @param session
     * @param productId
     * @param deviceName
     * @param address
     * @return
     */
    @RequestMapping(value = "/addDevice.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addDevice(HttpSession session, String productId,String deviceName,String address){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        return deviceService.addDevice(attribute.getData().getUserId(),productId,deviceName,address);
    }

    /**
     * 按需查找所有
     * @param session
     * @param deviceName 可选项
     * @param page 可选项
     * @param many 可选项
     * @param status 可选项
     * @return
     */
    @RequestMapping(value = "/selectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectAll(HttpSession session,String deviceName,Integer page,Integer many,Integer status){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(10,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.selectAll(user.getUserId(),deviceName,page,many,status);
    }

    /**
     * 按需查找某个产品下的设备
     * @param session
     * @param productId
     * @param deviceName
     * @param page
     * @param many
     * @param status
     * @return
     */
    @RequestMapping(value = "/selectAllByProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectAllByProduct(HttpSession session,String productId,String deviceName,Integer page,Integer many,Integer status){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.selectAllByProduct(user.getUserId(),productId,deviceName,page,many,status);
    }

    /**
     * 移除设备
     * @param session
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/removeDevice.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData removeDevice(HttpSession session,String deviceId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.removeDevice(user.getUserId(),deviceId);
    }


    /**
     * 前端面板中心接口
     * @param session
     * @return
     */
    @RequestMapping(value = "/selectCenterDevice.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectCenterDevice(HttpSession session){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.selectCenterDevice(user.getUserId());
    }

    /**
     * 获取设备详情
     * @param session
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getDeviceInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getDeviceInfo(HttpSession session,String deviceId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.getDeviceInfo(user.getUserId(),deviceId);
    }

    /**
     * 获取设备通用输入输出引脚信息
     * @param session
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getDeviceGPIOInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getDeviceGPIOInfo(HttpSession session,String deviceId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = attribute.getData();
        return deviceService.getDeviceGPIOInfo(user.getUserId(),deviceId);
    }


    /**
     * 搜索软件接口
     * @param session
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/selectSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectSoftByDeviceId(HttpSession session,String deviceId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (deviceId==null || "".equals(deviceId)){
            return ResponseData.createByError();
        }
        User user = attribute.getData();
        return deviceService.selectSoftByDeviceId(user.getUserId(),deviceId);
    }

    /**
     * 设备通用输入输出的控制，
     * 压入Redis，队列
     * @param session
     * @param deviceId
     * @param pin
     * @param highOrLow
     * @param inOrOut
     * @param password
     * @return
     */
    @RequestMapping(value = "/pressInIOControl.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData pressInIOControl(HttpSession session,String deviceId,Integer pin,Integer highOrLow,Integer inOrOut,String password){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (deviceId==null || "".equals(deviceId) || highOrLow==null || inOrOut==null || password==null){
            return ResponseData.createByErrorMessage("参数错误");
        }
        User user = attribute.getData();
        return deviceService.pressInIOControl(user.getEmail(),deviceId,pin,highOrLow,inOrOut,password);
    }
}
