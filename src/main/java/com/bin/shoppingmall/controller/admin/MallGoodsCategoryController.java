package com.bin.shoppingmall.controller.admin;

import com.bin.shoppingmall.common.MallCategoryLevelEnum;
import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.service.CategoryService;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.Result;
import com.bin.shoppingmall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class MallGoodsCategoryController {
    @Resource
    CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param params page，limit，order
     * @return
     */
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty((String) params.get("page")) || StringUtils.isEmpty((String) params.get("limit")) || StringUtils.isEmpty((String) params.get("categoryLevel")) || StringUtils.isEmpty((String) params.get("parentId"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(categoryService.getCategoryPage(pageUtil));
    }

    /**
     * 新增分类信息
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    public Result save(@RequestBody Category category) {
        String result = categoryService.insertSelective(category);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);

        }

    }

    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Category category) {
        if (Objects.isNull(category.getCategoryId())
                || Objects.isNull(category.getCategoryLevel())
                || StringUtils.isEmpty(category.getCategoryName())
                || Objects.isNull(category.getParentId())
                || Objects.isNull(category.getCategoryRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = categoryService.updateCategorySelective(category);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数错误");
        }
        if (categoryService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @GetMapping("/categories")
    public String categoriesPage(HttpServletRequest request, @RequestParam("categoryLevel") Byte categoryLevel, @RequestParam("parentId") Long parentId, @RequestParam("backParentId") Long backParentId) {
        if (categoryLevel == null || categoryLevel < 1 || categoryLevel > 3) {
            return "error/error_5xx";
        }
        System.out.println("ssss" + backParentId);
        request.setAttribute("path", "mall_category");
        request.setAttribute("parentId", parentId);
        request.setAttribute("backParentId", backParentId);
        request.setAttribute("categoryLevel", categoryLevel);
        return "admin/mall_category";
    }

    @GetMapping("/coupling-test")
    public String couplingTest(HttpServletRequest request) {
        request.setAttribute("path", "coupling-test");
        //查询所有的一级分类
        //Collections.singletonList()返回的是不可变的集合，
        // 但是这个长度的集合只有1，可以减少内存空间。
        // 但是返回的值依然是Collections的内部实现类，
        // 同样没有add的方法，调用add，set方法会报错
        System.out.println("lce" + Collections.singletonList(0L));
        List<Category> firstLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<Category> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<Category> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                return "admin/coupling-test";
            }
        }
        return "error/error_5xx";
    }
    @RequestMapping(value = "/categories/listForSelect", method = RequestMethod.GET)
    @ResponseBody
    public Result listForSelect(@RequestParam("categoryId") Long categoryId) {
        System.out.println("ceshi ....");
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("缺少参数！");
        }
        Category category = categoryService.getGoodsCategoryById(categoryId);
        if (category == null || category.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map cateResult = new HashMap(2);

        if (category.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_ONE.getLevel()) {
            List<Category> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(category.getCategoryId()), MallCategoryLevelEnum.LEVEL_TWO.getLevel());
        if(!CollectionUtils.isEmpty(secondLevelCategories)){
            List<Category> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
            cateResult.put("secondLevelCategories",secondLevelCategories);
            cateResult.put("thirdLevelCategories",thirdLevelCategories);
        }
        }
        if (category.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
            //如果是二级分类则返回当前分类下的所有三级分类列表
            List<Category> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
            cateResult.put("thirdLevelCategories", thirdLevelCategories);
        }
return ResultGenerator.genSuccessResult(cateResult);
    }
}
