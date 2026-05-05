<template>
  <div class="glass-page">
    <!-- 玻璃风格背景 -->
    <div class="glass-bg">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 页面标题区 -->
    <header class="glass-header">
      <div class="header-content">
        <div class="title-section">
          <div class="title-icon">
            <UserOutlined />
          </div>
          <div class="title-text">
            <h1>学生信息</h1>
            <p>管理学生信息，维护班级关联</p>
          </div>
        </div>
        <div class="header-actions">
          <a-button @click="toBatchImport" class="btn-import">
            <ImportOutlined /> 批量导入
          </a-button>
          <a-button type="primary" @click="toAdd" class="btn-create">
            <PlusOutlined /> 创建学生信息
          </a-button>
        </div>
      </div>
    </header>

    <!-- 搜索卡片 -->
    <div class="glass-search">
      <div class="search-content">
        <div class="search-inputs">
          <a-input
            v-model:value="queryForm.studentName"
            placeholder="搜索姓名..."
            class="search-input"
            allowClear
          >
            <template #prefix><SearchOutlined /></template>
          </a-input>
          <a-input
            v-model:value="queryForm.studentNo"
            placeholder="搜索学号..."
            class="search-input"
            allowClear
          >
            <template #prefix><NumberOutlined /></template>
          </a-input>
          <a-select
            v-model:value="queryForm.classId"
            placeholder="选择班级"
            class="search-select"
            allowClear
            :loading="classLoading"
          >
            <a-select-option v-for="item in classOptions" :key="item.value" :value="item.value">
              {{ item.label }}
            </a-select-option>
          </a-select>
          <a-input
            v-model:value="queryForm.phone"
            placeholder="搜索电话..."
            class="search-input"
            allowClear
          >
            <template #prefix><PhoneOutlined /></template>
          </a-input>
        </div>
        <div class="search-actions">
          <a-button @click="resetQuery" class="btn-reset">
            <ReloadOutlined /> 重置
          </a-button>
          <a-button type="primary" @click="onSearch" class="btn-search">
            <SearchOutlined /> 查询
          </a-button>
        </div>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="glass-action-bar">
      <div class="section-title">
        <span class="title-text">学生列表</span>
        <span class="title-count">共 {{ total }} 名学生</span>
      </div>
    </div>

    <!-- 学生列表表格 -->
    <div class="glass-table-container">
      <a-table
        :loading="tableLoading"
        :dataSource="tableData"
        :columns="columns"
        rowKey="id"
        :pagination="false"
        size="large"
        :scroll="{ x: 800 }"
      >
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.dataIndex === 'seq'">
            {{ (queryForm.pageNum - 1) * queryForm.pageSize + index + 1 }}
          </template>
          <template v-else-if="column.dataIndex === 'gender'">
            <a-tag :class="['gender-tag', record.gender]">
              {{ dictStore.getDataLabels('gender', record.gender) || '未知' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <a-tag :class="['status-tag', record.status]">
              {{ dictStore.getDataLabels('student_status', record.status) || '未知' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'className'">
            <span v-if="record.className">
              <BankOutlined /> {{ record.className }}
            </span>
            <span v-else class="text-muted">未分配班级</span>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="table-actions">
              <a-tooltip title="编辑">
                <a-button type="text" @click="toEdit(record)" class="action-btn">
                  <EditOutlined />
                </a-button>
              </a-tooltip>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 空状态 -->
    <a-empty v-if="tableData.length === 0 && !tableLoading" description="暂无学生数据" class="glass-empty">
      <a-button type="primary" @click="toAdd">创建第一个学生</a-button>
    </a-empty>

    <!-- 分页 -->
    <div class="glass-pagination" v-if="total > 0">
      <a-pagination
        showSizeChanger
        showQuickJumper
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryData"
        :show-total="(total) => `共 ${total} 条`"
      />
    </div>

    <!-- 弹窗组件 -->
    <StudentFormModal ref="formModalRef" @reloadList="queryData" />
    <StudentBatchImportModal ref="batchImportModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { studentApi } from '/@/api/smartmind/student-api.js';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import StudentFormModal from './student-form-modal.vue';
  import StudentBatchImportModal from './student-batch-import-modal.vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    UserOutlined,
    BankOutlined,
    EditOutlined,
    NumberOutlined,
    PhoneOutlined,
    ImportOutlined
  } from '@ant-design/icons-vue';

  const dictStore = useDictStore();

  // 查询表单
  const queryForm = reactive({
    studentName: '',
    studentNo: '',
    classId: undefined,
    phone: '',
    pageNum: 1,
    pageSize: 10,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const classOptions = ref([]);
  const classLoading = ref(false);

  // 弹窗引用
  const formModalRef = ref();
  const batchImportModalRef = ref();

  // 表格列定义
  const columns = [
    {
      title: '序号',
      dataIndex: 'seq',
      width: 80,
      align: 'center',
    },
    {
      title: '姓名',
      dataIndex: 'studentName',
      width: 120,
    },
    {
      title: '学号',
      dataIndex: 'studentNo',
      width: 150,
    },
    {
      title: '班级',
      dataIndex: 'className',
      width: 150,
    },
    {
      title: '电话',
      dataIndex: 'phone',
      width: 140,
    },
    {
      title: '性别',
      dataIndex: 'gender',
      width: 80,
      align: 'center',
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      align: 'center',
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 80,
      align: 'center',
      fixed: 'right',
    },
  ];

  onMounted(() => {
    queryData();
    loadClassOptions();
  });

  // 加载班级选项
  async function loadClassOptions() {
    classLoading.value = true;
    try {
      const res = await clazzApi.selectVo();
      classOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      classLoading.value = false;
    }
  }

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await studentApi.queryPage(queryForm);
      const data = res.data || {};
      tableData.value = data.rows || data.list || [];
      total.value = data.total || data.totalRow || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 搜索
  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  // 重置
  function resetQuery() {
    queryForm.studentName = '';
    queryForm.studentNo = '';
    queryForm.classId = undefined;
    queryForm.phone = '';
    queryForm.pageNum = 1;
    queryData();
  }

  // 打开编辑弹窗
  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  // 打开新增弹窗
  function toAdd() {
    formModalRef.value.showModal();
  }

  // 打开批量导入弹窗
  function toBatchImport() {
    batchImportModalRef.value.showModal();
  }
</script>

<style lang="less">
  // 导入公共玻璃主题
  @import '/@/styles/glass-theme.less';

  // 页面头部操作按钮样式（学生页面特有）
  .glass-header {
    .header-actions {
      display: flex;
      gap: 16px;

      .btn-import {
        height: 48px;
        padding: 0 28px;
        font-size: 15px;
        font-weight: 600;
        border-radius: 12px;
        background: rgba(255, 255, 255, 0.7);
        backdrop-filter: blur(10px);
        border: 1px solid @glass-border;
        color: @text-primary;
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;

        &:hover {
          background: rgba(255, 255, 255, 0.9);
          border-color: @accent-cyan;
          color: @accent-cyan;
          transform: translateY(-2px);
          box-shadow: 0 12px 35px rgba(79, 172, 254, 0.2);
        }

        .anticon {
          margin-right: 6px;
        }
      }
    }
  }

  // 表格容器样式
  .glass-table-container {
    background: @glass-card;
    backdrop-filter: blur(20px);
    border: 1px solid @glass-border;
    border-radius: 24px;
    padding: 20px;
    margin-bottom: 24px;
    box-shadow: @glass-shadow;
    position: relative;
    z-index: 10;

    .ant-table {
      background: transparent;

      .ant-table-thead > tr > th {
        background: rgba(255, 255, 255, 0.5);
        font-weight: 600;
        color: @text-primary;
        border-bottom: 1px solid @glass-border;
      }

      .ant-table-tbody > tr > td {
        border-bottom: 1px solid rgba(255, 255, 255, 0.3);
        color: @text-primary;
      }

      .ant-table-tbody > tr:hover > td {
        background: rgba(255, 255, 255, 0.4);
      }
    }

    .gender-tag {
      border-radius: 12px;
      padding: 2px 12px;
      font-size: 13px;
      border: none;

      &.1 {
        background: rgba(79, 172, 254, 0.15);
        color: @accent-blue;
      }

      &.0 {
        background: rgba(255, 105, 180, 0.15);
        color: #e91e63;
      }

      &.2 {
        background: rgba(160, 174, 192, 0.15);
        color: @text-muted;
      }
    }

    .status-tag {
      border-radius: 12px;
      padding: 2px 12px;
      font-size: 13px;
      border: none;

      // 在读 - 蓝色
      &.0 {
        background: rgba(79, 172, 254, 0.15);
        color: @accent-blue;
      }

      // 休学 - 橙色
      &.1 {
        background: rgba(255, 152, 0, 0.15);
        color: #ff9800;
      }

      // 退学 - 红色
      &.2 {
        background: rgba(244, 67, 54, 0.15);
        color: #f44336;
      }

      // 毕业 - 绿色
      &.3 {
        background: rgba(76, 175, 80, 0.15);
        color: #4caf50;
      }
    }

    .text-muted {
      color: @text-muted;
    }

    .table-actions {
      .action-btn {
        width: 36px;
        height: 36px;
        padding: 0;
        border-radius: 8px;
        color: @text-secondary;

        &:hover {
          background: linear-gradient(135deg, rgba(79, 172, 254, 0.15), rgba(102, 126, 234, 0.1));
          color: @accent-blue;
        }
      }
    }
  }
</style>
