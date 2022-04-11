package com.huqz.utils;

import com.huqz.exception.FileTypeException;
import com.huqz.pojo.imgDTO.FileDTO;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

public class FileUtils {
    private static String absolutePath = "";

    private final static String usableSuffix = ".png.jpg.jpeg.webp";

    private static String STATIC_FOLDER = Paths.get("static").toString();

    private static String UPLOAD_FOLDER = Paths.get("upload", "images").toString();

    private static String AVATAR_FOLDER = Paths.get("upload", "avatar").toString();

    public static FileDTO upload(MultipartFile file) throws FileTypeException, IOException {
        return save(file, "image");
    }

    public static FileDTO uploadHead(MultipartFile file) throws IOException, FileTypeException {
        return save(file, "head");
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

    public static FileDTO save(MultipartFile file, String type) throws IOException, FileTypeException {
        createDirIfNotExists();

        if (file == null || file.isEmpty() ) throw new FileNotFoundException("文件不存在");

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (!usableSuffix.contains(suffix)) {
            throw new FileTypeException("文件格式错误");
        }

        String rename = genFilename(type, suffix);
        String targetFolder = getUploadFolder(type);

        Path path = Paths.get(absolutePath, STATIC_FOLDER, targetFolder, rename);
        File upload = new File(String.valueOf(path));
        Boolean aBoolean = upload.exists() || upload.mkdirs();
        file.transferTo(upload);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(filename);
        fileDTO.setUploadTime(String.valueOf(System.currentTimeMillis()));
        fileDTO.setPath(Paths.get(targetFolder, rename).toString());
        fileDTO.setFlag(true);
        fileDTO.setAbsPath(path.toString());

        return fileDTO;
    }

    public static boolean saveBin(HttpServletRequest request) throws IOException {
        String dest = "D:/imgs/hello.jpg";
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
        int len = 0;
        byte[] bytes = new byte[8*1024];
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        bis.close();
        bos.close();
        return true;

    }

    private static String genFilename(String type, String suffix) {
        String rename;
        if ("head".equals(type)) {
            rename = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            rename = sdf.format(new Date()) + "/"
                    + UUID.randomUUID().toString().replace("-", "")
                    + "." + suffix;
        }
        return rename;
    }

    private static String getUploadFolder(String type) {
        return "head".equals(type) ? AVATAR_FOLDER : UPLOAD_FOLDER;
    }

    private static void touch(String absolutePath) {
        File file = new File(absolutePath);
    }


}
