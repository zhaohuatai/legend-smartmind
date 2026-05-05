<!--
  * 权限选择弹窗
-->
<template>
  <a-modal
    :open="visible"
    title="选择权限"
    :width="900"
    destroyOnClose
    @cancel="onClose"
    @ok="onConfirm"
  >
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="名称/编码/路径" />
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="query">查询</a-button>
          <a-button class="smart-margin-left10" @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-table
      rowKey="id"
      :columns="columns"
      :dataSource="tableData"
      :pagination="pagination"
      :loading="tableLoading"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
      size="small"
      bordered
      @change="handleTableChange"
    >
      <template #bodyCell="{ text, column }">
        <template v-if="column.dataIndex === 'type'">
          <a-tag v-if="text === 'X'" color="blue">菜单权限</a-tag>
          <a-tag v-else-if="text === 'B'" color="green">按钮权限</a-tag>
          <span v-else>{{ text }}</span>
        </template>
      </template>
    </a-table>
  </a-modal>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { permissionApi } from '/@/api/system/permission-api';
import { smartSentry } from '/@/lib/smart-sentry';

const emit = defineEmits(['select']);

const visible = ref(false);
const tableLoading = ref(false);
const tableData = ref([]);
const selectedRowKeys = ref([]);
const selectedRows = ref([]);

const queryForm = reactive({
  keywords: '',
});

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
});

const columns = [
  {
    title: '权限名称',
    dataIndex: 'name',
    width: 150,
  },
  {
    title: '权限编码',
    dataIndex: 'code',
    width: 200,
  },
  {
    title: '路径',
    dataIndex: 'path',
    width: 200,
    ellipsis: true,
  },
  {
    title: '组件',
    dataIndex: 'component',
    width: 200,
    ellipsis: true,
  },
];

function show() {
  visible.value = true;
  selectedRowKeys.value = [];
  selectedRows.value = [];
  query();
}

function onClose() {
  visible.value = false;
}

function onSelectChange(keys, rows) {
  selectedRowKeys.value = keys;
  selectedRows.value = rows;
}

async function query() {
  try {
    tableLoading.value = true;
    const params = {
      ...queryForm,
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    };
    const res = await permissionApi.queryPermsForMenuAssign(params);
    tableData.value = res.data.rows;
    pagination.total = res.data.total;
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    tableLoading.value = false;
  }
}

function resetQuery() {
  queryForm.keywords = '';
  pagination.current = 1;
  query();
}

function handleTableChange(pag) {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  query();
}

function onConfirm() {
  if (selectedRows.value.length > 0) {
    emit('select', selectedRows.value[0]);
    onClose();
  }
}

defineExpose({
  show,
});
</script>
