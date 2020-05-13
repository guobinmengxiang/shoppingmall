package com.bin.shoppingmall.dao;

import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarouselMapper {
    int deleteByPrimaryKey(Integer carouselId);

    int insert(Carousel record);
    //新增轮播图
    int insertSelective(Carousel record);
    //删除轮播图
     int deleteBatch(Integer [] ids);
     //获取单条数据
    Carousel selectByPrimaryKey(Integer carouselId);
    //修改数据
    int updateByPrimaryKeySelective(Carousel record);

    int updateByPrimaryKey(Carousel record);

    /**
     * 分页查询数据
     * @param pageUtil
     * @return
     */
    List<Carousel> findCarouselList(PageQueryUtil pageUtil);

    /**
     * 查询总条数
     * @param pageUtil
     * @return
     */
    int getTotalCarousels(PageQueryUtil pageUtil);
}