package com.medirec.service;

import com.medirec.entity.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medirec.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsm
 * @since 2022-05-17
 */
public interface ImageService extends IService<Image> {

    Image uploadAndReconstruct(MultipartFile file, User loginUser, Boolean sinogram) throws IOException;
}
