<template>
  <div class="glass-page class-session-page">
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
        <div class="title-section">
          <div class="title-icon">
            <DesktopOutlined />
          </div>
          <div class="title-text">
            <h1>课堂教学</h1>
            <p>{{ courseInfo?.courseName || '加载中...' }}</p>
          </div>
        </div>
        <a-button type="primary" @click="showCreateModal" class="btn-create">
          <PlusOutlined /> 创建课堂
        </a-button>
      </div>
    </header>

    <!-- 内容区域 -->
    <div class="session-content-area">
      <!-- 左侧：历史课堂列表 -->
      <div class="session-sidebar">
        <div class="sidebar-header">
          <HistoryOutlined />
          <span>历史课堂</span>
          <span class="session-count">{{ sessionList.length }}</span>
        </div>
        <div class="session-list-container">
          <div
            v-for="session in (sessionList || []).filter(s => s)"
            :key="session.id"
            class="session-list-item"
            :class="{ active: selectedSession?.id === session.id }"
            @click="selectSession(session)"
          >
            <div class="session-item-header">
              <span class="session-name">{{ session.sessionName }}</span>
              <a-tag :class="['status-tag', getStatusClass(session.status)]" size="small">
                {{ dictStore.getDataLabels('class_session_status', session.status) }}
              </a-tag>
            </div>
            <div class="session-item-info">
              <span class="info-item">
                <TeamOutlined />
                {{ session.clazzName }}
              </span>
              <span class="info-item">
                <ClockCircleOutlined />
                {{ formatDateTime(session.startTime) }}
              </span>
              <span class="info-item">
                <FieldTimeOutlined />
                {{ session.duration }}分钟
              </span>
            </div>
            <div class="session-item-units" v-if="session.unitsInfo && parseUnitsInfo(session.unitsInfo).length > 0">
              <a-tag v-for="(unit, idx) in parseUnitsInfo(session.unitsInfo)" :key="idx" size="small">
                {{ unit.unitName }}
              </a-tag>
            </div>
          </div>

          <a-empty v-if="sessionList.length === 0 && !loading" description="暂无课堂记录" class="empty-sessions" />
          <a-spin v-if="loading" class="loading-sessions" />
        </div>
      </div>

      <!-- 右侧：核心区域 -->
      <div class="session-main">
        <div class="main-placeholder" v-if="!selectedSession">
          <a-empty description="请在左侧选择课堂查看详情">
            <template #image>
              <DesktopOutlined style="font-size: 64px; color: #d1d5db;" />
            </template>
          </a-empty>
        </div>

        <div class="main-content" v-else>
          <div class="main-header">
            <h2>{{ selectedSession.sessionName }}</h2>
          </div>
          <div class="activity-type-bar">
            <div class="activity-type-btn quiz" @click="showQuizModal">
              <div class="btn-icon"><FileTextOutlined /></div>
              <span class="btn-label">随堂测试</span>
            </div>
            <div class="activity-type-btn exercise" @click="showExerciseModal">
              <div class="btn-icon"><EditOutlined /></div>
              <span class="btn-label">随堂练习</span>
            </div>
            <div class="activity-type-btn experiment" @click="showExperimentModal">
              <div class="btn-icon"><ExperimentOutlined /></div>
              <span class="btn-label">课堂实验</span>
            </div>
            <div class="activity-type-btn discussion" @click="showDiscussionModal">
              <div class="btn-icon"><CommentOutlined /></div>
              <span class="btn-label">话题讨论</span>
            </div>
          </div>
          <div class="main-body">
            <!-- 活动列表区域 -->
            <div class="activity-section">
              <div class="section-title">
                <ThunderboltOutlined />
                <span>课堂活动</span>
              </div>
              <div class="activity-list">
                <a-empty v-if="!activityList || activityList.length === 0" description="暂无课堂活动" />
                <div v-else class="activity-cards">
                  <div
                    v-for="activity in activityList"
                    :key="activity.id"
                    class="glass-card activity-card"
                    :class="{ active: selectedActivity?.id === activity.id }"
                  >
                    <div class="card-header">
                      <span class="tag">
                        {{ dictStore.getDataLabels('class_activity_type', activity.activityType) }}
                      </span>
                      
                      <a-tag :class="['status-tag', getActivityStatusClass(activity.status)]">
                        {{ dictStore.getDataLabels('class_activity_status', activity.status) }}
                      </a-tag>
                    </div>

                    <div class="card-body">
                      <div class="code">{{ activity.activityCode }}</div>
                      <h3 class="card-title">{{ activity.activityName }}</h3>
                     
                      <!-- <div class="meta-row">
                        <span class="meta-item">
                          <ClockCircleOutlined />
                          {{ activity.duration }}分钟
                        </span>
                        <span class="meta-item" v-if="activity.startTime">
                          <CalendarOutlined />
                          {{ formatDateTime(activity.startTime) }}
                        </span>
                      </div> -->

                      <div class="meta-row time-rows">
                        <span class="meta-item time-item" v-if="activity.startTime">
                          <PlayCircleOutlined /> 开始: {{ formatDateTime(activity.startTime) }}
                        </span>
                        <span class="meta-item time-item" v-if="activity.endTime">
                          <StopOutlined /> 截止: {{ formatDateTime(activity.endTime) }}
                        </span>
                        <span class="meta-item countdown-item" :class="getCountdownClass(activity)">
                          <HourglassOutlined /> {{ getCountdownText(activity) }}
                        </span>
                      </div>
                      <div class="activity-stats">
                        <span class="stat-item">
                          <CheckCircleOutlined /> 已提交: {{ activity.submitStuCount || 0 }}/{{ activity.totalStuCount || 0 }}人
                        </span>
                        <span class="stat-item avg-score">
                          平均分: {{ activity.avgScore !== null && activity.avgScore !== undefined ? activity.avgScore : '-' }}
                        </span>
                      </div>
                    </div>

                    <div class="card-actions">
                      <!-- <a-tooltip title="修改">
                        <a-button type="text" @click.stop="editActivity(activity)" class="action-btn">
                          <EditOutlined />
                          <span class="btn-text">修改</span>
                        </a-button>
                      </a-tooltip> -->

                      <a-tooltip title="查看内容">
                        <a-button type="text" @click.stop="showContentViewer(activity)" class="action-btn">
                          <FileTextOutlined />
                          <span class="btn-text">内容</span>
                        </a-button>
                      </a-tooltip>

                      <a-tooltip title="查看">
                        <a-button type="text" @click.stop="viewActivity(activity)" class="action-btn">
                          <EyeOutlined />
                          <span class="btn-text">查看</span>
                        </a-button>
                      </a-tooltip>

                      <a-tooltip title="开始" v-if="activity.status === '0'">
                        <a-button type="text" @click.stop="startActivity(activity)" class="action-btn start">
                          <PlayCircleOutlined />
                          <span class="btn-text">开始</span>
                        </a-button>
                      </a-tooltip>

                      <a-tooltip title="取消" v-if="activity.status === '1'">
                        <a-button type="text" @click.stop="cancelActivity(activity)" class="action-btn cancel">
                          <CloseCircleOutlined />
                          <span class="btn-text">取消</span>
                        </a-button>
                      </a-tooltip>

                      <a-tooltip title="停止" v-if="activity.status === '1'">
                        <a-button type="text" @click.stop="stopActivity(activity)" class="action-btn stop">
                          <PauseCircleOutlined />
                          <span class="btn-text">停止</span>
                        </a-button>
                      </a-tooltip>

                      <a-popconfirm
                        title="确定要删除此活动吗?"
                        description="删除后无法恢复"
                        @confirm="deleteActivity(activity)"
                        placement="topRight"
                      >
                        <a-tooltip title="删除">
                          <a-button type="text" class="action-btn delete">
                            <DeleteOutlined />
                            <span class="btn-text">删除</span>
                          </a-button>
                        </a-tooltip>
                      </a-popconfirm>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 活动详情和实时数据 -->
            <div class="activity-detail-section" v-if="selectedActivity">
              <div class="detail-header">
                <h3>{{ selectedActivity.activityName }}</h3>
                <div class="detail-actions">
                  <a-button size="small" @click="refreshActivityData">
                    <ReloadOutlined /> 刷新数据
                  </a-button>
                </div>
              </div>

              <div class="detail-content">
                <!-- 实时统计卡片 -->
                <div class="stats-cards">
                  <div class="stat-card">
                    <div class="stat-icon total">
                      <TeamOutlined />
                    </div>
                    <div class="stat-info">
                      <div class="stat-value">{{ activityStats.totalStudents }}</div>
                      <div class="stat-label">总人数</div>
                    </div>
                  </div>
                  <div class="stat-card">
                    <div class="stat-icon submitted">
                      <CheckCircleOutlined />
                    </div>
                    <div class="stat-info">
                      <div class="stat-value">{{ activityStats.submittedCount }}</div>
                      <div class="stat-label">已提交</div>
                    </div>
                  </div>
                  <div class="stat-card">
                    <div class="stat-icon unsubmitted">
                      <ClockCircleOutlined />
                    </div>
                    <div class="stat-info">
                      <div class="stat-value">{{ activityStats.unsubmittedCount }}</div>
                      <div class="stat-label">未提交</div>
                    </div>
                  </div>
                  <div class="stat-card">
                    <div class="stat-icon rate">
                      <PercentageOutlined />
                    </div>
                    <div class="stat-info">
                      <div class="stat-value">{{ activityStats.submitRate }}%</div>
                      <div class="stat-label">提交率</div>
                    </div>
                  </div>
                </div>

                <!-- 提交进度条 -->
                <div class="progress-section">
                  <div class="progress-header">
                    <span>提交进度</span>
                    <span class="progress-text">{{ activityStats.submittedCount }} / {{ activityStats.totalStudents }}</span>
                  </div>
                  <a-progress
                    :percent="activityStats.submitRate"
                    :stroke-color="{ '0%': '#667eea', '100%': '#764ba2' }"
                    :show-info="false"
                  />
                </div>

                <!-- 学生提交列表 -->
                <div class="student-list-section">
                  <div class="section-header">
                    <div class="section-subtitle">
                      <UserOutlined />
                      <span>学生提交情况</span>
                    </div>
                    <a-input
                      v-model:value="studentSearchText"
                      placeholder="搜索学生姓名"
                      allow-clear
                      size="small"
                      class="student-search-input"
                    >
                      <template #prefix>
                        <SearchOutlined />
                      </template>
                    </a-input>
                  </div>
                  <a-table
                    :columns="studentColumns"
                    :data-source="filteredStudentSubmissions"
                    :pagination="false"
                    :scroll="{ y: 450 }"
                    :row-class-name="getRowClassName"
                    size="small"
                    row-key="studentId"
                  >
                    <template #bodyCell="{ column, record }">
                      <template v-if="column.key === 'studentName'">
                        <div class="student-name-cell">
                          <div class="student-avatar">{{ record.studentName?.charAt(0) || '学' }}</div>
                          <span>{{ record.studentName }}</span>
                        </div>
                      </template>
                      <template v-else-if="column.key === 'studentNo'">
                        <span>{{ record.studentNo }}</span>
                      </template>
                      <template v-else-if="column.key === 'score'">
                        {{ record.score !== null && record.score !== undefined ? record.score : '-' }}
                      </template>
                      <template v-else-if="column.key === 'submitTime'">
                        {{ record.submitTime ? formatDateTime(record.submitTime) : '-' }}
                      </template>
                      <template v-else-if="column.key === 'status'">
                        <a-tag v-if="record.submitted" :color="record.status === '2' ? 'blue' : 'orange'" size="small">
                          {{ record.status === '2' ? '已评分' : '待评分' }}
                        </a-tag>
                        <a-tag v-else color="default" size="small">
                          <ClockCircleOutlined /> 未提交
                        </a-tag>
                      </template>
                      <template v-else-if="column.key === 'action'">
                        <a-button type="link" size="small" @click.stop="viewStudentDetail(record)">
                          查看详情
                        </a-button>
                      </template>
                    </template>
                  </a-table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 创建课堂弹窗 -->
    <CreateSessionModal
      v-model:open="createModalVisible"
      :course-id="courseId"
      :course-info="courseInfo"
      :unit-tree-data="unitTreeData"
      :unit-options="unitOptions"
      :clazz-options="clazzOptions"
      :teacher-id="route.query.teacherId"
      :teacher-name="route.query.teacherName"
      @success="loadSessions"
    />

    <!-- 随堂测试弹窗 -->
    <ClassSessionQuizModal
      v-model:open="quizModalVisible"
      :session="selectedSession"
      :course-id="courseId"
      @success="loadActivities(selectedSession?.id)"
    />

    <!-- 随堂练习弹窗 -->
    <ClassSessionExerciseModal
      v-model:open="exerciseModalVisible"
      :session="selectedSession"
      :course-id="courseId"
      @success="loadActivities(selectedSession?.id)"
    />

    <!-- 课堂实验弹窗 -->
    <ClassSessionExperimentModal
      v-model:open="experimentModalVisible"
      :session="selectedSession"
      :course-id="courseId"
      @success="loadActivities(selectedSession?.id)"
    />

    <!-- 话题讨论弹窗 -->
    <ClassSessionDiscussionModal
      v-model:open="discussionModalVisible"
      :session="selectedSession"
      :course-id="courseId"
      @success="loadActivities(selectedSession?.id)"
    />

    <!-- 编辑活动弹窗 -->
    <EditActivityModal
      v-model:open="editActivityModalVisible"
      :activity-id="editingActivityId"
      @success="loadActivities(selectedSession?.id)"
    />

    <!-- 活动内容查看弹窗 -->
    <ActivityContentViewer
      v-model:open="contentViewerVisible"
      :activity="currentActivity"
    />

    <!-- 学生答题详情弹窗 -->
    <StudentDetailModal
      v-model:open="studentDetailVisible"
      :student="currentStudentSubmission"
      :detail-data="studentDetailData"
    />
  </div>
