package com.bin.shoppingmall.serviceImpl;

import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.controller.vo.MallIndexConfigGoodsVO;
import com.bin.shoppingmall.dao.GoodsMapper;
import com.bin.shoppingmall.dao.IndexConfigMapper;
import com.bin.shoppingmall.entity.Goods;
import com.bin.shoppingmall.entity.IndexConfig;
import com.bin.shoppingmall.service.MallIndexConfigService;
import com.bin.shoppingmall.util.BeanUtil;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallIndexConfigServiceImpl  implements MallIndexConfigService {
    @Resource
    IndexConfigMapper indexConfigMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        if(indexConfigMapper.insertSelective(indexConfig)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteIndexConfig(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除首页配置数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if(temp==null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if(indexConfigMapper.updateByPrimaryKeySelective(indexConfig)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<MallIndexConfigGoodsVO> mallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<Goods> mallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            mallIndexConfigGoodsVOS = BeanUtil.copyList(mallGoods, MallIndexConfigGoodsVO.class);
            for (MallIndexConfigGoodsVO mallIndexConfigGoodsVO : mallIndexConfigGoodsVOS) {
                String goodsName = mallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = mallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    mallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    mallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return mallIndexConfigGoodsVOS;
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }
}
