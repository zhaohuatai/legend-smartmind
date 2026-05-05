package com.smartmind.biz.router;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.legend.framework.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.base.dao.mybatis.query.DtoToWrapperUtil;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.StatusListDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicPageQueryDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicCreateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicUpdateDto;
import com.smartmind.biz.bo.dto.discussiontopic.DiscussionTopicBatchCreateDto;
import com.smartmind.biz.bo.model.DiscussionTopic;
import com.smartmind.biz.bo.model.ExperimentGuide;
import com.smartmind.biz.service.IDiscussionTopicService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/discussionTopic")
public class DiscussionTopicController implements BaseController {

	
	@Autowired
	private IDiscussionTopicService discussionTopicService;
	
	//@RequiresPermissions(value="discussionTopic:loadDataSet",desc="讨论活动话题列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody DiscussionTopicPageQueryDto pageQueryDto){
    	 ChainWrapper<DiscussionTopic> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, DiscussionTopic.class);
	     DataSetDto<DiscussionTopic> dataSet=  discussionTopicService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="discussionTopic:create",desc="讨论活动话题添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody DiscussionTopicCreateDto discussionTopicCreateDto){
		discussionTopicService.createDiscussionTopic(discussionTopicCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="discussionTopic:load",desc="讨论活动话题加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<DiscussionTopic> ops = discussionTopicService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="discussionTopic:update",desc="讨论活动话题更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody DiscussionTopicUpdateDto discussionTopicUpdateDto){
		discussionTopicService.updateDiscussionTopic(discussionTopicUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="discussionTopic:delete",desc="讨论活动话题删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	discussionTopicService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="discussionTopic:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		discussionTopicService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    
    @RequestMapping(value="/loadByCourseAndUnit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnit(@RequestParam Long courseId, @RequestParam(required = false) String unitCode){
		List<DiscussionTopic> list = discussionTopicService.loadByCourseIdAndUnitCode(courseId, unitCode);
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/loadByCourseAndUnitCodes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadByCourseAndUnitCodes(@RequestBody DiscussionTopicPageQueryDto queryDto){
		List<DiscussionTopic> list = discussionTopicService.loadByCourseIdAndUnitCodes(queryDto.getCourseId(), queryDto.getUnitCodes());
		return ajaxQuery(list);
    }
    
    @RequestMapping(value="/batchCreate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object batchCreate(@RequestBody DiscussionTopicBatchCreateDto batchCreateDto){
		int count = discussionTopicService.batchCreateTopics(batchCreateDto.getTopics(), SecurityUtil.getSimpleUser());
		return ajaxSuccess("批量创建成功，共创建" + count + "个话题", count);
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = discussionTopicService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
