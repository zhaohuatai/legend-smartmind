import { postRequest } from '/@/lib/axios';

export const permissionApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/system/permission/loadPermDataSet', param);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/system/permission/createPerm', param);
  },
  // 更新
  update: (param) => {
    return postRequest('/manage/system/permission/updatePerm', param);
  },
  // 获取详情
  getDetail: (id) => {
    return postRequest(`/manage/system/permission/loadPerm/${id}`);
  },
  // 菜单分配权限查询
  queryPermsForMenuAssign: (param) => {
    return postRequest('/manage/system/permission/loadPermsForMenuAssign', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/manage/system/permission/setPermStatus', { id, status });
  }
};
