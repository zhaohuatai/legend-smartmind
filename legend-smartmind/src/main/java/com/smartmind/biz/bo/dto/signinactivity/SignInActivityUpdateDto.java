package com.smartmind.biz.bo.dto.signinactivity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到活动更新Dto")
public class SignInActivityUpdateDto extends SignInActivityBaseDto{
	
	@NotNull(message="ID不能为空")
	@Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
	private Long id;

}
