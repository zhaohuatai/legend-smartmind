package com.smartmind.biz.bo.dto.courseware;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class CoursewareBaseDto{

	
	public CoursewareBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="课件编码不能为空")
	@jakarta.validation.constraints.NotNull(message="课件编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="课件编码长度不能大于50")
	@Schema(description = "课件编码")
 	private java.lang.String coursewareCode;
 
	@jakarta.validation.constraints.NotBlank(message="课件名称不能为空")
	@jakarta.validation.constraints.NotNull(message="课件名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="课件名称长度不能大于200")
	@Schema(description = "课件名称")
 	private java.lang.String coursewareName;
 

	@jakarta.validation.constraints.NotNull(message="课件类型: 1-在线课件, 2-本地课件不能为空")

	@Schema(description = "课件类型: 1-在线课件, 2-本地课件")
 	private java.lang.Byte coursewareType;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="单元ID不能为空")

	@Schema(description = "单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 
	@jakarta.validation.constraints.Size(min=0,max=60,message="单元编号长度不能大于60")
	@Schema(description = "单元编号")
 	private java.lang.String unitCode;
 

	@Schema(description = "文件ID")
 	private java.lang.String fileId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="文件名称长度不能大于200")
	@Schema(description = "文件名称")
 	private java.lang.String fileName;
 

	@Schema(description = "排序序号")
 	private java.lang.Integer sortOrder;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-失效, 1-有效不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-失效, 1-有效长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效")
 	private java.lang.String status;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="Markdown内容长度不能大于2,147,483,647")
	@Schema(description = "Markdown内容")
 	private java.lang.String markdownContent;
 
	

}
