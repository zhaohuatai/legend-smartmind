package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 学生信息文本解析DTO
 */
@Data
@Schema(description = "学生信息文本解析DTO")
public class StudentParseTextDto {

    @NotBlank(message = "文本内容不能为空")
    @Schema(description = "要解析的文本内容", required = true)
    private String text;
}
