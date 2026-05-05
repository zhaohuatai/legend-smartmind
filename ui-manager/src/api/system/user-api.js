import { postRequest, getRequest } from '/@/lib/axios';

export const userApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/system/user/loadUserDataSet', param);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/system/user/createUser', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/system/user/updateUser', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
      return postRequest('/manage/system/user/setUserStatus', { id, status });
  },
  // 重置密码
  resetPassword: (userId, password) => {
    return postRequest('/manage/system/user/restPassword', { userId, password });
  },
  // 分配角色
  setUserRoles: (userId, roleIds) => {
    return postRequest('/manage/system/user/setUserRoles', { userId, roleIds });
  },
  // 详情
  getDetail: (id) => {
    return postRequest(`/manage/system/user/loadUser/${id}`);
  }
};
