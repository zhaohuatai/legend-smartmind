package com.smartmind.biz.bo.dto.teacher;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class TeacherBaseDto{

	
	public TeacherBaseDto() {}
	//<-------------------------------------------->
 
	@jakarta.validation.constraints.NotBlank(message="工号不能为空")
	@jakarta.validation.constraints.NotNull(message="工号不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="工号长度不能大于50")
	@Schema(description = "工号")
 	private java.lang.String teacherNo;
 
	@jakarta.validation.constraints.NotBlank(message="教师姓名不能为空")
	@jakarta.validation.constraints.NotNull(message="教师姓名不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="教师姓名长度不能大于50")
	@Schema(description = "教师姓名")
 	private java.lang.String teacherName;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="性别长度不能大于10")
	@Schema(description = "性别")
 	private java.lang.String gender;
 
	@jakarta.validation.constraints.Size(min=0,max=18,message="身份证号长度不能大于18")
	@Schema(description = "身份证号")
 	private java.lang.String idCard;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="联系电话长度不能大于20")
	@Schema(description = "联系电话")
 	private java.lang.String phone;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="邮箱长度不能大于100")
	@Schema(description = "邮箱")
 	private java.lang.String email;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="照片地址长度不能大于500")
	@Schema(description = "照片地址")
 	private java.lang.String photoUrl;
 
//	@jakarta.validation.constraints.NotBlank(message="所教学科不能为空")
//	@jakarta.validation.constraints.NotNull(message="所教学科不能为空")
//	@jakarta.validation.constraints.Size(min=0,max=10,message="所教学科长度不能大于10")
//	@Schema(description = "所教学科")
 	private java.lang.String subjectType;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="职称: 1-助教, 2-讲师, 3-副教授, 4-教授长度不能大于50")
	@Schema(description = "职称: 1-助教, 2-讲师, 3-副教授, 4-教授")
 	private java.lang.String title;
 

	@Schema(description = "教龄(年)")
 	private java.lang.Integer teachingExperience;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学历长度不能大于50")
	@Schema(description = "学历")
 	private java.lang.String education;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="教学特长描述长度不能大于200")
	@Schema(description = "教学特长描述")
 	private java.lang.String specialty;
 
	@jakarta.validation.constraints.NotBlank(message="状态: 0-失效, 1-有效, 2-离职不能为空")
	@jakarta.validation.constraints.NotNull(message="状态: 0-失效, 1-有效, 2-离职不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态: 0-失效, 1-有效, 2-离职长度不能大于2")
	@Schema(description = "状态: 0-失效, 1-有效, 2-离职")
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="AI功能偏好设置长度不能大于1,073,741,824")
	@Schema(description = "AI功能偏好设置")
 	private java.lang.String aiPreference;
 
	

}
