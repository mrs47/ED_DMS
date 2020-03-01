package cn.mrs47.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mrs47
 */
@Repository
public interface BalanceMapper {

    int updateBalance(@Param("userId") String userId, @Param("balance") Integer balance);

    Integer selectBalanceByUserId(@Param("userId")String userId);

    int insertBalance(@Param("userId")String userId);
}
