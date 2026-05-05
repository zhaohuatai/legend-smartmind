<template>
  <div class="glass-page activity-detail-page">
    <!-- 玻璃风格背景 -->
    <div class="glass-bg">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 页面头部 -->
    <header class="glass-header">
      <div class="header-content">
        <div class="header-left">
          <a-button type="text" @click="goBack" class="btn-back">
            <ArrowLeftOutlined /> 返回
          </a-button>
          <div class="title-section">
            <div class="title-icon" :style="{ background: getActivityTypeGradient(activityInfo.activityType) }">
              <component :is="getActivityTypeIcon(activityInfo.activityType)" />
            </div>
            <div class="title-text">
              <h1>{{ activityInfo.activityName || '活动详情' }}</h1>
              <p>{{ activityInfo.sessionName }} · {{ activityInfo.clazzName }}</p>
            </div>
          </div>
        </div>
        <div class="header-right">
          <a-tag :color="getActivityTypeColor(activityInfo.activityType)" size="large">
            {{ dictStore.getDataLabels('class_activity_type', activityInfo.activityType) }}
          </a-tag>
          <a-tag :class="['status-tag', getActivityStatusClass(activityInfo.status)]" size="large">
            {{ dictStore.getDataLabels('class_activity_status', activityInfo.status) }}
          </a-tag>
        </div>
      </div>
    </header>

    <!-- 内容区域 -->
    <div class="activity-content-area">
      <!-- 左侧：活动信息和资源 -->
      <div class="activity-sidebar">
        <!-- 活动基本信息 -->
        <div class="info-card">
          <div class="card-title">
            <InfoCircleOutlined />
            <span>活动信息</span>
          </div>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">活动时长</span>
              <span class="info-value">{{ activityInfo.duration }}分钟</span>
            </div>
            <div class="info-item">
              <span class="info-label">开始时间</span>
              <span class="info-value">{{ formatDateTime(activityInfo.startTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">关联单元</span>
              <span class="info-value">{{ activityInfo.unitName || '-' }}</span>
            </div>
            <div class="info-item" v-if="activityInfo.activityContent">
              <span class="info-label">活动内容</span>
              <span class="info-value content">{{ activityInfo.activityContent }}</span>
            </div>
          </div>
        </div>

        <!-- 关联资源 -->
        <div class="info-card" v-if="activityResources && activityResources.length > 0">
          <div class="card-title">
            <LinkOutlined />
            <span>关联资源</span>
          </div>
          <div class="resource-list">
            <div
              v-for="resource in activityResources"
              :key="resource.id"
              class="resource-item"
            >
              <div class="resource-icon" :style="{ background: getResourceTypeColor(resource.resourceType) }">
                <component :is="getResourceTypeIcon(resource.resourceType)" />
              </div>
              <div class="resource-info">
                <div class="resource-name">{{ resource.resourceName }}</div>
                <div class="resource-type">{{ dictStore.getDataLabels('class_activity_resource_type', resource.resourceType) }}</div>
              </div>
              <div class="resource-score" v-if="resource.score">
                {{ resource.score }}分
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-card">
          <a-button type="primary" block @click="handleStartActivity" v-if="activityInfo.status === '0'">
            <PlayCircleOutlined /> 开始活动
          </a-button>
          <a-button type="primary" block @click="handleEndActivity" v-if="activityInfo.status === '1'">
            <StopOutlined /> 结束活动
          </a-button>
          <a-button block @click="handleExportData">
            <DownloadOutlined /> 导出数据
          </a-button>
        </div>
      </div>

      <!-- 右侧：实时数据和统计 -->
      <div class="activity-main">
        <!-- 统计卡片 -->
        <div class="stats-row">
          <div class="stat-card">
            <div class="stat-icon total">
              <TeamOutlined />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalStudents }}</div>
              <div class="stat-label">班级总人数</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon submitted">
              <CheckCircleOutlined />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.submittedCount }}</div>
              <div class="stat-label">已提交</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon unsubmitted">
              <ClockCircleOutlined />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.unsubmittedCount }}</div>
              <div class="stat-label">未提交</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon rate">
              <PercentageOutlined />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.submitRate }}%</div>
              <div class="stat-label">提交率</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon avg">
              <StarOutlined />
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.avgScore }}</div>
              <div class="stat-label">平均分</div>
            </div>
          </div>
        </div>

        <!-- 提交进度 -->
        <div class="progress-card">
          <div class="card-title">
            <FieldTimeOutlined />
            <span>提交进度</span>
            <span class="progress-text">{{ stats.submittedCount }} / {{ stats.totalStudents }}</span>
          </div>
          <a-progress
            :percent="stats.submitRate"
            :stroke-color="{ '0%': '#667eea', '100%': '#764ba2' }"
            :show-info="false"
            :stroke-width="12"
          />
        </div>

        <!-- Tab切换 -->
        <div class="tabs-card">
          <a-tabs v-model:activeKey="activeTab">
            <a-tab-pane key="students" tab="学生列表">
              <div class="student-table-wrapper">
                <div class="table-toolbar">
                  <a-input-search
                    v-model:value="searchKeyword"
                    placeholder="搜索学生姓名或学号"
                    style="width: 240px"
                    @search="handleSearch"
                  />
                  <a-select
                    v-model:value="filterStatus"
                    style="width: 120px"
                    @change="handleFilter"
                  >
                    <a-select-option value="">全部状态</a-select-option>
                    <a-select-option value="submitted">已提交</a-select-option>
                    <a-select-option value="unsubmitted">未提交</a-select-option>
                  </a-select>
                </div>
                <a-table
                  :columns="studentColumns"
                  :data-source="filteredStudents"
                  :pagination="{ pageSize: 10, showSizeChanger: true, showTotal: (total) => `共 ${total} 条` }"
                  row-key="studentId"
                  size="small"
                >
                  <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'studentName'">
                      <div class="student-cell">
                        <div class="student-avatar-small">
                          {{ record.studentName?.charAt(0) || '学' }}
                        </div>
                        <div>
                          <div class="student-name-cell">{{ record.studentName }}</div>
                          <div class="student-no-cell">{{ record.studentNo }}</div>
                        </div>
                      </div>
                    </template>
                    <template v-if="column.key === 'status'">
                      <a-tag v-if="record.submitted" color="success" size="small">
                        <CheckCircleOutlined /> 已提交
                      </a-tag>
                      <a-tag v-else color="default" size="small">
                        <ClockCircleOutlined /> 未提交
                      </a-tag>
                    </template>
                    <template v-if="column.key === 'score'">
                      <span v-if="record.submitted" class="score-value">{{ record.score || '-' }}</span>
                      <span v-else class="score-empty">-</span>
                    </template>
                    <template v-if="column.key === 'action'">
                      <a-button type="link" size="small" @click="viewStudentDetail(record)" v-if="record.submitted">
                        查看详情
                      </a-button>
                    </template>
                  </template>
                </a-table>
              </div>
            </a-tab-pane>

            <a-tab-pane key="statistics" tab="统计分析">
              <div class="statistics-content">
                <!-- 分数分布 -->
                <div class="stat-section">
                  <div class="section-title">分数分布</div>
                  <div class="score-distribution">
                    <div
                      v-for="item in scoreDistribution"
                      :key="item.range"
                      class="distribution-item"
                    >
                      <div class="distribution-label">{{ item.range }}</div>
                      <div class="distribution-bar">
                        <div
                          class="distribution-fill"
                          :style="{ width: item.percent + '%', background: item.color }"
                        ></div>
                      </div>
                      <div class="distribution-count">{{ item.count }}人</div>
                    </div>
                  </div>
                </div>

                <!-- 正确率统计 -->
                <div class="stat-section" v-if="correctRateStats.length > 0">
                  <div class="section-title">题目正确率</div>
                  <div class="correct-rate-list">
                    <div
                      v-for="item in correctRateStats"
                      :key="item.questionId"
                      class="correct-rate-item"
                    >
                      <div class="rate-info">
                        <span class="rate-label">{{ item.questionTitle }}</span>
                        <span class="rate-value">{{ item.correctRate }}%</span>
                      </div>
                      <a-progress
                        :percent="item.correctRate"
                        :stroke-color="getCorrectRateColor(item.correctRate)"
                        :show-info="false"
                        size="small"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </a-tab-pane>
          </a-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { postRequest } from '/@/lib/axios';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    ArrowLeftOutlined,
    InfoCircleOutlined,
    LinkOutlined,
    PlayCircleOutlined,
    StopOutlined,
    DownloadOutlined,
    TeamOutlined,
    CheckCircleOutlined,
    ClockCircleOutlined,
    PercentageOutlined,
    StarOutlined,
    FieldTimeOutlined,
  } from '@ant-design/icons-vue';
  import dayjs from 'dayjs';

  const route = useRoute();
  const router = useRouter();
  const dictStore = useDictStore();

  const activityId = ref(null);
  const activityInfo = ref({});
  const activityResources = ref([]);
  const stats = ref({
    totalStudents: 0,
    submittedCount: 0,
    unsubmittedCount: 0,
    submitRate: 0,
    avgScore: 0,
  });
  const studentList = ref([]);
  const searchKeyword = ref('');
  const filterStatus = ref('');
  const activeTab = ref('students');
  const scoreDistribution = ref([]);
  const correctRateStats = ref([]);

  const studentColumns = [
    { title: '学生', key: 'studentName', width: 180 },
    { title: '状态', key: 'status', width: 100 },
    { title: '得分', key: 'score', width: 80 },
    { title: '提交时间', key: 'submitTime', width: 160 },
    { title: '操作', key: 'action', width: 100, fixed: 'right' },
  ];

  const filteredStudents = computed(() => {
    let list = studentList.value;
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase();
      list = list.filter(s =>
        s.studentName?.toLowerCase().includes(keyword) ||
        s.studentNo?.toLowerCase().includes(keyword)
      );
    }
    if (filterStatus.value === 'submitted') {
      list = list.filter(s => s.submitted);
    } else if (filterStatus.value === 'unsubmitted') {
      list = list.filter(s => !s.submitted);
    }
    return list;
  });

  onMounted(() => {
    activityId.value = route.query.activityId;
    if (activityId.value) {
      loadActivityInfo();
      loadActivityResources();
      loadActivityStats();
      loadStudentList();
    }
  });

  async function loadActivityInfo() {
    try {
      const res = await postRequest(`/manage/smartmind/classactivity/loadById/${activityId.value}`);
      activityInfo.value = res.data || {};
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadActivityResources() {
    try {
      const res = await postRequest('/manage/smartmind/classactivityresource/queryByActivityId', { activityId: activityId.value });
      activityResources.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadActivityStats() {
    try {
      const res = await postRequest('/manage/smartmind/classactivitysubmit/getActivityStats', { activityId: activityId.value });
      const data = res.data || {};
      stats.value = {
        totalStudents: data.totalStudents || 0,
        submittedCount: data.submittedCount || 0,
        unsubmittedCount: data.unsubmittedCount || 0,
        submitRate: data.submitRate || 0,
        avgScore: data.avgScore || 0,
      };
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadStudentList() {
    try {
      const res = await postRequest('/manage/smartmind/classactivitysubmit/getStudentSubmissions', { activityId: activityId.value });
      studentList.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function goBack() {
    router.back();
  }

  function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    return dayjs(dateStr).format('YYYY-MM-DD HH:mm');
  }

  function getActivityTypeColor(type) {
    const map = { 'quiz': 'primary', 'exercise': 'success', 'experiment': 'warning', 'discussion': 'purple' };
    return map[type] || 'default';
  }

  function getActivityTypeGradient(type) {
    const map = {
      'quiz': 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      'exercise': 'linear-gradient(135deg, #52c41a 0%, #73d13d 100%)',
      'experiment': 'linear-gradient(135deg, #faad14 0%, #ffc53d 100%)',
      'discussion': 'linear-gradient(135deg, #722ed1 0%, #9254de 100%)',
    };
    return map[type] || 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
  }

  function getActivityTypeIcon(type) {
    return 'FormOutlined';
  }

  function getActivityStatusClass(status) {
    const map = { '0': 'status-not-started', '1': 'status-in-progress', '2': 'status-finished', '3': 'status-cancelled' };
    return map[status] || '';
  }

  function getResourceTypeColor(type) {
    const map = { 'question': '#667eea', 'discussion': '#722ed1', 'experiment': '#faad14' };
    return map[type] || '#667eea';
  }

  function getResourceTypeIcon(type) {
    return 'FileTextOutlined';
  }

  function getCorrectRateColor(rate) {
    if (rate >= 80) return '#52c41a';
    if (rate >= 60) return '#faad14';
    return '#ff4d4f';
  }

  function handleSearch() {
  }

  function handleFilter() {
  }

  function viewStudentDetail(record) {
    message.info('查看学生详情：' + record.studentName);
  }

  async function handleStartActivity() {
    try {
      await postRequest('/manage/smartmind/classactivity/setStatus', { id: activityId.value, status: '1' });
      message.success('活动已开始');
      loadActivityInfo();
    } catch (e) {
      smartSentry.captureError(e);
      message.error('操作失败');
    }
  }

  async function handleEndActivity() {
    try {
      await postRequest('/manage/smartmind/classactivity/setStatus', { id: activityId.value, status: '2' });
      message.success('活动已结束');
      loadActivityInfo();
    } catch (e) {
      smartSentry.captureError(e);
      message.error('操作失败');
    }
  }

  function handleExportData() {
    message.info('导出功能开发中');
  }
</script>

<style scoped lang="less">
.activity-detail-page {
  min-height: 100vh;
}

.glass-header {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding: 16px 32px;
  margin-bottom: 24px;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1400px;
    margin: 0 auto;
  }

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .btn-back {
      color: #6b7280;
      font-size: 14px;
    }
  }

  .title-section {
    display: flex;
    align-items: center;
    gap: 16px;

    .title-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 24px;
    }

    .title-text {
      h1 {
        font-size: 20px;
        font-weight: 600;
        color: #1a1a2e;
        margin: 0 0 4px 0;
      }

      p {
        font-size: 13px;
        color: #6b7280;
        margin: 0;
      }
    }
  }

  .header-right {
    display: flex;
    gap: 8px;
  }
}

.activity-content-area {
  display: flex;
  gap: 24px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 32px 32px;
  min-height: calc(100vh - 120px);
}

.activity-sidebar {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;

  .card-title {
    padding: 16px 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.06);
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #1a1a2e;
  }

  .info-list {
    padding: 16px 20px;

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding: 8px 0;

      &:not(:last-child) {
        border-bottom: 1px solid rgba(0, 0, 0, 0.04);
      }

      .info-label {
        font-size: 13px;
        color: #6b7280;
        flex-shrink: 0;
      }

      .info-value {
        font-size: 13px;
        color: #1a1a2e;
        text-align: right;
        max-width: 180px;

        &.content {
          text-align: left;
          word-break: break-all;
        }
      }
    }
  }

  .resource-list {
    padding: 12px 20px;

    .resource-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 10px 0;

      &:not(:last-child) {
        border-bottom: 1px solid rgba(0, 0, 0, 0.04);
      }

      .resource-icon {
        width: 36px;
        height: 36px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-size: 16px;
        flex-shrink: 0;
      }

      .resource-info {
        flex: 1;
        min-width: 0;

        .resource-name {
          font-size: 13px;
          font-weight: 500;
          color: #1a1a2e;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .resource-type {
          font-size: 11px;
          color: #6b7280;
        }
      }

      .resource-score {
        font-size: 13px;
        font-weight: 600;
        color: #667eea;
      }
    }
  }
}

.action-card {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.activity-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;

  .stat-card {
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(20px);
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.3);
    padding: 16px;
    display: flex;
    align-items: center;
    gap: 12px;

    .stat-icon {
      width: 44px;
      height: 44px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      color: #fff;

      &.total { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      &.submitted { background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%); }
      &.unsubmitted { background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%); }
      &.rate { background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%); }
      &.avg { background: linear-gradient(135deg, #722ed1 0%, #9254de 100%); }
    }

    .stat-info {
      .stat-value {
        font-size: 22px;
        font-weight: 700;
        color: #1a1a2e;
        line-height: 1.2;
      }

      .stat-label {
        font-size: 12px;
        color: #6b7280;
        margin-top: 2px;
      }
    }
  }
}

