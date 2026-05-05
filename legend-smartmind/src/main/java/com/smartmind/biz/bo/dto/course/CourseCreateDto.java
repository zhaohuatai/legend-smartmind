package com.smartmind.biz.bo.dto.course;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import com.smartmind.biz.bo.dto.classcourse.CourseClassRelationDto;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课程信息创建Dto")
public class CourseCreateDto extends CourseBaseDto{

	
	private List<Long>  classIds;
}
