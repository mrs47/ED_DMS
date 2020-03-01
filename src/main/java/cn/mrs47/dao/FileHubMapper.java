package cn.mrs47.dao;

import cn.mrs47.pojo.FileHub;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */

@Repository
public interface FileHubMapper {
    int insertFile(FileHub fileHub);

    List<FileHub> selectAll(@Param("userId") String userId, @Param("fileName") String fileName, @Param("index") Integer index, @Param("many") Integer many);

    int selectCount(@Param("userId")String userId, @Param("fileName") String fileName);

    int removeFile(@Param("userId")String userId, @Param("fileId")String fileId);

    FileHub selectFileByFileId(@Param("userId")String userId, @Param("fileId")String fileId);

    int checkFileName(@Param("userId")String userId, @Param("fileName")String fileName);
}
