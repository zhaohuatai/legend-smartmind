package com.smartmind.biz.bo.dto.exampaper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "试卷创建Dto")
public class ExamPaperCreateDto extends ExamPaperBaseDto{

	

}
