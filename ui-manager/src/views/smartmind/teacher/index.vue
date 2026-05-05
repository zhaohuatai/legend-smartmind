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
            <TeamOutlined />
          </div>
          <div class="title-text">
            <h1>教师管理</h1>
            <p>管理教师信息，维护师资队伍</p>
          </div>
        </div>
        <a-button type="primary" @click="toAdd" class="btn-create">
          <PlusOutlined /> 新建教师
        </a-button>
      </div>
    </header>

    <!-- 搜索卡片 -->
    <div class="glass-search">
      <div class="search-content">
        <div class="search-inputs">
          <a-input
            v-model:value="queryForm.teacherNo"
            placeholder="搜索工号..."
            class="search-input"
            allowClear
          >
            <template #prefix><IdcardOutlined /></template>
          </a-input>
          <a-input
            v-model:value="queryForm.teacherName"
            placeholder="搜索姓名..."
            class="search-input"
            allowClear
          >
            <template #prefix><SearchOutlined /></template>
          </a-input>
          <a-input
            v-model:value="queryForm.phone"
            placeholder="搜索手机号..."
            class="search-input"
            allowClear
          >
            <template #prefix><PhoneOutlined /></template>
          </a-input>
          <a-select
            v-model:value="queryForm.status"
            placeholder="选择状态"
            class="search-select"
            allowClear
          >
            <a-select-option value="1">有效</a-select-option>
            <a-select-option value="0">失效</a-select-option>
          </a-select>
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
        <span class="title-text">教师列表</span>
        <span class="title-count">共 {{ total }} 名教师</span>
      </div>
    </div>

    <!-- 教师列表表格 -->
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
          <template v-else-if="column.dataIndex === 'title'">
            <a-tag class="title-tag">
              {{ dictStore.getDataLabels('teacher_title', record.title) || '-' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            <a-tag :class="['status-tag', record.status]">
              {{ record.status === '1' ? '有效' : '失效' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="table-actions">
              <a-tooltip title="编辑">
                <a-button type="text" @click="toEdit(record)" class="action-btn">
                  <EditOutlined />
                </a-button>
              </a-tooltip>
              <a-tooltip title="配置课程">
                <a-button type="text" @click="toConfigCourseClass(record)" class="action-btn config">
                  <BookOutlined />
                </a-button>
              </a-tooltip>
              <a-tooltip :title="record.status === '1' ? '禁用' : '启用'">
                <a-popconfirm
                  :title="record.status === '1' ? '确定要禁用该教师吗?' : '确定要启用该教师吗?'"
                  @confirm="toggleStatus(record)"
                >
                  <a-button type="text" :class="['action-btn', record.status === '1' ? 'disable' : 'enable']">
                    <StopOutlined v-if="record.status === '1'" />
                    <CheckCircleOutlined v-else />
                  </a-button>
                </a-popconfirm>
              </a-tooltip>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 空状态 -->
    <a-empty v-if="tableData.length === 0 && !tableLoading" description="暂无教师数据" class="glass-empty">
      <a-button type="primary" @click="toAdd">创建第一个教师</a-button>
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
    <TeacherFormModal ref="formModalRef" @reloadList="queryData" />
    <TeacherCourseClassModal ref="courseClassModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { teacherApi } from '/@/api/smartmind/teacher-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import TeacherFormModal from './teacher-form-modal.vue';
  import TeacherCourseClassModal from './teacher-course-class-modal.vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    TeamOutlined,
    IdcardOutlined,
    PhoneOutlined,
    EditOutlined,
    StopOutlined,
    CheckCircleOutlined,
    BookOutlined,
  } from '@ant-design/icons-vue';

  const dictStore = useDictStore();

  // 查询表单
  const queryForm = reactive({
    teacherNo: '',
    teacherName: '',
    phone: '',
    status: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const formModalRef = ref();
  const courseClassModalRef = ref();

  // 表格列定义 - 只保留指定字段
  const columns = [
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
      align: 'center',
    },
    {
      title: '工号',
      dataIndex: 'teacherNo',
      width: 100,
    },
    {
      title: '姓名',
      dataIndex: 'teacherName',
      width: 100,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 130,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 200,
      ellipsis: true,
    },
    {
      title: '职称',
      dataIndex: 'title',
      width: 90,
      align: 'center',
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      align: 'center',
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 150,
      align: 'center',
      fixed: 'right',
    },
  ];

  onMounted(() => {
    queryData();
  });

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await teacherApi.queryPage(queryForm);
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
    queryForm.teacherNo = '';
    queryForm.teacherName = '';
    queryForm.phone = '';
    queryForm.status = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 打开配置课程班级弹窗
  function toConfigCourseClass(record) {
    courseClassModalRef.value.showModal(record);
  }

  // 打开编辑弹窗
  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  // 打开新增弹窗
  function toAdd() {
    formModalRef.value.showModal();
  }

  // 切换状态
  async function toggleStatus(record) {
    try {
      const newStatus = record.status === '1' ? '0' : '1';
      await teacherApi.updateStatus(record.id, newStatus);
      message.success(newStatus === '1' ? '启用成功' : '禁用成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>

<style lang="less">
  // 导入公共玻璃主题
  @import '/@/styles/glass-theme.less';

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

    .title-tag {
      border-radius: 12px;
      padding: 2px 12px;
      font-size: 13px;
      border: none;
      background: rgba(161, 140, 209, 0.15);
      color: @accent-purple;
    }

    .status-tag {
      border-radius: 12px;
      padding: 2px 12px;
      font-size: 13px;
      border: none;

      &.1 {
        background: rgba(76, 175, 80, 0.15);
        color: #4caf50;
      }

      &.0 {
        background: rgba(156, 163, 175, 0.15);
        color: @text-muted;
      }
    }

    .table-actions {
      display: flex;
      justify-content: center;
      gap: 8px;

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

        &.disable:hover {
          background: rgba(244, 67, 54, 0.1);
          color: #f44336;
        }

        &.enable:hover {
          background: rgba(76, 175, 80, 0.1);
          color: #4caf50;
        }

        &.config:hover {
          background: rgba(102, 126, 234, 0.1);
          color: #667eea;
        }
      }
    }
  }
</style>
