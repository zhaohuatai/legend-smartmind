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
@Table(name="smd_standard_achievement")
@TableFieldMode(FieldMode.camel_to_underscore)
public class StandardAchievement extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
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
    
 	@Schema(description = "课标条目ID")
    private java.lang.Long standardId;
    
 	@Schema(description = "知识点")
    private java.lang.String knowledgePoint;
    
 	@Schema(description = "要求层级: 1-了解, 2-理解, 3-掌握, 4-应用")
    private java.lang.String requirementLevel;
    
 	@Schema(description = "被测次数")
    private java.lang.Integer testedCount;
    
 	@Schema(description = "正确次数")
    private java.lang.Integer correctCount;
    
 	@Schema(description = "达成率(%)")
    private java.math.BigDecimal achievementRate;
    
 	@Schema(description = "是否达成: 0-否, 1-是")
    private java.lang.String achieved;
    
 	@Schema(description = "统计时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date statisticsTime;
    
 	@Schema(description = "计算类型: 1-班级整体, 2-个人")
    private java.lang.String calculationType;
    
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
    
 	@Schema(description = "差距分析")
    private java.lang.String gapAnalysis;
    
 	@Schema(description = "提升计划")
    private java.lang.String improvementPlan;
    
	

}
