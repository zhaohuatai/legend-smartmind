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
@Table(name="smd_teacher")
@TableFieldMode(FieldMode.camel_to_underscore)
public class Teacher extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.None)
 	@Schema(description = "主键ID(Base36 Snowflake, 15 chars, Sortable)")
    private java.lang.String id;
    
    
 	@Schema(description = "工号")
    private java.lang.String teacherNo;
    
 	@Schema(description = "教师姓名")
    private java.lang.String teacherName;
    
 	@Schema(description = "性别")
    private java.lang.String gender;
    
 	@Schema(description = "身份证号")
    private java.lang.String idCard;
    
 	@Schema(description = "联系电话")
    private java.lang.String phone;
    
 	@Schema(description = "邮箱")
    private java.lang.String email;
    
 	@Schema(description = "照片地址")
    private java.lang.String photoUrl;
    
 	@Schema(description = "所教学科")
    private java.lang.String subjectType;
    
 	@Schema(description = "职称: 1-助教, 2-讲师, 3-副教授, 4-教授")
    private java.lang.String title;
    
 	@Schema(description = "教龄(年)")
    private java.lang.Integer teachingExperience;
    
 	@Schema(description = "学历")
    private java.lang.String education;
    
 	@Schema(description = "教学特长描述")
    private java.lang.String specialty;
    
 	@Schema(description = "状态: 0-失效, 1-有效, 2-离职")
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
    
 	@Schema(description = "AI功能偏好设置")
    private java.lang.String aiPreference;
    
	

}
