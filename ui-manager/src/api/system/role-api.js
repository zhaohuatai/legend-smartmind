import { postRequest, getRequest } from '/@/lib/axios';

export const roleApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/manage/system/role/loadRoleDataSet', param);
  },
  // 列表查询
  queryList: (param) => {
    return postRequest('/manage/system/role/loadRoleList', param);
  },
  // 添加
  add: (param) => {
    return postRequest('/manage/system/role/createRole', param);
  },
  addRole: (param) => {
    return postRequest('/manage/system/role/createRole', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/manage/system/role/updateRole', param);
  },
  updateRole: (param) => {
    return postRequest('/manage/system/role/updateRole', param);
  },
  // 更新状态
  updateStatus: (id, status) => {
      return postRequest('/manage/system/role/setRoleStataus', { id, status });
  },
  deleteRole: (id) => {
      return postRequest('/manage/system/role/setRoleStataus', { id, status: '0' });
  },
  // 详情
  getDetail: (id) => {
    return postRequest(`/manage/system/role/loadRole?id=${id}`);
  },

  // 分配权限
  setRolePerms: (roleId, permIds) => {
    return postRequest('/manage/system/role/setRolePerms', { roleId, permIds });
  },
  
  // 角色列表（不分页，用于下拉）
  getAllRoles: () => {
      return postRequest('/manage/system/role/loadRoleList', {});
  },

  // Query users for role assignment (both in role and not in role)
  queryRoleUser: (param) => {
      return postRequest('/manage/system/role/loadUsersForRoleAssign', param);
  },
  // Add users to role
  addUsersToRole: (param) => {
      return postRequest('/manage/system/role/addUsersToRole', param);
  },
  // Remove users from role
  removeUsersFromRole: (param) => {
      return postRequest('/manage/system/role/removeUsersFromRole', param);
  },
  
  // Query permissions for role assignment (both in role and not in role)
  queryRolePerm: (param) => {
      return postRequest('/manage/system/role/loadPermsForRoleAssign', param);
  },
  // Add permissions to role
  addPermsToRole: (param) => {
      return postRequest('/manage/system/role/addPermsToRole', param);
  },
  // Remove permissions from role
  removePermsFromRole: (param) => {
      return postRequest('/manage/system/role/removePermsFromRole', param);
  },
  // Set role index permission (home page)
  setRoleIndexPerm: (roleId, permId) => {
      return postRequest('/manage/system/role/setRoleIndexPerm', { roleId, permId });
  },
  
  // Load role index permission (home page)
  loadRoleIndexPerm: (roleId) => {
      return postRequest(`/manage/system/role/loadRoleIndexPerm?roleId=${roleId}`);
  }
};
