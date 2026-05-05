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
@Table(name="smd_question")
@TableFieldMode(FieldMode.camel_to_underscore)
public class Question extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "题目标识码")
    private java.lang.String questionCode;
    
 	@Schema(description = "题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题")
    private java.lang.String questionType;
    
 	@Schema(description = "所属课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "所属单元ID")
    private java.lang.Long unitId;
    
 	@Schema(description = "单元名称")
    private java.lang.String unitName;
    
 	@Schema(description = "单元编码")
    private java.lang.String unitCode;
    
 	@Schema(description = "难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难")
    private java.lang.String difficultyLevel;
    
 	@Schema(description = "认知层次: 1-识记, 2-理解, 3-应用, 4-分析, 5-综合, 6-评价")
    private java.lang.String cognitiveLevel;
    
 	@Schema(description = "题目分值")
    private java.math.BigDecimal score;
    
 	@Schema(description = "使用次数")
    private java.lang.Integer usageCount;
    
 	@Schema(description = "历史正确率(%)")
    private java.math.BigDecimal correctRate;
    
 	@Schema(description = "来源类型: 1-系统题库, 2-教师自建, 3-AI生成, 4-导入")
    private java.lang.String sourceType;
    
 	@Schema(description = "状态: 0-失效, 1-有效")
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
    
 	@Schema(description = "题目内容")
    private java.lang.String questionContent;
    
 	@Schema(description = "知识点标签列表")
    private java.lang.String knowledgePoints;
    
 	@Schema(description = "参考答案")
    private java.lang.String answer;
    
 	@Schema(description = "答案解析")
    private java.lang.String answerAnalysis;
    
 	@Schema(description = "AI分析标签(易错点/考察重点等)")
    private java.lang.String aiAnalysisTags;
    
 	@Schema(description = "AI推荐相似题")
    private java.lang.String aiSimilarQuestions;
    
 	@Schema(description = "选项列表JSON结构")
    private java.lang.String options;
    
	

}
