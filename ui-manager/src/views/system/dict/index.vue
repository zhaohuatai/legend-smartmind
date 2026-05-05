<template>
  <a-row :gutter="10" class="height100">
    <!-- 左侧：字典类型 -->
    <a-col :span="10">
      <a-card size="small" :bordered="false" class="height100" title="字典类型">
        <template #extra>
             <a-button type="primary" size="small" @click="addType">新建</a-button>
        </template>
        
        <!-- 字典类型查询 -->
        <a-form class="smart-query-form" style="margin-bottom: 10px;">
          <div style="display: flex; gap: 8px;">
            <a-input v-model:value="typeQueryForm.keywords" placeholder="类型名称/编码" style="flex: 1;" />
            <a-button type="primary" @click="queryType">
              <template #icon><SearchOutlined /></template>
              查询
            </a-button>
            <a-button @click="resetQueryType">
              <template #icon><ReloadOutlined /></template>
              重置
            </a-button>
          </div>
        </a-form>

        <a-table
            size="small"
            :loading="typeLoading"
            :dataSource="typeList"
            :columns="typeColumns"
            rowKey="id"
            :pagination="typePagination"
            :row-selection="{ selectedRowKeys: selectedTypeKeys, onChange: onSelectTypeChange, type: 'radio' }"
            :customRow="customRow"
            @change="onTypeTableChange"
            :scroll="{ y: 500 }"
        >
            <template #bodyCell="{ record, column }">
                 <template v-if="column.dataIndex === 'action'">
                    <a-button type="link" size="small" @click.stop="editType(record)">编辑</a-button>
                 </template>
            </template>
        </a-table>
      </a-card>
    </a-col>

    <!-- 右侧：字典数据 -->
    <a-col :span="14">
      <a-card size="small" :bordered="false" class="height100" :title="currentType ? `[${currentType.dictName}] 数据列表` : '字典数据'">
         <template #extra>
             <a-button type="primary" size="small" @click="addData" :disabled="!currentType">新建数据</a-button>
        </template>

        <!-- 字典数据查询 -->
        <a-form class="smart-query-form" style="margin-bottom: 10px;">
          <div style="display: flex; gap: 8px;">
            <a-input v-model:value="dataQueryForm.keywords" placeholder="数据标签/键值" style="flex: 1;" />
            <a-button type="primary" @click="queryData">
              <template #icon><SearchOutlined /></template>
              查询
            </a-button>
            <a-button @click="resetQueryData">
              <template #icon><ReloadOutlined /></template>
              重置
            </a-button>
          </div>
        </a-form>

        <a-table
            size="small"
            :loading="dataLoading"
            :dataSource="dataList"
            :columns="dataColumns"
            rowKey="id"
            :pagination="dataPagination"
            @change="onDataTableChange"
            :scroll="{ y: 500 }"
        >
             <template #bodyCell="{ record, column }">
                 <template v-if="column.dataIndex === 'action'">
                    <a-button type="link" size="small" @click="editData(record)">编辑</a-button>
                    <a-popconfirm title="确定要更新状态吗?" @confirm="updateDataStatus(record)">
                         <a-button type="link" danger size="small">{{ record.status == '1' ? '禁用' : '启用' }}</a-button>
                    </a-popconfirm>
                 </template>
                 <template v-else-if="column.dataIndex === 'status'">
                    <a-tag :color="record.status == '1' ? 'green' : 'red'">
                        {{ record.status == '1' ? '正常' : '禁用' }}
                    </a-tag>
                </template>
            </template>
        </a-table>
      </a-card>
    </a-col>

    <DictTypeFormModal ref="typeFormModal" @reload="loadTypes" />
    <DictDataFormModal ref="dataFormModal" @reload="loadData" />
  </a-row>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import { dictApi } from '/@/api/system/dict-api.js';
import DictTypeFormModal from './components/dict-type-form-modal.vue';
import DictDataFormModal from './components/dict-data-form-modal.vue';

