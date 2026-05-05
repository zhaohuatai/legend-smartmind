import { postRequest } from '/@/lib/axios';

export const configGlobalApi = {
  queryPage: (param) => {
    return postRequest('/manage/system/config/global/loadDataSet', param);
  },

  add: (param) => {
    return postRequest('/manage/system/config/global/create', param);
  },

  update: (param) => {
    return postRequest('/manage/system/config/global/update', param);
  },

  delete: (id) => {
    return postRequest(`/manage/system/config/global/delete/${id}`);
  },

  loadById: (id) => {
    return postRequest(`/manage/system/config/global/loadById/${id}`);
  },
};

