package cn.mrs47.dao;

import cn.mrs47.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */

@Repository
public interface UserMapper {
    
    List<User> findAll();
    List<User> findByUserName(String userName);
    int findUserByEmail(@Param("email") String email);
    User findByPrimaryKey(Integer userId);
    User selectLogin(@Param("email") String email, @Param("password") String password);
    int checkPassword(@Param("password")String password, @Param("userId")String userId);
    int insertUser(User user);
    int selectUserByUserId(String userId);

    /**
     * 用户激活的参数校验
     * uid是否一致 code是否一致  status是否为0 同时成立
     * @param userId
     * @param code
     * @return
     */
    int selectActivate(@Param("userId")String userId, @Param("code")String code);

    /**
     * 用户激活的数据更新
     * 更新项status=1,oauthcode=''
     * @param userId
     * @return
     */
    int userActivate(@Param("userId")String userId);

    String findUserIdByEmail(String email);

    int updatePassword(@Param("userId") String userId, @Param("password") String password);
}
