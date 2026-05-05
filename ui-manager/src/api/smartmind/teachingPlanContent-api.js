import { postRequest } from '/@/lib/axios';

export const teachingPlanContentApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/smartmind/teachingplancontent/loadDataSet', param);
  },
  // 查询详情
  getDetail: (id) => {
    return postRequest(`/manage/smartmind/teachingplancontent/loadById/${id}`);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/smartmind/teachingplancontent/create', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/smartmind/teachingplancontent/update', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/smartmind/teachingplancontent/setStatus', { id, status });
  },
  // 删除
  delete: (id) => {
    return postRequest(`/manage/smartmind/teachingplancontent/delete/${id}`);
  }
};

// 导入参考表API用于 SelectVO 查询
import { smdTeachingPlanApi } from '/@/api/smartmind/smdTeachingPlan-api.js';
