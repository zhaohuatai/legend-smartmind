package com.smartmind.biz.bo.dto.learningsnapshot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学习快照创建Dto")
public class LearningSnapshotCreateDto extends LearningSnapshotBaseDto{

	

}
