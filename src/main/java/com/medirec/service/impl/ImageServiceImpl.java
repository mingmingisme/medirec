package com.medirec.service.impl;

import com.medirec.entity.Image;
import com.medirec.entity.User;
import com.medirec.mapper.ImageMapper;
import com.medirec.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zsm
 * @since 2022-05-17
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Value("${file-upload-path}")
    private String imageUploadPath;

    private static final String INPUT_IMAGE_APPENDIX = "_input";
    private static final String DEMO_IMAGE_APPENDIX = "_demo";
    private static final String OUTPUT_IMAGE_APPENDIX = "_output";

    private static final String DATE_TIME_PATTERN = "yyyyMMdd_HHmmss";

    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_TIME_PATTERN);

    @Override
    public Image uploadAndReconstruct(MultipartFile file, User loginUser, Boolean sinogram) throws IOException {
        System.out.println(file);
        System.out.println(loginUser);
        System.out.println("is sinogram? : " + sinogram);
        Image uploadedImage = storeImage(file, loginUser, sinogram, Paths.get(imageUploadPath, loginUser.getUsername()).toString());
        int[] heightAndWidth = getHeightAndWidth(uploadedImage);
        uploadedImage.setHeight(heightAndWidth[0]);
        uploadedImage.setWidth(heightAndWidth[1]);
        return uploadedImage;
    }

    private int[] getHeightAndWidth(Image image) {
        int[] heightAndWidth = new int[]{512, 512};
        return heightAndWidth;
    }

    private Image storeImage(MultipartFile file, User loginUser, Boolean sinogram, String imageParentUploadPath) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = getImageSuffix(fileName);
        String username = loginUser.getUsername();
        Long userId = loginUser.getId();
        String dateTime = SDF.format(new Date());
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));

        // path like /${dateTime}_input.npy
        String inputRelativePath = getRelativePath(dateTime, INPUT_IMAGE_APPENDIX, suffix);
        // path like ./file/${username}/${dateTime}_input.npy
        String toPath = imageParentUploadPath + inputRelativePath;

        System.out.println("inputRelativePath: " + inputRelativePath);
        System.out.println("toPath: " + toPath);

        File toFile = new File(toPath).getParentFile();
        if (!toFile.exists()) {
            toFile.mkdirs(); //自动创建目录
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(toPath);
            out.write(file.getBytes());
            out.flush();
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (out != null) {
                out.close();
            }
        }

        String inputUrl = "./" + username + inputRelativePath;
        String demoRelativePath = getRelativePath(dateTime, DEMO_IMAGE_APPENDIX, ".jpg");
        String demoUrl = "./" + username + demoRelativePath;
        String outputRelativePath = getRelativePath(dateTime, OUTPUT_IMAGE_APPENDIX, ".jpg");
        String outputUrl = "./" + username + outputRelativePath;
        Image image = new Image(userId, fileName, sinogram, suffix, inputUrl, demoUrl, outputUrl, localDateTime);

        return image;
    }

    private String getRelativePath(String dateTime, String APPENDIX, String suffix) {
        return "/" + dateTime + APPENDIX + suffix;
    }

    private String getImageSuffix(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index);
    }


}
