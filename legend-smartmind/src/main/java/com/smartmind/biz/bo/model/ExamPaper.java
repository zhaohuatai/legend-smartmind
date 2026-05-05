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
@Table(name="smd_exam_paper")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ExamPaper extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "试卷编码")
    private java.lang.String paperCode;
    
 	@Schema(description = "试卷名称")
    private java.lang.String paperName;
    
 	@Schema(description = "试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他")
    private java.lang.String paperType;
    
 	@Schema(description = "所属课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "所属单元ID")
    private java.lang.Long unitId;
    
 	@Schema(description = "单元名称")
    private java.lang.String unitName;
    
 	@Schema(description = "关联教案ID")
    private java.lang.Long planId;
    
 	@Schema(description = "总分值")
    private java.math.BigDecimal totalScore;
    
 	@Schema(description = "题目数量")
    private java.lang.Integer questionCount;
    
 	@Schema(description = "考试时长(分钟)")
    private java.lang.Integer duration;
    
 	@Schema(description = "AI生成模式")
    private java.lang.String aiGenerateMode;
    
 	@Schema(description = "使用次数")
    private java.lang.Integer usageCount;
    
 	@Schema(description = "历史平均分")
    private java.math.BigDecimal averageScore;
    
 	@Schema(description = "来源类型")
    private java.lang.String sourceType;
    
 	@Schema(description = "状态: 0-草稿, 1-已发布, 2-已归档")
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
    
 	@Schema(description = "难度分布设置")
    private java.lang.String difficultyDistribution;
    
 	@Schema(description = "知识点覆盖范围")
    private java.lang.String knowledgeCoverage;
    
 	@Schema(description = "AI组卷提示词")
    private java.lang.String aiPrompt;
    
 	@Schema(description = "AI试卷评估")
    private java.lang.String aiEvaluation;
    
	

}
