package com.smartmind.biz.bo.dto.teachercourseclass;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 教师课程班级关联批量创建Dto
 * 根据教师ID和课程ID列表自动关联班级
 */
@Data
@Schema(description = "教师课程班级关联批量创建Dto")
public class TeacherCourseClassBatchCreateDto {

    @NotNull(message = "教师ID不能为空")
    @Schema(description = "教师ID")
    private String teacherId;

    @NotEmpty(message = "课程ID列表不能为空")
    @Schema(description = "课程ID列表")
    private List<Long> courseIds;

}
