package com.smartmind.biz.bo.dto.teacher;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教师信息创建Dto")
public class TeacherCreateDto extends TeacherBaseDto{


	@jakarta.validation.constraints.Size(min=0,max=100,message="密码长度不能大于100")
	@Schema(description = "登录密码")
 	private java.lang.String password;

}
