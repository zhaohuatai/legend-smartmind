package com.smartmind.biz.bo.dto.student;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生信息分页查询DTO")
public class StudentPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "学号")
	private java.lang.String studentNo;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "身份证号")
	private java.lang.String idCard;
	
	@Schema(description = "出生日期开始")
	private java.util.Date birthDateStart;
	@Schema(description = "出生日期截止")
	private java.util.Date birthDateEnd;
	
	@Schema(description = "联系电话")
	private java.lang.String phone;
	
	@Schema(description = "邮箱")
	private java.lang.String email;
	
	@Schema(description = "入学日期开始")
	private java.util.Date admissionDateStart;
	@Schema(description = "入学日期截止")
	private java.util.Date admissionDateEnd;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;


}
