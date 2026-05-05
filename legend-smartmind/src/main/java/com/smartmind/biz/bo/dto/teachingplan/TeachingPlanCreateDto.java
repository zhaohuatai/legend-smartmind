package com.smartmind.biz.bo.dto.teachingplan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教案创建Dto")
public class TeachingPlanCreateDto extends TeachingPlanBaseDto{

	

}
