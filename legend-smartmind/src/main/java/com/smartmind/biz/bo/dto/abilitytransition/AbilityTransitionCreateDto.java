package com.smartmind.biz.bo.dto.abilitytransition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "能力跃迁创建Dto")
public class AbilityTransitionCreateDto extends AbilityTransitionBaseDto{

	

}
