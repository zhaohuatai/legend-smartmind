import { postRequest } from '/@/lib/axios';

export const learningSnapshotApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/learningsnapshot/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/learningsnapshot/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/learningsnapshot/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/learningsnapshot/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/learningsnapshot/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/learningsnapshot/delete/${id}`);
  }
};
