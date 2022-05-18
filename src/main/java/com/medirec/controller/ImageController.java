package com.medirec.controller;

import com.medirec.entity.User;
import com.medirec.utils.SessionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.medirec.utils.JsonResponse;
import com.medirec.service.ImageService;
import com.medirec.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * ImageController类
 *
 * @author zsm
 */
@Controller
@RequestMapping("/image")
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    /**
     *
     */
    @RequestMapping(value = "/uploadAndReconstructImage", method = RequestMethod.POST)
    public String uploadAndReconstructImage(MultipartFile file,
                                            Boolean sinogram,
                                            HttpServletRequest request) throws IOException {
        User loginUser = SessionUtils.getLoginUserFromSession();
        System.out.println(loginUser);
        Image uploadedImage = null;
        if (file != null) {
            System.out.println("file is not null");
            uploadedImage = imageService.uploadAndReconstruct(file, loginUser, sinogram);
        } else {
            System.out.println("file is null!");
        }

        System.out.println(uploadedImage);

        if (imageService.save(uploadedImage)) {
            request.setAttribute("uploadedImage", uploadedImage);
        }
        return "userhome";
    }

//    @RequestMapping(value = "/uploadAndReconstructImage", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse uploadAndReconstructImage(MultipartFile file,
//                                            Boolean sinogram,
//                                            HttpServletRequest request) throws IOException {
//        User loginUser = SessionUtils.getLoginUserFromSession();
//        System.out.println(loginUser);
//        Image uploadedImage = null;
//        if (file != null) {
//            System.out.println("file is not null");
//            uploadedImage = imageService.uploadAndReconstruct(file, loginUser, sinogram);
//        } else {
//            System.out.println("file is null!");
//        }
//
//        System.out.println(uploadedImage);
//
//        if (imageService.save(uploadedImage)) {
//            request.setAttribute("uploadedImage", uploadedImage);
//        }
//        return JsonResponse.success(uploadedImage);
//    }

    /**
     * 描述：根据Id 查询
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id) throws Exception {
        Image image = imageService.getById(id);
        return JsonResponse.success(image);
    }

    /**
     * 描述：根据Id删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        imageService.removeById(id);
        return JsonResponse.success(null);
    }

    /**
     * 描述：根据Id更新
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonResponse updateImage(@PathVariable("id") Long id, Image image) throws Exception {
        image.setId(id);
        imageService.updateById(image);
        return JsonResponse.success(null);
    }

    /**
     * 描述:创建Image
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(Image image) throws Exception {
        imageService.save(image);
        return JsonResponse.success(null);
    }
}