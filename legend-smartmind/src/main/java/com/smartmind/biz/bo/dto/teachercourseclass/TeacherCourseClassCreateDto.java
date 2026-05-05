package com.smartmind.biz.bo.dto.teachercourseclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教师课程班级关联创建Dto")
public class TeacherCourseClassCreateDto extends TeacherCourseClassBaseDto{

	

}
