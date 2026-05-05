package com.smartmind.biz.bo.model;
import org.legend.framework.core.data.IBaseModel;
import org.legend.framework.base.dao.mybatis.annotation.PKId;
import org.legend.framework.base.dao.mybatis.annotation.TableFieldMode;
import org.legend.framework.base.dao.mybatis.enums.FieldMode;
import org.legend.framework.base.dao.mybatis.enums.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@ToString(callSuper = true)
@Table(name="smd_learning_report")
@TableFieldMode(FieldMode.camel_to_underscore)
public class LearningReport extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "报告名称")
    private java.lang.String reportName;
    
 	@Schema(description = "报告类型: 1-班级报告, 2-个人报告, 3-单元报告, 4-活动/作业报告")
    private java.lang.String reportType;
    
 	@Schema(description = "活动ID: 单活动/作业报告时关联")
    private java.lang.Long activityId;
    
 	@Schema(description = "活动名称")
    private java.lang.String activityName;
    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "单元ID")
    private java.lang.Long unitId;
    
 	@Schema(description = "单元名称")
    private java.lang.String unitName;
    
 	@Schema(description = "班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "班级名称")
    private java.lang.String className;
    
 	@Schema(description = "学生ID")
    private java.lang.String studentId;
    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "统计开始日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    
 	@Schema(description = "统计结束日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    
 	@Schema(description = "参与率(%)")
    private java.math.BigDecimal participationRate;
    
 	@Schema(description = "平均分")
    private java.math.BigDecimal averageScore;
    
 	@Schema(description = "最高分")
    private java.math.BigDecimal highestScore;
    
 	@Schema(description = "最低分")
    private java.math.BigDecimal lowestScore;
    
 	@Schema(description = "排名")
    private java.lang.Integer rank;
    
 	@Schema(description = "活动总数")
    private java.lang.Integer totalActivities;
    
 	@Schema(description = "完成活动数")
    private java.lang.Integer completedActivities;
    
 	@Schema(description = "总题数")
    private java.lang.Integer totalQuestions;
    
 	@Schema(description = "正确题数")
    private java.lang.Integer correctQuestions;
    
 	@Schema(description = "总正确率(%)")
    private java.math.BigDecimal overallCorrectRate;
    
 	@Schema(description = "生成时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date generateTime;
    
 	@Schema(description = "状态: 0-草稿, 1-已生成")
    private java.lang.String status;
    
 	@Schema(description = "创建时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    
 	@Schema(description = "更新时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    
 	@Schema(description = "创建人")
    private java.lang.String createBy;
    
 	@Schema(description = "更新人")
    private java.lang.String updateBy;
    
 	@Schema(description = "备注")
    private java.lang.String remark;
    
 	@Schema(description = "优势知识点")
    private java.lang.String strengthPoints;
    
 	@Schema(description = "薄弱知识点")
    private java.lang.String weaknessPoints;
    
 	@Schema(description = "改进建议")
    private java.lang.String improvementSuggestions;
    
 	@Schema(description = "学习趋势数据")
    private java.lang.String learningTrend;
    
 	@Schema(description = "活动详情数据")
    private java.lang.String activityDetails;
    
 	@Schema(description = "AI总结评语")
    private java.lang.String aiSummary;
    
	

}
