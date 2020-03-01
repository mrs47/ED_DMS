package cn.mrs47.dao;

import cn.mrs47.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mrs47
 */

@Repository
public interface OrderMapper {

    int insertOrder(Order order);

    Order selectByOrderNo(String orderNo);

    int updateOrderByOrderId(@Param("order") Order order);
}
