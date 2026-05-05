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
@Table(name="smd_discussion_topic")
@TableFieldMode(FieldMode.camel_to_underscore)
public class DiscussionTopic extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "话题编码")
    private java.lang.String topicCode;
    
 	@Schema(description = "话题名称")
    private java.lang.String topicName;
    
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
    
 	@Schema(description = "话题类型: 1-开放讨论, 2-辩论赛, 3-案例分析, 4-小组研讨, 5-头脑风暴")
    private java.lang.Byte topicType;
    
 	@Schema(description = "预计讨论时长(分钟)")
    private java.lang.Integer estimatedDuration;
    
 	@Schema(description = "建议小组人数")
    private java.lang.Integer groupSize;
    
 	@Schema(description = "使用次数")
    private java.lang.Integer usageCount;
    
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
    
 	@Schema(description = "话题内容/讨论问题")
    private java.lang.String topicContent;
    
 	@Schema(description = "知识点标签列表")
    private java.lang.String knowledgePoints;
    
 	@Schema(description = "背景材料/案例描述")
    private java.lang.String backgroundMaterial;
    
 	@Schema(description = "教师引导提示")
    private java.lang.String guidanceTips;
    
 	@Schema(description = "预期答案要点")
    private java.lang.String expectedAnswers;
    
 	@Schema(description = "评价标准")
    private java.lang.String evaluationCriteria;
    
	

}
