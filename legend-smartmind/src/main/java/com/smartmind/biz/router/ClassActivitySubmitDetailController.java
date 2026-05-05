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
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailPageQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmitdetail.ClassActivitySubmitDetailUpdateDto;
import com.smartmind.biz.bo.model.ClassActivitySubmitDetail;

import com.smartmind.biz.service.IClassActivitySubmitDetailService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/classactivitysubmitdetail")
public class ClassActivitySubmitDetailController implements BaseController {

	
	@Autowired
	private IClassActivitySubmitDetailService classActivitySubmitDetailService;
	
	//@RequiresPermissions(value="classActivitySubmitDetail:loadDataSet",desc="学生课堂活动提交明细列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ClassActivitySubmitDetailPageQueryDto pageQueryDto){
    	 ChainWrapper<ClassActivitySubmitDetail> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ClassActivitySubmitDetail.class);
	     DataSetDto<ClassActivitySubmitDetail> dataSet=  classActivitySubmitDetailService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="classActivitySubmitDetail:create",desc="学生课堂活动提交明细添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ClassActivitySubmitDetailCreateDto classActivitySubmitDetailCreateDto){
		classActivitySubmitDetailService.createClassActivitySubmitDetail(classActivitySubmitDetailCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivitySubmitDetail:load",desc="学生课堂活动提交明细加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ClassActivitySubmitDetail> ops = classActivitySubmitDetailService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="classActivitySubmitDetail:update",desc="学生课堂活动提交明细更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ClassActivitySubmitDetailUpdateDto classActivitySubmitDetailUpdateDto){
		classActivitySubmitDetailService.updateClassActivitySubmitDetail(classActivitySubmitDetailUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	
    
}
