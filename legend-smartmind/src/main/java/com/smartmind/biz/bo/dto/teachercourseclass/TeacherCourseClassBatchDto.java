package com.smartmind.biz.bo.dto.teachercourseclass;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 教师课程班级关联批量创建Dto
 */
@Data
@Schema(description = "教师课程班级关联批量创建Dto")
public class TeacherCourseClassBatchDto {

    @NotEmpty(message = "关联列表不能为空")
    @Schema(description = "关联列表")
    private List<TeacherCourseClassItemDto> items;

}
