package com.smartmind.biz.bo.model;
import org.legend.framework.core.data.IBaseModel;
import org.legend.framework.base.dao.mybatis.annotation.PKId;
import org.legend.framework.base.dao.mybatis.annotation.TableFieldMode;
import org.legend.framework.base.dao.mybatis.enums.FieldMode;
import org.legend.framework.base.dao.mybatis.enums.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
@Table(name="smd_learning_unit")
@TableFieldMode(FieldMode.camel_to_underscore)
public class LearningUnit extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
	@JsonSerialize(using = ToStringSerializer.class)
    private java.lang.Long id;
    
 	@Schema(description = "单元编码")
    private java.lang.String unitCode;
    
 	@Schema(description = "单元名称")
    private java.lang.String unitName;
    
 	@Schema(description = "所属课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "父单元ID(0表示根单元)")
	@JsonSerialize(using = ToStringSerializer.class)
    private java.lang.Long parentId;
    
 	@Schema(description = "单元层级: 1-大单元, 2-子单元, 3-课时")
    private java.lang.String unitLevel;
    
 	@Schema(description = "排序序号")
    private java.lang.Integer sortOrder;
    
 	@Schema(description = "预计课时数")
    private java.lang.Integer estimatedHours;
    
 	@Schema(description = "计划开始日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    
 	@Schema(description = "计划结束日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    
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
    
 	@Schema(description = "单元描述")
    private java.lang.String unitDesc;
    
 	@Schema(description = "单元目标(知识、能力、素养)")
    private java.lang.String unitObjectives;
    
 	@Schema(description = "重难点分析")
    private java.lang.String keyPoints;
    
 	@Schema(description = "知识点标签列表")
    private java.lang.String knowledgeTags;
    
 	@Schema(description = "课标对应关系")
    private java.lang.String standardMapping;
    
 	@Schema(description = "前置单元ID列表")
    private java.lang.String prerequisiteUnits;
    
	

}
