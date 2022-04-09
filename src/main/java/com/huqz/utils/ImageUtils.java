package com.huqz.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.DigestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public class ImageUtils {

    public static String createThumb(String filepath) throws IOException {
        if (filepath == null || filepath.isEmpty()) {
            throw new FileNotFoundException("文件未找到");
        }

        String absolutePath = FileUtils.getAbsolutePath(filepath);

        String fileName = getFileName(filepath);
        String fileSuffix = fileName.split("\\.")[1];

        String thumbName = "thumb-" + DigestUtils.md5DigestAsHex(fileName.getBytes()) + fileSuffix;
        String thumbAbsolutePath = absolutePath.replace(fileName, thumbName);
        String thumbPath = filepath.replace(fileName, thumbName);

        Thumbnails.of(absolutePath).size(160, 160).toFile(thumbAbsolutePath);
        return thumbPath;
    }

    public static String getFileName(String filepath) {
        System.out.println(filepath);
        // split的参数是正则表达式，直接传入 File.separator 会被解析成其他字符导致报错
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String[] splits = filepath.split(pattern);
        return splits[splits.length-1];
    }
}
