package cn.mrs47.controller;

import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.User;
import cn.mrs47.service.FileService;
import cn.mrs47.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @author mrs47
 */

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    /**
     * 文件上传
     * @param session
     * @param file
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/uploadFile.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadFile(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, @RequestParam(value = "fileName",required = false)String fileName){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String filePath = session.getServletContext().getRealPath("uploadFile");
        return fileService.uploadFile(responseData
                .getData().getUserId(),file,fileName,filePath);
    }

    /**
     * 软件上传
     * @param session
     * @param file
     * @param softName
     * @param version
     * @param productId
     * @return
     */
    @RequestMapping(value = "/uploadSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadSoft(HttpSession session, @RequestParam(value = "upload_soft",required = false) MultipartFile file, @RequestParam(value = "softName",required = false)String softName
                                    ,@RequestParam(value = "version",required = false) String version,@RequestParam(value = "productId",required = false) String productId){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String filePath = session.getServletContext().getRealPath("uploadFile");
        return fileService.uploadSoft(responseData
                .getData().getUserId(),file,softName,version,productId,filePath);
    }

    /**
     * 搜索文件仓库
     * @param session
     * @param fileName
     * @param page
     * @param many
     * @return
     */
    @RequestMapping(value = "/selectFileAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<PageInfo> selectFileAll(HttpSession session,String fileName,Integer page, Integer many){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = responseData.getData();
        return fileService.selectAll(user.getUserId(),fileName,page,many);
    }

    /**
     * 从文件仓库移除文件
     * @param session
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/removeFile.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData removeFile(HttpSession session,String fileId){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (fileId==null || "".equals(fileId.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        User user = responseData.getData();
        return fileService.removeFile(user.getUserId(),fileId);
    }

    /**
     * 检查文件名是否冲突（同一用户）
     * @param session
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/checkFileName.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData checkFileName(HttpSession session,String fileName){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (fileName==null || "".equals(fileName.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        User user = responseData.getData();
        return fileService.checkFileName(user.getUserId(),fileName);
    }

    /**
     * 从软件仓库搜索软件 按需
     * @param session
     * @param softName
     * @param page
     * @param many
     * @return
     */
    @RequestMapping(value = "/selectSoftAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<PageInfo> selectSoftAll(HttpSession session,String softName, Integer page, Integer many){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        User user = responseData.getData();
        return fileService.selectSoftAll(user.getUserId(),softName,page,many);
    }


    /**
     * 从软件仓库移除软件
     * @param session
     * @param softId
     * @return
     */
    @RequestMapping(value = "/removeSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData removeSoft(HttpSession session,String softId){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByErrorMessage("未登录");
        }
        if (softId==null || "".equals(softId.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        User user = responseData.getData();
        return fileService.removeSoft(user.getUserId(),softId);
    }
}
