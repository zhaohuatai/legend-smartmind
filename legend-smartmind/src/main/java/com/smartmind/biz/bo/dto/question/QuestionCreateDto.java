package com.smartmind.biz.bo.dto.question;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "题目创建Dto")
public class QuestionCreateDto extends QuestionBaseDto{

	

}
