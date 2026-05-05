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
@Table(name="smd_course")
@TableFieldMode(FieldMode.camel_to_underscore)
public class Course extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "课程编码")
    private java.lang.String courseCode;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "学科类型")
    private java.lang.String subjectType;
    
 	@Schema(description = "面向年级")
    private java.lang.String gradeLevel;
    
 	@Schema(description = "学期")
    private java.lang.String semester;
    
 	@Schema(description = "学年")
    private java.lang.String schoolYear;
    
 	@Schema(description = "学分")
    private java.lang.Byte credit;
    
 	@Schema(description = "总课时")
    private java.lang.Integer totalHours;
    
 	@Schema(description = "教材信息")
    private java.lang.String textbookInfo;
    
 	@Schema(description = "课程封面图地址")
    private java.lang.String coverImage;
    
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
    
 	@Schema(description = "课程描述")
    private java.lang.String courseDesc;
    
 	@Schema(description = "课程目标")
    private java.lang.String courseObjectives;
    
 	@Schema(description = "知识框架结构(JSON格式)")
    private java.lang.String knowledgeFramework;
    
 	@Schema(description = "参考资料")
    private java.lang.String referenceMaterials;
    
 	@Schema(description = "关联班级名称")
    private java.lang.String className;
    
	

}
