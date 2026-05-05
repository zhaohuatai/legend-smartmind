package com.smartmind.biz.bo.dto.signinrecord;

import org.legend.framework.core.data.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到记录分页查询DTO")
public class SignInRecordPageQueryDto extends PageParam{
	private static final long serialVersionUID = 1L;

	@Schema(description = "签到活动ID")
	private java.lang.Long signId;
	
	@Schema(description = "学生ID")
	private java.lang.String studentId;
	
	@Schema(description = "学生姓名")
	private java.lang.String studentName;
	
	@Schema(description = "班级ID")
	private java.lang.Long classId;
	
	@Schema(description = "签到时间开始")
	private java.util.Date signTimeStart;
	@Schema(description = "签到时间截止")
	private java.util.Date signTimeEnd;
	


}
