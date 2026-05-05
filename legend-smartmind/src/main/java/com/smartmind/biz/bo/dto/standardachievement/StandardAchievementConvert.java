package com.smartmind.biz.bo.dto.standardachievement;
import com.smartmind.biz.bo.model.StandardAchievement;
import org.mapstruct.ReportingPolicy;
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StandardAchievementConvert {

    StandardAchievementConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(StandardAchievementConvert.class);

    
    StandardAchievement convert(StandardAchievementCreateDto createDto);

    StandardAchievement convert(StandardAchievementUpdateDto updateDto);


   
}
