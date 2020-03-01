package cn.mrs47.service;

import cn.mrs47.common.ResponseData;

import java.util.Map;

/**
 * @author mrs47
 */
public interface OrderService {
    ResponseData subOrder(String userId, String goodsId, Integer number, String filePath);

    ResponseData payCallBack(Map<String, String> params);
}
