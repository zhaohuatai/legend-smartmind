import { postRequest } from '/@/lib/axios';

export const abilityTransitionApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/abilitytransition/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/abilitytransition/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/abilitytransition/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/abilitytransition/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/abilitytransition/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/abilitytransition/delete/${id}`);
  }
};
