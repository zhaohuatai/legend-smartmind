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
@Table(name="smd_class_activity_submit")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ClassActivitySubmit extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "提交编码")
    private java.lang.String submitCode;
    
 	@Schema(description = "课堂ID")
    private java.lang.Long sessionId;
 	
 	@Schema(description = "课堂活动ID")
    private java.lang.Long activityId;
    
 	@Schema(description = "学生ID")
    private java.lang.String studentId;
 	

    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "班级ID")
    private java.lang.Long clazzId;

    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "总分")
    private java.math.BigDecimal totalScore;
    
 	@Schema(description = "当前得分")
    private java.math.BigDecimal score;
    
 	@Schema(description = "做题数量")
    private java.lang.Integer submitCount;
    
 	@Schema(description = "状态: 0-未提交, 1-已提交, 2-已评分")
    private java.lang.String status;
    
 	@Schema(description = "提交时间")
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
    
	

}