</template>

<script setup>
  import { ref, onMounted, onUnmounted, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { TreeSelect } from 'ant-design-vue';
  import { postRequest, getRequest } from '/@/lib/axios';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import {
    DesktopOutlined,
    PlusOutlined,
    HistoryOutlined,
    TeamOutlined,
    ClockCircleOutlined,
    FieldTimeOutlined,
    ThunderboltOutlined,
    CalendarOutlined,
    ReloadOutlined,
    EyeOutlined,
    CheckCircleOutlined,
    PercentageOutlined,
    UserOutlined,
    FileTextOutlined,
    EditOutlined,
    ExperimentOutlined,
    CommentOutlined,
    PlayCircleOutlined,
    PauseCircleOutlined,
    CloseCircleOutlined,
    DeleteOutlined,
    StopOutlined,
    HourglassOutlined,
    SearchOutlined,
  } from '@ant-design/icons-vue';
  import dayjs from 'dayjs';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { classCourseApi } from '/@/api/smartmind/class-course-api.js';
  import { learningUnitApi } from '/@/api/smartmind/learning-unit-api.js';
  import { classActivityApi } from '/@/api/smartmind/classActivity-api.js';
  import { classActivitySubmitApi } from '/@/api/smartmind/classActivitySubmit-api.js';
  import CreateSessionModal from './create-session-modal.vue';
  import ClassSessionQuizModal from './class-session-quiz-modal.vue';
  import ClassSessionExerciseModal from './class-session-exercise-modal.vue';
  import ClassSessionExperimentModal from './class-session-experiment-modal.vue';
  import ClassSessionDiscussionModal from './class-session-discussion-modal.vue';
  import EditActivityModal from './edit-activity-modal.vue';
  import ActivityContentViewer from './activity-content-viewer.vue';
  import StudentDetailModal from './student-detail-modal.vue';

  const route = useRoute();
  const router = useRouter();
  const dictStore = useDictStore();

  const courseId = ref(null);
  const courseInfo = ref(null);
  const sessionList = ref([]);
  const selectedSession = ref(null);
  const activityList = ref([]);
  const selectedActivity = ref(null);
  const activityStats = ref({
    totalStudents: 0,
    submittedCount: 0,
    unsubmittedCount: 0,
    submitRate: 0,
  });
  const studentSubmissions = ref([]);
  const loading = ref(false);
  const createModalVisible = ref(false);
  const unitTreeData = ref([]);
  const unitOptions = ref([]);
  const clazzOptions = ref([]);

  const quizModalVisible = ref(false);
  const exerciseModalVisible = ref(false);
  const experimentModalVisible = ref(false);
  const discussionModalVisible = ref(false);
  const editActivityModalVisible = ref(false);
  const editingActivityId = ref(null);
  const contentViewerVisible = ref(false);
  const currentActivity = ref(null);
  const studentDetailVisible = ref(false);
  const currentStudentSubmission = ref(null);
  const studentDetailData = ref(null);
  const studentSearchText = ref('');
  const countdownMap = ref({});
  let countdownTimer = null;

  const studentColumns = [
    { title: '学生姓名', key: 'studentName', width: '20%' },
     { title: '学号', key: 'studentNo', width: '20%' },
    { title: '得分', key: 'score', width: '15%', align: 'center' },
    { title: '提交时间', key: 'submitTime', width: '25%', align: 'center' },
    { title: '提交状态', key: 'status', width: '20%', align: 'center' },
    { title: '操作', key: 'action', width: '20%', align: 'center' },
  ];

  const filteredStudentSubmissions = computed(() => {
    if (!studentSearchText.value) {
      return studentSubmissions.value;
    }
    const keyword = studentSearchText.value.toLowerCase();
    return studentSubmissions.value.filter(
      (s) => s.studentName && s.studentName.toLowerCase().includes(keyword)
    );
  });

  onMounted(() => {
    courseId.value = route.query.courseId;
    if (courseId.value) {
      loadCourseInfo();
      loadSessions();
      loadUnits();
      loadClazzes();
    }
    startCountdown();
  });

  onUnmounted(() => {
    if (countdownTimer) {
      clearInterval(countdownTimer);
    }
  });

  function startCountdown() {
    if (countdownTimer) {
      clearInterval(countdownTimer);
    }
    countdownTimer = setInterval(() => {
      updateCountdowns();
    }, 1000);
    updateCountdowns();
  }

  function updateCountdowns() {
    const now = new Date().getTime();
    const map = {};
    activityList.value.forEach(activity => {
      if (activity.endTime) {
        const end = new Date(activity.endTime).getTime();
        const diff = end - now;
        map[activity.id] = diff;
      }
    });
    countdownMap.value = map;
  }

  function getCountdownText(activity) {
    const diff = countdownMap.value[activity.id];
    if (diff === undefined || diff === null) {
      return '未设置截止时间';
    }
    if (diff <= 0) {
      return '已截止';
    }
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((diff % (1000 * 60)) / 1000);
    if (days > 0) {
      return `${days}天${hours}时${minutes}分`;
    } else if (hours > 0) {
      return `${hours}时${minutes}分${seconds}秒`;
    } else {
      return `${minutes}分${seconds}秒`;
    }
  }

  function getCountdownClass(activity) {
    const diff = countdownMap.value[activity.id];
    if (diff === undefined || diff === null) {
      return '';
    }
    if (diff <= 0) {
      return 'countdown-expired';
    }
    if (diff < 1000 * 60 * 60) {
      return 'countdown-warning';
    }
    return 'countdown-normal';
  }

  function showContentViewer(activity) {
    currentActivity.value = activity;
    contentViewerVisible.value = true;
  }

  async function viewStudentDetail(student) {
    try {
      const res = await classActivitySubmitApi.loadStudentActivityDetail(selectedActivity.value.id, student.studentId);
      studentDetailData.value = res.data || null;
      currentStudentSubmission.value = student;
      studentDetailVisible.value = true;
    } catch (e) {
      smartSentry.captureError(e);
      message.error('加载学生详情失败');
    }
  }

  function getRowClassName(record) {
    return record.submitted ? 'submitted-row' : '';
  }

  async function loadCourseInfo() {
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadSessions() {
    loading.value = true;
    try {
      const res = await postRequest('/manage/smartmind/classsession/loadDataSet', {
        courseId: Number(courseId.value),
        pageNum: 1,
        pageSize: 100,
        sortField: 'create_time',
        sortOrder: 'desc',
      });
      const data = res.data;
      if (Array.isArray(data)) {
        sessionList.value = data;
      } else if (data && Array.isArray(data.rows)) {
        sessionList.value = data.rows;
      } else if (data && Array.isArray(data.list)) {
        sessionList.value = data.list;
      } else if (data && data.records && Array.isArray(data.records)) {
        sessionList.value = data.records;
      } else {
        sessionList.value = [];
      }
      if (route.query.sessionId && sessionList.value.length > 0) {
        const targetSession = sessionList.value.find(s => s.id == route.query.sessionId);
        if (targetSession) {
          selectedSession.value = targetSession;
          loadActivities(targetSession.id);
        }
      }
    } catch (e) {
      smartSentry.captureError(e);
      sessionList.value = [];
    } finally {
      loading.value = false;
    }
  }

  async function loadUnits() {
    try {
      const res = await postRequest(`/manage/smartmind/learningunit/queryByCourseId/${courseId.value}`);
      const units = res.data || [];
      unitOptions.value = units;
      unitTreeData.value = buildUnitTree(units);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadClazzes() {
    try {
      const res = await classCourseApi.queryCourseClass(courseId.value);
      const data = res.data || [];
      if (Array.isArray(data)) {
        clazzOptions.value = data;
      } else {
        clazzOptions.value = [];
      }
    } catch (e) {
      smartSentry.captureError(e);
      clazzOptions.value = [];
    }
  }

  async function loadActivities(sessionId) {
    try {
      const res = await getRequest(`/manage/smartmind/classactivity/queryBySessionId/${sessionId}`);
      activityList.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function selectSession(session) {
    selectedSession.value = session;
    router.replace({
      query: { ...route.query, sessionId: session.id },
    });
    loadActivities(session.id);
  }

  function selectActivity(activity) {
    selectedActivity.value = activity;
    loadActivityStats(activity.id);
    loadStudentSubmissions(activity.id);
  }

  async function loadActivityStats(activityId) {
    try {
      const res = await postRequest('/manage/smartmind/classactivitysubmit/getActivityStats', { activityId });
      const stats = res.data || {};
      activityStats.value = {
        totalStudents: stats.totalStudents || 0,
        submittedCount: stats.submittedCount || 0,
        unsubmittedCount: stats.unsubmittedCount || 0,
        submitRate: stats.submitRate || 0,
      };
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function loadStudentSubmissions(activityId) {
    try {
      const res = await postRequest('/manage/smartmind/classactivitysubmit/getStudentSubmissions', { activityId });
      studentSubmissions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function refreshActivityData() {
    if (selectedActivity.value) {
      await loadActivityStats(selectedActivity.value.id);
      await loadStudentSubmissions(selectedActivity.value.id);
      message.success('数据已刷新');
    }
  }

  function viewActivityDetail() {
    if (selectedActivity.value) {
      router.push({
        path: '/smartmind/class-activity-detail',
        query: { activityId: selectedActivity.value.id },
      });
    }
  }

  function showCreateModal() {
    createModalVisible.value = true;
  }

  function showQuizModal() {
    if (!selectedSession.value) {
      message.warning('请先选择课堂');
      return;
    }
    quizModalVisible.value = true;
  }

  function showExerciseModal() {
    if (!selectedSession.value) {
      message.warning('请先选择课堂');
      return;
    }
    exerciseModalVisible.value = true;
  }

  function showExperimentModal() {
    if (!selectedSession.value) {
      message.warning('请先选择课堂');
      return;
    }
    experimentModalVisible.value = true;
  }

  function showDiscussionModal() {
    if (!selectedSession.value) {
      message.warning('请先选择课堂');
      return;
    }
    discussionModalVisible.value = true;
  }

  function editActivity(activity) {
    editingActivityId.value = activity.id;
    editActivityModalVisible.value = true;
  }

  function viewActivity(activity) {
    selectedActivity.value = activity;
    loadActivityStats(activity.id);
    loadStudentSubmissions(activity.id);
  }

  async function startActivity(activity) {
    try {
      await postRequest(`/manage/smartmind/classactivity/startActivity/${activity.id}`);
      message.success('活动已开始');
      loadActivities(selectedSession.value.id);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function stopActivity(activity) {
    try {
      await postRequest('/manage/smartmind/classactivity/setStatus', {
        id: activity.id,
        status: '2',
      });
      message.success('活动已停止');
      loadActivities(selectedSession.value.id);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function cancelActivity(activity) {
    try {
      await postRequest('/manage/smartmind/classactivity/setStatus', {
        id: activity.id,
        status: '3',
      });
      message.success('活动已取消');
      loadActivities(selectedSession.value.id);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  async function deleteActivity(activity) {
    try {
      await classActivityApi.delete(activity.id);
      message.success('删除成功');
      loadActivities(selectedSession.value.id);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function getStatusClass(status) {
    const map = { '0': 'status-not-started', '1': 'status-in-progress', '2': 'status-finished', '3': 'status-cancelled' };
    return map[status] || '';
  }

  function getActivityStatusClass(status) {
    return getStatusClass(status);
  }

  function getActivityTypeColor(type) {
    const map = { 'quiz': 'primary', 'exercise': 'success', 'experiment': 'warning', 'discussion': 'purple' };
    return map[type] || 'default';
  }

  function formatDateTime(dateStr) {
    if (!dateStr) return '';
    return dayjs(dateStr).format('MM-DD HH:mm');
  }

  function buildUnitTree(units) {
    const map = {};
    const roots = [];

    units.forEach(unit => {
      const key = `unit-${unit.id}`;
      map[unit.id] = {
        key: key,
        title: unit.unitName,
        value: key,
        children: [],
        data: unit,
      };
    });

    units.forEach(unit => {
      const node = map[unit.id];
      if (unit.parentId && map[unit.parentId]) {
        map[unit.parentId].children.push(node);
      } else {
        roots.push(node);
      }
    });

    return roots;
  }

  function parseUnitsInfo(unitsInfo) {
    try {
      return JSON.parse(unitsInfo) || [];
    } catch {
      return [];
    }
  }
</script>

<style scoped lang="less">
@import '/@/styles/glass-theme.less';

.class-session-page {
  min-height: 100vh;
  padding: 24px;
}

.glass-header {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 20px 24px;
  margin-bottom: 20px;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .title-section {
    display: flex;
    align-items: center;
    gap: 16px;

    .title-icon {
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 24px;
      flex-shrink: 0;
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

  .btn-create {
    height: 36px;
    padding: 0 20px;
    border-radius: 8px;
    font-weight: 500;
    font-size: 14px;
  }
}

.session-content-area {
  display: flex;
  gap: 20px;
  min-height: calc(100vh - 160px);
}

.session-sidebar {
  width: 280px;
  flex-shrink: 0;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 160px);

  .sidebar-header {
    padding: 16px 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.06);
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #1a1a2e;

    .session-count {
      margin-left: auto;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      font-size: 12px;
      padding: 2px 8px;
      border-radius: 10px;
      font-weight: 500;
    }
  }

  .session-list-container {
    flex: 1;
    overflow-y: auto;
    padding: 12px;
  }
}

.session-list-item {
  padding: 12px 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 6px;
  border: 1px solid transparent;

  &:hover {
    background: rgba(102, 126, 234, 0.08);
    border-color: rgba(102, 126, 234, 0.2);
  }

  &.active {
    background: rgba(102, 126, 234, 0.12);
    border-color: rgba(102, 126, 234, 0.3);
  }

  .session-item-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;

    .session-name {
      font-size: 13px;
      font-weight: 500;
      color: #1a1a2e;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin-right: 8px;
    }
  }

  .session-item-info {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 6px;

    .info-item {
      font-size: 11px;
      color: #6b7280;
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .session-item-units {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }
}

.session-main {
  flex: 1;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.main-placeholder {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;

  .main-header {
    padding: 16px 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.06);
    display: flex;
    align-items: center;

    h2 {
      font-size: 16px;
      font-weight: 600;
      color: #1a1a2e;
      margin: 0;
    }
  }

  .activity-type-bar {
    padding: 16px 20px;
    display: flex;
    gap: 16px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  }

  .activity-type-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    padding: 16px 24px;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.25s;
    min-width: 100px;

    .btn-icon {
      width: 44px;
      height: 44px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      color: #fff;
      transition: transform 0.25s;
    }

    .btn-label {
      font-size: 13px;
      font-weight: 500;
      color: #4b5563;
    }

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);

      .btn-icon {
        transform: scale(1.1);
      }
    }

    &.quiz {
      background: rgba(102, 126, 234, 0.08);

      .btn-icon {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &:hover {
        background: rgba(102, 126, 234, 0.15);
      }
    }

    &.exercise {
      background: rgba(82, 196, 26, 0.08);

      .btn-icon {
        background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
      }

      &:hover {
        background: rgba(82, 196, 26, 0.15);
      }
    }

    &.experiment {
      background: rgba(24, 144, 255, 0.08);

      .btn-icon {
        background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
      }

      &:hover {
        background: rgba(24, 144, 255, 0.15);
      }
    }

    &.discussion {
      background: rgba(250, 173, 20, 0.08);

      .btn-icon {
        background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
      }

      &:hover {
        background: rgba(250, 173, 20, 0.15);
      }
    }
  }

  .main-body {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }
}

.activity-section {
  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #1a1a2e;
    margin-bottom: 14px;
  }

  .activity-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;
  }
}

.activity-card {
  &.active {
    border-color: rgba(102, 126, 234, 0.5);
    background: rgba(102, 126, 234, 0.08);
  }

  .card-header {
    .tag {
      padding: 6px 14px;
      background: linear-gradient(135deg, rgba(79, 172, 254, 0.1), rgba(102, 126, 234, 0.05));
      border: 1px solid rgba(79, 172, 254, 0.2);
      border-radius: 20px;
      font-size: 14px;
      color: #667eea;
      font-weight: 600;
    }

    .status-tag {
      border-radius: 20px;
      font-size: 13px;
      padding: 4px 12px;
      height: 26px;
      line-height: 18px;
      border: none;

      &.status-not-started {
        background: rgba(255, 167, 38, 0.1);
        color: #d97706;
      }

      &.status-in-progress {
        background: rgba(67, 233, 123, 0.1);
        color: #059669;
      }

      &.status-finished {
        background: rgba(160, 174, 192, 0.1);
        color: #64748b;
      }

      &.status-cancelled {
        background: rgba(255, 0, 0, 0.05);
        color: #dc2626;
      }
    }
  }

  .card-body {
    .code {
      font-family: 'SFMono-Regular', monospace;
      font-size: 13px;
      color: #a0aec0;
      margin-bottom: 8px;
      letter-spacing: 1px;
    }

    .card-title {
      font-size: 20px;
      font-weight: 600;
      color: #2d3748;
      margin: 0 0 16px 0;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .meta-row {
      display: flex;
      gap: 20px;
      margin-bottom: 12px;

      .meta-item {
        font-size: 15px;
        color: #718096;
        display: flex;
        align-items: center;
        gap: 6px;

        :deep(.anticon) {
          font-size: 16px;
          color: #4facfe;
        }
      }
    }

    .time-rows {
      flex-direction: column;
      gap: 6px;
      margin-bottom: 10px;

      .time-item {
        font-size: 16px;
        color: #718096;
        display: flex;
        align-items: center;
        gap: 6px;

        :deep(.anticon) {
          font-size: 14px;
          color: #667eea;
        }
      }

      .countdown-item {
        font-weight: 600;
        font-size: 14px;

        :deep(.anticon) {
          color: #ff6b9d;
        }
      }

      .countdown-normal {
        color: #059669;

        :deep(.anticon) {
          color: #059669;
        }
      }

      .countdown-warning {
        color: #d97706;

        :deep(.anticon) {
          color: #d97706;
        }
      }

      .countdown-expired {
        color: #dc2626;

        :deep(.anticon) {
          color: #dc2626;
        }
      }
    }

    .activity-stats {
      margin-top: 10px;
      display: flex;
      gap: 16px;
      flex-wrap: nowrap;

      .stat-item {
        font-size: 13px;
        color: #667eea;
        display: flex;
        align-items: center;
        gap: 6px;
        white-space: nowrap;
      }
    }
  }

  .card-actions {
    margin-top: 5px !important;
    padding-top: 5px !important;

    .action-btn {
      min-width: 44px;
      width: 44px;
      height: 44px;
      padding: 0;

      .btn-text {
        display: none;
      }

      :deep(.anticon) {
        font-size: 20px;
      }

      &.start:hover {
        background: rgba(67, 233, 123, 0.1);
        color: #059669;
      }

      &.stop:hover {
        background: rgba(255, 0, 0, 0.05);
        color: #dc2626;
      }

      &.cancel:hover {
        background: rgba(255, 0, 0, 0.05);
        color: #dc2626;
      }
    }
  }
}

.activity-detail-section {
  margin-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  padding-top: 20px;

  .detail-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    h3 {
      font-size: 15px;
      font-weight: 600;
      color: #1a1a2e;
      margin: 0;
    }

    .detail-actions {
      display: flex;
      gap: 8px;
    }
  }

  .detail-content {
    .stats-cards {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 12px;
      margin-bottom: 20px;

      .stat-card {
        background: rgba(255, 255, 255, 0.6);
        border-radius: 10px;
        padding: 14px;
        display: flex;
        align-items: center;
        gap: 10px;
        border: 1px solid rgba(0, 0, 0, 0.06);

        .stat-icon {
          width: 40px;
          height: 40px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 18px;
          color: #fff;
          flex-shrink: 0;

          &.total {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.submitted {
            background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
          }

          &.unsubmitted {
            background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
          }

          &.rate {
            background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
          }
        }

        .stat-info {
          .stat-value {
            font-size: 20px;
            font-weight: 700;
            color: #1a1a2e;
            line-height: 1.2;
          }

          .stat-label {
            font-size: 11px;
            color: #6b7280;
            margin-top: 2px;
          }
        }
      }
    }

    .progress-section {
      margin-bottom: 20px;

      .progress-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;
        font-size: 13px;
        color: #1a1a2e;

        .progress-text {
          font-size: 12px;
          color: #6b7280;
        }
      }
    }

    .student-list-section {
      .section-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 12px;

        .section-subtitle {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 13px;
          font-weight: 600;
          color: #1a1a2e;
          margin-bottom: 0;
        }

        .student-search-input {
          width: 200px;

          :deep(.ant-input-affix-wrapper) {
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.7);
            border: 1px solid rgba(102, 126, 234, 0.2);
            padding: 0 12px;
            box-shadow: none;
            transition: all 0.3s ease;
          }

          :deep(.ant-input-affix-wrapper .ant-input) {
            font-size: 12px;
            background: transparent;
            border: none;
            box-shadow: none;
            padding: 4px 0;
          }

          :deep(.ant-input-affix-wrapper .ant-input:focus),
          :deep(.ant-input-affix-wrapper .ant-input:hover) {
            border: none;
            box-shadow: none;
          }

          :deep(.ant-input-affix-wrapper:hover),
          :deep(.ant-input-affix-wrapper:focus),
          :deep(.ant-input-affix-wrapper-focused) {
            border-color: rgba(102, 126, 234, 0.4);
            background: rgba(255, 255, 255, 0.85);
            box-shadow: 0 2px 8px rgba(102, 126, 234, 0.15);
          }

          :deep(.ant-input-prefix) {
            color: #667eea;
            margin-right: 6px;
          }

          :deep(.ant-input-suffix) {
            color: #9ca3af;
          }
        }
      }

      .section-subtitle {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 13px;
        font-weight: 600;
        color: #1a1a2e;
        margin-bottom: 12px;
      }

      :deep(.ant-table-wrapper) {
        background: rgba(255, 255, 255, 0.65);
        border: 1px solid rgba(255, 255, 255, 0.4);
        border-radius: 12px;
        backdrop-filter: blur(20px);
        box-shadow: 0 8px 32px rgba(31, 38, 135, 0.1);
        overflow: hidden;
      }

      :deep(.ant-table) {
        background: transparent;
        font-family: inherit;
      }

      :deep(.ant-table-container) {
        border-radius: 12px;
      }

      :deep(.ant-table-thead > tr > th) {
        background: linear-gradient(135deg, rgba(102, 126, 234, 0.12) 0%, rgba(118, 75, 162, 0.12) 100%);
        font-size: 13px;
        font-weight: 600;
        color: #2d3748;
        border-bottom: 2px solid rgba(102, 126, 234, 0.2);
        padding: 14px 16px;
        text-align: center;
        white-space: nowrap;
      }

      :deep(.ant-table-thead > tr > th::before) {
        display: none;
      }

      :deep(.ant-table-tbody > tr > td) {
        font-size: 13px;
        color: #2d3748;
        border-bottom: 1px solid rgba(0, 0, 0, 0.04);
        padding: 14px 16px;
        text-align: center;
        background: transparent;
        vertical-align: middle;
      }

      :deep(.ant-table-tbody > tr:last-child > td) {
        border-bottom: none;
      }

      :deep(.ant-table-tbody > tr:hover > td) {
        background: rgba(102, 126, 234, 0.08) !important;
      }

      :deep(.ant-table-tbody > tr.submitted-row > td) {
        background: rgba(82, 196, 26, 0.06) !important;
      }

      :deep(.ant-table-tbody > tr.submitted-row:hover > td) {
        background: rgba(102, 126, 234, 0.1) !important;
      }

      :deep(.ant-table-tbody > tr > td:first-child) {
        text-align: left;
      }

      .student-name-cell {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        white-space: nowrap;
        line-height: 1;

        .student-avatar {
          width: 32px;
          height: 32px;
          min-width: 32px;
          border-radius: 50%;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 14px;
          font-weight: 600;
          flex-shrink: 0;
          box-shadow: 0 2px 8px rgba(102, 126, 234, 0.35);
        }

        span {
          display: inline-block;
          line-height: 1.4;
        }
      }
    }
  }
}

.create-form {
  padding-top: 8px;
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

.empty-sessions,
.loading-sessions {
  padding: 40px 0;
}
</style>
