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
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementPageQueryDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementCreateDto;
import com.smartmind.biz.bo.dto.standardachievement.StandardAchievementUpdateDto;
import com.smartmind.biz.bo.model.StandardAchievement;

import com.smartmind.biz.service.IStandardAchievementService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/standardachievement")
public class StandardAchievementController implements BaseController {

	
	@Autowired
	private IStandardAchievementService standardAchievementService;
	
	//@RequiresPermissions(value="standardAchievement:loadDataSet",desc="课标达成度列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody StandardAchievementPageQueryDto pageQueryDto){
    	 ChainWrapper<StandardAchievement> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, StandardAchievement.class);
	     DataSetDto<StandardAchievement> dataSet=  standardAchievementService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="standardAchievement:create",desc="课标达成度添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody StandardAchievementCreateDto standardAchievementCreateDto){
		standardAchievementService.createStandardAchievement(standardAchievementCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="standardAchievement:load",desc="课标达成度加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<StandardAchievement> ops = standardAchievementService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="standardAchievement:update",desc="课标达成度更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody StandardAchievementUpdateDto standardAchievementUpdateDto){
		standardAchievementService.updateStandardAchievement(standardAchievementUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="standardAchievement:delete",desc="课标达成度删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	standardAchievementService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="standardAchievement:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		standardAchievementService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = standardAchievementService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
