package com.smartmind.biz.bo.dto.courseware;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课件管理创建Dto")
public class CoursewareCreateDto extends CoursewareBaseDto{

	

}
