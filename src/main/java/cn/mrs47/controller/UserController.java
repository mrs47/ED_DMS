package cn.mrs47.controller;

import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.User;
import cn.mrs47.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author mrs47
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> register(User user){
        return userService.register(user);
    }

    /**
     * 登录
     * @param email
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<User> login(@RequestParam("email") String email,
                                    @RequestParam("password") String password, HttpSession session){
        ResponseData<User> response = userService.login(email, password);
        if (response.isSuccess()){
            session.setAttribute(ServiceConstant.CURRENT_USER,response);
        }
        return response;
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping(value ="/logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData logout(HttpSession session){
        session.removeAttribute(ServiceConstant.CURRENT_USER);
        session.invalidate();
        return ResponseData.createBySuccess();
    }

    /**
     * 账号激活
     * @param userId
     * @param code
     * @return
     */
    @RequestMapping(value = "/userActivate.do",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData<String>  activate(@RequestParam("id") String userId,@RequestParam("code") String code){
        return userService.activate(userId,code);
    }

    /**
     * 忘记密码
     * @param email
     * @param password
     * @param code
     * @return
     */
    @RequestMapping(value = "/forgetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> forgetPassword(String email,String password,String code){
        return userService.forgetPassword(email,password,code);
    }

    /**
     * 修改密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value = "/modifyPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String>  modifyPassword(@RequestParam("passwordOld") String passwordOld,@RequestParam("passwordNew")String passwordNew,HttpSession session){
        ResponseData<User> responseData = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (responseData==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,null,null);
        }
        User user = responseData.getData();
        ResponseData<String> stringResponseData = userService.modifyPassword(user.getUserId(), passwordOld, passwordNew);
        if (stringResponseData.isSuccess()){
            session.removeAttribute(ServiceConstant.CURRENT_USER);
            return stringResponseData;
        }
        return stringResponseData;
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    @RequestMapping(value = "/sendCode.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> makeCode(String email){
        if ("".equals(email.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        return userService.makeCode(email);
    }

    /**
     * 检查邮箱是否已注册
     * @param email
     * @return
     */
    @RequestMapping(value = "/checkEmail.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> checkEmail(String email){
        if ("".equals(email.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        int count = userService.checkEmail(email);
        if (count > 0){
            return ResponseData.createByError();
        }
        return ResponseData.createBySuccess();
    }

}
