package com.smartmind.biz.bo.dto.classactivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂活动创建Dto")
public class ClassActivityCreateDto extends ClassActivityBaseDto{

	private List<ClassActivityResourceCreateDto> activityResourceList;
	            
}
