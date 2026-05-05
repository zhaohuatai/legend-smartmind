package com.smartmind.biz.bo.dto.signinrecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "签到记录创建Dto")
public class SignInRecordCreateDto extends SignInRecordBaseDto{

	

}
