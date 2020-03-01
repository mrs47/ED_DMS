package cn.mrs47.controller;


import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.User;
import cn.mrs47.service.OrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author mrs47
 */
@Controller
@RequestMapping("/pay/")
public class AliPayController {

    private static  final Logger logger = LoggerFactory.getLogger(AliPayController.class);
    @Autowired
    private OrderService orderService;


    /**
     * 提交订单返回二维码
     * @param session
     * @param goodsId
     * @param number
     * @return
     */
    @RequestMapping(value = "subOrder.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData subOrder(HttpSession session,String goodsId,Integer number){

        ResponseData<User> attribute = (ResponseData<User>)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        if (goodsId==null || number == null ){
            return ResponseData.createByErrorMessage("参数错误");
        }
        String filePath = session.getServletContext().getRealPath("/qrFile");
        return orderService.subOrder(attribute.getData().getUserId(),goodsId,number,filePath);
    }

    /**
     * 支付宝回调函数
     * @param request
     * @return
     */
    @RequestMapping(value = "payCallBack.do",method = RequestMethod.POST)
    @ResponseBody
    public Object payCallBack(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }

        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ResponseData.createByErrorMessage("非法请求,验证不通过");
        }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }
        ResponseData response = orderService.payCallBack(params);
        if(response.isSuccess()){
            return ResponseCode.AILIPAY_SUCCESS;
        }
        return ResponseCode.AILIPAY_FAILED;
    }
}
