package com.smartmind.biz.bo.model;
import org.legend.framework.core.data.IBaseModel;
import org.legend.framework.base.dao.mybatis.annotation.PKId;
import org.legend.framework.base.dao.mybatis.annotation.TableFieldMode;
import org.legend.framework.base.dao.mybatis.enums.FieldMode;
import org.legend.framework.base.dao.mybatis.enums.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@ToString(callSuper = true)
@Table(name="smd_class_activity_resource")
@TableFieldMode(FieldMode.camel_to_underscore)
public class ClassActivityResource extends IBaseModel{

	private static final long serialVersionUID = 1L;
	//<-------------------------------------------->
	@PKId(keyType = KeyType.Auto)
 	@Schema(description = "主键ID")
    private java.lang.Long id;
    
 	@Schema(description = "课堂活动ID")
    private java.lang.Long activityId;
    
 	@Schema(description = "资源类型")
    private java.lang.String resourceType;
    
 	@Schema(description = "资源ID")
    private java.lang.Long resourceId;
    
 	@Schema(description = "资源名称")
    private java.lang.String resourceName;
    
 	@Schema(description = "资源分数(题库为每题分数,其他为总分)")
    private java.math.BigDecimal score;
    
 	@Schema(description = "排序")
    private java.lang.Integer sortOrder;
    
 	@Schema(description = "创建时间")
 	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    
	

}
