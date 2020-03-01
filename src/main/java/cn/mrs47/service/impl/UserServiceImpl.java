package cn.mrs47.service.impl;

import cn.mrs47.common.ResponseData;
import cn.mrs47.common.TokenCache;
import cn.mrs47.dao.BalanceMapper;
import cn.mrs47.dao.UserMapper;
import cn.mrs47.pojo.EmailMessage;
import cn.mrs47.pojo.User;
import cn.mrs47.service.UserService;
import cn.mrs47.util.EmailUtil;
import cn.mrs47.util.IdUtil;
import cn.mrs47.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mrs47
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BalanceMapper balanceMapper;
    @Override
    public ResponseData<String> register(User user) {
        if("".equals(user.getEmail().trim()) || "".equals(user.getPassword().trim()) || "".equals(user.getUserName().trim())){
            return ResponseData.createByErrorMessage("参数不能为空");
        }
        int n = checkEmail(user.getEmail());
        if (n>0){
            return ResponseData.createByErrorMessage("邮箱已占用！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setUserId(IdUtil.getUserId());
        user.setStatus(0);
        String oauthCode = IdUtil.getIdCode();
        user.setOauthCode(oauthCode);
        int i = userMapper.insertUser(user);
        if (i<1){
            return ResponseData.createByErrorMessage("注册失败！");
        }
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setUserName(user.getUserName());
        emailMessage.setUserEmail(user.getEmail());
        emailMessage.setUrl("id="+user.getUserId()+"&code="+user.getOauthCode());
        try {
            EmailUtil.sendRegisterEmail(emailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = balanceMapper.insertBalance(user.getUserId());
        if (count<1){
            return ResponseData.createByErrorMessage("注册失败！");
        }

        return ResponseData.createBySuccessMessage("注册成功！");
    }

    @Override
    public ResponseData<User> login(String email, String password) {
        int i = checkEmail(email);
        if (i == 0){
            return ResponseData.createByErrorMessage("用户不存在");
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(email,MD5Password);
        if (user==null){
            return ResponseData.createByErrorMessage("密码错误");
        }
        if (user.getStatus() == 0){
            return ResponseData.createByErrorMessage("用户未激活");
        }
        user.setPassword("");
        user.setStatus(0);
        user.setOauthCode("");
        return ResponseData.createBySuccess(user);
    }

    @Override
    public int checkEmail(String email) {
        return  userMapper.findUserByEmail(email);
    }

    @Override
    public ResponseData<String> activate(String userId, String code) {
        if ("".equals(userId.trim()) || "".equals(code.trim())){
            return ResponseData.createByErrorMessage("参数错误");
        }
        if (userMapper.selectActivate(userId,code) < 1){
            return ResponseData.createByErrorMessage("激活失败");
        }
        if (userMapper.userActivate(userId) < 1){
            return ResponseData.createByErrorMessage("激活失败");
        }
        return ResponseData.createBySuccessMessage("激活成功");
    }

    @Override
    public ResponseData<String> forgetPassword(String email, String password, String code) {
        String userId = userMapper.findUserIdByEmail(email);
        if(userId==null){
            return ResponseData.createByErrorMessage("邮箱有误");
        }
        String key = TokenCache.getKey(userId);
        if (!code.equals(key)){
            return ResponseData.createByErrorMessage("验证码错误");
        }
        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        int i=userMapper.updatePassword(userId,MD5Password);
        if (i < 1){
            return ResponseData.createByErrorMessage("密码修改失败");
        }
        TokenCache.setKey(userId,IdUtil.getIdCode());
        return ResponseData.createBySuccessMessage("密码修改成功");
    }

    @Override
    public ResponseData<String> makeCode(String email) {
        String userId = userMapper.findUserIdByEmail(email);
        if(userId==null){
            return ResponseData.createByErrorMessage("邮箱有误");
        }
        //产生随机 用于账号激活认证 使用Guava缓存
        String idCode = IdUtil.getIdCode();
        TokenCache.setKey(userId, idCode);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setUserEmail(email);
        emailMessage.setCode(idCode);
        try {
            EmailUtil.sendForgetEmail(emailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.createBySuccess();
    }

    @Override
    public ResponseData<String> modifyPassword(String userId, String passwordOld, String passwordNew) {
        String password = MD5Util.MD5EncodeUtf8(passwordOld);
        int i = userMapper.checkPassword(password, userId);
        if (i < 1){
            return ResponseData.createByErrorMessage("密码错误");
        }
        password = MD5Util.MD5EncodeUtf8(passwordNew);
        int n = userMapper.updatePassword(userId, password);
        if (n < 1){
            return ResponseData.createByErrorMessage("修改失败");
        }
        return ResponseData.createBySuccessMessage("密码修改成功");
    }
}
