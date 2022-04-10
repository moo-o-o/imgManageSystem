package com.huqz.utils;

import com.huqz.exception.FileTypeException;
import com.huqz.pojo.imgDTO.FileDTO;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUtils {
    private static String absolutePath = "";

    private final static String usableSuffix = ".png.jpg.jpeg.webp";

    private static String STATIC_FOLDER = Paths.get("static").toString();

    private static String UPLOAD_FOLDER = Paths.get("upload", "images").toString();

    public static FileDTO upload(MultipartFile file) throws FileTypeException, IOException {
        createDirIfNotExists();

        if (file.isEmpty()) throw new FileNotFoundException("文件不存在");

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (!usableSuffix.contains(suffix)) {
            throw new FileTypeException("文件格式错误");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String rename = sdf.format(new Date()) + "/"
                + UUID.randomUUID().toString().replace("-", "")
                + "." + suffix;

        Path path = Paths.get(absolutePath, STATIC_FOLDER, UPLOAD_FOLDER, rename);
        File upload = new File(String.valueOf(path));
        if (!upload.exists()) {
            upload.mkdirs();
        }
        file.transferTo(upload);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(filename);
        fileDTO.setUploadTime(String.valueOf(System.currentTimeMillis()));
        fileDTO.setPath(Paths.get(UPLOAD_FOLDER, rename).toString());
        fileDTO.setFlag(true);
        fileDTO.setAbsPath(path.toString());

        return fileDTO;
    }

    public static String getAbsolutePath(String filepath) {
        createDirIfNotExists();
        return Paths.get(absolutePath, STATIC_FOLDER, filepath).toString();
    }

    public static void createDirIfNotExists() {
        if (!absolutePath.isEmpty()) return;

        // 根目录文件
        File file = null;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("获取根目录失败，无法创建上传目录");
        }

        if (!file.exists()) {
            file = new File("");
        }

        absolutePath = file.getAbsolutePath();
        File upload = new File(absolutePath, UPLOAD_FOLDER);
        if (!upload.exists()) {
            upload.mkdirs();
        }

    }

}
