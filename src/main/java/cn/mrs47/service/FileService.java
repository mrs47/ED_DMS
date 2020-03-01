package cn.mrs47.service;

import cn.mrs47.common.ResponseData;
import cn.mrs47.pojo.User;
import cn.mrs47.vo.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mrs47
 */
public interface FileService {
    ResponseData uploadFile(String userId, MultipartFile file, String fileName, String filePath);

    ResponseData<PageInfo> selectAll(String userId, String fileName, Integer page, Integer many);

    ResponseData removeFile(String userId, String fileId);

    ResponseData uploadSoft(String userId, MultipartFile file, String softName, String version, String productId, String filePath);

    ResponseData<PageInfo> selectSoftAll(String userId, String softName, Integer page, Integer many);

    ResponseData removeSoft(String userId, String softId);

    String update(String deviceKey, String productKey);

    ResponseData checkFileName(String userId, String fileName);

    ResponseData pressInFile(User user, String softId, String fileId, String deviceId, String productId, String password);
}
