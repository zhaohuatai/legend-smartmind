import { postRequest } from '/@/lib/axios';

export const clazzApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/clazz/loadDataSet', param);
  },

  // 创建
  create: (param) => {
    return postRequest('/manage/smartmind/clazz/create', param);
  },

  // 创建（别名，兼容add调用）
  add: (param) => {
    return postRequest('/manage/smartmind/clazz/create', param);
  },

  // 根据ID查询
  queryById: (id) => {
    return postRequest(`/manage/smartmind/clazz/loadById/${id}`);
  },

  // 更新
  update: (param) => {
    return postRequest('/manage/smartmind/clazz/update', param);
  },

  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/clazz/delete/${id}`);
  },

  // 修改状态
  setStatus: (param) => {
    return postRequest('/manage/smartmind/clazz/setStatus', param);
  },

  // 查询SelectVo列表
  selectVo: () => {
    return postRequest('/manage/smartmind/clazz/selectVo');
  },

  // 根据年级查询班级列表
  queryByGrade: (gradeLevel) => {
    return postRequest('/manage/smartmind/clazz/loadDataSet', {
      gradeLevel,
      status: '1',
      pageNum: 1,
      pageSize: 1000,
    });
  },
};
