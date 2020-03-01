package cn.mrs47.dao;

import cn.mrs47.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */

@Repository
public interface ProductMapper {
    int insertProduct(Product product);

    List selectAll(@Param("userId") String userId, @Param("productName") String productName, @Param("index") Integer index, @Param("many") Integer many);

    int removeProductByProductId(@Param("userId") String userId, @Param("productId") String productId);

    int selectCount(@Param("userId") String userId, @Param("productName") String productName);

    int checkProductEmpty(@Param("productId")  String productId);

    String selectTokenByProductKey(@Param("productKey")String productKey);

    int checkProductName(@Param("userId")String userId, @Param("productName")String productName);

    String selectProductNameById(@Param("productId")String productId);

    int checkProductId(@Param("productId")String productId, @Param("userId")String userId);

    String selectProductKeyByProductId(@Param("userId")String userId,@Param("productId")String productId);
}
