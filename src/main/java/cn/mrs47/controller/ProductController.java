package cn.mrs47.controller;

import cn.mrs47.common.ResponseCode;
import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.pojo.Product;
import cn.mrs47.pojo.User;
import cn.mrs47.service.ProductService;
import cn.mrs47.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author mrs47
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 新增产品
     * @param session
     * @param productName
     * @param category
     * @param token
     * @param remark
     * @return
     */
    @RequestMapping(value = "/addProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> addProduct(HttpSession session,String productName,String category,String token,String remark){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String userId = attribute.getData().getUserId();
        ResponseData responseData = productService.addProduct(userId,productName, category, token, remark);
        return responseData;
    }

    /**
     * 搜索产品
     * @param session
     * @param productName
     * @param page
     * @param every
     * @return
     */
    @RequestMapping(value = "/selectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<PageInfo<Product>> selectAll(HttpSession session,String productName,Integer page,Integer every){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String userId = attribute.getData().getUserId();
        return productService.selectAll(userId,productName, page, every);
    }

    /**
     * 移除产品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/removeProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<PageInfo<Product>> removeProduct(HttpSession session,String productId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String userId = attribute.getData().getUserId();
        ResponseData responseData = productService.removeProduct(userId, productId);
        return responseData;
    }

    /**
     * 获取分类
     * @param session
     * @return
     */
    @RequestMapping(value = "/getCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> getCategory(HttpSession session){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        ResponseData responseData = productService.getCategory();
        return responseData;
    }

    /**
     * 检查产品名是否冲突
     * @param session
     * @param productName
     * @return
     */
    @RequestMapping(value = "/checkProductName.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> checkProductName(HttpSession session,String productName){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        String userId = attribute.getData().getUserId();
        ResponseData responseData = productService.checkProductName(userId,productName);
        return responseData;
    }

    /**
     * 按产品搜索软件
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/selectSoft.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> selectSoft(HttpSession session,String productId){
        ResponseData<User> attribute = (ResponseData)session.getAttribute(ServiceConstant.CURRENT_USER);
        if (attribute==null){
            return ResponseData.createByError(ResponseCode.NEED_LOGIN,"未登录",null);
        }
        return  productService.selectSoft(productId);
    }
}
