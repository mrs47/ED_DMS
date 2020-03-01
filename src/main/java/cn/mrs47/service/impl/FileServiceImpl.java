package cn.mrs47.service.impl;

import cn.mrs47.common.ResponseData;
import cn.mrs47.common.ServiceConstant;
import cn.mrs47.dao.*;
import cn.mrs47.pojo.*;
import cn.mrs47.service.FileService;
import cn.mrs47.util.*;
import cn.mrs47.vo.FileInfo;
import cn.mrs47.vo.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author mrs47
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileHubMapper fileHubMapper;
    @Autowired
    SoftHubMapper softHubMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public ResponseData uploadFile(String userId, MultipartFile file, String fileName, String filePath) {
        ResponseData responseData = checkFileName(userId, fileName);
        if (!responseData.isSuccess()) {
            return ResponseData.createByErrorMessage("文件名重复");
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String fileId = IdUtil.getFileId();
        String realFilename = fileId + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, filePath, realFilename);

        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(filePath, realFilename);
        String path = "files/" + userId + "/";
        //写入数据库
        FileHub fileHub = new FileHub();
        fileHub.setFileId(fileId);
        fileHub.setUserId(userId);
        fileHub.setFileName(fileName);
        fileHub.setFilePath(path + realFilename);
        fileHub.setHost(FTPUtil.getPropertiesValue("ftp.server.ip"));
        int n = fileHubMapper.insertFile(fileHub);
        if (n < 1) {
            return ResponseData.createByErrorMessage("新增失败");
        }
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(path, Lists.newArrayList(targetFile));
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            e.printStackTrace();
            return ResponseData.createByErrorMessage("上传失败");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            targetFile.delete();
        }
        return ResponseData.createBySuccessMessage("上传成功");
    }

    @Override
    public ResponseData<PageInfo> selectAll(String userId, String fileName, Integer page, Integer many) {
        int count = fileHubMapper.selectCount(userId, fileName);
        if (count < 1) {
            return ResponseData.createBySuccessMessage("无数据");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (many == null || many < 1) {
            many = 8;
        }
        Integer totalPage = (count % many == 0) ? (count / many) : (count / many + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        Integer index = (page - 1) * many;
        List<FileHub> files = fileHubMapper.selectAll(userId, fileName, index, many);
        PageInfo pageInfo = new PageInfo(count, many, page, files);
        return ResponseData.createBySuccess(pageInfo);
    }

    @Override
    @Transactional
    public ResponseData removeFile(String userId, String fileId) {
        FileHub file = fileHubMapper.selectFileByFileId(userId, fileId);
        if (file == null) {
            return ResponseData.createByErrorMessage("删除失败");
        }
        int n = fileHubMapper.removeFile(userId, fileId);
        if (n < 1) {
            return ResponseData.createByErrorMessage("删除失败");
        }
        String filePath = file.getFilePath();
        String[] split = filePath.split("/");
        String filename = split[split.length - 1];
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            path.append(split[i] + "/");
        }
        boolean isSuccess = false;
        try {
            isSuccess = FTPUtil.removeFile(path.toString(), Lists.newArrayList(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isSuccess) {
            try {
                //回滚
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return ResponseData.createByErrorMessage("删除失败");
            }
        }
        return ResponseData.createBySuccessMessage("删除成功");
    }

    @Override
    @Transactional
    public ResponseData uploadSoft(String userId, MultipartFile file, String softName, String version, String productId, String filePath) {

        if (softName == null || "".equals(softName.trim())) {
            return ResponseData.createByErrorMessage("参数错误");
        }
        if (version == null || "".equals(version.trim())) {
            version = "v1.0";
        }
        boolean isEmpty = checkProductId(productId, userId);

        if (isEmpty) {
            return ResponseData.createByErrorMessage("产品名不存在");
        }
        isEmpty = checkSoftNameAndVersion(productId, softName, version);
        if (!isEmpty) {
            return ResponseData.createByErrorMessage("软件版本重复");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String softId = IdUtil.getFileId();
        String realFilename;
        if (fileExtensionName == null || "".equals(fileExtensionName.trim())) {
            realFilename = softId;
        } else {
            realFilename = softId + "." + fileExtensionName;
        }

        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", softName, filePath, realFilename);

        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(filePath, realFilename);

        String path = "soft/" + userId + "/" + productId + "/";
        String productName = productMapper.selectProductNameById(productId);
        //写入数据库
        SoftHub softHub = new SoftHub();
        softHub.setSoftId(softId);
        softHub.setProductId(productId);
        softHub.setProductName(productName);
        softHub.setSoftName(softName);
        softHub.setVersion(version);
        softHub.setPath(path + realFilename);
        softHub.setHost(FTPUtil.getPropertiesValue("ftp.server.ip"));

        int n = softHubMapper.insertSoft(softHub);
        if (n < 1) {
            return ResponseData.createByErrorMessage("新增失败");
        }
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(path, Lists.newArrayList(targetFile));
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            e.printStackTrace();
            //无法正常回滚
            return ResponseData.createByErrorMessage("上传失败");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            targetFile.delete();
        }
        return ResponseData.createBySuccessMessage("上传成功");
    }

    @Override
    public ResponseData<PageInfo> selectSoftAll(String userId, String softName, Integer page, Integer many) {
        int count = softHubMapper.selectCount(userId, softName);
        if (count < 1) {
            return ResponseData.createBySuccessMessage("无数据");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (many == null || many < 1) {
            many = 8;
        }
        Integer totalPage = (count % many == 0) ? (count / many) : (count / many + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        Integer index = (page - 1) * many;
        List<SoftHub> files = softHubMapper.selectAll(userId, softName, index, many);
        PageInfo pageInfo = new PageInfo(count, many, page, files);
        return ResponseData.createBySuccess(pageInfo);
    }

    @Override
    @Transactional
    public ResponseData removeSoft(String userId, String softId) {
        SoftHub soft = softHubMapper.selectSoftByFileId(userId, softId);
        if (soft == null) {
            return ResponseData.createByErrorMessage("删除失败");
        }
        int n = softHubMapper.removeSoft(userId, softId);
        if (n < 1) {
            return ResponseData.createByErrorMessage("删除失败");
        }
        String filePath = soft.getPath();
        String[] split = filePath.split("/");
        String filename = split[split.length - 1];
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            path.append(split[i] + "/");
        }
        boolean isSuccess = false;
        try {
            isSuccess = FTPUtil.removeFile(path.toString(), Lists.newArrayList(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isSuccess) {
            try {
                //回滚
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return ResponseData.createByErrorMessage("删除失败");
            }
        }
        return ResponseData.createBySuccessMessage("删除成功");
    }

    /**
     * 设备检查是否有软件更新或者文件推送
     *
     * @param deviceKey
     * @param productKey
     * @return
     */
    @Override
    public String update(String deviceKey, String productKey) {
        Device device = deviceMapper.selectDeviceByProductKeyAndDeviceKey(productKey, deviceKey);
        if (device == null) {
            return "";
        }
        String token = productMapper.selectTokenByProductKey(productKey);
        // 检查统一推送
        Jedis jedis = RedisUtil.getJedis();
        String info = jedis.lpop(ServiceConstant.REDIS_MQ_FILE +device.getDeviceId());
        jedis.close();
        if (info == null){
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCode(70);
            fileInfo.setRandom("0000000000000000");
            fileInfo.setDeviceKey(deviceKey);
            fileInfo.setProductKey(productKey);
            return AESUtil.encrypt(new Gson().toJson(fileInfo), token, fileInfo.getRandom());
        }
        return AESUtil.encrypt(info, token, "0000000000000000");
    }

    private boolean checkSoftNameAndVersion(String productId, String softName, String version) {
        int n = softHubMapper.checkSoftNameAndVersion(productId, softName, version);
        if (n < 1) {
            return true;
        }
        return false;
    }

    private boolean checkProductId(String productId, String userId) {
        int i = productMapper.checkProductId(productId, userId);
        if (i < 1) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseData checkFileName(String userId, String fileName) {
        int n = fileHubMapper.checkFileName(userId, fileName);
        if (n > 0) {
            return ResponseData.createByError();
        }
        return ResponseData.createBySuccess();
    }

    @Override
    public ResponseData pressInFile(User user, String softId, String fileId, String deviceId, String productId, String password) {
        User isNull = userMapper.selectLogin(user.getEmail(), MD5Util.MD5EncodeUtf8(password));
        if (isNull == null){
            return ResponseData.createByErrorMessage("密码错误");
        }
        String tempProductId = deviceMapper.selectProductIdByDeviceId(deviceId);

        FileInfo fileInfo = new FileInfo();
        String productKey = productMapper.selectProductKeyByProductId(user.getUserId(), tempProductId);
        fileInfo.setProductKey(productKey);
        fileInfo.setCode(71);
        fileInfo.setRandom("0000000000000000");
       if (softId != null){
           SoftHub softHub = softHubMapper.selectSoftByFileId(user.getUserId(), softId);
           if(softHub == null){
               return ResponseData.createByErrorMessage("参数错误");
           }
           FileControlInfo fileControlInfo = new FileControlInfo();
           fileControlInfo.setTag(1);
           fileControlInfo.setHost(softHub.getHost());
           fileControlInfo.setName(softHub.getSoftName());
           fileControlInfo.setPassword(PropertiesUtil.getProperty("ftp.properties", "ftp.pass"));
           fileControlInfo.setUserName(PropertiesUtil.getProperty("ftp.properties", "ftp.user"));
           fileControlInfo.setUri(softHub.getPath());
           fileControlInfo.setVersion(softHub.getVersion());
           fileControlInfo.setPort(PropertiesUtil.getProperty("ftp.properties", "ftp.port"));
           fileInfo.setData(fileControlInfo);
       }else if (fileId != null){
            FileHub fileHub = fileHubMapper.selectFileByFileId(user.getUserId(), fileId);
           if(fileHub == null){
               return ResponseData.createByErrorMessage("参数错误");
           }
            FileControlInfo fileControlInfo = new FileControlInfo();
            fileControlInfo.setTag(0);
            fileControlInfo.setHost(fileHub.getHost());
            fileControlInfo.setName(fileHub.getFileName());
            fileControlInfo.setPassword(PropertiesUtil.getProperty("ftp.properties", "ftp.pass"));
            fileControlInfo.setUserName(PropertiesUtil.getProperty("ftp.properties", "ftp.user"));
            fileControlInfo.setUri(fileHub.getFilePath());
            fileControlInfo.setPort(PropertiesUtil.getProperty("ftp.properties", "ftp.port"));
            fileInfo.setData(fileControlInfo);
        }else {
           return ResponseData.createByErrorMessage("参数错误");
       }
        Jedis jedis = RedisUtil.getJedis();
        if (productId != null) {
            List<Device> devices = deviceMapper.selectDeviceByProductId(productId);
            for (Device device : devices) {
                fileInfo.setDeviceKey(device.getDeviceKey());
                jedis.lpush(ServiceConstant.REDIS_MQ_FILE + device.getDeviceId(), new Gson().toJson(fileInfo));
            }
        }
        if (deviceId != null) {
            String deviceKey = deviceMapper.selectDeviceKeyByDeviceId(deviceId);
            fileInfo.setDeviceKey(deviceKey);
            jedis.lpush(ServiceConstant.REDIS_MQ_FILE  + deviceId, new Gson().toJson(fileInfo));
        }
        jedis.close();
        return ResponseData.createBySuccessMessage("命令已入队列，约5分钟后生效");
    }
}
