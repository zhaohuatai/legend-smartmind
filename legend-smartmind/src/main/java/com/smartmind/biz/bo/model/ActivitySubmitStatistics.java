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
@Table(name="smd_activity_submit_statistics")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ActivitySubmitStatistics extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "提交记录ID")
    private java.lang.Long submitId;
    
 	@Schema(description = "活动ID")
    private java.lang.Long activityId;
    
 	@Schema(description = "活动名称")
    private java.lang.String activityName;
    
 	@Schema(description = "学生ID")
    private java.lang.String studentId;
    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "班级名称")
    private java.lang.String className;
    
 	@Schema(description = "提交次数")
    private java.lang.Integer submitCount;
    
 	@Schema(description = "总得分")
    private java.math.BigDecimal totalScore;
    
 	@Schema(description = "正确题数")
    private java.lang.Integer correctCount;
    
 	@Schema(description = "错误题数")
    private java.lang.Integer wrongCount;
    
 	@Schema(description = "正确率(%)")
    private java.math.BigDecimal correctRate;
    
 	@Schema(description = "平均每题用时(秒)")
    private java.lang.Integer averageTimePerQuestion;
    
 	@Schema(description = "班级排名")
    private java.lang.Integer rankInClass;
    
 	@Schema(description = "排名百分比")
    private java.math.BigDecimal rankPercentile;
    
 	@Schema(description = "较上次提交进步(%)")
    private java.math.BigDecimal comparedToLast;
    
 	@Schema(description = "统计时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsTime;
    
 	@Schema(description = "状态")
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
    
 	@Schema(description = "各题得分分布")
    private java.lang.String scoreDistribution;
    
 	@Schema(description = "知识点掌握情况")
    private java.lang.String knowledgeMastery;
    
 	@Schema(description = "薄弱点分析")
    private java.lang.String weakPoints;
    
 	@Schema(description = "提升建议")
    private java.lang.String improvementSuggestions;
    
 	@Schema(description = "AI综合分析")
    private java.lang.String aiAnalysis;
    
	

}
