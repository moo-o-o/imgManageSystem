package com.huqz.utils;

import com.huqz.exception.FileTypeException;
import com.huqz.pojo.imgDTO.FileDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

        // 保存图片
        byte[] bytes = file.getBytes();
        String s = new String(bytes);
        byte[] bytes1 = Base64.decodeBase64(s);
        FileOutputStream fos = new FileOutputStream(path.toString());
        fos.write(bytes1);
        fos.flush();
        fos.close();

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(filename);
        fileDTO.setUploadTime(String.valueOf(System.currentTimeMillis()));
        fileDTO.setPath(Paths.get(targetFolder, rename).toString());
        fileDTO.setFlag(true);
        fileDTO.setAbsPath(path.toString());

        return fileDTO;
    }

    public static void saveBin(MultipartFile file) throws IOException {
//        String s = Arrays.toString(Base64.encodeBase64(file.getBytes()));
//        System.out.println(s);
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        String dest = "D:/imgs/hello." + suffix;
        // 直接存bytes
//        byte[] bytes = file.getBytes();
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
//        bos.write(bytes);
//        bos.flush();
//        bos.close();
        // 存字节流
//        InputStream is = file.getInputStream();
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
//        int len = 0;
//        byte[] bys = new byte[8*1024];
//        while ((len = is.read(bys)) != -1) {
//            bos.write(bys, 0, len);
//        }
//        bos.flush();
//        bos.close();
//        is.close();
        // buff
//        InputStream is = file.getInputStream();
//        BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
//        bis.
//        BufferedOutputStream bos = new BufferedOutputStream()

        // bytes
        byte[] bytes = file.getBytes();
        String s = new String(bytes);
        byte[] bytes1 = Base64.decodeBase64(s);
        FileOutputStream fos = new FileOutputStream(dest);
        fos.write(bytes1);
        fos.flush();
        fos.close();



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
}
