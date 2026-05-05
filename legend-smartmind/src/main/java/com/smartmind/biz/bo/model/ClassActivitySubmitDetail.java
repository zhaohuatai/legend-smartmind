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
@Table(name="smd_class_activity_submit_detail")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ClassActivitySubmitDetail extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "提交记录ID")
    private java.lang.Long submitId;
    
 	@Schema(description = "课堂活动ID")
    private java.lang.Long activityId;
    
 	@Schema(description = "资源ID")
    private java.lang.Long resourceId;
    
 	@Schema(description = "资源类型: 1-题库, 2-讨论话题, 4-实验指导书")
    private java.lang.String resourceType;
    
 	@Schema(description = "资源名称")
    private java.lang.String resourceName;
    
 	@Schema(description = "是否正确: 0-错误, 1-正确, NULL-不适用")
    private java.lang.String isCorrect;
    
 	@Schema(description = "得分")
    private java.math.BigDecimal score;
    
 	@Schema(description = "满分")
    private java.math.BigDecimal fullScore;
    
 	@Schema(description = "状态: 0-未作答, 1-已作答, 2-已评分")
    private java.lang.String status;
    
 	@Schema(description = "作答时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date submitTime;
    
 	@Schema(description = "评分时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gradeTime;
    
 	@Schema(description = "评分人")
    private java.lang.String gradeBy;
    
 	@Schema(description = "创建时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    
 	@Schema(description = "更新时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    
 	@Schema(description = "答案内容")
    private java.lang.String answerContent;
    
 	@Schema(description = "智能体评级")
    private java.lang.String aiEvaluate;
    
 	@Schema(description = "教师评价")
    private java.lang.String teacherEvaluate;
    
	

}
