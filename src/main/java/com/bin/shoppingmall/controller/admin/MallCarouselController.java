package com.bin.shoppingmall.controller.admin;

import ch.qos.logback.core.pattern.color.GreenCompositeConverter;
import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.config.Constants;
import com.bin.shoppingmall.entity.Carousel;
import com.bin.shoppingmall.service.CarouselService;
import com.bin.shoppingmall.util.MyMaillUtils;
import com.bin.shoppingmall.util.PageQueryUtil;
import com.bin.shoppingmall.util.Result;
import com.bin.shoppingmall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class MallCarouselController {
    @Autowired
    CarouselService carouselServicce;

    /**
     * 跳转到轮播图页面
     *
     * @param request
     * @return
     */
    @GetMapping("/carousels")
    public String carouselPage(HttpServletRequest request) {
        request.setAttribute("path", "mall_carousel");
        return "admin/mall_carousel";
    }

    /**
     * 分页查询
     * @param params page，limit，order
     * @return
     */
    @RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(carouselServicce.getCarouselPage(pageUtil));
    }

    /**
     * 上传轮播图
     *
     * @param httpServletRequest
     * @param file
     * @return
     * @throws URISyntaxException
     */
    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletRequest httpServletRequest,
                         @RequestParam("file") MultipartFile file)
            throws URISyntaxException {
        //得到上传时的文件名
        String fileName = file.getOriginalFilename();
        //截取后缀后
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        //文件名为日期+随机数+后缀组成
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            //文件上传
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
            //返回文件完整路径用于回显
            resultSuccess.setData(MyMaillUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    /**
     * 添加轮播图
     * @param carousel
     * @return 返回信息，枚举类型
     */
    // @RequestBody的作用其实是将json格式的数据转为java对象
    @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Carousel carousel) {
        if (StringUtils.isEmpty(carousel.getCarouselUrl()) || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数不能为空");
        }
        //获取返回信息，枚举类型
        String result = carouselServicce.insertSelective(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);

        }
    }

    /**
     * 轮播图删除
     */
    @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
    //把结果解析成json,要不然后台会报错
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (carouselServicce.deleteSelective(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 获取单条数据，用于修改时数据回显
     */
    @GetMapping(value = "/carousels/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        Carousel carousel = carouselServicce.selectByPrimaryKey(id);

        if (carousel == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(carousel);
    }

    /**
     * 修改数据
     */
    @RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody Carousel carousel) {
        if (Objects.isNull(carousel.getCarouselId())
                || StringUtils.isEmpty(carousel.getCarouselUrl())
                || Objects.isNull(carousel.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = carouselServicce.updateSelective(carousel);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
}