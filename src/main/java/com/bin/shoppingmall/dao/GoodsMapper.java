package com.bin.shoppingmall.dao;

import com.bin.shoppingmall.entity.Goods;
import com.bin.shoppingmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);
    List<Goods> findMallGoodsList(PageQueryUtil pageUtil);

    int getTotalMallGoods(PageQueryUtil pageUtil);
    int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);
    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);
    List<Goods> findMallGoodsListBySearch(PageQueryUtil pageUtil);
    int getTotalGoodsBySearch(PageQueryUtil pageUtil);
}