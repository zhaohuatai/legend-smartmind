package com.smartmind.biz.bo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

import org.legend.framework.base.dao.mybatis.annotation.TableFieldMode;
import org.legend.framework.base.dao.mybatis.enums.FieldMode;

/**
 * 学生列表VO（包含班级信息）
 */
@Data
@Schema(description = "学生列表VO")
@Table(name="smd_student")
@TableFieldMode(FieldMode.camel_to_underscore)
public class StudentListVo {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "学号")
    private String studentNo;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "性别: 0-女, 1-男, 2-保密")
    private String gender;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "状态: 0-在读, 1-休学, 2-退学, 3-毕业")
    private String status;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "班级编码")
    private String classCode;

    @Schema(description = "创建时间")
    private Date createTime;
}
