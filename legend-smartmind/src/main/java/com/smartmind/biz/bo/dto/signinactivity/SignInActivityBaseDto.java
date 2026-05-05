package com.smartmind.biz.bo.dto.signinactivity;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class SignInActivityBaseDto{

	
	public SignInActivityBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="签到编码不能为空")
	@jakarta.validation.constraints.NotNull(message="签到编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="签到编码长度不能大于50")
	@Schema(description = "签到编码")
 	private java.lang.String signCode;
 
	@jakarta.validation.constraints.NotBlank(message="签到名称不能为空")
	@jakarta.validation.constraints.NotNull(message="签到名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="签到名称长度不能大于200")
	@Schema(description = "签到名称")
 	private java.lang.String signName;
 
	@jakarta.validation.constraints.NotBlank(message="签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到不能为空")
	@jakarta.validation.constraints.NotNull(message="签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到长度不能大于10")
	@Schema(description = "签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到")
 	private java.lang.String signType;
 

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
 

	@jakarta.validation.constraints.NotNull(message="发布教师ID不能为空")

	@Schema(description = "发布教师ID")
 	private java.lang.String teacherId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="教师姓名长度不能大于50")
	@Schema(description = "教师姓名")
 	private java.lang.String teacherName;
 

	@jakarta.validation.constraints.NotNull(message="开始时间不能为空")

	@Schema(description = "开始时间")
 	private java.util.Date startTime;
 

	@Schema(description = "结束时间")
 	private java.util.Date endTime;
 

	@Schema(description = "签到时长(分钟)")
 	private java.lang.Integer duration;
 

	@Schema(description = "总人数")
 	private java.lang.Integer totalStudents;
 

	@Schema(description = "已签到人数")
 	private java.lang.Integer signedCount;
 

	@Schema(description = "迟到人数")
 	private java.lang.Integer lateCount;
 

	@Schema(description = "缺勤人数")
 	private java.lang.Integer absentCount;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="签到率(%)数据精度错误")@Schema(description = "签到率(%)")
 	private java.math.BigDecimal signRate;
 
	@jakarta.validation.constraints.NotBlank(message="签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消不能为空")
	@jakarta.validation.constraints.NotNull(message="签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消长度不能大于10")
	@Schema(description = "签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
 	private java.lang.String signStatus;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="签到配置(如手势密码、位置范围等)长度不能大于1,073,741,824")
	@Schema(description = "签到配置(如手势密码、位置范围等)")
 	private java.lang.String signConfig;
 
	

}
