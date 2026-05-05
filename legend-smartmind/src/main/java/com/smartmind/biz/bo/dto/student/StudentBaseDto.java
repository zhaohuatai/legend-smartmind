package com.smartmind.biz.bo.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class StudentBaseDto{

	
	public StudentBaseDto() {}
	//<-------------------------------------------->
 
	@jakarta.validation.constraints.NotBlank(message="学号不能为空")
	@jakarta.validation.constraints.NotNull(message="学号不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="学号长度不能大于50")
	@Schema(description = "学号")
 	private java.lang.String studentNo;
 
	@jakarta.validation.constraints.NotBlank(message="学生姓名不能为空")
	@jakarta.validation.constraints.NotNull(message="学生姓名不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="性别: 0-女, 1-男, 2-保密长度不能大于10")
	@Schema(description = "性别: 0-女, 1-男, 2-保密")
 	private java.lang.String gender;
 
	@jakarta.validation.constraints.Size(min=0,max=18,message="身份证号长度不能大于18")
	@Schema(description = "身份证号")
 	private java.lang.String idCard;
 

	@Schema(description = "出生日期")
 	private java.util.Date birthDate;
 
	@jakarta.validation.constraints.Size(min=0,max=20,message="联系电话长度不能大于20")
	@Schema(description = "联系电话")
 	private java.lang.String phone;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="邮箱长度不能大于100")
	@Schema(description = "邮箱")
 	private java.lang.String email;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="照片地址长度不能大于500")
	@Schema(description = "照片地址")
 	private java.lang.String photoUrl;
 

	@Schema(description = "入学日期")
 	private java.util.Date admissionDate;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="学生类型: 1-正式生, 2-借读生, 3-旁听生长度不能大于10")
	@Schema(description = "学生类型: 1-正式生, 2-借读生, 3-旁听生")
 	private java.lang.String studentType;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="学力水平: 1-基础薄弱, 2-中等, 3-良好, 4-优秀长度不能大于10")
	@Schema(description = "学力水平: 1-基础薄弱, 2-中等, 3-良好, 4-优秀")
 	private java.lang.String learningLevel;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学习风格长度不能大于50")
	@Schema(description = "学习风格")
 	private java.lang.String learningStyle;

 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="薄弱知识点记录长度不能大于1,073,741,824")
	@Schema(description = "薄弱知识点记录")
 	private java.lang.String weaknessPoints;
 
	@jakarta.validation.constraints.NotNull(message="班级不能为空")
	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态长度不能大于2")
	@Schema(description = "状态")
 	private java.lang.String status;

}
