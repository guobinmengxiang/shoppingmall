package com.bin.shoppingmall.service;

import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;

public interface CarouselService {
    PageResult getCarouselPage(PageQueryUtil pageUtil);
    String  insertSelective(Carousel record);
    Boolean deleteSelective(Integer[] ids);
    Carousel selectByPrimaryKey(Integer carouselId);
   String updateSelective(Carousel record);
}