// --- Type ---
const typeLoading = ref(false);
const typeList = ref([]);
const selectedTypeKeys = ref([]);
const currentType = ref(null);
const typeColumns = [
    { title: '名称', dataIndex: 'dictName' },
    { title: '编码', dataIndex: 'dictType' },
    { title: '操作', dataIndex: 'action', width: 60 },
];

const typeQueryForm = reactive({
    keywords: ''
});

const typePagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total) => `共 ${total} 条`
});

const typeFormModal = ref();

// --- Data ---
const dataLoading = ref(false);
const dataList = ref([]);
const dataColumns = [
    { title: '标签', dataIndex: 'dictLabel' },
    { title: '键值', dataIndex: 'dictValue' },
    { title: '排序', dataIndex: 'dictSort', width: 60 },
    { title: '状态', dataIndex: 'status', width: 80 },
    { title: '操作', dataIndex: 'action', width: 120 },
];

const dataQueryForm = reactive({
    keywords: ''
});

const dataPagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total) => `共 ${total} 条`
});

const dataFormModal = ref();

onMounted(() => {
    loadTypes();
});

// 加载类型
async function loadTypes() {
    try {
        typeLoading.value = true;
        let res = await dictApi.queryTypePage({
            pageNum: typePagination.current,
            pageSize: typePagination.pageSize,
            keywords: typeQueryForm.keywords
        });
        typeList.value = res.data.rows || [];
        typePagination.total = res.data.total;
        
        // 如果当前没有选中，且有数据，默认选中第一条
        if (typeList.value.length > 0 && !currentType.value) {
            handleTypeSelect(typeList.value[0]);
        }
    } finally {
        typeLoading.value = false;
    }
}

function queryType() {
    typePagination.current = 1;
    loadTypes();
}

function resetQueryType() {
    typeQueryForm.keywords = '';
    queryType();
}

function onTypeTableChange(pagination) {
    typePagination.current = pagination.current;
    typePagination.pageSize = pagination.pageSize;
    loadTypes();
}

function handleTypeSelect(record) {
    currentType.value = record;
    selectedTypeKeys.value = [record.id];
    // 切换类型时，重置数据分页到第一页
    dataPagination.current = 1;
    loadData();
}

function onSelectTypeChange(selectedKeys) {
    const record = typeList.value.find(item => item.id === selectedKeys[0]);
    if (record) {
        handleTypeSelect(record);
    }
}

const customRow = (record) => {
  return {
    onClick: () => {
      handleTypeSelect(record);
    },
  };
};

function addType() {
    typeFormModal.value.show();
}

function editType(record) {
    typeFormModal.value.show(record);
}

// 加载数据
async function loadData() {
    if (!currentType.value) {
        dataList.value = [];
        dataPagination.total = 0;
        return;
    }
    try {
        dataLoading.value = true;
        let res = await dictApi.queryDataPage({ 
            pageNum: dataPagination.current, 
            pageSize: dataPagination.pageSize, 
            dictType: currentType.value.dictType,
            keywords: dataQueryForm.keywords
        });
        dataList.value = res.data.rows || [];
        dataPagination.total = res.data.total;
    } finally {
        dataLoading.value = false;
    }
}

function queryData() {
    dataPagination.current = 1;
    loadData();
}

function resetQueryData() {
    dataQueryForm.keywords = '';
    queryData();
}

function onDataTableChange(pagination) {
    dataPagination.current = pagination.current;
    dataPagination.pageSize = pagination.pageSize;
    loadData();
}

function addData() {
    if (!currentType.value) return;
    dataFormModal.value.show(null, currentType.value.dictType);
}

function editData(record) {
    dataFormModal.value.show(record, currentType.value.dictType);
}

async function updateDataStatus(record) {
    try {
        const newStatus = record.status == '1' ? '0' : '1';
        await dictApi.updateDataStatus(record.id, newStatus);
        message.success('状态更新成功');
        loadData();
    } catch (e) {
        // ignore
    }
}

</script>

<style scoped>
.height100 {
    height: 100%;
}
</style>