.progress-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 16px 20px;

  .card-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #1a1a2e;
    margin-bottom: 12px;

    .progress-text {
      margin-left: auto;
      font-size: 13px;
      color: #6b7280;
      font-weight: 400;
    }
  }
}

.tabs-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 16px 20px;
  flex: 1;

  .student-table-wrapper {
    .table-toolbar {
      display: flex;
      gap: 12px;
      margin-bottom: 16px;
    }

    .student-cell {
      display: flex;
      align-items: center;
      gap: 10px;

      .student-avatar-small {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        font-weight: 600;
        flex-shrink: 0;
      }

      .student-name-cell {
        font-size: 13px;
        font-weight: 500;
        color: #1a1a2e;
      }

      .student-no-cell {
        font-size: 11px;
        color: #6b7280;
      }
    }

    .score-value {
      font-weight: 600;
      color: #667eea;
    }

    .score-empty {
      color: #d1d5db;
    }
  }

  .statistics-content {
    .stat-section {
      margin-bottom: 24px;

      .section-title {
        font-size: 14px;
        font-weight: 600;
        color: #1a1a2e;
        margin-bottom: 16px;
      }
    }

    .score-distribution {
      .distribution-item {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 12px;

        .distribution-label {
          width: 60px;
          font-size: 12px;
          color: #6b7280;
          flex-shrink: 0;
        }

        .distribution-bar {
          flex: 1;
          height: 8px;
          background: #f3f4f6;
          border-radius: 4px;
          overflow: hidden;

          .distribution-fill {
            height: 100%;
            border-radius: 4px;
            transition: width 0.3s;
          }
        }

        .distribution-count {
          width: 50px;
          font-size: 12px;
          color: #1a1a2e;
          text-align: right;
          flex-shrink: 0;
        }
      }
    }

    .correct-rate-list {
      .correct-rate-item {
        margin-bottom: 16px;

        .rate-info {
          display: flex;
          justify-content: space-between;
          margin-bottom: 6px;

          .rate-label {
            font-size: 13px;
            color: #1a1a2e;
          }

          .rate-value {
            font-size: 13px;
            font-weight: 600;
          }
        }
      }
    }
  }
}

.status-not-started {
  background: #e6f4ff;
  color: #1677ff;
  border-color: #91caff;
}

.status-in-progress {
  background: #f6ffed;
  color: #52c41a;
  border-color: #b7eb8f;
}

.status-finished {
  background: #f5f5f5;
  color: #8c8c8c;
  border-color: #d9d9d9;
}

.status-cancelled {
  background: #fff2f0;
  color: #ff4d4f;
  border-color: #ffccc7;
}
</style>
