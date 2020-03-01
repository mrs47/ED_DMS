package cn.mrs47.dao;

import cn.mrs47.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mrs47
 */

@Repository
public interface GoodsMapper {
    Goods selectPriceByGoodsId(@Param("goodsId") String goodsId);
}
