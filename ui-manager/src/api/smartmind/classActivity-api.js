import { postRequest, getRequest } from '/@/lib/axios';

export const classActivityApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/classactivity/loadDataSet', param);
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
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/classactivity/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/classactivity/delete/${id}`);
  },
  // 根据课堂ID查询活动列表
  queryBySessionId: (sessionId) => {
    return getRequest(`/manage/smartmind/classactivity/queryBySessionId/${sessionId}`);
  },
  // 根据ID查询创建DTO
  loadCreateDtoById: (id) => {
    return getRequest(`/manage/smartmind/classactivity/loadCreateDtoById/${id}`);
  }
};
