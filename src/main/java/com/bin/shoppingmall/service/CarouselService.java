package com.bin.shoppingmall.service;

import com.bin.shoppingmall.controller.vo.MallIndexCarouselVO;
import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;

import java.util.List;

public interface CarouselService {
    PageResult getCarouselPage(PageQueryUtil pageUtil);
    String  insertSelective(Carousel record);
    Boolean deleteSelective(Integer[] ids);
    Carousel selectByPrimaryKey(Integer carouselId);
   String updateSelective(Carousel record);
    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<MallIndexCarouselVO> getCarouselsForIndex(int number);
}
