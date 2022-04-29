package com.huqz.utils;

import com.huqz.exception.FileTypeException;
import com.huqz.pojo.imgDTO.FileDTO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
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

    public FileDTO saveUrl(String url) throws IOException {
        String s1 = url.substring(url.indexOf("://") + 3);
        String domain = s1.substring(0, s1.indexOf("/"));
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(builder -> builder.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1024*1024*20))).build();
        Mono<Resource> mono = webClient.get().uri(url).header("referer", domain).retrieve().bodyToMono(Resource.class);
        Resource res = mono.block();

        String suffix = getTypeByStream(res.getInputStream());
        String rename = genFilename(suffix);
        String destFolder = getUploadFolder("formUrl");
        String filepath = Paths.get(absolutePath, uploadFolder, destFolder, rename).toString();

        Thumbnails.of(res.getInputStream()).scale(1f).toFile(filepath);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilename(url);
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

        Thumbnails.of(absolutePath).size(233, 233).toFile(thumbAbsolutePath);
        return thumbPath;
    }

    public String getTypeByStream(InputStream is) throws IOException {
        if (is == null) {
            throw new NullPointerException("input stream == null");
        }

        byte[] bys = new byte[4];
        is.read(bys, 0, bys.length);
        String type = byteArrayToHexString(bys).toUpperCase();
        System.out.println("@@@" + type);

        return getType(type);

    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @return 十六进制字符串
     */
    private String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    // 十六进制下数字到字符的映射数组
    private final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private String getType(String s) {
        if (s.contains("89504E47")) return "png";
        else if (s.contains("47494638")) return "gif";
        else if (s.contains("424D")) return "bmp";
        else if (s.contains("FFD8FF")) return "jpg";
        else throw new IllegalArgumentException("图片格式错误");

    }

}
