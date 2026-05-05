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
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionPageQueryDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionCreateDto;
import com.smartmind.biz.bo.dto.exampaperquestion.ExamPaperQuestionUpdateDto;
import com.smartmind.biz.bo.model.ExamPaperQuestion;

import com.smartmind.biz.service.IExamPaperQuestionService;
import org.legend.framework.core.data.SelectVo;
@Controller 
@RequestMapping("/manage/smartmind/exampaperquestion")
public class ExamPaperQuestionController implements BaseController {

	
	@Autowired
	private IExamPaperQuestionService examPaperQuestionService;
	
	//@RequiresPermissions(value="examPaperQuestion:loadDataSet",desc="试卷题目关联列表")
    @RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadDataSet(@RequestBody ExamPaperQuestionPageQueryDto pageQueryDto){
    	 ChainWrapper<ExamPaperQuestion> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, ExamPaperQuestion.class);
	     DataSetDto<ExamPaperQuestion> dataSet=  examPaperQuestionService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult( dataSet);
    }
	
	//@RequiresPermissions(value="examPaperQuestion:create",desc="试卷题目关联添加")
    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Object create(@RequestBody ExamPaperQuestionCreateDto examPaperQuestionCreateDto){
		examPaperQuestionService.createExamPaperQuestion(examPaperQuestionCreateDto,SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="examPaperQuestion:load",desc="试卷题目关联加载")
    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object loadById(@PathVariable Long id){
		Optional<ExamPaperQuestion> ops = examPaperQuestionService.loadById(id);
		return ajaxQueryResult(ops);
    }
	
	//@RequiresPermissions(value="examPaperQuestion:update",desc="试卷题目关联更新")
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object update(@RequestBody ExamPaperQuestionUpdateDto examPaperQuestionUpdateDto){
		examPaperQuestionService.updateExamPaperQuestion(examPaperQuestionUpdateDto,SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }
	
	//@RequiresPermissions(value="examPaperQuestion:delete",desc="试卷题目关联删除")
    //@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object delete(Long id){
	//	examPaperQuestionService.delete(id);
	//	return ajaxDoneSuccess("数据操作成功");
    //}
    
    @RequiresPermissions(value="examPaperQuestion:load",desc="宠物信息加载")
	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  Object setStatus(@RequestBody StatusDto status){
		examPaperQuestionService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }
    

    //@RequestMapping(value="/loadSelectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    //public @ResponseBody  Object loadSelectVo(String keywords){
	//	List<SelectVo>  ops = examPaperQuestionService.loadSelectVo(SecurityUtil.getTenantId(),"1",keywords);
	//	return ajaxQuery(ops);
    //}
    
}
