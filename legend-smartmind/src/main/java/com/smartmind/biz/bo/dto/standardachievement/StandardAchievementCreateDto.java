package com.smartmind.biz.bo.dto.standardachievement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课标达成度创建Dto")
public class StandardAchievementCreateDto extends StandardAchievementBaseDto{

	

}
