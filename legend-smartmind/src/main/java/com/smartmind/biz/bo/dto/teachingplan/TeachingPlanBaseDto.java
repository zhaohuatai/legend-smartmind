package com.smartmind.biz.bo.dto.teachingplan;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class TeachingPlanBaseDto{

	
	public TeachingPlanBaseDto() {}
	//<-------------------------------------------->

 
	@jakarta.validation.constraints.NotBlank(message="教案编码不能为空")
	@jakarta.validation.constraints.NotNull(message="教案编码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=50,message="教案编码长度不能大于50")
	@Schema(description = "教案编码")
 	private java.lang.String planCode;
 
	@jakarta.validation.constraints.NotBlank(message="教案名称不能为空")
	@jakarta.validation.constraints.NotNull(message="教案名称不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="教案名称长度不能大于200")
	@Schema(description = "教案名称")
 	private java.lang.String planName;
 

	@jakarta.validation.constraints.NotNull(message="课程ID不能为空")

	@Schema(description = "课程ID")
 	private java.lang.Long courseId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="课程名称长度不能大于100")
	@Schema(description = "课程名称")
 	private java.lang.String courseName;
 

	@jakarta.validation.constraints.NotNull(message="单元ID不能为空")

	@Schema(description = "单元ID")
 	private java.lang.Long unitId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="单元名称长度不能大于100")
	@Schema(description = "单元名称")
 	private java.lang.String unitName;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="单元编码长度不能大于50")
	@Schema(description = "单元编码")
 	private java.lang.String unitCode;
 

//	@jakarta.validation.constraints.NotNull(message="课时序号不能为空")
	@Schema(description = "课时序号")
 	private java.lang.Integer lessonHour;
 

	@jakarta.validation.constraints.NotNull(message="第几次课不能为空")

	@Schema(description = "第几次课")
 	private java.lang.Integer lessonSession;
// 
//	@jakarta.validation.constraints.NotBlank(message="课时主题不能为空")
//	@jakarta.validation.constraints.NotNull(message="课时主题不能为空")
	@jakarta.validation.constraints.Size(min=0,max=200,message="课时主题长度不能大于200")
	@Schema(description = "课时主题")
 	private java.lang.String lessonTitle;
 
    @Schema(description = "授课内容简介")
    private String lessonSummary;
    
	@Schema(description = "设计教师ID")
 	private java.lang.String teacherId;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="教师姓名长度不能大于50")
	@Schema(description = "教师姓名")
 	private java.lang.String teacherName;
 

	@Schema(description = "授课班级ID")
 	private java.lang.Long classId;
 
	@jakarta.validation.constraints.Size(min=0,max=100,message="授课班级名称长度不能大于100")
	@Schema(description = "授课班级名称")
 	private java.lang.String className;
 

	@Schema(description = "计划授课日期")
 	private java.util.Date teachingDate;
 
	@jakarta.validation.constraints.Size(min=0,max=50,message="授课时间长度不能大于50")
	@Schema(description = "授课时间")
 	private java.lang.String teachingTime;
 

	@Schema(description = "预计课时数（学时）")
 	private java.lang.Integer estimatedHours;
 

	@Schema(description = "AI生成模式")
 	private java.lang.Byte aiGenerateMode;
 
//	@jakarta.validation.constraints.NotBlank(message="状态不能为空")
//	@jakarta.validation.constraints.NotNull(message="状态不能为空")
	@jakarta.validation.constraints.Size(min=0,max=1,message="状态长度不能大于1")
	@Schema(description = "状态")
 	private java.lang.String status;
 


	@Schema(description = "版本号")
 	private java.lang.Integer version;
 

 

 
	@jakarta.validation.constraints.Size(min=0,max=64,message="创建人长度不能大于64")
	@Schema(description = "创建人")
 	private java.lang.String createBy;
 
	@jakarta.validation.constraints.Size(min=0,max=64,message="更新人长度不能大于64")
	@Schema(description = "更新人")
 	private java.lang.String updateBy;
 
	@jakarta.validation.constraints.Size(min=0,max=500,message="备注长度不能大于500")
	@Schema(description = "备注")
 	private java.lang.String remark;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="知识目标长度不能大于65,535")
	@Schema(description = "知识目标")
 	private java.lang.String knowledgeObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="能力目标长度不能大于65,535")
	@Schema(description = "能力目标")
 	private java.lang.String abilityObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="素养目标长度不能大于65,535")
	@Schema(description = "素养目标")
 	private java.lang.String literacyObjectives;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教学重点长度不能大于65,535")
	@Schema(description = "教学重点")
 	private java.lang.String keyPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教学难点长度不能大于65,535")
	@Schema(description = "教学难点")
 	private java.lang.String difficultPoints;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="教学方法列表长度不能大于2,147,483,647")
	@Schema(description = "教学方法列表")
 	private java.lang.String teachingMethods;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="教学工具/媒体长度不能大于2,147,483,647")
	@Schema(description = "教学工具/媒体")
 	private java.lang.String teachingTools;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教学主要内容长度不能大于65,535")
	@Schema(description = "教学主要内容")
 	private java.lang.String mainContent;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教学资源长度不能大于65,535")
	@Schema(description = "教学资源")
 	private java.lang.String teachingResources;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="参考资料长度不能大于65,535")
	@Schema(description = "参考资料")
 	private java.lang.String referenceMaterials;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="教学反思长度不能大于65,535")
	@Schema(description = "教学反思")
 	private java.lang.String teachingReflection;
 
	@jakarta.validation.constraints.Size(min=0,max=2147483647,message="教案完整内容长度不能大于2,147,483,647")
	@Schema(description = "教案完整内容")
 	private java.lang.String markdownContent;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI生成提示词长度不能大于65,535")
	@Schema(description = "AI生成提示词")
 	private java.lang.String aiPrompt;
 
	@jakarta.validation.constraints.Size(min=0,max=65535,message="AI优化建议长度不能大于65,535")
	@Schema(description = "AI优化建议")
 	private java.lang.String aiSuggestions;
 
	

}
