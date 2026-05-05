package com.smartmind.biz.bo.dto.classstudent;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassStudentBaseDto{

	
	public ClassStudentBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@jakarta.validation.constraints.NotNull(message="学生ID不能为空")

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学号长度不能大于50")
	@Schema(description = "学号")
 	private java.lang.String studentNo;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="关联类型: 1-行政班, 2-教学班长度不能大于10")
	@Schema(description = "关联类型: 1-行政班, 2-教学班")
 	private java.lang.String relationType;
 

	@Schema(description = "加入时间")
 	private java.util.Date joinTime;
 

	@Schema(description = "离开时间")
 	private java.util.Date leaveTime;
 
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
