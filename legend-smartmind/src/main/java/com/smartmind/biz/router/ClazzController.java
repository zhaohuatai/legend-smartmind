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
import org.legend.framework.auth.shiro.util.SecurityUtil;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.base.dao.mybatis.query.DtoToWrapperUtil;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.SelectVo;
import org.legend.framework.core.data.StatusDto;
import org.legend.framework.core.data.StatusListDto;
import com.smartmind.biz.bo.dto.clazz.ClazzPageQueryDto;
import com.smartmind.biz.bo.dto.clazz.ClazzCreateDto;
import com.smartmind.biz.bo.dto.clazz.ClazzUpdateDto;
import com.smartmind.biz.bo.model.Clazz;
import com.smartmind.biz.service.IClazzService;

@Controller
@RequestMapping("/manage/smartmind/clazz")
public class ClazzController implements BaseController {

	@Autowired
	private IClazzService clazzService;

	@RequestMapping(value="/loadDataSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadDataSet(@RequestBody ClazzPageQueryDto pageQueryDto){
    	 ChainWrapper<Clazz> chainWrapper = DtoToWrapperUtil.convertToChainWrapper(pageQueryDto, Clazz.class);
	     DataSetDto<Clazz> dataSet= clazzService.loadDataSetByQuery(chainWrapper);
	    return ajaxQueryResult(dataSet);
    }

    @RequestMapping(value="/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object create(@RequestBody ClazzCreateDto clazzCreateDto){
		clazzService.createClazz(clazzCreateDto, SecurityUtil.getSimpleUser());
    	return ajaxDoneSuccess("数据操作成功");
    }

    @RequestMapping(value="/loadById/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object loadById(@PathVariable Long id){
		Optional<Clazz> ops = clazzService.loadById(id);
		return ajaxQueryResult(ops);
    }

    @RequestMapping(value="/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object update(@RequestBody ClazzUpdateDto clazzUpdateDto){
		clazzService.updateClazz(clazzUpdateDto, SecurityUtil.getSimpleUser());
		return ajaxDoneSuccess("数据操作成功");
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object delete(@PathVariable Long id){
		clazzService.deleteById(id);
		return ajaxDoneSuccess("删除成功");
    }

	@RequestMapping(value="/setStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object setStatus(@RequestBody StatusDto status){
		clazzService.setStatus(status);
		return ajaxDoneSuccess("数据操作成功");
    }

	@RequestMapping(value="/selectVo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object selectVo(){
		List<SelectVo> list = clazzService.loadSelectVo();
		return ajaxQueryResult(list);
	}

}
