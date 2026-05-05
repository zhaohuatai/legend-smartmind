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
@Table(name="smd_activity_question_statistics")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ActivityQuestionStatistics extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "提交记录ID")
    private java.lang.Long submitId;
    
 	@Schema(description = "活动ID")
    private java.lang.Long activityId;
    
 	@Schema(description = "题目ID")
    private java.lang.Long questionId;
    
 	@Schema(description = "题号")
    private java.lang.Integer questionSeq;
    
 	@Schema(description = "是否正确: 0-否, 1-是")
    private java.lang.String isCorrect;
    
 	@Schema(description = "得分")
    private java.math.BigDecimal score;
    
 	@Schema(description = "用时(秒)")
    private java.lang.Integer timeSpent;
    
 	@Schema(description = "是否修改过答案: 0-否, 1-是")
    private java.lang.String optionChanged;
    
 	@Schema(description = "置信度等级: 1-高, 2-中, 3-低")
    private java.lang.String confidenceLevel;
    
 	@Schema(description = "错误类型")
    private java.lang.String commonMistakeType;
    
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
    
 	@Schema(description = "选择答案")
    private java.lang.String answerOptions;
    
 	@Schema(description = "AI题目分析")
    private java.lang.String aiAnalysis;
    
	

}
