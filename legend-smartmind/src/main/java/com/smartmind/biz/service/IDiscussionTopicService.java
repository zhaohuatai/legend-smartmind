package com.smartmind.biz.service;
import java.util.List;
import java.util.Optional;
import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.base.service.IBaseService;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.SelectVo;
import com.smartmind.biz.bo.model.DiscussionTopic;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicCreateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicUpdateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicPageQueryDto;
public interface IDiscussionTopicService extends IBaseService<DiscussionTopic>{
    //List<SelectVo> loadSelectVo(Long tenantId,String status,String keywords);
    
   	int createDiscussionTopic(DiscussionTopicCreateDto discussionTopicCreateVo,SimpleUserBo simpleUser);

	int updateDiscussionTopic(DiscussionTopicUpdateDto discussionTopicUpdateVo,SimpleUserBo simpleUser);
    
    void setStatus(StatusListDto statusListDto);
    
    void setStatus(StatusDto status);

	List<DiscussionTopic> loadByCourseIdAndUnitCode(Long courseId, String unitCode);

	List<DiscussionTopic> loadByCourseIdAndUnitCodes(Long courseId, List<String> unitCodes);

	int batchCreateTopics(List<DiscussionTopicCreateDto> topics, SimpleUserBo simpleUser);
    
}