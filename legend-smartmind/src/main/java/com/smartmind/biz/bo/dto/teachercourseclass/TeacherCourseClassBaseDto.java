package com.smartmind.biz.bo.dto.teachercourseclass;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class TeacherCourseClassBaseDto{

	
	public TeacherCourseClassBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="教师ID不能为空")

	@Schema(description = "教师ID")
 	private java.lang.String teacherId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="教师姓名长度不能大于50")
	@Schema(description = "教师姓名")
 	private java.lang.String teacherName;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="学期长度不能大于20")
	@Schema(description = "学期")
 	private java.lang.String semester;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="学年长度不能大于20")
	@Schema(description = "学年")
 	private java.lang.String schoolYear;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="教学角色: 1-主讲教师, 2-辅导教师, 3-助教长度不能大于10")
	@Schema(description = "教学角色: 1-主讲教师, 2-辅导教师, 3-助教")
 	private java.lang.String teachingRole;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="是否班主任: 0-否, 1-是长度不能大于10")
	@Schema(description = "是否班主任: 0-否, 1-是")
 	private java.lang.String isHeadTeacher;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="授课时间(如: 周一第3节)长度不能大于100")
	@Schema(description = "授课时间(如: 周一第3节)")
 	private java.lang.String teachingTime;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="授课教室长度不能大于50")
	@Schema(description = "授课教室")
 	private java.lang.String classroom;
 
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
 
	

}
