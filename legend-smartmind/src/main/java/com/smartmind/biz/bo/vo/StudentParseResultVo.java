package com.smartmind.biz.bo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生信息解析结果VO
 */
@Data
@Schema(description = "学生信息解析结果VO")
public class StudentParseResultVo {

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "学号")
    private String studentNo;

    @Schema(description = "性别: 0-女, 1-男, 2-保密")
    private String gender;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级名称（解析时识别到的）")
    private String className;
}
