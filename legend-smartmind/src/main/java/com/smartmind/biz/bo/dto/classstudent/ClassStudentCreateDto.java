package com.smartmind.biz.bo.dto.classstudent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "学生班级关联创建Dto")
public class ClassStudentCreateDto extends ClassStudentBaseDto{

	

}
