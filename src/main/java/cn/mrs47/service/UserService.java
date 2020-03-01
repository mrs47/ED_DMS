package cn.mrs47.service;

import cn.mrs47.common.ResponseData;
import cn.mrs47.pojo.User;

/**
 * @author mrs47
 */
public interface UserService {
    ResponseData<String> register(User user);
    ResponseData<User> login(String email,String password);
    int checkEmail(String email);
    ResponseData<String> activate(String userId,String code);

    ResponseData<String> forgetPassword(String email,String password, String code);

    ResponseData<String> makeCode(String userId);

    ResponseData<String> modifyPassword(String userId, String passwordOld, String passwordNew);
}
