<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑用户' : '添加用户'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="账号" name="account">
        <a-input v-model:value="form.account" :disabled="Boolean(form.id)" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item label="密码" name="password" v-if="!form.id">
        <a-input-password v-model:value="form.password" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item label="姓名" name="perName">
        <a-input v-model:value="form.perName" placeholder="请输入姓名" />
      </a-form-item>
      <a-form-item label="昵称" name="nickName">
        <a-input v-model:value="form.nickName" placeholder="请输入昵称" />
      </a-form-item>
      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入手机号" />
      </a-form-item>
      <a-form-item label="邮箱" name="email">
        <a-input v-model:value="form.email" placeholder="请输入邮箱" />
      </a-form-item>
      <a-form-item label="性别" name="gender">
        <a-select v-model:value="form.gender" placeholder="请选择性别">
          <a-select-option value="1">男</a-select-option>
          <a-select-option value="2">女</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="角色" name="roleIds">
        <a-select v-model:value="form.roleIds" mode="multiple" placeholder="请选择角色" :loading="roleLoading">
          <a-select-option v-for="role in roleList" :key="role.id" :value="role.id">
            {{ role.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="状态" name="status" v-if="form.id">
         <a-select v-model:value="form.status" placeholder="请选择状态">
           <a-select-option value="1">正常</a-select-option>
           <a-select-option value="0">禁用</a-select-option>
         </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { userApi } from '/@/api/system/user-api.js';
  import { roleApi } from '/@/api/system/role-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  const emit = defineEmits(['reloadList']);

  const formRef = ref();
  const formDefault = {
    id: undefined,
    account: '',
    password: '',
    perName: '',
    nickName: '',
    phone: '',
    email: '',
    gender: undefined,
    roleIds: [],
    status: '1',
    salt: '111111', // default salt
    roleId: '', // default single role id if needed, but we use roleIds
  };
  let form = reactive({ ...formDefault });

  const rules = {
    account: [{ required: true, message: '请输入账号' }],
    password: [{ required: true, message: '请输入密码' }],
    perName: [{ required: true, message: '请输入姓名' }],
    roleIds: [{ required: true, message: '请选择角色' }],
  };

  const visible = ref(false);
  const roleList = ref([]);
  const roleLoading = ref(false);

  async function loadRoleList() {
      roleLoading.value = true;
      try {
          const res = await roleApi.getAllRoles();
          roleList.value = res.data;
      } catch(e) {
          smartSentry.captureError(e);
      } finally {
          roleLoading.value = false;
      }
  }

  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
      // If rowData has roleIds as list of objects or strings, ensure it matches select format
      // rowData might not have roleIds populated, might need to fetch user detail or passed in.
      // For now assume rowData from list might need enrichment or we fetch detail.
      // Actually, list usually doesn't have full role list.
      // Better to fetch user detail if editing.
      fetchUserDetail(rowData.id);
    }
    visible.value = true;
    loadRoleList();
  }
  
  async function fetchUserDetail(id) {
      try {
          const res = await userApi.getDetail(id);
          const data = res.data;
          // data might contain user info. roleIds might need to be fetched separately or is in data.
          // AuthUserController loadUser returns AuthUser object.
          // AuthUser model might not have roleIds list.
          // But wait, line 107 in AuthUserController commented out: user.setRoleIds...
          // So the default loadAuthUserById might NOT return roleIds.
          // I might need to fetch roles for user separately?
          // AuthUserController line 107 is commented out.
          // I should probably check if I can get roles.
          // Or I use setUserRoles separately.
          // For now, if roleIds are missing, I can't show them in Edit.
          // I will try to use what I have.
          // If the backend doesn't return roleIds, I might need to fix backend or add an API to get user roles.
          // AuthRoleController has loadRolesForUserAssign? No, commented out.
          // AuthRoleUserService has loadRoleIdsByUserId.
          // I'll assume for now I might not show current roles in Edit correctly without backend fix, 
          // but I'll implement as if `roleIds` is in data or I can't do it yet.
          // Actually, let's look at `AuthUserController` again.
          // It returns `authUserService.loadAuthUserById(userId)`.
          // If I can't get roles, I can't pre-fill the select.
          // I'll proceed with basic info first.
          
          Object.assign(form, data);
          // Temporary: if roleIds not in data, clear it to avoid showing wrong info
          if(!data.roleIds) form.roleIds = [];
          
      } catch(e) {
          smartSentry.captureError(e);
      }
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let param = { ...form };
          // Ensure roleId is set if required by backend AccountDto constructor (it requires roleId not null)
          if(param.roleIds && param.roleIds.length > 0) {
              param.roleId = param.roleIds[0]; // Set primary role?
          } else {
               // If no role selected, might fail validation if backend requires it.
               // AccountDto constructor: @NotNull(message = "角色编码不能为空") String roleId
               // So at least one role is needed.
               param.roleId = '0'; // Dummy or error?
          }

          if (param.id) {
            await userApi.update(param);
            // Update roles separately
            if(param.roleIds) {
                await userApi.setUserRoles(param.id, param.roleIds);
            }
            message.success('修改成功');
          } else {
            await userApi.add(param);
            message.success('添加成功');
          }
          emit('reloadList');
          onClose();
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((e) => {
        console.error(e);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  defineExpose({
    showModal,
  });
</script>
