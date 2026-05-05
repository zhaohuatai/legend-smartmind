package com.smartmind.biz.bo.dto.dataexportlog;
import com.smartmind.biz.bo.model.DataExportLog;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataExportLogConvert {

    DataExportLogConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(DataExportLogConvert.class);

    
    DataExportLog convert(DataExportLogCreateDto createDto);

    DataExportLog convert(DataExportLogUpdateDto updateDto);


   
}
