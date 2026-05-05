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
@Table(name="smd_ability_transition")
@TableFieldMode(FieldMode.camel_to_underscore)
public class AbilityTransition extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "学生ID")
    private java.lang.String studentId;
    
 	@Schema(description = "学号")
    private java.lang.String studentNo;
    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "起始单元ID")
    private java.lang.Long fromUnitId;
    
 	@Schema(description = "起始单元名称")
    private java.lang.String fromUnitName;
    
 	@Schema(description = "目标单元ID")
    private java.lang.Long toUnitId;
    
 	@Schema(description = "目标单元名称")
    private java.lang.String toUnitName;
    
 	@Schema(description = "起始分数")
    private java.math.BigDecimal fromScore;
    
 	@Schema(description = "目标分数")
    private java.math.BigDecimal toScore;
    
 	@Schema(description = "分数变化")
    private java.math.BigDecimal scoreChange;
    
 	@Schema(description = "变化率(%)")
    private java.math.BigDecimal changeRate;
    
 	@Schema(description = "跃迁类型: 1-进步, 2-退步, 3-稳定")
    private java.lang.String transitionType;
    
 	@Schema(description = "分析时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date analysisTime;
    
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
    
 	@Schema(description = "能力维度变化")
    private java.lang.String abilityDimensions;
    
 	@Schema(description = "可视化数据")
    private java.lang.String visualizationData;
    
 	@Schema(description = "AI分析评语")
    private java.lang.String aiAnalysis;
    
	

}
