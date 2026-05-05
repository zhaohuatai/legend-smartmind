package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 学生批量导入DTO
 */
@Data
@Schema(description = "学生批量导入DTO")
public class StudentBatchImportDto {

    @NotEmpty(message = "学生列表不能为空")
    @Valid
    @Schema(description = "学生信息列表", required = true)
    private List<StudentCreateDto> students;
}
