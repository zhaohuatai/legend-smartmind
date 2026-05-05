<template>
  <a-modal
    v-model:open="visible"
    title="添加权限"
    width="800px"
    @ok="onOk"
    @cancel="onClose"
  >
    <a-form layout="inline" style="margin-bottom: 10px">
        <a-form-item label="名称">
            <a-input v-model:value="queryForm.name" placeholder="权限名称" />
        </a-form-item>
        <a-form-item label="编码">
            <a-input v-model:value="queryForm.code" placeholder="权限编码" />
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
import { ref, reactive, watch, defineProps, defineEmits, defineExpose } from 'vue';
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
    name: '',
    code: ''
});
const selectedRowKeys = ref([]);

const columns = [
    { title: '权限名称', dataIndex: 'name' },
    { title: '权限编码', dataIndex: 'code' },
    { title: '类型', dataIndex: 'type' }, 
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
        const res = await roleApi.queryRolePerm(params);
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
    queryForm.name = '';
    queryForm.code = '';
    query();
}

function onSelectChange(keys) {
    selectedRowKeys.value = keys;
}

async function onOk() {
    if (selectedRowKeys.value.length === 0) {
        message.warning('请选择权限');
        return;
    }
    try {
        await roleApi.addPermsToRole({
            roleId: props.roleId,
            permIds: selectedRowKeys.value
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
