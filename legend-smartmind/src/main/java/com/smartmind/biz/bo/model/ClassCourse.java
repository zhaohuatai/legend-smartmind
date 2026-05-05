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
@Table(name="smd_class_course")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ClassCourse extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "班级ID")
    private java.lang.Long classId;
    
 	@Schema(description = "班级名称")
    private java.lang.String className;
    
 	@Schema(description = "课程ID")
    private java.lang.Long courseId;
    
 	@Schema(description = "课程名称")
    private java.lang.String courseName;
    
 	@Schema(description = "学期")
    private java.lang.String semester;
    
 	@Schema(description = "学年")
    private java.lang.String schoolYear;
    
 	@Schema(description = "授课时间(如: 周一第3节)")
    private java.lang.String teachingTime;
    
 	@Schema(description = "授课教室")
    private java.lang.String classroom;
    
 	@Schema(description = "开始周次")
    private java.lang.Integer startWeek;
    
 	@Schema(description = "结束周次")
    private java.lang.Integer endWeek;
    
 	@Schema(description = "安排类型: 1-固定班级, 2-走班排课")
    private java.lang.String arrangementType;
    
 	@Schema(description = "状态: 0-失效, 1-有效, 2-已结课")
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
    
	

}
