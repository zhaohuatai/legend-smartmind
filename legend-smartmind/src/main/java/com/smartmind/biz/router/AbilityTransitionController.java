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
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionPageQueryDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionCreateDto;
import com.smartmind.biz.bo.dto.abilitytransition.AbilityTransitionUpdateDto;
import com.smartmind.biz.bo.model.AbilityTransition;

import com.smartmind.biz.service.IAbilityTransitionService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/abilitytransition")
public class AbilityTransitionController implements BaseController {

	
	@Autowired
	private IAbilityTransitionService abilityTransitionService;
	
	//@RequiresPermissions(value="abilityTransition:loadDataSet",desc="能力跃迁列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody AbilityTransitionPageQueryDto pageQueryDto){
    	 ChainWrapper<AbilityTransition> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, AbilityTransition.class);
	     DataSetDto<AbilityTransition> dataSet=  abilityTransitionService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="abilityTransition:create",desc="能力跃迁添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody AbilityTransitionCreateDto abilityTransitionCreateDto){
		abilityTransitionService.createAbilityTransition(abilityTransitionCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="abilityTransition:load",desc="能力跃迁加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<AbilityTransition> ops = abilityTransitionService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="abilityTransition:update",desc="能力跃迁更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody AbilityTransitionUpdateDto abilityTransitionUpdateDto){
		abilityTransitionService.updateAbilityTransition(abilityTransitionUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="abilityTransition:delete",desc="能力跃迁删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	abilityTransitionService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="abilityTransition:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		abilityTransitionService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = abilityTransitionService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
