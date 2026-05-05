package com.smartmind.biz.bo.dto.teachingfile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "教学文件创建Dto")
public class TeachingFileCreateDto extends TeachingFileBaseDto{

	

}
