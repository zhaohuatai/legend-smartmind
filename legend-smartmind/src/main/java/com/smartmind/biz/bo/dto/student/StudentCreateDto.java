package com.smartmind.biz.bo.dto.student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生信息创建Dto")
public class StudentCreateDto extends StudentBaseDto{



//	@jakarta.validation.constraints.NotBlank(message="密码不能为空")
//	@jakarta.validation.constraints.NotNull(message="密码不能为空")
	@jakarta.validation.constraints.Size(min=0,max=100,message="密码长度不能大于100")
	@Schema(description = "登录密码")
 	private java.lang.String password;

}
