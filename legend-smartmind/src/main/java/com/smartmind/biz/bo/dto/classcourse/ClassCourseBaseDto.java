package com.smartmind.biz.bo.dto.classcourse;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassCourseBaseDto{

	
	public ClassCourseBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")
	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="学期长度不能大于20")
	@Schema(description = "学期")
 	private java.lang.String semester;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="学年长度不能大于20")
	@Schema(description = "学年")
 	private java.lang.String schoolYear;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="授课时间(如: 周一第3节)长度不能大于100")
	@Schema(description = "授课时间(如: 周一第3节)")
 	private java.lang.String teachingTime;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="授课教室长度不能大于50")
	@Schema(description = "授课教室")
 	private java.lang.String classroom;
 

	@Schema(description = "开始周次")
 	private java.lang.Integer startWeek;
 

	@Schema(description = "结束周次")
 	private java.lang.Integer endWeek;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="安排类型: 1-固定班级, 2-走班排课长度不能大于10")
	@Schema(description = "安排类型: 1-固定班级, 2-走班排课")
 	private java.lang.String arrangementType;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-失效, 1-有效, 2-已结课不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-失效, 1-有效, 2-已结课不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-失效, 1-有效, 2-已结课长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效, 2-已结课")
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
 
	

}
