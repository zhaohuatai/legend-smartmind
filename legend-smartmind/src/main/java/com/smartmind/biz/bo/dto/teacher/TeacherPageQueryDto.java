package com.smartmind.biz.bo.dto.teacher;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教师信息分页查询DTO")
public class TeacherPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "工号")
	private java.lang.String teacherNo;
	
	@Schema(description = "教师姓名")
	private java.lang.String teacherName;
	
	@Schema(description = "身份证号")
	private java.lang.String idCard;
	
	@Schema(description = "联系电话")
	private java.lang.String phone;
	
	@Schema(description = "邮箱")
	private java.lang.String email;
	


}
