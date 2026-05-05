import { postRequest } from '/@/lib/axios';

export const classSessionApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classsession/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classsession/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classsession/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classsession/update', param);
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classsession/delete/${id}`);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classsession/setStatus', { id, status });
  }
};

export const classActivityApi = {
  // 根据sessionId查询活动列表
  queryBySessionId: (param) => {
    return postRequest('/manage/smartmind/classactivity/queryBySessionId', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/classactivity/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/classactivity/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/classactivity/update', param);
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classactivity/delete/${id}`);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivity/setStatus', { id, status });
  }
};
