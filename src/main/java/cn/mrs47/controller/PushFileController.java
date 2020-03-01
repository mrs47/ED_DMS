package cn.mrs47.controller;


import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.User;
import cn.mrs47.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author mrs47
 */
@RequestMapping("/download")
@Controller
public class PushFileController {

    @Autowired
    FileService fileService;

    /**
     * 设备检查软件或文件更新的接口，
     * 暴露给设备端
     * @param deviceKey
     * @param productKey
     * @return
     */
    @RequestMapping(value = "/fileOrSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public String pushFileOrSoft(String deviceKey, String productKey){
        if ("".equals(deviceKey) || "".equals(productKey)){
            return "";
        }
        return fileService.update(deviceKey,productKey);
    }


    /**
     * 文件或软件更新命令
     * 写入Redis
     * @param session
     * @param softId
     * @param fileId
     * @param deviceId
     * @param productId
     * @param password
     * @return
     */
    @RequestMapping(value = "/pressInSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData pressInSoft(HttpSession session, String softId,String fileId,String deviceId,String productId,String password){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (softId==null && fileId==null){
            return ResponseData.createByError();
        }

        if ("".equals(fileId) && "".equals(softId)){
            return ResponseData.createByError();
        }
        return fileService.pressInFile(attribute.getData(),softId,fileId,deviceId,productId, password);
    }
}
