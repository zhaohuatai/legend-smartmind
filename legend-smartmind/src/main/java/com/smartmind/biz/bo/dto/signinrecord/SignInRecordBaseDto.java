package com.smartmind.biz.bo.dto.signinrecord;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class SignInRecordBaseDto{

	
	public SignInRecordBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="签到活动ID不能为空")

	@Schema(description = "签到活动ID")
 	private java.lang.Long signId;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="签到名称长度不能大于200")
	@Schema(description = "签到名称")
 	private java.lang.String signName;
 

	@jakarta.validation.constraints.NotNull(message="学生ID不能为空")

	@Schema(description = "学生ID")
 	private java.lang.String studentId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学号长度不能大于50")
	@Schema(description = "学号")
 	private java.lang.String studentNo;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="学生姓名长度不能大于50")
	@Schema(description = "学生姓名")
 	private java.lang.String studentName;
 

	@jakarta.validation.constraints.NotNull(message="班级ID不能为空")

	@Schema(description = "班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="班级名称长度不能大于100")
	@Schema(description = "班级名称")
 	private java.lang.String className;
 

	@jakarta.validation.constraints.NotNull(message="签到时间不能为空")

	@Schema(description = "签到时间")
 	private java.util.Date signTime;
 
	@jakarta.validation.constraints.NotBlank(message="签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假不能为空")
	@jakarta.validation.constraints.NotNull(message="签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假不能为空")
	@jakarta.validation.constraints.Size(min=0,max=10,message="签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假长度不能大于10")
	@Schema(description = "签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假")
 	private java.lang.String signResult;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="签到方式: 1-点击签到, 2-位置签到, 3-手势签到, 4-扫码签到长度不能大于10")
	@Schema(description = "签到方式: 1-点击签到, 2-位置签到, 3-手势签到, 4-扫码签到")
 	private java.lang.String signMethod;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="签到位置长度不能大于200")
	@Schema(description = "签到位置")
 	private java.lang.String location;
 

	@jakarta.validation.constraints.Digits(integer=2,fraction=8,message="纬度数据精度错误")@Schema(description = "纬度")
 	private java.math.BigDecimal latitude;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=8,message="经度数据精度错误")@Schema(description = "经度")
 	private java.math.BigDecimal longitude;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="IP地址长度不能大于50")
	@Schema(description = "IP地址")
 	private java.lang.String ipAddress;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="设备类型长度不能大于50")
	@Schema(description = "设备类型")
 	private java.lang.String deviceType;
 
	@jakarta.validation.constraints.Size(min=0,max=10,message="是否早退: 0-否, 1-是长度不能大于10")
	@Schema(description = "是否早退: 0-否, 1-是")
 	private java.lang.String leaveEarly;
 
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
 
	

}
