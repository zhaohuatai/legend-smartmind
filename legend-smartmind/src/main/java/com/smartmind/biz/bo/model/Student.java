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
@Table(name="smd_student")
@TableFieldMode(FieldMode.camel_to_underscore)
public class Student extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.None)
 	@Schema(description = "主键ID(Base36 Snowflake, 15 chars, Sortable)")
    private java.lang.String id;
    
 	@Schema(description = "学号")
    private java.lang.String studentNo;
    
 	@Schema(description = "学生姓名")
    private java.lang.String studentName;
    
 	@Schema(description = "性别: 0-女, 1-男, 2-保密")
    private java.lang.String gender;
    
 	@Schema(description = "身份证号")
    private java.lang.String idCard;
    
 	@Schema(description = "出生日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date birthDate;
    
 	@Schema(description = "联系电话")
    private java.lang.String phone;
    
 	@Schema(description = "邮箱")
    private java.lang.String email;
    
 	@Schema(description = "照片地址")
    private java.lang.String photoUrl;
    
 	@Schema(description = "入学日期")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date admissionDate;
    
 	@Schema(description = "学生类型: 1-正式生, 2-借读生, 3-旁听生")
    private java.lang.String studentType;
    
 	@Schema(description = "学力水平: 1-基础薄弱, 2-中等, 3-良好, 4-优秀")
    private java.lang.String learningLevel;
    
 	@Schema(description = "学习风格")
    private java.lang.String learningStyle;
    
 	@Schema(description = "状态: 0-失效, 1-有效, 2-休学, 3-退学")
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
    
 	@Schema(description = "薄弱知识点记录")
    private java.lang.String weaknessPoints;
    
	

}
