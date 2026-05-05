package com.smartmind.biz.bo.dto.teachingplan;
import com.smartmind.biz.bo.model.TeachingPlan;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeachingPlanConvert {

    TeachingPlanConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(TeachingPlanConvert.class);

    
    TeachingPlan convert(TeachingPlanCreateDto createDto);

    TeachingPlan convert(TeachingPlanUpdateDto updateDto);


   
}
