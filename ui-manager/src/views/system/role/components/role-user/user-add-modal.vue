<template>
  <a-modal
    v-model:open="visible"
    title="添加用户"
    width="800px"
    @ok="onOk"
    @cancel="onClose"
  >
    <a-form layout="inline" style="margin-bottom: 10px">
        <a-form-item label="姓名">
            <a-input v-model:value="queryForm.perName" placeholder="姓名" />
        </a-form-item>
        <a-form-item label="账号">
            <a-input v-model:value="queryForm.account" placeholder="账号" />
        </a-form-item>
        <a-form-item>
            <a-button type="primary" @click="query">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data-source="tableData"
      :pagination="pagination"
      :loading="loading"
      row-key="id"
      size="small"
      @change="handleTableChange"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    >
    </a-table>
  </a-modal>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, defineExpose } from 'vue';
import { roleApi } from '/@/api/system/role-api';
import { message } from 'ant-design-vue';

const props = defineProps({
    roleId: String
});
const emit = defineEmits(['success']);

const visible = ref(false);
const loading = ref(false);
const tableData = ref([]);
const pagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true
});
const queryForm = reactive({
    perName: '',
    account: ''
});
const selectedRowKeys = ref([]);

const columns = [
    { title: '姓名', dataIndex: 'perName' },
    { title: '账号', dataIndex: 'account' },
    { title: '手机号', dataIndex: 'phone' },
];

function showModal() {
    visible.value = true;
    selectedRowKeys.value = [];
    query();
}

function onClose() {
    visible.value = false;
    selectedRowKeys.value = [];
}

async function query() {
    loading.value = true;
    try {
        const params = {
            ...queryForm,
            roleId: props.roleId,
            roleHave: false,
            pageNum: pagination.current,
            pageSize: pagination.pageSize
        };
        const res = await roleApi.queryRoleUser(params);
        tableData.value = res.data?.rows || [];
        pagination.total = res.data?.total || 0;
    } finally {
        loading.value = false;
    }
}

function handleTableChange(pag) {
    pagination.current = pag.current;
    pagination.pageSize = pag.pageSize;
    query();
}

function reset() {
    queryForm.perName = '';
    queryForm.account = '';
    query();
}

function onSelectChange(keys) {
    selectedRowKeys.value = keys;
}

async function onOk() {
    if (selectedRowKeys.value.length === 0) {
        message.warning('请选择用户');
        return;
    }
    try {
        await roleApi.addUsersToRole({
            roleId: props.roleId,
            userIds: selectedRowKeys.value
        });
        message.success('添加成功');
        emit('success');
        onClose();
    } catch (e) {
        console.error(e);
    }
}

defineExpose({
    showModal
});
</script>
