package cn.mrs47.service.impl;

import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.dao.BalanceMapper;
import cn.mrs47.dao.GoodsMapper;
import cn.mrs47.dao.OrderMapper;
import cn.mrs47.pojo.Goods;
import cn.mrs47.pojo.Order;
import cn.mrs47.service.OrderService;
import cn.mrs47.util.IdUtil;
import cn.mrs47.util.PropertiesUtil;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mrs47
 */

@Service("orderService")

public class OrderServiceImpl implements OrderService {


    private static Log log = LogFactory.getLog(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    private static  AlipayTradeService tradeService;


    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");
        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    @Override
    @Transactional
    public ResponseData subOrder(String userId, String goodsId, Integer number, String filePath) {

       Goods goods = goodsMapper.selectPriceByGoodsId(goodsId);
        if (goods == null){
            return ResponseData.createByErrorMessage("无此商品");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(IdUtil.getOrderId());
        order.setGoodsId(goodsId);
        order.setAmount(goods.getPrice().multiply(new BigDecimal(number)));
        order.setStatus(ServiceConstant.SUB_ORDER);
        order.setBalance(number);


        int count = orderMapper.insertOrder(order);
        if (count < 1){
            return ResponseData.createByErrorMessage("创建订单失败");
        }
        filePath+="/";
        File fileDir = new File(filePath);
        System.out.println(filePath);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderId();
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "嵌入式管理系统当面付扫码消费";
        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getAmount().toString();
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买"+goods.getGoodsName()+"商品"+number+"件共"+order.getAmount()+"元";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "10000";
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        // 支付超时，定义为6分钟
        String timeoutExpress = "6m";


        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance(goods.getGoodsId(), goods.getGoodsName(), goods.getPrice().multiply(new BigDecimal(100)).longValue(), number);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl(PropertiesUtil.getProperty("zfbinfo.properties","call_back"))
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);

        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                // 需要修改为运行机器上的路径
                String path = String.format(filePath+"qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + path);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, path);
               return ResponseData.createBySuccess(String.format("qrFile/qr-%s.png",
                       response.getOutTradeNo()));
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ResponseData.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ResponseData.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ResponseData.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    @Override
    @Transactional
    public ResponseData payCallBack(Map<String, String> params) {

        String orderNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        String buyerId = params.get("buyer_id");
        String tradeStatus = params.get("trade_status");

        Order order = orderMapper.selectByOrderNo(orderNo);
        order.setTradeNo(tradeNo);
        order.setBuyerId(buyerId);

        if(order == null){
            return ResponseData.createByErrorMessage("非本站的订单,回调忽略");
        }
        if(order.getStatus() >= ServiceConstant.PAID){
            return ResponseData.createBySuccess("支付宝重复调用");
        }
        if(ServiceConstant.AILIPAY_TRADE_SUCCESS.equals(tradeStatus)){
            try {
                order.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(params.get("gmt_payment")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            order.setStatus(ServiceConstant.PAID);
            int count = orderMapper.updateOrderByOrderId(order);
            if (count < 1){
                return ResponseData.createByError();
            }
            Integer balance = balanceMapper.selectBalanceByUserId(order.getUserId());
            count = balanceMapper.updateBalance(order.getUserId(),balance+order.getBalance());
            if (count < 1){
                return ResponseData.createByErrorMessage("余额更新失败");
            }
        }
        return ResponseData.createBySuccess();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
}
