package com.smartmind.biz.bo.dto.classcourse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "班级课程关联创建Dto")
public class ClassCourseCreateDto extends ClassCourseBaseDto{

	

}
