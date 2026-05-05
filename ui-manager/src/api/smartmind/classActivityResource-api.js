import { postRequest, getRequest } from '/@/lib/axios';

export const classActivityResourceApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classactivityresource/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classactivityresource/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classactivityresource/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classactivityresource/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivityresource/setStatus', { id, status });
  },
  // 批量保存资源
  batchSave: (param) => {
    return postRequest('/manage/smartmind/classactivityresource/batchSave', param);
  },
  // 根据活动ID查询资源
  loadByActivityId: (activityId) => {
    return getRequest(`/manage/smartmind/classactivityresource/loadByActivityId/${activityId}`);
  }
};
