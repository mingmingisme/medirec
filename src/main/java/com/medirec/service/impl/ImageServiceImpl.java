package com.medirec.service.impl;

import com.medirec.entity.Image;
import com.medirec.entity.User;
import com.medirec.mapper.ImageMapper;
import com.medirec.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    // Note that this value annotated by @Value could not be static, or it will return null
    @Value("${file-upload-path}")
    private String imageUploadPath;

    private static final String PYTHON_INTERPRETER_PATH = "F:/Anaconda3/envs/bishe/python.exe";

    private static final String JPG_TO_NET_SCRIPT_PATH = "./reconstruct/jpg_to_net.py";
    private static final String NPY_SINO_TO_NET_SCRIPT_PATH = "./reconstruct/npy_sino_to_net.py";

    private static final String MODEL_PARAM_PATH = "./reconstruct/result_model/FbpConvNet_norm.pkl";

    private static final String INPUT_IMAGE_APPENDIX = "_input";
    private static final String DEMO_IMAGE_APPENDIX = "_demo";
    private static final String OUTPUT_IMAGE_APPENDIX = "_output";

    private static final String DATE_TIME_PATTERN = "yyyyMMdd_HHmmss";

    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_TIME_PATTERN);

    @Override
    public Image uploadAndReconstruct(MultipartFile file, User loginUser, Boolean sinogram) throws IOException {
        System.out.println(file);
        System.out.println(loginUser);
//        System.out.println("is sinogram? : " + sinogram);
        Image uploadedImage = storeImage(file, loginUser, sinogram, Paths.get(imageUploadPath, loginUser.getUsername()).toString());
        int[] heightAndWidth = getHeightAndWidth(uploadedImage);
        uploadedImage.setHeight(heightAndWidth[0]);
        uploadedImage.setWidth(heightAndWidth[1]);

        reconstructImage(uploadedImage);

        return uploadedImage;
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
        String demoRelativePath = getRelativePath(dateTime, DEMO_IMAGE_APPENDIX, ".jpg");
        String outputRelativePath = getRelativePath(dateTime, OUTPUT_IMAGE_APPENDIX, ".jpg");


        // path like ./file/${username}/${dateTime}_input.npy
        String inputImagePath = imageParentUploadPath + inputRelativePath;
        String demoImagePath = imageParentUploadPath + demoRelativePath;

        boolean isInputUploaded = saveFile(file, inputImagePath);
        boolean isDemoUploaded = false;

        if (suffix.equals(".jpg")) {
            isDemoUploaded = saveFile(file, demoImagePath);
            System.out.println("**Demo image has" + (isDemoUploaded ? " " : " not ") + "been saved...");
        } else if (suffix.equals(".npy")) {
            isDemoUploaded = false;
        }


//        System.out.println("inputRelativePath: " + inputRelativePath);
//        System.out.println("inputImagePath: " + inputImagePath);


        String inputUrl = "/" + username + inputRelativePath;
        String demoUrl = "/" + username + demoRelativePath;
        String outputUrl = "/" + username + outputRelativePath;
        Image image = new Image(userId, fileName, sinogram, suffix, inputUrl, demoUrl, outputUrl, localDateTime);

        return image;
    }

    private void reconstructImage(Image uploadedImage) {
        String suffix = uploadedImage.getSuffix();
        boolean isSino = uploadedImage.getSinogram();
        String inputUrl = uploadedImage.getInputUrl();
        String demoUrl = uploadedImage.getDemoUrl();
        String outputUrl = uploadedImage.getOutputUrl();
        if (suffix.equalsIgnoreCase(".npy")) {
            if (isSino) {
                System.out.println("Ready to reconstruct a sino npy...");
                reconstructSinoNpy(inputUrl, demoUrl, outputUrl);
            } else {
                System.out.println("Ready to reconstruct a non-sino npy...");
                reconstructNpy(inputUrl, outputUrl);
            }

        } else if (suffix.equalsIgnoreCase(".jpg")) {
            if (isSino) {
                System.out.println("Ready to reconstruct a sino jpg...");
                reconstructSinoJpg(inputUrl, outputUrl);
            } else {
                System.out.println("Ready to reconstruct a non-sino jpg...");
                reconstructJpg(inputUrl, outputUrl);
            }
        } else if(suffix.equalsIgnoreCase(".dcm") || suffix.equalsIgnoreCase(".dcm")) {

        }
    }

    private void reconstructSinoNpy(String inputUrl, String demoUrl, String outputUrl) {
        String inputRelativePath = imageUploadPath + inputUrl;
        String demoRelativePath = imageUploadPath + demoUrl;
        String outputRelativePath = imageUploadPath + outputUrl;

        String command = String.format(PYTHON_INTERPRETER_PATH +
                " " + NPY_SINO_TO_NET_SCRIPT_PATH +
                " " + inputRelativePath +
                " " + demoRelativePath +
                " " + outputRelativePath +
                " " + MODEL_PARAM_PATH);

        executePythonScripts(command);
    }

    private void reconstructNpy(String inputUrl, String outputUrl) {
        return;
    }

    private void reconstructSinoJpg(String inputUrl, String outputUrl) {

    }

    private void reconstructJpg(String inputUrl, String outputUrl) {
        String inputRelativePath = imageUploadPath + inputUrl;
        String outputRelativePath = imageUploadPath + outputUrl;

        String command = String.format(PYTHON_INTERPRETER_PATH +
                " " + JPG_TO_NET_SCRIPT_PATH +
                " " + inputRelativePath +
                " " + outputRelativePath +
                " " + MODEL_PARAM_PATH);

        executePythonScripts(command);

    }

    private void executePythonScripts(String command) {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean saveFile(MultipartFile file, String pathToUpload) throws IOException {
        File toFile = new File(pathToUpload).getParentFile();
        if (!toFile.exists()) {
            toFile.mkdirs(); //自动创建目录
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathToUpload);
            out.write(file.getBytes());
            out.flush();
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (out != null) {
                out.close();
                return true;
            }
            return false;
        }
    }

    private int[] getHeightAndWidth(Image image) {
        int[] heightAndWidth = new int[]{512, 512};
        if (image.getSinogram()) {
            heightAndWidth[0] = 128;
        }
        return heightAndWidth;
    }

    private String getRelativePath(String dateTime, String APPENDIX, String suffix) {
        return "/" + dateTime + APPENDIX + suffix;
    }

    private String getImageSuffix(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index);
    }


}
