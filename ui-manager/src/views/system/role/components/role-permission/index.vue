<template>
  <div class="role-permission-container">
    <a-form layout="inline" style="margin-bottom: 10px">
        <a-form-item label="名称">
            <a-input v-model:value="queryForm.name" placeholder="权限名称" />
        </a-form-item>
        <a-form-item>
            <a-button type="primary" @click="query">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </a-form-item>
        <a-form-item style="margin-left: auto">
            <a-button type="primary" @click="showAddModal" v-privilege="'system:role:update'">添加权限</a-button>
            <a-button danger @click="batchRemove" :disabled="selectedRowKeys.length === 0" style="margin-left: 8px" v-privilege="'system:role:update'">批量移除</a-button>
            <a-button @click="setHome" :disabled="isSetHomeDisabled" style="margin-left: 8px" v-privilege="'system:role:update'">设置为首页</a-button>
            <a-tag v-if="currentHomeName" color="error" style="margin-left: 10px; height: 32px; line-height: 30px; font-size: 14px; padding: 0 12px; display: inline-flex; align-items: center;">
                <template #icon>
                    <HomeOutlined style="font-size: 15px; margin-right: 5px;" />
                </template>
                当前角色首页：{{ currentHomeName }}
            </a-tag>
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
        <template v-if="column.dataIndex === 'type'">
             <a-tag color="blue" v-if="record.type === 'M'">菜单</a-tag>
             <a-tag color="orange" v-else-if="record.type === 'B'">按钮</a-tag>
             <span v-else>{{ record.type }}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
             <a style="color: red" @click="removePerm(record.id)" v-privilege="'system:role:update'">移除</a>
        </template>
      </template>
    </a-table>

    <PermissionAddModal ref="addModal" :roleId="roleId" @success="query" />
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, defineProps, computed } from 'vue';
import { roleApi } from '/@/api/system/role-api';
import PermissionAddModal from './permission-add-modal.vue';
import { message, Modal } from 'ant-design-vue';
import { HomeOutlined } from '@ant-design/icons-vue';

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
    name: ''
});
const selectedRowKeys = ref([]);
const addModal = ref();
const currentHomeName = ref('');

const isSetHomeDisabled = computed(() => {
    if (selectedRowKeys.value.length !== 1) {
        return true;
    }
    const selectedId = selectedRowKeys.value[0];
    const record = tableData.value.find(item => item.id === selectedId);
    return !record || record.type !== 'M';
});

const columns = [
    { title: '权限名称', dataIndex: 'name' },
    { title: '权限编码', dataIndex: 'code' },
    { title: '类型', dataIndex: 'type', width: 80, align: 'center' },
    { title: '操作', dataIndex: 'action', width: 100, align: 'center' },
];

watch(() => props.roleId, () => {
    if (props.roleId) {
        query();
        loadHomePage();
    } else {
        tableData.value = [];
        currentHomeName.value = '';
    }
});

async function loadHomePage() {
    if (!props.roleId) return;
    try {
        const res = await roleApi.loadRoleIndexPerm(props.roleId);
        if (res.data) {
            currentHomeName.value = res.data.name;
        } else {
            currentHomeName.value = '';
        }
    } catch (e) {
        console.error(e);
    }
}

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

function removePerm(permId) {
    Modal.confirm({
        title: '提示',
        content: '确定要移除该权限么？',
        onOk: async () => {
            await roleApi.removePermsFromRole({
                roleId: props.roleId,
                permIds: [permId]
            });
            message.success('移除成功');
            query();
        }
    });
}

function batchRemove() {
    Modal.confirm({
        title: '提示',
        content: '确定要移除选中的权限么？',
        onOk: async () => {
            await roleApi.removePermsFromRole({
                roleId: props.roleId,
                permIds: selectedRowKeys.value
            });
            message.success('移除成功');
            selectedRowKeys.value = [];
            query();
        }
    });
}

function setHome() {
    if (selectedRowKeys.value.length !== 1) {
        message.warning('请选择一个权限作为首页');
        return;
    }
    const selectedId = selectedRowKeys.value[0];
    const record = tableData.value.find(item => item.id === selectedId);
    if (!record) return;

    if (record.type !== 'M') {
        message.warning('只有菜单权限可以设置为首页');
        return;
    }

    Modal.confirm({
        title: '提示',
        content: `确定要将 [${record.name}] 设置为首页么？`,
        onOk: async () => {
            await roleApi.setRoleIndexPerm(props.roleId, selectedId);
            message.success('设置成功');
            query();
            loadHomePage();
        }
    });
}

onMounted(() => {
    if (props.roleId) {
        query();
        loadHomePage();
    }
});
</script>
<style scoped>
.role-permission-container {
    padding: 10px;
}
</style>
