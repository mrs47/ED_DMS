package cn.mrs47.service.impl;

import cn.mrs47.common.ResponseData;
import cn.mrs47.dao.CategoryMapper;
import cn.mrs47.dao.ProductMapper;
import cn.mrs47.dao.SoftHubMapper;
import cn.mrs47.pojo.Category;
import cn.mrs47.pojo.Product;
import cn.mrs47.pojo.SoftHub;
import cn.mrs47.service.ProductService;
import cn.mrs47.util.IdUtil;
import cn.mrs47.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mrs47
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    SoftHubMapper softHubMapper;
    @Override
    public ResponseData addProduct(String userId, String productName, String category, String token, String remark) {
        Product product=new Product();
        product.setProductId(IdUtil.getProductId());
        product.setProductName(productName);
        product.setUserId(userId);
        product.setProductKey(IdUtil.getProductKey());
        product.setCategoryId(category);
        product.setToken(token);
        product.setRemark(remark);
        int n=productMapper.insertProduct(product);
        if (n<1){
            return ResponseData.createByErrorMessage("新增失败");
        }
        return ResponseData.createBySuccessMessage("新增成功");
    }

    @Override
    public ResponseData<PageInfo<Product>> selectAll(String userId, String productName,Integer page, Integer every) {

        int count = productMapper.selectCount(userId,productName);
        if (count<1){
            return ResponseData.createByErrorMessage("无数据");
        }
        if ( page==null || page<1  ){
            page=1;
        }
        if(every==null || every<1  ){
            every=8;
        }
        Integer totalPage=(count%every==0)?(count/every):(count/every+1);
        if (page>totalPage){
            page=totalPage;
        }
        Integer index=(page-1)*every;
        Integer many=every;

        List products=productMapper.selectAll(userId,productName,index,many);
        PageInfo<Product> pageInfo=new PageInfo(count,every,page,products);
        return ResponseData.createBySuccess(pageInfo);
    }

    @Override
    public ResponseData removeProduct(String userId, String productId) {
        ResponseData responseData = checkProductEmpty(productId);
        if (!responseData.isSuccess()){
            return responseData;
        }
        int n=productMapper.removeProductByProductId(userId,productId);
        if (n<1){
            return ResponseData.createByErrorMessage("删除失败");
        }
        return ResponseData.createBySuccessMessage("删除成功");
    }


    @Override
    public ResponseData checkProductEmpty(String productId){
        int count = productMapper.checkProductEmpty(productId);
        if (count > 0){
            return ResponseData.createByErrorMessage("产品设备不为空");
        }
        return ResponseData.createBySuccess();
    }

    @Override
    public ResponseData getCategory() {
        List<Category> list = categoryMapper.selectCategory();
        if (list == null){
            return ResponseData.createByErrorMessage("分类为空");
        }
        return ResponseData.createBySuccess(list);
    }

    @Override
    public ResponseData checkProductName(String userId, String productName) {
        int count = productMapper.checkProductName(userId,productName);
        if (count > 0){
            return ResponseData.createByError();
        }
        return ResponseData.createBySuccess();
    }

    @Override
    public ResponseData selectSoft(String productId) {
        List<SoftHub> list = softHubMapper.selectSoftByProductId(productId);
        return ResponseData.createBySuccess(list);
    }
}
