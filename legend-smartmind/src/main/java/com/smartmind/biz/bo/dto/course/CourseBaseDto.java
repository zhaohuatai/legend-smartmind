package com.smartmind.biz.bo.dto.course;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class CourseBaseDto{

	
	public CourseBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="课程编码不能为空")
	@jakarta.validation.constraints.NotNull(message="课程编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="课程编码长度不能大于50")
	@Schema(description = "课程编码")
 	private java.lang.String courseCode;
 
	@jakarta.validation.constraints.NotBlank(message="课程名称不能为空")
	@jakarta.validation.constraints.NotNull(message="课程名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="学科类型长度不能大于10")
	@Schema(description = "学科类型")
 	private java.lang.String subjectType;
 
	@jakarta.validation.constraints.NotBlank(message="面向年级不能为空")
	@jakarta.validation.constraints.NotNull(message="面向年级不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="面向年级长度不能大于10")
	@Schema(description = "面向年级")
 	private java.lang.String gradeLevel;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="学期长度不能大于20")
	@Schema(description = "学期")
 	private java.lang.String semester;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="学年长度不能大于10")
	@Schema(description = "学年")
 	private java.lang.String schoolYear;
 

	@jakarta.validation.constraints.Digits(integer=2,fraction=1,message="学分数据精度错误")@Schema(description = "学分")
 	private java.lang.Byte credit;
 

	@Schema(description = "总课时")
 	private java.lang.Integer totalHours;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="教材信息长度不能大于500")
	@Schema(description = "教材信息")
 	private java.lang.String textbookInfo;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="课程封面图地址长度不能大于500")
	@Schema(description = "课程封面图地址")
 	private java.lang.String coverImage;
 
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态长度不能大于2")
	@Schema(description = "状态")
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
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="课程描述长度不能大于65,535")
	@Schema(description = "课程描述")
 	private java.lang.String courseDesc;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="课程目标长度不能大于65,535")
	@Schema(description = "课程目标")
 	private java.lang.String courseObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="知识框架结构(JSON格式)长度不能大于1,073,741,824")
	@Schema(description = "知识框架结构(JSON格式)")
 	private java.lang.String knowledgeFramework;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="参考资料长度不能大于65,535")
	@Schema(description = "参考资料")
 	private java.lang.String referenceMaterials;
 
	

}
