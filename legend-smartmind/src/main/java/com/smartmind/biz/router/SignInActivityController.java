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
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityPageQueryDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityCreateDto;
import com.smartmind.biz.bo.dto.signinactivity.SignInActivityUpdateDto;
import com.smartmind.biz.bo.model.SignInActivity;

import com.smartmind.biz.service.ISignInActivityService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/signinactivity")
public class SignInActivityController implements BaseController {

	
	@Autowired
	private ISignInActivityService signInActivityService;
	
	//@RequiresPermissions(value="signInActivity:loadDataSet",desc="签到活动列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody SignInActivityPageQueryDto pageQueryDto){
    	 ChainWrapper<SignInActivity> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, SignInActivity.class);
	     DataSetDto<SignInActivity> dataSet=  signInActivityService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="signInActivity:create",desc="签到活动添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody SignInActivityCreateDto signInActivityCreateDto){
		signInActivityService.createSignInActivity(signInActivityCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="signInActivity:load",desc="签到活动加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<SignInActivity> ops = signInActivityService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="signInActivity:update",desc="签到活动更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody SignInActivityUpdateDto signInActivityUpdateDto){
		signInActivityService.updateSignInActivity(signInActivityUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="signInActivity:delete",desc="签到活动删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	signInActivityService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="signInActivity:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		signInActivityService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = signInActivityService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
