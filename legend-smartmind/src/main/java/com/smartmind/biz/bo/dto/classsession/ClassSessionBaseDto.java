package com.smartmind.biz.bo.dto.classsession;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassSessionBaseDto{

	
	public ClassSessionBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="课堂编码不能为空")
	@jakarta.validation.constraints.NotNull(message="课堂编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="课堂编码长度不能大于50")
	@Schema(description = "课堂编码")
 	private java.lang.String sessionCode;
 
	@jakarta.validation.constraints.NotBlank(message="课堂名称不能为空")
	@jakarta.validation.constraints.NotNull(message="课堂名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="课堂名称长度不能大于200")
	@Schema(description = "课堂名称")
 	private java.lang.String sessionName;
 

	@jakarta.validation.constraints.NotNull(message="所属课程ID不能为空")

	@Schema(description = "所属课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="上课班级ID不能为空")

	@Schema(description = "上课班级ID")
 	private java.lang.Long clazzId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String clazzName;
 

	@jakarta.validation.constraints.NotNull(message="授课教师ID不能为空")
	@Schema(description = "授课教师ID")
 	private java.lang.String teacherId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="教师姓名长度不能大于50")
	@Schema(description = "教师姓名")
 	private java.lang.String teacherName;
 

	@jakarta.validation.constraints.NotNull(message="上课时间不能为空")

	@Schema(description = "上课时间")
 	private java.util.Date startTime;
 

	@jakarta.validation.constraints.NotNull(message="课堂时长(分钟)不能为空")

	@Schema(description = "课堂时长(分钟)")
 	private java.lang.Integer duration;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消长度不能大于2")
	@Schema(description = "状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
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
 
	//(JSON格式数组:[{"unitCode":"xxx","unitName":"xxx"}])
	@jakarta.validation.constraints.Size(min=0,max=65535,message="关联单元信息长度不能大于65,535")
	@Schema(description = "关联单元信息")
 	private java.lang.String unitsInfo;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="关联单元ID列表长度不能大于500")
	@Schema(description = "关联单元ID列表，逗号分隔")
 	private java.lang.String unitIds;
 
	

}
