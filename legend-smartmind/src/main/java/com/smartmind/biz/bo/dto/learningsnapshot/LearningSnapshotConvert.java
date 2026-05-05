package com.smartmind.biz.bo.dto.learningsnapshot;
import com.smartmind.biz.bo.model.LearningSnapshot;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LearningSnapshotConvert {

    LearningSnapshotConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(LearningSnapshotConvert.class);

    
    LearningSnapshot convert(LearningSnapshotCreateDto createDto);

    LearningSnapshot convert(LearningSnapshotUpdateDto updateDto);


   
}
