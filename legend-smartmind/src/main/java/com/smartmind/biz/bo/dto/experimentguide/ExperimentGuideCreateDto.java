package com.smartmind.biz.bo.dto.experimentguide;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "实验指导书创建Dto")
public class ExperimentGuideCreateDto extends ExperimentGuideBaseDto{

	

}
