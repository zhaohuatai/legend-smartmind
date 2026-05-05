package com.smartmind.biz.bo.dto.dataexportlog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "数据导出日志创建Dto")
public class DataExportLogCreateDto extends DataExportLogBaseDto{

	

}
