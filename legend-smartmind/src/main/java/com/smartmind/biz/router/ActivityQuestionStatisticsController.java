package com.smartmind.biz.router;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsPageQueryDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activityquestionstatistics.ActivityQuestionStatisticsUpdateDto;
import com.smartmind.biz.bo.model.ActivityQuestionStatistics;

import com.smartmind.biz.service.IActivityQuestionStatisticsService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/activityquestionstatistics")
public class ActivityQuestionStatisticsController implements BaseController {

	
	@Autowired
	private IActivityQuestionStatisticsService activityQuestionStatisticsService;
	
	//@RequiresPermissions(value="activityQuestionStatistics:loadDataSet",desc="题目统计列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ActivityQuestionStatisticsPageQueryDto pageQueryDto){
    	 ChainWrapper<ActivityQuestionStatistics> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ActivityQuestionStatistics.class);
	     DataSetDto<ActivityQuestionStatistics> dataSet=  activityQuestionStatisticsService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="activityQuestionStatistics:create",desc="题目统计添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ActivityQuestionStatisticsCreateDto activityQuestionStatisticsCreateDto){
		activityQuestionStatisticsService.createActivityQuestionStatistics(activityQuestionStatisticsCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="activityQuestionStatistics:load",desc="题目统计加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ActivityQuestionStatistics> ops = activityQuestionStatisticsService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="activityQuestionStatistics:update",desc="题目统计更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ActivityQuestionStatisticsUpdateDto activityQuestionStatisticsUpdateDto){
		activityQuestionStatisticsService.updateActivityQuestionStatistics(activityQuestionStatisticsUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="activityQuestionStatistics:delete",desc="题目统计删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	activityQuestionStatisticsService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="activityQuestionStatistics:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		activityQuestionStatisticsService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = activityQuestionStatisticsService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
