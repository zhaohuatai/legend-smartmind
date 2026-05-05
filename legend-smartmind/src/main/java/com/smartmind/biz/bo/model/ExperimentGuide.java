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
@Table(name="smd_experiment_guide")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ExperimentGuide extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "指导书编码")
    private java.lang.String guideCode;
    
 	@Schema(description = "指导书名称")
    private java.lang.String guideName;
    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "单元ID")
    private java.lang.Long unitId;
    
 	@Schema(description = "单元名称")
    private java.lang.String unitName;
    
 	@Schema(description = "单元编码")
    private java.lang.String unitCode;
    
 	@Schema(description = "第几次实验")
    private java.lang.Integer experimentSession;
    
 	@Schema(description = "预计课时数（学时）")
    private java.lang.Integer estimatedHours;
    
 	@Schema(description = "AI生成模式")
    private java.lang.Byte aiGenerateMode;
    
 	@Schema(description = "状态")
    private java.lang.String status;
    
 	@Schema(description = "版本号")
    private java.lang.Integer version;
    
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
    
 	@Schema(description = "实验目的")
    private java.lang.String experimentObjectives;
    
 	@Schema(description = "实验原理")
    private java.lang.String experimentPrinciple;
    
 	@Schema(description = "实验器材/设备")
    private java.lang.String experimentEquipment;
    
 	@Schema(description = "实验步骤")
    private java.lang.String experimentSteps;
    
 	@Schema(description = "注意事项")
    private java.lang.String precautions;
    
 	@Schema(description = "实验报告要求")
    private java.lang.String reportRequirements;
    
 	@Schema(description = "实验指导书完整内容")
    private java.lang.String mainContent;
    
 	@Schema(description = "AI生成提示词")
    private java.lang.String aiPrompt;
    
 	@Schema(description = "AI优化建议")
    private java.lang.String aiSuggestions;
    
	

}
