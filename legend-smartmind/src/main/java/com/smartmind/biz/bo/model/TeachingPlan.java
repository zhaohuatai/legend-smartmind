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
@Table(name="smd_teaching_plan")
@TableFieldMode(FieldMode.camel_to_underscore)
public class TeachingPlan extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "教案编码")
    private java.lang.String planCode;
    
 	@Schema(description = "教案名称")
    private java.lang.String planName;
    
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
    
 	@Schema(description = "课时序号")
    private java.lang.Integer lessonHour;
    
 	@Schema(description = "第几次课")
    private java.lang.Integer lessonSession;
    
 	@Schema(description = "课时主题")
    private java.lang.String lessonTitle;
 	
    @Schema(description = "授课内容简介")
    private String lessonSummary;
    
 	@Schema(description = "设计教师ID")
    private java.lang.String teacherId;
    
 	@Schema(description = "教师姓名")
    private java.lang.String teacherName;
    
 	@Schema(description = "授课班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "授课班级名称")
    private java.lang.String className;
    
 	@Schema(description = "计划授课日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date teachingDate;
    
 	@Schema(description = "授课时间")
    private java.lang.String teachingTime;
    
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
    
 	@Schema(description = "知识目标")
    private java.lang.String knowledgeObjectives;
    
 	@Schema(description = "能力目标")
    private java.lang.String abilityObjectives;
    
 	@Schema(description = "素养目标")
    private java.lang.String literacyObjectives;
    
 	@Schema(description = "教学重点")
    private java.lang.String keyPoints;
    
 	@Schema(description = "教学难点")
    private java.lang.String difficultPoints;
    
 	@Schema(description = "教学方法列表")
    private java.lang.String teachingMethods;
    
 	@Schema(description = "教学工具/媒体")
    private java.lang.String teachingTools;
    
 	@Schema(description = "教学主要内容")
    private java.lang.String mainContent;
    
 	@Schema(description = "教学资源")
    private java.lang.String teachingResources;
    
 	@Schema(description = "参考资料")
    private java.lang.String referenceMaterials;
    
 	@Schema(description = "教学反思")
    private java.lang.String teachingReflection;
    
 	@Schema(description = "教案完整内容")
    private java.lang.String markdownContent;
    
 	@Schema(description = "AI生成提示词")
    private java.lang.String aiPrompt;
    
 	@Schema(description = "AI优化建议")
    private java.lang.String aiSuggestions;
    
	

}
