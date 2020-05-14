package com.bin.shoppingmall.serviceImpl;

import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.controller.vo.MallIndexCarouselVO;
import com.bin.shoppingmall.dao.CarouselMapper;
import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.service.CarouselService;
import com.bin.shoppingmall.util.BeanUtil;
import com.bin.shoppingmall.util.DateUtil;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Resource
    CarouselMapper carouselmapper;

    /**
     * 分页查询
     * @param pageUtil
     * @return
     */
    @Override
    public PageResult getCarouselPage(PageQueryUtil pageUtil) {
     List<Carousel> list=carouselmapper.findCarouselList(pageUtil);
     int total=carouselmapper.getTotalCarousels(pageUtil);
     PageResult page = new PageResult(list,total,pageUtil.getLimit(),pageUtil.getPage());
        return page;
    }

    /**
     * 新增轮播图
     * @param record
     * @return
     */
    @Override
    public String insertSelective(Carousel record) {
        try {
            record.setCreateTime(DateUtil.dateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(carouselmapper.insertSelective(record)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    /**
     * 删除轮播图
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteSelective(Integer[] ids) {
        if(ids.length<1){
            return  false;
        }
        return carouselmapper.deleteBatch(ids)>0;
    }

    /**
     * 根据id查询数据
     * @param carouselId
     * @return
     */
    @Override
    public Carousel selectByPrimaryKey(Integer carouselId) {

        return carouselmapper.selectByPrimaryKey(carouselId);
    }

    /**
     * 修改数据
     * @param record
     * @return
     */
    @Override
    public String updateSelective(Carousel record) {
       // 根据id 查询数据
        Carousel temp = carouselmapper.selectByPrimaryKey(record.getCarouselId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(record.getCarouselRank());
        temp.setRedirectUrl(record.getRedirectUrl());
        temp.setCarouselUrl(record.getCarouselUrl());
        try {
            temp.setUpdateTime(DateUtil.dateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (carouselmapper.updateByPrimaryKeySelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public List<MallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<MallIndexCarouselVO> mallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselmapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            mallIndexCarouselVOS = BeanUtil.copyList(carousels, MallIndexCarouselVO.class);
        }
        return mallIndexCarouselVOS;
    }
}
