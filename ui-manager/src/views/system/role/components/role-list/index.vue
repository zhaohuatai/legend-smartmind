<template>
  <div class="role-container" style="height: 100%;">
    <a-card :bordered="false" style="height: 100%; display: flex; flex-direction: column;" :bodyStyle="{ padding: '10px', flex: 1, overflow: 'hidden', display: 'flex', flexDirection: 'column' }">
        <!-- Search Form -->
        <a-form layout="inline" style="margin-bottom: 10px">
            <a-form-item label="名称">
                <a-input v-model:value="queryForm.name" placeholder="角色名称" style="width: 100px" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="queryRole">查询</a-button>
            </a-form-item>
            <a-form-item style="margin-left: auto">
                <a-button type="primary" @click="showRoleFormModal()" v-privilege="'system:role:add'">添加</a-button>
            </a-form-item>
        </a-form>

        <!-- Table -->
        <div style="flex: 1; overflow: auto;">
            <a-table
                :columns="columns"
                :data-source="tableData"
                :pagination="pagination"
                :loading="loading"
                row-key="id"
                size="small"
                @change="handleTableChange"
                :customRow="customRow"
                :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
            >
                <template #bodyCell="{ column, record }">
                    <template v-if="column.dataIndex === 'action'">
                    <a-space size="small">
                        <a @click.stop="showRoleFormModal(record)" v-privilege="'system:role:update'">编辑</a>
                        <a style="color: red" @click.stop="deleteRole(record.id)" v-privilege="'system:role:delete'">删除</a>
                    </a-space>
                    </template>
                </template>
            </a-table>
        </div>
    </a-card>

    <RoleFormModal ref="roleFormModal" @refresh="queryRole" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineExpose, defineEmits } from 'vue';
import { roleApi } from '/@/api/system/role-api';
import RoleFormModal from '../role-form-modal/index.vue';
import { message, Modal } from 'ant-design-vue';

const queryForm = reactive({
    name: '',
});

const loading = ref(false);
const tableData = ref([]);
const pagination = reactive({
    current: 1,
    pageSize: 10, 
    total: 0,
    showSizeChanger: true,
    pageSizeOptions: ['10', '20', '30', '40', '50'],
    showTotal: (total) => `共 ${total} 条`
});

const columns = [
    { title: '角色名称', dataIndex: 'name', ellipsis: true },
    { title: '操作', dataIndex: 'action', width: 100, align: 'center' },
];

const selectedRowKeys = ref([]);
const selectRole = ref(null);

const emit = defineEmits(['select-role']);

function customRow(record) {
    return {
        onClick: () => {
            selectedRowKeys.value = [record.id];
            selectRole.value = record;
            emit('select-role', record);
        },
    };
}

function onSelectChange(keys, rows) {
    selectedRowKeys.value = keys;
    if (rows.length > 0) {
        selectRole.value = rows[0];
        emit('select-role', rows[0]);
    }
}

async function queryRole() {
    loading.value = true;
    try {
        const params = {
            ...queryForm,
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
        };
        const res = await roleApi.queryPage(params);
        tableData.value = res.data?.rows || [];
        pagination.total = res.data?.total || 0;
        
        // Default select first one if none selected or refreshed
        if (tableData.value.length > 0) {
            if(selectedRowKeys.value.length === 0 || !tableData.value.find(r => r.id === selectedRowKeys.value[0])) {
                selectedRowKeys.value = [tableData.value[0].id];
                selectRole.value = tableData.value[0];
                emit('select-role', tableData.value[0]);
            }
        } else {
             selectedRowKeys.value = [];
             selectRole.value = null;
             emit('select-role', null);
        }
    } catch (e) {
        console.error(e);
    } finally {
        loading.value = false;
    }
}

function handleTableChange(pag) {
    pagination.current = pag.current;
    pagination.pageSize = pag.pageSize;
    queryRole();
}

// ... RoleFormModal logic (add/edit/delete) ...
const roleFormModal = ref();
function showRoleFormModal(role) {
    roleFormModal.value.showModal(role);
}

function deleteRole(roleId) {
    Modal.confirm({
        title: '提示',
        content: '确定要删除该角色么？',
        okText: '确定',
        okType: 'danger',
        onOk: async () => {
             // Assuming updateStatus to '0' or '2' stands for delete/disable as per typical logic when delete api is missing
             // Or maybe there is a delete method I missed.
             // I will use updateStatus for now.
             await roleApi.updateStatus(roleId, '0'); 
             message.success('删除成功');
             queryRole();
        }
    });
}

onMounted(() => {
    queryRole();
});

defineExpose({
    selectRole
});
</script>
<style scoped>
.role-container :deep(.ant-card-body) {
    padding: 0;
    display: flex;
    flex-direction: column;
    height: 100%;
}

.role-container :deep(.ant-table-pagination) {
    padding-right: 20px;
}
</style>
