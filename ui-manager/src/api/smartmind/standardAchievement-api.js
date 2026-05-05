import { postRequest } from '/@/lib/axios';

export const standardAchievementApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/standardachievement/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/standardachievement/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/standardachievement/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/standardachievement/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/standardachievement/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/standardachievement/delete/${id}`);
  }
};
