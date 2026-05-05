import { postRequest, getRequest } from '/@/lib/axios';

export const teachingPlanApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/teachingplan/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/teachingplan/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/teachingplan/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/teachingplan/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/teachingplan/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/teachingplan/delete/${id}`);
  },
  // 根据课程和单元查询教案列表
  loadByCourseAndUnit: (courseId, unitCode) => {
    return getRequest('/manage/smartmind/teachingplan/loadByCourseAndUnit', { courseId, unitCode });
  }
};
