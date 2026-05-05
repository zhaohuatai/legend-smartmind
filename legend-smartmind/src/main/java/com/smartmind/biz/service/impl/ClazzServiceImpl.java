package com.smartmind.biz.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.legend.framework.core.auth.SimpleUserBo;
import org.legend.framework.core.data.DataSetDto;
import org.legend.framework.core.data.SelectVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.legend.framework.base.dao.mybatis.query.ChainWrapper;
import org.legend.framework.core.util.ZBeanUtil;
import org.legend.framework.core.util.ids.ZUidUtil;
import org.legend.framework.base.dao.mybatis.BaseMapper;
import org.legend.framework.core.data.StatusListDto;
import org.legend.framework.core.data.StatusDto;
import com.smartmind.biz.bo.dto.clazz.ClazzCreateDto;
import com.smartmind.biz.bo.dto.clazz.ClazzUpdateDto;
import com.smartmind.biz.bo.dto.clazz.ClazzConvert;
import org.legend.framework.base.service.BaseServiceImpl;
import com.smartmind.biz.service.IClazzService;
import com.smartmind.biz.dao.ClazzMapper;
import com.smartmind.biz.dao.ClassStudentMapper;
import com.smartmind.biz.bo.model.Clazz;
import com.smartmind.biz.bo.model.ClassStudent;

@Service
public class ClazzServiceImpl extends BaseServiceImpl<ClazzMapper, Clazz> implements IClazzService {

	@Autowired
	private ClazzMapper clazzMapper;

	@Autowired
	private ClassStudentMapper classStudentMapper;

	@Override
	public BaseMapper<Clazz> getMapper() {
		return clazzMapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int createClazz(ClazzCreateDto clazzCreateDto, SimpleUserBo simpleUser) {
		// 生成班级编码
		clazzCreateDto.setClassCode(ZUidUtil.base36Snowid());
		ZBeanUtil.validateBean(clazzCreateDto);
		Clazz clazz = ClazzConvert.INSTANCE.convert(clazzCreateDto);
		return create(clazz);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateClazz(ClazzUpdateDto clazzUpdateDto, SimpleUserBo simpleUser) {
		ZBeanUtil.validateBean(clazzUpdateDto);
		Clazz clazz = ClazzConvert.INSTANCE.convert(clazzUpdateDto);
		return updateById(clazz);
	}

	@Override
	public List<SelectVo> loadSelectVo() {
		ChainWrapper<Clazz> wrapper = ChainWrapper.create(Clazz.class);
		wrapper.where("status", "=", "1");
		wrapper.orderBy("grade_level", true).orderBy("class_name", true);
		List<Clazz> list = clazzMapper.selectByQuery(wrapper);
		return list.stream()
			.map(clazz -> new SelectVo(clazz.getId(), clazz.getClassName() + " (" + clazz.getClassCode() + ")"))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setStatus(StatusListDto statusListDto) {
		ZBeanUtil.validateBean(statusListDto);
		setStatusByIds(statusListDto.getIds(), statusListDto.getStatus());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setStatus(StatusDto status) {
		ZBeanUtil.validateBean(status);
		setStatusById(status.getId(), status.getStatus());
	}

	@Override
	public void updateClassStudentCount(Long classId) {
		if (classId == null) {
			return;
		}

		// 查询该班级的学生数量
		ChainWrapper<ClassStudent> countWrapper = ChainWrapper.create(ClassStudent.class);
		countWrapper.where("class_id", "=", classId);
		countWrapper.where("status", "=", "1");
		Long count = classStudentMapper.selectCountByQuery(countWrapper);

		// 更新班级学生数
		Clazz clazz = new Clazz();
		clazz.setId(classId);
		clazz.setStudentCount(count.intValue());
		clazzMapper.updateById(clazz);
	}

}
