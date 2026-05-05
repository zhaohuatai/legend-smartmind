import { postRequest } from '/@/lib/axios';

export const knowledgeHeatmapApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/knowledgeheatmap/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/knowledgeheatmap/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/knowledgeheatmap/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/knowledgeheatmap/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/knowledgeheatmap/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/knowledgeheatmap/delete/${id}`);
  }
};
