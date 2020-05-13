package com.bin.shoppingmall.serviceImpl;
import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.dao.CategoryMapper;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.service.CategoryService;
import com.bin.shoppingmall.util.DateUtil;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Category> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        return CategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);//0代表查询所有
    }

}
