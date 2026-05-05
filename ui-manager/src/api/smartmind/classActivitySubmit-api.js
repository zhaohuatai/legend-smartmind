import { postRequest } from '/@/lib/axios';

export const classActivitySubmitApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classactivitysubmit/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classactivitysubmit/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classactivitysubmit/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classactivitysubmit/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivitysubmit/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classactivitysubmit/delete/${id}`);
  },
  // 获取学生提交列表
  getStudentSubmissions: (activityId) => {
    return postRequest('/manage/smartmind/classactivitysubmit/getStudentSubmissions', { activityId });
  },
  // 获取活动统计
  getActivityStats: (activityId) => {
    return postRequest('/manage/smartmind/classactivitysubmit/getActivityStats', { activityId });
  },
  // 获取学生活动详情
  loadStudentActivityDetail: (activityId, studentId) => {
    return postRequest('/manage/smartmind/classactivitysubmit/loadStudentActivityDetail', { activityId, studentId });
  },
};
