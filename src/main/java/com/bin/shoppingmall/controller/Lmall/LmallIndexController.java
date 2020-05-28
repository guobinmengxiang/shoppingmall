package com.bin.shoppingmall.controller.Lmall;
import com.bin.shoppingmall.common.IndexConfigTypeEnum;
import com.bin.shoppingmall.config.Constants;
import com.bin.shoppingmall.controller.vo.MallIndexCarouselVO;
import com.bin.shoppingmall.controller.vo.MallIndexCategoryVO;
import com.bin.shoppingmall.controller.vo.MallIndexConfigGoodsVO;
import com.bin.shoppingmall.service.CarouselService;
import com.bin.shoppingmall.service.CategoryService;
import com.bin.shoppingmall.service.MallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LmallIndexController {

    @Resource
    private CarouselService mallCarouselService;
    @Resource
    private MallIndexConfigService mallIndexConfigService;
    @Resource
    private CategoryService mallCategoryService;

    @GetMapping({"/index","", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<MallIndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<MallIndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<MallIndexConfigGoodsVO> hotGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<MallIndexConfigGoodsVO> newGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<MallIndexConfigGoodsVO> recommendGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "Lmall/index";
    }
}
