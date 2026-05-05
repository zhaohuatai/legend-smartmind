package com.smartmind.biz.bo.dto.classactivity;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ClassActivityBaseDto{

	
	public ClassActivityBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="活动编码不能为空")
	@jakarta.validation.constraints.NotNull(message="活动编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="活动编码长度不能大于50")
	@Schema(description = "活动编码")
 	private java.lang.String activityCode;
 
	@jakarta.validation.constraints.Size(min=0,max=200,message="活动名称长度不能大于200")
	@Schema(description = "活动名称")
 	private java.lang.String activityName;
 

	@jakarta.validation.constraints.NotNull(message="所属课堂ID不能为空")

	@Schema(description = "所属课堂ID")
 	private java.lang.Long sessionId;
 

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
 

	@jakarta.validation.constraints.NotBlank(message="活动类型不能为空")
	@jakarta.validation.constraints.Size(min=1,max=2,message="活动类型长度不能大于2")
	@Schema(description = "活动类型: 1-随堂测试, 2-随堂练习, 3-课堂实验, 4-话题讨论")
 	private java.lang.String activityType;
 

	@Schema(description = "活动开始时间")
 	private java.util.Date startTime;
 

	@Schema(description = "活动截止时间")
 	private java.util.Date endTime;
 
	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="活动总分数据精度错误")
	@Schema(description = "活动总分")
 	private java.math.BigDecimal score;

	@Schema(description = "活动时长(分钟)")
 	private java.lang.Integer duration;
 
	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=2,message="状态长度不能大于2")
	@Schema(description = "状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消")
 	private java.lang.String status;
 

	@Schema(description = "排序")
 	private java.lang.Integer sortOrder;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=40,message="创建人长度不能大于40")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=40,message="更新人长度不能大于40")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="活动内容/描述长度不能大于65,535")
	@Schema(description = "活动内容/描述")
 	private java.lang.String activityContent;
 
	@Schema(description = "已提交学生数")
 	private java.lang.Integer submitStuCount;
 
	@Schema(description = "班级总人数")
 	private java.lang.Integer totalStuCount;
 
	@Schema(description = "平均分")
 	private java.math.BigDecimal avgScore;
 
	

}
