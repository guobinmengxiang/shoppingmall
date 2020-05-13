package com.bin.shoppingmall.serviceImpl;

import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.dao.GoodsMapper;
import com.bin.shoppingmall.entity.Goods;
import com.bin.shoppingmall.service.GoodsService;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsMapper goodsMapper;
    @Override
    public String save(Goods goods) {
        if(goodsMapper.insertSelective(goods)>0){
            return  ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Goods getGoods(Long goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public String  update(Goods goods) {
        Goods temp=goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if(temp==null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if(goodsMapper.updateByPrimaryKeySelective(goods)>0){
            return   ServiceResultEnum.SUCCESS.getResult();
        }
        return  ServiceResultEnum.DB_ERROR.getResult();
    }
    @Override
        public PageResult getMallGoodsPage(PageQueryUtil pageUtil) {
        List<Goods> goodsList = goodsMapper.findMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override

    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }
}
