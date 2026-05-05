package com.smartmind.biz.bo.dto.signinactivity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到活动创建Dto")
public class SignInActivityCreateDto extends SignInActivityBaseDto{

	

}
