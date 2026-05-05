package com.smartmind.biz.bo.dto.classstudent;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生班级关联分页查询DTO")
public class ClassStudentPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "加入时间开始")
	private java.util.Date joinTimeStart;
	@Schema(description = "加入时间截止")
	private java.util.Date joinTimeEnd;
	
	@Schema(description = "离开时间开始")
	private java.util.Date leaveTimeStart;
	@Schema(description = "离开时间截止")
	private java.util.Date leaveTimeEnd;
	


}
