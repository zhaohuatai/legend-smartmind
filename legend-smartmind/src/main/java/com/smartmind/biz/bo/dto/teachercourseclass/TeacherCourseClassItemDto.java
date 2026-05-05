package com.smartmind.biz.bo.dto.teachercourseclass;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 教师课程班级关联单项Dto
 */
@Data
@Schema(description = "教师课程班级关联单项Dto")
public class TeacherCourseClassItemDto {

    @NotNull(message = "教师ID不能为空")
    @Schema(description = "教师ID")
    private String teacherId;

    @NotNull(message = "课程ID不能为空")
    @Schema(description = "课程ID")
    private Long courseId;

    @NotNull(message = "班级ID不能为空")
    @Schema(description = "班级ID")
    private Long classId;

}
