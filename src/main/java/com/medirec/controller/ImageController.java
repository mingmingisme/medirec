package com.medirec.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.medirec.entity.Image;
import com.medirec.entity.ImageDTO;
import com.medirec.entity.User;
import com.medirec.service.ImageService;
import com.medirec.utils.JsonResponse;
import com.medirec.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * ImageControllerē±»
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
//    @RequestMapping(value = "/uploadAndReconstructImage", method = RequestMethod.POST)
//    public String uploadAndReconstructImage(MultipartFile file,
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
////            request.setAttribute("uploadedImage", uploadedImage);
//            SessionUtils.saveUploadedImageToSession(uploadedImage);
//        }
//        return "userhome";
//    }
    @RequestMapping(value = "/uploadAndReconstructImage", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse uploadAndReconstructImage(MultipartFile file,
                                                  Boolean sinogram) throws IOException {
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
//            request.setAttribute("uploadedImage", uploadedImage);
            SessionUtils.saveUploadedImageToSession(uploadedImage);
        }
        return JsonResponse.success(uploadedImage);
    }

    @RequestMapping("/restore")
    @ResponseBody
    public JsonResponse restore(HttpServletRequest request) {
        SessionUtils.removeUploadedImageFromSession();
        System.out.println("$$restoring");

        return JsonResponse.success("1");
    }

    @RequestMapping("/records")
    @ResponseBody
    public List<ImageDTO> getUploadRecords() {
        User loginUser = SessionUtils.getLoginUserFromSession();
        List<Image> imageList = imageService.list(
                new QueryWrapper<Image>().lambda().eq(Image::getUserId, loginUser.getId()).orderByDesc(Image::getUploadTime)
        );

        List<ImageDTO> imageDTOList= new ArrayList<ImageDTO>();
        for (Image image : imageList) {
            ImageDTO imageDTO= new ImageDTO(image);
            imageDTOList.add(imageDTO);
        }

        return imageDTOList;
    }

    /**
     * ęčæ°ļ¼ę ¹ę®Id ę„čÆ¢
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id) throws Exception {
        Image image = imageService.getById(id);
        return JsonResponse.success(image);
    }

    /**
     * ęčæ°ļ¼ę ¹ę®Idå é¤
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") String id) throws Exception {
        System.out.println(id);
        if(imageService.removeById(id)) {
            return JsonResponse.success(null);
        } else {
            return JsonResponse.failure("å é¤å¤±č“„");
        }

    }

    /**
     * ęčæ°ļ¼ę ¹ę®Idę“ę°
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonResponse updateImage(@PathVariable("id") Long id, Image image) throws Exception {
        image.setId(id);
        imageService.updateById(image);
        return JsonResponse.success(null);
    }

    /**
     * ęčæ°:åå»ŗImage
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(Image image) throws Exception {
        imageService.save(image);
        return JsonResponse.success(null);
    }
}