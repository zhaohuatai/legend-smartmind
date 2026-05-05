package com.smartmind.biz.bo.dto.classactivitysubmit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生课堂活动提交创建Dto")
public class ClassActivitySubmitCreateDto extends ClassActivitySubmitBaseDto{

	

}
