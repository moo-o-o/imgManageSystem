package com.huqz.utils;

import com.huqz.exception.FileTypeException;
import com.huqz.pojo.imgDTO.FileDTO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FilesUtils {

    @Value("${imgs.upload.dir}")
    private String absolutePath;

    @Value("${imgs.upload.suffix}")
    private String usableSuffix;

    private final String uploadFolder = "upload";

    public FileDTO save(MultipartFile file, String type) throws IOException, FileTypeException {
        createDirIfNotExist();
        if (file == null || file.isEmpty())
            throw new FileNotFoundException("文件不存在");

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!usableSuffix.contains(suffix))
            throw new FileTypeException("文件格式不支持");

        String rename = genFilename(suffix);
        String destFolder = getUploadFolder(type);

        String filepath = Paths.get(absolutePath, uploadFolder, destFolder, rename).toString();
        FileOutputStream fos = new FileOutputStream(filepath);

        byte[] bytes = file.getBytes();
        String s = new String(bytes);
        byte[] bys = Base64.decodeBase64(s);
        fos.write(bys);
        fos.flush();
        fos.close();

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(filename);
        fileDTO.setUploadTime(String.valueOf(System.currentTimeMillis()));
        fileDTO.setPath(Paths.get(uploadFolder, destFolder, rename).toString());
        fileDTO.setFlag(true);
        fileDTO.setAbsPath(filepath);
        return fileDTO;

    }

    public void createDirIfNotExist() throws IOException {
        File file = new File(absolutePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private String genFilename(String suffix) {
        return UUID.randomUUID().toString().replace("-", "") + "." + suffix;
    }

    private String getUploadFolder(String type) {
        String folder;
        File f;
        if ("head".equals(type)) {
            folder = "avatar";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String format = sdf.format(new Date());
            folder = Paths.get("image", format).toString();
        }
        String absFolder = Paths.get(absolutePath, uploadFolder, folder).toString();
        // 创建文件夹
        f = new File(absFolder);
        Boolean b = f.exists() ? null : f.mkdirs();
        return folder;
    }

    public String getAbsolutePath(String filepath) {
        return Paths.get(absolutePath,  filepath).toString();
    }

    public String getHeadAbsolutePath(String filepath) {
        return Paths.get(absolutePath, uploadFolder, "avatar", filepath).toString();
    }

    public String createThumb(String filepath) throws IOException {
        if (filepath == null || filepath.isEmpty()) {
            throw new FileNotFoundException("文件未找到");
        }

        String absolutePath = getAbsolutePath(filepath);
        String fileName = filepath.substring(filepath.lastIndexOf(File.separator) + 1);
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

//        String thumbName = "thumb-" + DigestUtils.md5DigestAsHex(fileName.getBytes()) + '.' + fileSuffix;
        String thumbName = "thumb-" + fileName;
        String thumbAbsolutePath = absolutePath.replace(fileName, thumbName);
        System.out.println("thumbAbsolutePath: " + thumbAbsolutePath);
//        String thumbPath = filepath.replace(fileName, thumbName);
        String thumbPath = "thumb-" + DigestUtils.md5DigestAsHex(fileName.getBytes());

        Thumbnails.of(absolutePath).size(160, 160).toFile(thumbAbsolutePath);
        return thumbPath;
    }
}
