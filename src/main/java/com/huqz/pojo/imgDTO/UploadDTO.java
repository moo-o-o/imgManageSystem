package com.huqz.pojo.imgDTO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Validated
public class UploadDTO {
//    private MultipartFile file;
    private List<String> tags;
    @Pattern(
            regexp = "[0-9]+",
            message = "参数不合法"
    )
    private Integer categoryId;
}
