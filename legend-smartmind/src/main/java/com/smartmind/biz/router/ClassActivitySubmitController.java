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
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitPageQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitStudentQueryDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitCreateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.ClassActivitySubmitUpdateDto;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmissionVo;
import com.smartmind.biz.bo.dto.classactivitysubmit.ActivityStatsVo;
import com.smartmind.biz.bo.dto.classactivitysubmit.StudentSubmitRequestDto;
import com.smartmind.biz.bo.dto.classactivity.detail.StudentActivityDetailVo;
import com.smartmind.biz.bo.model.ClassActivitySubmit;

import com.smartmind.biz.service.IClassActivitySubmitService;
import com.smartmind.biz.service.IStudentActivitySubmitService;
import com.smartmind.biz.service.IStudentActivityDetailQueryService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/classactivitysubmit")
public class ClassActivitySubmitController implements BaseController {

	
	@Autowired
	private IClassActivitySubmitService classActivitySubmitService;

	@Autowired
	private IStudentActivitySubmitService studentActivitySubmitService;

	@Autowired
	private IStudentActivityDetailQueryService studentActivityDetailQueryService;
	
	//@RequiresPermissions(value="classActivitySubmit:loadDataSet",desc="学生课堂活动提交列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ClassActivitySubmitPageQueryDto pageQueryDto){
    	 ChainWrapper<ClassActivitySubmit> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ClassActivitySubmit.class);
	     DataSetDto<ClassActivitySubmit> dataSet=  classActivitySubmitService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="classActivitySubmit:create",desc="学生课堂活动提交添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ClassActivitySubmitCreateDto classActivitySubmitCreateDto){
		classActivitySubmitService.createClassActivitySubmit(classActivitySubmitCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="classActivitySubmit:load",desc="学生课堂活动提交加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ClassActivitySubmit> ops = classActivitySubmitService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="classActivitySubmit:update",desc="学生课堂活动提交更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ClassActivitySubmitUpdateDto classActivitySubmitUpdateDto){
		classActivitySubmitService.updateClassActivitySubmit(classActivitySubmitUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	

	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		classActivitySubmitService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    
    @RequestMapping(value="/getStudentSubmissions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object getStudentSubmissions(@RequestBody ClassActivitySubmitPageQueryDto queryDto){
		Long activityId = queryDto.getActivityId();
		List<StudentSubmissionVo> list = classActivitySubmitService.getStudentSubmissions(activityId);
		return ajaxQueryResult(list);
    }
    
    @RequestMapping(value="/getActivityStats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object getActivityStats(@RequestBody ClassActivitySubmitPageQueryDto queryDto){
		Long activityId = queryDto.getActivityId();
		ActivityStatsVo stats = classActivitySubmitService.getActivityStats(activityId);
		return ajaxQueryResult(stats);
    }
    
    @RequestMapping(value="/submitAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object submitAnswer(@RequestBody StudentSubmitRequestDto request){
		studentActivitySubmitService.submitAnswer(request);
		return ajaxDoneSuccess("提交成功");
    }

    @RequestMapping(value="/loadStudentActivityDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadStudentActivityDetail(@RequestBody ClassActivitySubmitStudentQueryDto queryDto){
		Long activityId = queryDto.getActivityId();
		String studentId = queryDto.getStudentId();
		StudentActivityDetailVo detail = studentActivityDetailQueryService.queryStudentActivityDetail(activityId, studentId,true);
		return ajaxQueryResult(detail);
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = classActivitySubmitService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
