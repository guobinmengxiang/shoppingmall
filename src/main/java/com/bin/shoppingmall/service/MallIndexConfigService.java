package com.bin.shoppingmall.service;

import com.bin.shoppingmall.controller.vo.MallIndexConfigGoodsVO;
import com.bin.shoppingmall.entity.Category;
import com.bin.shoppingmall.entity.IndexConfig;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;

import java.util.List;

public interface MallIndexConfigService {
    PageResult getConfigsPage(PageQueryUtil pageUtil);
    String saveIndexConfig(IndexConfig indexConfig);
    Boolean deleteIndexConfig(Long[] ids);
    String updateIndexConfig(IndexConfig indexConfig);
    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);
    IndexConfig getIndexConfigById(Long id);

}
