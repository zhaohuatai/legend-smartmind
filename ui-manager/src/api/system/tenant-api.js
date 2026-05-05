import { postRequest } from '/@/lib/axios';

export const tenantApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/system/tenant/loadTenantDataSet', param);
  },
  // 搜索
  search: (name) => {
    return postRequest('/manage/system/tenant/searchTenant', { name });
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/system/tenant/createTenant', param);
  },
  // 更新
  update: (param) => {
    return postRequest('/manage/system/tenant/updateTenant', param);
  },
  // 获取详情
  getDetail: (id) => {
    return postRequest(`/manage/system/tenant/loadTenant/${id}`);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/system/tenant/setTenantStatus', { id, status });
  }
};
