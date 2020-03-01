package cn.mrs47.dao;

import cn.mrs47.pojo.SoftHub;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */


@Repository
public interface SoftHubMapper {
    int checkSoftNameAndVersion(@Param("productId") String productId, @Param("softName") String softName, @Param("version") String version);

    int insertSoft(SoftHub softHub);

    int selectCount(@Param("userId") String userId, @Param("softName") String softName);

    List<SoftHub> selectAll(@Param("userId") String userId, @Param("softName")String softName, @Param("index") Integer index, @Param("many") Integer many);

    SoftHub selectSoftByFileId(@Param("userId")String userId, @Param("softId")String softId);

    int removeSoft(@Param("userId")String userId, @Param("softId")String softId);

    List<SoftHub> selectSoftByProductId(@Param("productId")String productId);
}
