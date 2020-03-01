package cn.mrs47.controller;

import cn.mrs47.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mrs47
 *
 */
@Controller
@RequestMapping("/upload/device/")
public class UploadDeviceInfoController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 设备上传设备状态信息接口
     * @param info
     * @param deviceKey
     * @param productKey
     * @return
     */
    @RequestMapping(value = "info.do",method = RequestMethod.POST)
    @ResponseBody
    public String uploadDeviceInfo(String info , String deviceKey, String productKey){
        if ("".equals(info) || "".equals(deviceKey) || "".equals(productKey)){
            return "";
        }
        return deviceService.uploadDeviceInfo(info,deviceKey,productKey);
    }

    /**
     * 设备上传通用输入输出引脚的状态接口
     * @param info
     * @param deviceKey
     * @param productKey
     * @return
     */
    @RequestMapping(value = "gpio.do",method = RequestMethod.POST)
    @ResponseBody
    public String uploadGPIOInfo(String info , String deviceKey, String productKey){
        if ("".equals(info) || "".equals(deviceKey) || "".equals(productKey)){
            return "";
        }
        return deviceService.uploadGPIOInfo(info,productKey,deviceKey);
    }

}
