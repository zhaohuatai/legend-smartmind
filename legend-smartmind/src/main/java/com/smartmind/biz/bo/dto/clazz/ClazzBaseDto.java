package com.smartmind.biz.bo.dto.clazz;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClazzBaseDto{

	
	public ClazzBaseDto() {}
	//<-------------------------------------------->

 	@Schema(description = "班级编码")
 	private java.lang.String classCode;
 
	@jakarta.validation.constraints.NotBlank(message="班级名称不能为空")
	@jakarta.validation.constraints.NotNull(message="班级名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="年级长度不能大于10")
	@Schema(description = "年级")
 	private java.lang.String gradeLevel;

	@Schema(description = "学生人数")
 	private java.lang.Integer studentCount;
 
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效")
 	private java.lang.String status;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="班级描述长度不能大于65,535")
	@Schema(description = "班级描述")
 	private java.lang.String remark;
 
	

}
