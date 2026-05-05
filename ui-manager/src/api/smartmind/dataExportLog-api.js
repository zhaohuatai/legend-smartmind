import { postRequest } from '/@/lib/axios';

export const dataExportLogApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/dataexportlog/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/dataexportlog/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/dataexportlog/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/dataexportlog/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/dataexportlog/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/dataexportlog/delete/${id}`);
  }
};
