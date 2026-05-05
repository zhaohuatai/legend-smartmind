package com.smartmind.biz.bo.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "学生个人信息更新Dto")
public class StudentProfileUpdateDto {

	@Schema(description = "联系电话")
	private String phone;

	@Schema(description = "邮箱")
	private String email;

	@Schema(description = "照片地址")
	private String photoUrl;
}
