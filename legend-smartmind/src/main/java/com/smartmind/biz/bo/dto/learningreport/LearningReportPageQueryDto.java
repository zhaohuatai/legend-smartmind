package com.smartmind.biz.bo.dto.learningreport;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学情报告分页查询DTO")
public class LearningReportPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "报告名称")
	private java.lang.String reportName;
	
	@Schema(description = "活动ID: 单活动/作业报告时关联")
	private java.lang.Long activityId;
	
	@Schema(description = "课程ID")
	private java.lang.Long courseId;
	
	@Schema(description = "单元ID")
	private java.lang.Long unitId;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "统计开始日期开始")
	private java.util.Date startDateStart;
	@Schema(description = "统计开始日期截止")
	private java.util.Date startDateEnd;
	
	@Schema(description = "统计结束日期开始")
	private java.util.Date endDateStart;
	@Schema(description = "统计结束日期截止")
	private java.util.Date endDateEnd;
	
	@Schema(description = "生成时间开始")
	private java.util.Date generateTimeStart;
	@Schema(description = "生成时间截止")
	private java.util.Date generateTimeEnd;
	


}
