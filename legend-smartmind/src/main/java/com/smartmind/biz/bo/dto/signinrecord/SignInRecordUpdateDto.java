package com.smartmind.biz.bo.dto.signinrecord;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到记录更新Dto")
public class SignInRecordUpdateDto extends SignInRecordBaseDto{
	
	@NotNull(message="ID不能为空")
	@Schema(description = "ID", requiredMode = RequiredMode.REQUIRED)
	private Long id;

}
