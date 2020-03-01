package cn.mrs47.dao;

import cn.mrs47.pojo.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mrs47
 */
@Repository
public interface CategoryMapper {

    List<Category> selectCategory();
}
