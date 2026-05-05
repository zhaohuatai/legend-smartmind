package com.smartmind.biz.bo.dto.classactivity;

import java.util.List;

import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceCreateDto;
import com.smartmind.biz.bo.dto.classactivityresource.ClassActivityResourceUpdateDto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂活动更新Dto")
public class ClassActivityUpdateDto extends ClassActivityBaseDto{
	
	@NotNull(message="ID不能为空")
	@Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
	private Long id;

	
	private List<ClassActivityResourceUpdateDto> activityResourceList;
}
