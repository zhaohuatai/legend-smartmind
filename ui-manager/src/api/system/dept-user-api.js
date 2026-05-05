import { postRequest } from '/@/lib/axios';

export const deptUserApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/system/deptuser/loadUserDataSet', param);
  }
};
