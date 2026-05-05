import { postRequest } from '/@/lib/axios';

export const configTenantApi = {
  queryPage: (param) => {
    return postRequest('/manage/system/config/tenant/loadDataSet', param);
  },

  add: (param) => {
    return postRequest('/manage/system/config/tenant/create', param);
  },

  update: (param) => {
    return postRequest('/manage/system/config/tenant/update', param);
  },

  delete: (id) => {
    return postRequest(`/manage/system/config/tenant/delete/${id}`);
  },

  loadById: (id) => {
    return postRequest(`/manage/system/config/tenant/loadById/${id}`);
  },
};
