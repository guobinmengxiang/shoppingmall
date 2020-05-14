package com.bin.shoppingmall.service;

import com.bin.shoppingmall.controller.vo.MallIndexCategoryVO;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryService {
    PageResult getCategoryPage(PageQueryUtil pageUtil);
    String  insertSelective(Category category);
    String updateCategorySelective(Category record);
    //Category selectByLevelAndName(Byte categoryLevel, String categoryName);
    Boolean deleteBatch(Integer[] ids);
    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
     List<Category> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
      Category getGoodsCategoryById(Long id);
    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<MallIndexCategoryVO> getCategoriesForIndex();

}
