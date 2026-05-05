package com.smartmind.biz.bo.dto.learningunit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学习单元创建Dto")
public class LearningUnitCreateDto extends LearningUnitBaseDto{

	

}
