<template>
  <div class="role-user-container">
    <a-form layout="inline" style="margin-bottom: 10px">
        <a-form-item label="姓名">
            <a-input v-model:value="queryForm.perName" placeholder="姓名" />
        </a-form-item>
        <a-form-item>
            <a-button type="primary" @click="query">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </a-form-item>
        <a-form-item style="margin-left: auto">
            <a-button type="primary" @click="showAddModal" v-privilege="'system:role:update'">添加用户</a-button>
            <a-button danger @click="batchRemove" :disabled="selectedRowKeys.length === 0" style="margin-left: 8px" v-privilege="'system:role:update'">批量移除</a-button>
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
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'action'">
             <a style="color: red" @click="removeUser(record.id)" v-privilege="'system:role:update'">移除</a>
        </template>
      </template>
    </a-table>

    <UserAddModal ref="addModal" :roleId="roleId" @success="query" />
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, defineProps } from 'vue';
import { roleApi } from '/@/api/system/role-api';
import UserAddModal from './user-add-modal.vue';
import { message, Modal } from 'ant-design-vue';

const props = defineProps({
    roleId: String
});

const loading = ref(false);
const tableData = ref([]);
const pagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true
});
const queryForm = reactive({
    perName: ''
});
const selectedRowKeys = ref([]);
const addModal = ref();

const columns = [
    { title: '账号', dataIndex: 'account' },
    { title: '姓名', dataIndex: 'perName' },
    { title: '手机号', dataIndex: 'phone' },
    { title: '操作', dataIndex: 'action', width: 100, align: 'center' },
];

watch(() => props.roleId, () => {
    if (props.roleId) {
        query();
    } else {
        tableData.value = [];
    }
});

async function query() {
    if (!props.roleId) return;
    loading.value = true;
    try {
        const params = {
            ...queryForm,
            roleId: props.roleId,
            roleHave: true,
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
    query();
}

function onSelectChange(keys) {
    selectedRowKeys.value = keys;
}

function showAddModal() {
    if (!props.roleId) {
        message.warning('请选择角色');
        return;
    }
    addModal.value.showModal();
}

function removeUser(userId) {
    Modal.confirm({
        title: '提示',
        content: '确定要移除该用户么？',
        onOk: async () => {
            await roleApi.removeUsersFromRole({
                roleId: props.roleId,
                userIds: [userId]
            });
            message.success('移除成功');
            query();
        }
    });
}

function batchRemove() {
    Modal.confirm({
        title: '提示',
        content: '确定要移除选中的用户么？',
        onOk: async () => {
            await roleApi.removeUsersFromRole({
                roleId: props.roleId,
                userIds: selectedRowKeys.value
            });
            message.success('移除成功');
            selectedRowKeys.value = [];
            query();
        }
    });
}

onMounted(() => {
    if (props.roleId) {
        query();
    }
});
</script>
<style scoped>
.role-user-container {
    padding: 10px;
}
</style>
