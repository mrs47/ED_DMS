package cn.mrs47.service;

import cn.mrs47.common.ResponseData;
import cn.mrs47.pojo.Product;
import cn.mrs47.vo.PageInfo;

/**
 * @author 56597
 */
public interface ProductService {
    ResponseData addProduct(String userId, String productName, String category, String token, String remark);

    ResponseData<PageInfo<Product>> selectAll(String userId, String productName,Integer page, Integer every);

    ResponseData removeProduct(String userId, String productId);

    ResponseData checkProductEmpty(String productId);

    ResponseData getCategory();

    ResponseData checkProductName(String userId, String productName);

    ResponseData<String> selectSoft(String productId);
}
