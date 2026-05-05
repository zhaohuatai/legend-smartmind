package com.smartmind.biz.bo.dto.classactivityresource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "课堂活动资源关联创建Dto")
public class ClassActivityResourceCreateDto extends ClassActivityResourceBaseDto{

	

}
