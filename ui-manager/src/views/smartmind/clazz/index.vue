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
            <h1>班级管理</h1>
            <p>管理班级信息，掌握学生动态</p>
          </div>
        </div>
        <a-button type="primary" @click="toAdd" class="btn-create">
          <PlusOutlined /> 新建班级
        </a-button>
      </div>
    </header>

    <!-- 搜索卡片 -->
    <div class="glass-search">
      <div class="search-content">
        <div class="search-inputs">
          <a-input
            v-model:value="queryForm.className"
            placeholder="搜索班级名称..."
            class="search-input"
            allowClear
          >
            <template #prefix><SearchOutlined /></template>
          </a-input>
          <a-select
            v-model:value="queryForm.gradeLevel"
            placeholder="年级"
            class="search-select"
            allowClear
          >
            <a-select-option v-for="item in dictStore.getDictData('grade_level')" :key="item.dataValue" :value="item.dataValue">
              {{ item.dataLabel }}
            </a-select-option>
          </a-select>
          <a-select
            v-model:value="queryForm.status"
            placeholder="状态"
            class="search-select"
            allowClear
          >
            <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">
              {{ item.dataLabel }}
            </a-select-option>
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

    <!-- 统计卡片 -->
    <div class="glass-stats">
      <div class="stat-card total">
        <div class="stat-icon"><TeamOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ total }}</div>
          <div class="stat-label">全部班级</div>
        </div>
      </div>
      <div class="stat-card active">
        <div class="stat-icon"><CheckCircleOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ activeCount }}</div>
          <div class="stat-label">有效班级</div>
        </div>
      </div>
      <div class="stat-card inactive">
        <div class="stat-icon"><StopOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ inactiveCount }}</div>
          <div class="stat-label">已失效</div>
        </div>
      </div>
      <div class="stat-card students">
        <div class="stat-icon"><UserOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ totalStudents }}</div>
          <div class="stat-label">学生总数</div>
        </div>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="glass-action-bar">
      <div class="section-title">
        <span class="title-text">班级列表</span>
        <span class="title-count">共 {{ total }} 个班级</span>
      </div>
    </div>

    <!-- 班级卡片网格 -->
    <div class="glass-card-grid">
      <div
        v-for="clazz in tableData"
        :key="clazz.id"
        class="glass-card"
        :class="{ 'inactive': clazz.status === '0' }"
      >
        <!-- 卡片头部 -->
        <div class="card-header">
          <span class="tag">
            {{ dictStore.getDataLabels('grade_level', clazz.gradeLevel) }}
          </span>
          <a-tag :class="['status-tag', getStatusClass(clazz.status)]">
            {{ dictStore.getDataLabels('common_status', clazz.status) }}
          </a-tag>
        </div>

        <!-- 卡片主体 -->
        <div class="card-body">
          <div class="code">{{ clazz.classCode }}</div>
          <h3 class="card-title">{{ clazz.className }}</h3>
          <div class="meta-row">
            <span class="meta-item">
              <UserOutlined />
              {{ clazz.studentCount || 0 }} 人
            </span>
            <span class="meta-item">
              <CalendarOutlined />
              {{ formatDate(clazz.createTime) }}
            </span>
          </div>
          <p class="desc" v-if="clazz.remark">{{ clazz.remark }}</p>
          <p class="desc empty" v-else>暂无班级描述</p>
        </div>

        <!-- 卡片操作 -->
        <div class="card-actions">
          <a-tooltip title="编辑">
            <a-button type="text" @click="toEdit(clazz)" class="action-btn">
              <EditOutlined />
              <span class="btn-text">编辑</span>
            </a-button>
          </a-tooltip>
          <!-- 禁用按钮 - 状态为启用(1)时显示 -->
          <a-popconfirm
            v-if="clazz.status === '1'"
            title="确定要禁用此班级吗?"
            description="禁用后班级将无法使用"
            @confirm="toDisable(clazz)"
            placement="topRight"
          >
            <a-tooltip title="禁用">
              <a-button type="text" class="action-btn delete">
                <PoweroffOutlined />
                <span class="btn-text">禁用</span>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
          <!-- 启用按钮 - 状态为禁用(0)时显示 -->
          <a-popconfirm
            v-else
            title="确定要启用此班级吗?"
            description="启用后班级将恢复正常使用"
            @confirm="toEnable(clazz)"
            placement="topRight"
          >
            <a-tooltip title="启用">
              <a-button type="text" class="action-btn active">
                <PlayCircleOutlined />
                <span class="btn-text">启用</span>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <a-empty v-if="tableData.length === 0 && !tableLoading" description="暂无班级数据" class="glass-empty">
      <a-button type="primary" @click="toAdd">创建第一个班级</a-button>
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
    <ClazzFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref, computed } from 'vue';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import ClazzFormModal from './clazz-form-modal.vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    TeamOutlined,
    CheckCircleOutlined,
    StopOutlined,
    UserOutlined,
    EditOutlined,
    PoweroffOutlined,
    PlayCircleOutlined,
    CalendarOutlined
  } from '@ant-design/icons-vue';

  const dictStore = useDictStore();

  // 查询表单
  const queryForm = reactive({
    className: '',
    gradeLevel: undefined,
    status: undefined,
    pageNum: 1,
    pageSize: 12,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  // 统计数量
  const activeCount = computed(() => tableData.value.filter(c => c.status === '1').length);
  const inactiveCount = computed(() => tableData.value.filter(c => c.status === '0').length);
  const totalStudents = computed(() => tableData.value.reduce((sum, c) => sum + (c.studentCount || 0), 0));

  // 弹窗引用
  const formModalRef = ref();

  onMounted(() => {
    queryData();
  });

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await clazzApi.queryPage(queryForm);
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
    queryForm.className = '';
    queryForm.gradeLevel = undefined;
    queryForm.status = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 状态样式
  function getStatusClass(status) {
    const map = {
      '0': 'inactive',
      '1': 'active'
    };
    return map[status] || 'default';
  }

  // 格式化日期
  function formatDate(date) {
    if (!date) return '-';
    return date.split(' ')[0];
  }

  // 打开编辑弹窗
  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  // 打开新增弹窗
  function toAdd() {
    formModalRef.value.showModal();
  }

  // 禁用
  async function toDisable(record) {
    try {
      await clazzApi.setStatus({ id: record.id, status: '0' });
      message.success('禁用成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 启用
  async function toEnable(record) {
    try {
      await clazzApi.setStatus({ id: record.id, status: '1' });
      message.success('启用成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>

<style lang="less">
  // 导入公共玻璃主题
  @import '/@/styles/glass-theme.less';
</style>
