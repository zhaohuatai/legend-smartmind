<template>
  <div class="dept-container">
    <!-- Search Form -->
    <a-card class="search-card" :bordered="false">
      <a-form layout="inline">
        <a-form-item label="部门名称">
          <a-input v-model:value="queryForm.name" placeholder="请输入部门名称" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="query">查询</a-button>
          <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </a-form-item>
        <a-form-item style="margin-left: auto">
          <a-button type="primary" @click="showAddModal(null)" v-privilege="'system:dept:add'">添加部门</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- Table -->
    <a-card class="table-card" :bordered="false" :bodyStyle="{ padding: 0 }">
      <a-table
        :columns="columns"
        :data-source="tableData"
        :loading="loading"
        row-key="id"
        size="small"
        :pagination="false"
        :scroll="{ y: 'calc(100vh - 200px)' }"
        v-model:expandedRowKeys="expandedRowKeys"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'action'">
            <a-space>
              <a @click="showAddModal(record.id)" v-privilege="'system:dept:add'">添加下级</a>
              <a @click="showEditModal(record)" v-privilege="'system:dept:update'">编辑</a>
              <a style="color: red" @click="deleteDept(record.id)" v-privilege="'system:dept:delete'">删除</a>
            </a-space>
          </template>
           <template v-if="column.dataIndex === 'status'">
              <a-tag :color="record.status === 1 ? 'green' : 'red'">
                {{ record.status === 1 ? '启用' : '禁用' }}
              </a-tag>
          </template>
        </template>
      </a-table>
    </a-card>

    <DeptFormModal ref="modalRef" @refresh="query" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { departmentApi } from '/@/api/system/department-api';
import DeptFormModal from './components/dept-form-modal/index.vue';

const loading = ref(false);
const tableData = ref([]);
const expandedRowKeys = ref([]);
const queryForm = reactive({
  name: ''
});
const modalRef = ref();

const columns = [
  { title: '部门名称', dataIndex: 'name', width: 250 },
  { title: '部门编码', dataIndex: 'code', width: 150 },
  { title: '状态', dataIndex: 'status', width: 120, align: 'center' },
  { title: '排序', dataIndex: 'showIndex', width: 100, align: 'center' },
  { title: '负责人', dataIndex: 'manager', width: 120 },
  { title: '联系电话', dataIndex: 'phone', width: 150 },
  { title: '备注', dataIndex: 'remark', ellipsis: true },
  { title: '操作', dataIndex: 'action', width: 200, align: 'center', fixed: 'right' }
];

async function query() {
  loading.value = true;
  try {
    const res = await departmentApi.queryDeptTree();
    let data = [];
    if (res.data) {
        if (Array.isArray(res.data)) {
            data = res.data;
        } else {
            data = [res.data];
        }
    }
    
    if (queryForm.name) {
        data = filterTree(data, queryForm.name);
    }
    
    tableData.value = data;
    
    // 默认展开2层
    if (!queryForm.name) {
        expandedRowKeys.value = [];
        data.forEach(node => {
            expandedRowKeys.value.push(node.id);
            if (node.children) {
                node.children.forEach(child => {
                    expandedRowKeys.value.push(child.id);
                });
            }
        });
    } else {
        // 搜索时展开所有匹配节点
         expandedRowKeys.value = getAllIds(data);
    }

  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}

function getAllIds(tree) {
    let ids = [];
    tree.forEach(node => {
        ids.push(node.id);
        if (node.children) {
            ids = ids.concat(getAllIds(node.children));
        }
    });
    return ids;
}

function filterTree(tree, name) {
    let result = [];
    for (const node of tree) {
        if (node.name.includes(name)) {
             result.push(node);
        } else if (node.children && node.children.length > 0) {
            const children = filterTree(node.children, name);
            if (children.length > 0) {
                const newNode = { ...node, children };
                result.push(newNode);
            }
        }
    }
    return result;
}

function reset() {
  queryForm.name = '';
  query();
}

function showAddModal(parentId) {
  modalRef.value.showModal(null, parentId);
}

function showEditModal(record) {
  modalRef.value.showModal(record);
}

function deleteDept(id) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该部门及其子部门吗？',
    okText: '确定',
    okType: 'danger',
    onOk: async () => {
      try {
        await departmentApi.deleteDepartment(id); 
        message.success('删除成功');
        query();
      } catch (e) {
        console.error(e);
      }
    }
  });
}

onMounted(() => {
  query();
});
</script>

<style scoped>
.dept-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.search-card {
  margin-bottom: 10px;
}

.table-card {
  flex: 1;
  overflow: hidden;
  padding: 10px;
}
</style>
