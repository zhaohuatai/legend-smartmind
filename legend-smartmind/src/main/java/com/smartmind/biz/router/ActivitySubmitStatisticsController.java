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
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsPageQueryDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsCreateDto;
import com.smartmind.biz.bo.dto.activitysubmitstatistics.ActivitySubmitStatisticsUpdateDto;
import com.smartmind.biz.bo.model.ActivitySubmitStatistics;

import com.smartmind.biz.service.IActivitySubmitStatisticsService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/activitysubmitstatistics")
public class ActivitySubmitStatisticsController implements BaseController {

	
	@Autowired
	private IActivitySubmitStatisticsService activitySubmitStatisticsService;
	
	//@RequiresPermissions(value="activitySubmitStatistics:loadDataSet",desc="提交统计列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ActivitySubmitStatisticsPageQueryDto pageQueryDto){
    	 ChainWrapper<ActivitySubmitStatistics> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ActivitySubmitStatistics.class);
	     DataSetDto<ActivitySubmitStatistics> dataSet=  activitySubmitStatisticsService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="activitySubmitStatistics:create",desc="提交统计添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ActivitySubmitStatisticsCreateDto activitySubmitStatisticsCreateDto){
		activitySubmitStatisticsService.createActivitySubmitStatistics(activitySubmitStatisticsCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="activitySubmitStatistics:load",desc="提交统计加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ActivitySubmitStatistics> ops = activitySubmitStatisticsService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="activitySubmitStatistics:update",desc="提交统计更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ActivitySubmitStatisticsUpdateDto activitySubmitStatisticsUpdateDto){
		activitySubmitStatisticsService.updateActivitySubmitStatistics(activitySubmitStatisticsUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="activitySubmitStatistics:delete",desc="提交统计删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	activitySubmitStatisticsService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="activitySubmitStatistics:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		activitySubmitStatisticsService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = activitySubmitStatisticsService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
