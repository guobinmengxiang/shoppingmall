package com.bin.shoppingmall.service;

import com.bin.shoppingmall.entity.Goods;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;

public interface GoodsService {

    String  save(Goods goods);
    Goods getGoods(Long goodsId);
    String update (Goods goods );
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallGoodsPage(PageQueryUtil pageUtil);
    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

}
