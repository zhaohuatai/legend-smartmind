<template>
  <div class="height100">
    <a-row :gutter="16" class="height100">
      <a-col :span="6">
        <DepartmentTree ref="departmentTreeRef" @select="onDeptSelect" />
      </a-col>
      <a-col :span="18" class="height100">
        <div class="employee-box height100">
            <a-form class="smart-query-form">
            <a-row class="smart-query-form-row">
                <a-form-item label="账号" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.account" placeholder="请输入账号" />
                </a-form-item>
                <a-form-item label="姓名" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.perName" placeholder="请输入姓名" />
                </a-form-item>
                <a-form-item label="手机号" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.phone" placeholder="请输入手机号" />
                </a-form-item>
        
                <a-form-item class="smart-query-form-item smart-margin-left10">
                <a-button-group>
                    <a-button type="primary" @click="onSearch">
                    <template #icon>
                        <SearchOutlined />
                    </template>
                    查询
                    </a-button>
                    <a-button @click="resetQuery">
                    <template #icon>
                        <ReloadOutlined />
                    </template>
                    重置
                    </a-button>
                </a-button-group>
                <a-button @click="toAdd" type="primary" class="smart-margin-left20">
                    <template #icon>
                    <PlusOutlined />
                    </template>
                    新建用户
                </a-button>
                </a-form-item>
            </a-row>
            </a-form>
        
            <a-card size="small" :bordered="false" :hoverable="true" style="flex: 1; overflow: auto; display: flex; flex-direction: column;">
            <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="id" :pagination="false" :scroll="{ y: 500 }">
                <template #bodyCell="{ record, column, index }">
                <template v-if="column.dataIndex === 'seq'">
                    {{ index + 1 }}
                </template>
                <template v-else-if="column.dataIndex === 'status'">
                    <a-tag :color="record.status == '1' ? 'green' : 'red'">
                        {{ record.status == '1' ? '正常' : '禁用' }}
                    </a-tag>
                </template>
                <template v-else-if="column.dataIndex === 'gender'">
                    {{ record.gender == '1' ? '男' : (record.gender == '2' ? '女' : '未知') }}
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                    <a-button @click="toEdit(record)" type="link">编辑</a-button>
                    <a-button @click="onResetPwd(record)" type="link">重置密码</a-button>
                    </div>
                </template>
                </template>
            </a-table>
        
            <div class="smart-query-table-page">
                <a-pagination
                showSizeChanger
                showQuickJumper
                show-less-items
                :pageSizeOptions="PAGE_SIZE_OPTIONS"
                :defaultPageSize="queryForm.pageSize"
                v-model:current="queryForm.pageNum"
                v-model:pageSize="queryForm.pageSize"
                :total="total"
                @change="queryData"
                :show-total="(total) => `共${total}条`"
                />
            </div>
            </a-card>
        </div>
      </a-col>
    </a-row>
    <UserFormModal ref="formModalRef" @reloadList="queryData" />
    <a-modal v-model:open="pwdModalVisible" title="重置密码" @ok="submitResetPwd">
        <a-form :label-col="{ span: 6 }">
            <a-form-item label="新密码">
                <a-input-password v-model:value="newPassword" placeholder="请输入新密码" />
            </a-form-item>
        </a-form>
    </a-modal>
  </div>
</template>

<script setup>
  import { message, Modal } from 'ant-design-vue';
  import { onMounted, reactive, ref, computed } from 'vue';
  import { deptUserApi } from '/@/api/system/dept-user-api.js';
  import { userApi } from '/@/api/system/user-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import UserFormModal from '../user/user-form-modal.vue';
  import DepartmentTree from '../employee/components/department-tree/index.vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '账号',
      dataIndex: 'account',
      ellipsis: true,
    },
    {
      title: '姓名',
      dataIndex: 'perName',
      ellipsis: true,
    },
    {
      title: '昵称',
      dataIndex: 'nickName',
      ellipsis: true,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 120,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      ellipsis: true,
    },
    {
      title: '性别',
      dataIndex: 'gender',
      width: 60,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 150,
    },
  ]);

  const queryFormState = {
    account: '',
    perName: '',
    phone: '',
    pageNum: 1,
    pageSize: 10,
    deptTreeCode: '',
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const departmentTreeRef = ref();

  function onDeptSelect(idList) {
      if (idList && idList.length > 0 && departmentTreeRef.value && departmentTreeRef.value.departmentList) {
          const deptId = idList[0];
          const dept = departmentTreeRef.value.departmentList.find(d => d.id === deptId);
          if (dept) {
              queryForm.deptTreeCode = dept.deptTreeCode;
          } else {
              queryForm.deptTreeCode = '';
          }
      } else {
          queryForm.deptTreeCode = '';
      }
      onSearch();
  }

  function resetQuery() {
    let pageSize = queryForm.pageSize;
    let deptTreeCode = queryForm.deptTreeCode; // Keep selected dept
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryForm.deptTreeCode = deptTreeCode;
    queryData();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let res = await deptUserApi.queryPage(queryForm);
      tableData.value = res.data?.rows || res.data?.list || [];
      total.value = res.data?.total || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const formModalRef = ref();
  function toAdd() {
    formModalRef.value.showModal();
  }
  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  // Reset Password
  const pwdModalVisible = ref(false);
  const newPassword = ref('');
  const currentUserId = ref('');

  function onResetPwd(record) {
      currentUserId.value = record.id;
      newPassword.value = '';
      pwdModalVisible.value = true;
  }

  async function submitResetPwd() {
      if(!newPassword.value) {
          message.error('请输入新密码');
          return;
      }
      try {
          SmartLoading.show();
          await userApi.resetPassword(currentUserId.value, newPassword.value);
          message.success('重置密码成功');
          pwdModalVisible.value = false;
      } catch(e) {
          smartSentry.captureError(e);
      } finally {
          SmartLoading.hide();
      }
  }

  onMounted(() => {
    queryData();
  });
</script>

<style scoped lang="less">
  .height100 {
    height: 100%;
  }
  .employee-box {
    display: flex;
    flex-direction: column;
    height: 100%;
  }
</style>
