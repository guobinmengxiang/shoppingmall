package com.bin.shoppingmall.serviceImpl;
import com.bin.shoppingmall.common.MallCategoryLevelEnum;
import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.config.Constants;
import com.bin.shoppingmall.controller.vo.MallIndexCategoryVO;
import com.bin.shoppingmall.controller.vo.SearchPageCategoryVO;
import com.bin.shoppingmall.controller.vo.SecondLevelCategoryVO;
import com.bin.shoppingmall.controller.vo.ThirdLevelCategoryVO;
import com.bin.shoppingmall.dao.CategoryMapper;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.service.CategoryService;
import com.bin.shoppingmall.util.BeanUtil;
import com.bin.shoppingmall.util.DateUtil;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper CategoryMapper;

    /**
     * 分页查询
     *
     * @param pageUtil
     * @return
     * @return
     */
    @Override
    public PageResult getCategoryPage(PageQueryUtil pageUtil) {
        List<Category> list = new ArrayList<Category>();
        list = CategoryMapper.findGoodsCategoryList(pageUtil);
        int total = CategoryMapper.getTotalGoodsCategories(pageUtil);
        PageResult pageResult = new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * 新增分类信息
     *
     * @param category
     * @return
     */
    @Override
    public String insertSelective(Category category) {
        try {
            category.setCreateTime(DateUtil.dateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Category temp = CategoryMapper.selectByLevelAndName(category.getCategoryLevel(), category.getCategoryName());
        if (temp != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if (CategoryMapper.insertSelective(category) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    /**
     * 修改分类信息
     *
     * @param record
     * @return
     */
    @Override
    public String updateCategorySelective(Category record) {
        Category category = CategoryMapper.selectByPrimaryKey(record.getCategoryId());
        if (category == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        Category category1 = CategoryMapper.selectByLevelAndName(record.getCategoryLevel(), record.getCategoryName());
        if (category1 != null && !category1.getCategoryId().equals(record.getCategoryId())) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        record.setUpdateTime(new Date());
        if (CategoryMapper.updateByPrimaryKeySelective(record) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        return CategoryMapper.deleteBatch(ids) > 0;
    }

    @Override
    public Category getGoodsCategoryById(Long id) {
        return CategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MallIndexCategoryVO> getCategoriesForIndex() {
        List<MallIndexCategoryVO> mallIndexCategoryVOS = new ArrayList<>();
        //获取一级分类的固定数量的数据
        List<Category> firstLevelCategories = CategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
            //获取二级分类的数据
            List<Category> secondLevelCategories = CategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, MallCategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
                //获取三级分类的数据
                List<Category> thirdLevelCategories = CategoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds, MallCategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //根据 parentId 将 thirdLevelCategories 分组
                    Map<Long, List<Category>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(Category::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //处理二级分类
                    for (Category secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
                            List<Category> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //根据 parentId 将 thirdLevelCategories 分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (Category firstCategory : firstLevelCategories) {
                            MallIndexCategoryVO mallIndexCategoryVO = new MallIndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, mallIndexCategoryVO);
                            //如果该一级分类下有数据则放入 mallIndexCategoryVOS 对象中
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                mallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                mallIndexCategoryVOS.add(mallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return mallIndexCategoryVOS;
        } else {
            return null;
        }
    }

    @Override
    public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
        SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
        Category thirdLevelGoodsCategory = CategoryMapper.selectByPrimaryKey(categoryId);
        if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            //获取当前三级分类的二级分类
            Category secondLevelGoodsCategory = CategoryMapper.selectByPrimaryKey(thirdLevelGoodsCategory.getParentId());
            if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
                //获取当前二级分类下的三级分类List
                List<Category> thirdLevelCategories = CategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelGoodsCategory.getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel(), Constants.SEARCH_CATEGORY_NUMBER);
                searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
                return searchPageCategoryVO;
            }
        }
        return null;
    }

    public List<Category> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        return CategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);//0代表查询所有
    }

}
