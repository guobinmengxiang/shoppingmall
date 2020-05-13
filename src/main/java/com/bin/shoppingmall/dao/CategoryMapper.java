package com.bin.shoppingmall.dao;

import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long categoryId);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Long categoryId);

    /**
     * 根据父分类和分类名称查询库中有没有数据
     *
     * @param categoryLevel
     * @param categoryName
     * @return
     */
    Category selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("category_name") String categoryName);

    /**
     * 更改分类
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 分页查询数据
     *
     * @param pageUtil
     * @return
     */
    List<Category> findGoodsCategoryList(PageQueryUtil pageUtil);

    /**
     * 查询总条数
     *
     * @param pageUtil
     * @return
     */
    int getTotalGoodsCategories(PageQueryUtil pageUtil);

    /**
     * delete
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);

    /**
     * 三级联动
     * @param parentIds
     * @param categoryLevel
     * @param number
     * @return
     */
    List<Category> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds, @Param("categoryLevel") int categoryLevel, @Param("number") int number);

}