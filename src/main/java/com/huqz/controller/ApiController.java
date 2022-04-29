package com.huqz.controller;

import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.model.ApiKey;
import com.huqz.model.Image;
import com.huqz.pojo.imgDTO.FileDTO;
import com.huqz.service.ApiKeyService;
import com.huqz.service.ImageService;
import com.huqz.service.ImageTagsService;
import com.huqz.service.TagService;
import com.huqz.utils.CheckUtils;
import com.huqz.utils.FilesUtils;
import com.huqz.utils.UrnUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/fapi") // f --> first
public class ApiController {

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private FilesUtils filesUtils;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageTagsService imageTagsService;

    @Autowired
    private TagService tagService;

    @PostMapping("/imgs/{key}")
    public Result upload(@PathVariable String key, @RequestBody Map<String, List<Map<String, Object>>> map) throws IOException {
        if (key == null || key.length() != 32) {
            return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");
        }

        List<Map<String, Object>> imgs = map.get("imgs");
        if (imgs.size() == 0) return ResultGenerator.fail(ResultCode.INVALID_ARGS, "参数不合法");

        ApiKey apiKey = apiKeyService.getByApiKey(key);
        Integer userId = apiKey.getUserId();
        Integer categoryId = apiKey.getCategoryId();

        List<Object> res = new ArrayList<>();

        for (Map<String, Object> img : imgs) {
            String url = (String) img.get("url");
            boolean isUrl = CheckUtils.checkUrl(url);
            if (!isUrl) {
                System.out.println(url);
                return ResultGenerator.fail(ResultCode.INVALID_ARGS, "图片URL有误");
            }

            // 保存图片
            FileDTO fileDTO = filesUtils.saveUrl(url);
            String thumbUrn = filesUtils.createThumb(fileDTO.getPath());

            // 上传图片
            Image image = new Image();
            image.setUserId(userId);
            image.setUrl(fileDTO.getPath());
            image.setUrn(UrnUtils.genUrn());
            image.setThumbUrn(thumbUrn);
            image.setCategoryId(categoryId);
            imageService.save(image);

            // 绑定标签
            Object obj = img.get("tags");
            if (obj != null) {
                List<String> tags = (List<String>) obj;
                if (tags.size() != 0) {
                    List<Integer> tagIds = tagService.saveMany(tags);
                    imageTagsService.saveManyTags(image.getId(), tagIds);
                }
            }

            // 生成响应信息
            Map<String, Object> map1 = new HashMap<>();
            map1.put("url", fileDTO.getFilename());
            map1.put("flag", true);
            map1.put("urn", image.getUrn());
            map1.put("uploadTime", fileDTO.getUploadTime());
            res.add(map1);

        }

        return ResultGenerator.ok(res);


    }

}
