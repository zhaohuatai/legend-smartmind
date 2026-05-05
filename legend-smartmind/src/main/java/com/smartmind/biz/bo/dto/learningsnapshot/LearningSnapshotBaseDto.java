package com.smartmind.biz.bo.dto.learningsnapshot;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class LearningSnapshotBaseDto{

	
	public LearningSnapshotBaseDto() {}
	//<-------------------------------------------->

 

	@jakarta.validation.constraints.NotNull(message="活动ID不能为空")

	@Schema(description = "活动ID")
 	private java.lang.Long activityId;
 

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
 

	@jakarta.validation.constraints.NotNull(message="快照时间不能为空")

	@Schema(description = "快照时间")
 	private java.util.Date snapshotTime;
 

	@Schema(description = "总人数")
 	private java.lang.Integer totalStudents;
 

	@Schema(description = "在线人数")
 	private java.lang.Integer onlineStudents;
 

	@Schema(description = "已提交人数")
 	private java.lang.Integer submittedStudents;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="参与率(%)数据精度错误")@Schema(description = "参与率(%)")
 	private java.math.BigDecimal participationRate;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="提交率(%)数据精度错误")@Schema(description = "提交率(%)")
 	private java.math.BigDecimal submitRate;
 

	@jakarta.validation.constraints.Digits(integer=4,fraction=2,message="当前平均分数据精度错误")@Schema(description = "当前平均分")
 	private java.math.BigDecimal averageScore;
 

	@jakarta.validation.constraints.Digits(integer=3,fraction=2,message="当前正确率(%)数据精度错误")@Schema(description = "当前正确率(%)")
 	private java.math.BigDecimal correctRate;
 
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
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="各题统计长度不能大于1,073,741,824")
	@Schema(description = "各题统计")
 	private java.lang.String questionStats;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="需关注学生列表长度不能大于1,073,741,824")
	@Schema(description = "需关注学生列表")
 	private java.lang.String attentionStudents;
 
	@jakarta.validation.constraints.Size(min=0,max=1073741824,message="快照详细数据长度不能大于1,073,741,824")
	@Schema(description = "快照详细数据")
 	private java.lang.String snapshotData;
 
	

}
