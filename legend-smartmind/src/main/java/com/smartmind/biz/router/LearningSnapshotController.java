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
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotPageQueryDto;
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotCreateDto;
import com.smartmind.biz.bo.dto.learningsnapshot.LearningSnapshotUpdateDto;
import com.smartmind.biz.bo.model.LearningSnapshot;

import com.smartmind.biz.service.ILearningSnapshotService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/learningsnapshot")
public class LearningSnapshotController implements BaseController {

	
	@Autowired
	private ILearningSnapshotService learningSnapshotService;
	
	//@RequiresPermissions(value="learningSnapshot:loadDataSet",desc="学习快照列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody LearningSnapshotPageQueryDto pageQueryDto){
    	 ChainWrapper<LearningSnapshot> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, LearningSnapshot.class);
	     DataSetDto<LearningSnapshot> dataSet=  learningSnapshotService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="learningSnapshot:create",desc="学习快照添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody LearningSnapshotCreateDto learningSnapshotCreateDto){
		learningSnapshotService.createLearningSnapshot(learningSnapshotCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="learningSnapshot:load",desc="学习快照加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<LearningSnapshot> ops = learningSnapshotService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="learningSnapshot:update",desc="学习快照更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody LearningSnapshotUpdateDto learningSnapshotUpdateDto){
		learningSnapshotService.updateLearningSnapshot(learningSnapshotUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="learningSnapshot:delete",desc="学习快照删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	learningSnapshotService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="learningSnapshot:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		learningSnapshotService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = learningSnapshotService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
