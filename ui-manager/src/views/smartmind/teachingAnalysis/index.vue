<template>
  <div class="glass-page teaching-analysis-page">
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
            <BarChartOutlined />
          </div>
          <div class="title-text">
            <h1>教学分析</h1>
            <p>{{ courseInfo?.courseName || '加载中...' }}</p>
          </div>
        </div>
        <div class="header-actions">
          <a-radio-group v-model:value="analysisLevel" button-style="solid" size="small">
            <a-radio-button value="course">课程级分析</a-radio-button>
            <a-radio-button value="session">课堂级分析</a-radio-button>
          </a-radio-group>
        </div>
      </div>
    </header>

    <!-- 内容区域 -->
    <div class="analysis-content-area">
      <!-- 左侧：历史课堂列表 -->
      <div class="analysis-sidebar">
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
      <div class="analysis-main">
        <div class="main-placeholder" v-if="!selectedSession">
          <a-empty description="请在左侧选择课堂查看教学分析">
            <template #image>
              <BarChartOutlined style="font-size: 64px; color: #d1d5db;" />
            </template>
          </a-empty>
        </div>

        <!-- 课程级分析 -->
        <div class="main-content" v-else-if="analysisLevel === 'course'">
          <div class="main-header">
            <h2>{{ selectedSession.sessionName }} - 课程级分析</h2>
          </div>
          <div class="main-body">
            <!-- 课标达成度雷达图 -->
            <div class="analysis-section">
              <div class="section-title">
                <AimOutlined />
                <span>课标达成度分析</span>
              </div>
              <div class="radar-chart-container">
                <div ref="radarChartRef" class="echarts-radar"></div>
                <div class="radar-insights">
                  <div class="insight-card warning">
                    <WarningOutlined class="insight-icon" />
                    <div class="insight-content">
                      <strong>薄弱项：</strong>创新思维（45%）、应用能力（65%）
                    </div>
                  </div>
                  <div class="insight-card info">
                    <BulbOutlined class="insight-icon" />
                    <div class="insight-content">
                      <strong>建议：</strong>增加开放性编程实践，强化应用题训练
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 单元掌握度 -->
            <div class="analysis-section">
              <div class="section-title">
                <TrophyOutlined />
                <span>单元掌握度</span>
              </div>
              <div class="mastery-chart">
                <div ref="masteryChartRef" class="echarts-mastery"></div>
              </div>
            </div>

            <!-- 薄弱知识点 -->
            <div class="analysis-section">
              <div class="section-title">
                <ExclamationCircleOutlined />
                <span>薄弱知识点分析</span>
              </div>
              <div class="weak-points">
                <div ref="weakPointsChartRef" class="echarts-weak"></div>
              </div>
            </div>

            <!-- 资源使用分析 -->
            <div class="analysis-section">
              <div class="section-title">
                <PieChartOutlined />
                <span>资源使用分析</span>
              </div>
              <div ref="resourceChartRef" class="echarts-resource"></div>
            </div>
          </div>
        </div>

        <!-- 课堂级分析 -->
        <div class="main-content" v-else>
          <div class="main-header">
            <h2>{{ selectedSession.sessionName }} - 课堂级分析</h2>
          </div>
          <div class="main-body">
            <!-- 课堂参与统计 -->
            <div class="stats-cards-row">
              <div class="stat-card">
                <div class="stat-icon participation">
                  <UsergroupAddOutlined />
                </div>
                <div class="stat-info">
                  <div class="stat-value">1人</div>
                  <div class="stat-label">参与人数</div>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon accuracy">
                  <CheckCircleOutlined />
                </div>
                <div class="stat-info">
                  <div class="stat-value">72%</div>
                  <div class="stat-label">平均正确率</div>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon active">
                  <ThunderboltOutlined />
                </div>
                <div class="stat-info">
                  <div class="stat-value">1人</div>
                  <div class="stat-label">活跃学生</div>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon attention">
                  <EyeOutlined />
                </div>
                <div class="stat-info">
                  <div class="stat-value">0人</div>
                  <div class="stat-label">待关注</div>
                </div>
              </div>
            </div>

            <!-- 答题实时统计 -->
            <div class="analysis-section">
              <div class="section-title">
                <FileTextOutlined />
                <span>答题统计</span>
              </div>
              <div ref="answerStatsChartRef" class="echarts-answer-stats"></div>
            </div>

            <!-- 异常提醒 -->
                    <div class="analysis-section">
                      <div class="section-title">
                        <WarningOutlined />
                        <span>异常提醒</span>
                      </div>
                      <div class="alert-cards">
                        <div class="alert-card warning">
                          <div class="alert-icon">
                            <ClockCircleOutlined />
                          </div>
                          <div class="alert-content">
                            <div class="alert-title">答题进度</div>
                            <div class="alert-desc">第3题正确率较低，建议重点讲解</div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- 高质量推荐 -->
                    <div class="analysis-section">
                      <div class="section-title">
                        <StarOutlined />
                        <span>优秀表现</span>
                      </div>
                      <div class="alert-card success">
                        <div class="alert-icon">
                          <CheckCircleOutlined />
                        </div>
                        <div class="alert-content">
                          <div class="alert-title">课堂表现</div>
                          <div class="alert-desc">按时完成所有答题，实验报告完整</div>
                        </div>
                      </div>
                    </div>

            <!-- 能力跃迁检测 -->
            <!-- <div class="analysis-section">
              <div class="section-title">
                <LineChartOutlined />
                <span>能力跃迁检测</span>
              </div>
              <div class="ability-charts-row">
                <div class="ability-chart">
                  <div class="chart-label">学生A - 正常曲线</div>
                  <div ref="abilityChartARef" class="echarts-ability"></div>
                </div>
                <div class="ability-chart">
                  <div class="chart-label">学生B - 可疑跃迁</div>
                  <div ref="abilityChartBRef" class="echarts-ability"></div>
                </div>
              </div>
            </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted, nextTick, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { postRequest } from '/@/lib/axios';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import * as echarts from 'echarts';
  import {
    BarChartOutlined,
    HistoryOutlined,
    TeamOutlined,
    ClockCircleOutlined,
    FieldTimeOutlined,
    AimOutlined,
    TrophyOutlined,
    ExclamationCircleOutlined,
    PieChartOutlined,
    UsergroupAddOutlined,
    CheckCircleOutlined,
    ThunderboltOutlined,
    EyeOutlined,
    FileTextOutlined,
    WarningOutlined,
    BulbOutlined,
    StarOutlined,
    LineChartOutlined,
    CloseCircleOutlined,
  } from '@ant-design/icons-vue';
  import dayjs from 'dayjs';
  import { courseApi } from '/@/api/smartmind/course-api.js';

  const route = useRoute();
  const router = useRouter();
  const dictStore = useDictStore();

  const courseId = ref(null);
  const courseInfo = ref(null);
  const sessionList = ref([]);
  const selectedSession = ref(null);
  const loading = ref(false);
  const analysisLevel = ref('course');

  const radarChartRef = ref(null);
  const masteryChartRef = ref(null);
  const weakPointsChartRef = ref(null);
  const resourceChartRef = ref(null);
  const answerStatsChartRef = ref(null);
  const abilityChartARef = ref(null);
  const abilityChartBRef = ref(null);

  onMounted(() => {
    courseId.value = route.query.courseId;
    if (courseId.value) {
      loadCourseInfo();
      loadSessions();
    }
  });

  watch([() => selectedSession.value, () => analysisLevel.value], () => {
    if (selectedSession.value) {
      nextTick(() => {
        initCharts();
      });
    }
  });

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
        }
      }
    } catch (e) {
      smartSentry.captureError(e);
      sessionList.value = [];
    } finally {
      loading.value = false;
    }
  }

  function selectSession(session) {
    selectedSession.value = session;
    router.replace({
      query: { ...route.query, sessionId: session.id },
    });
  }

  function getStatusClass(status) {
    const map = { '0': 'status-not-started', '1': 'status-in-progress', '2': 'status-finished', '3': 'status-cancelled' };
    return map[status] || '';
  }

  function formatDateTime(dateStr) {
    if (!dateStr) return '';
    return dayjs(dateStr).format('MM-DD HH:mm');
  }

  function parseUnitsInfo(unitsInfo) {
    try {
      return JSON.parse(unitsInfo) || [];
    } catch {
      return [];
    }
  }

  function initCharts() {
    if (analysisLevel.value === 'course') {
      initRadarChart();
      initMasteryChart();
      initWeakPointsChart();
      initResourceChart();
    } else {
      initAnswerStatsChart();
      initAbilityCharts();
    }
  }

  function initRadarChart() {
    if (!radarChartRef.value) return;
    const chart = echarts.init(radarChartRef.value);
    const option = {
      tooltip: {},
      radar: {
        indicator: [
          { name: '知识理解', max: 100 },
          { name: '问题解决', max: 100 },
          { name: '应用能力', max: 100 },
          { name: '创新思维', max: 100 },
          { name: '协作交流', max: 100 },
          { name: '实践操作', max: 100 },
        ],
        radius: '65%',
        axisName: {
          fontSize: 12,
          color: '#4b5563',
        },
        splitArea: {
          areaStyle: {
            color: ['rgba(102, 126, 234, 0.05)', 'rgba(102, 126, 234, 0.1)'],
          },
        },
      },
      series: [{
        type: 'radar',
        data: [{
          value: [85, 70, 65, 45, 78, 72],
          name: '班级达成度',
          areaStyle: {
            color: 'rgba(102, 126, 234, 0.3)',
          },
          lineStyle: {
            color: '#667eea',
            width: 2,
          },
          itemStyle: {
            color: '#667eea',
          },
        }],
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initMasteryChart() {
    if (!masteryChartRef.value) return;
    const chart = echarts.init(masteryChartRef.value);
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        data: ['细胞结构', '光合作用', '遗传规律', '生态系统', '进化理论', '分子生物学'],
        axisLabel: { fontSize: 11 },
      },
      yAxis: {
        type: 'value',
        max: 100,
        axisLabel: { formatter: '{value}%' },
      },
      series: [{
        type: 'bar',
        data: [
          { value: 82, itemStyle: { color: '#52c41a' } },
          { value: 75, itemStyle: { color: '#52c41a' } },
          { value: 68, itemStyle: { color: '#faad14' } },
          { value: 45, itemStyle: { color: '#ff4d4f' } },
          { value: 71, itemStyle: { color: '#faad14' } },
          { value: 88, itemStyle: { color: '#52c41a' } },
        ],
        barWidth: '60%',
        label: {
          show: true,
          position: 'top',
          formatter: '{c}%',
          fontSize: 11,
        },
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initWeakPointsChart() {
    if (!weakPointsChartRef.value) return;
    const chart = echarts.init(weakPointsChartRef.value);
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'value',
        max: 100,
        axisLabel: { formatter: '{value}%' },
      },
      yAxis: {
        type: 'category',
        data: ['减数分裂', 'DNA复制', '基因表达', '蛋白质合成', '细胞呼吸'],
        axisLabel: { fontSize: 11 },
      },
      series: [{
        type: 'bar',
        data: [
          { value: 35, itemStyle: { color: '#ff4d4f' } },
          { value: 42, itemStyle: { color: '#ff4d4f' } },
          { value: 58, itemStyle: { color: '#faad14' } },
          { value: 65, itemStyle: { color: '#faad14' } },
          { value: 72, itemStyle: { color: '#52c41a' } },
        ],
        barWidth: '60%',
        label: {
          show: true,
          position: 'right',
          formatter: '{c}%',
          fontSize: 11,
        },
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initResourceChart() {
    if (!resourceChartRef.value) return;
    const chart = echarts.init(resourceChartRef.value);
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}次 ({d}%)',
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { fontSize: 11 },
      },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2,
        },
        label: {
          show: false,
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold',
          },
        },
        data: [
          { value: 156, name: '课件查看', itemStyle: { color: '#667eea' } },
          { value: 89, name: '实验操作', itemStyle: { color: '#52c41a' } },
          { value: 67, name: '题库练习', itemStyle: { color: '#faad14' } },
          { value: 45, name: '话题讨论', itemStyle: { color: '#1890ff' } },
          { value: 23, name: '教案查看', itemStyle: { color: '#722ed1' } },
        ],
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initAnswerStatsChart() {
    if (!answerStatsChartRef.value) return;
    const chart = echarts.init(answerStatsChartRef.value);
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        formatter: function(params) {
          const item = params.find(p => p.value === 1);
          return item ? `${item.name}<br/>选择：${item.seriesName}` : '';
        },
      },
      legend: {
        data: ['A', 'B', 'C', 'D'],
        bottom: 0,
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '10%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        data: ['第1题', '第2题', '第3题', '第4题', '第5题'],
      },
      yAxis: {
        type: 'value',
        max: 1,
        axisLabel: { formatter: '{value}' },
      },
      series: [
        {
          name: 'A',
          type: 'bar',
          stack: 'total',
          data: [0, 0, 1, 0, 0],
          itemStyle: { color: '#ff4d4f' },
          label: { show: true, position: 'inside', formatter: function(p) { return p.value === 1 ? '✓' : ''; }, fontSize: 14, fontWeight: 'bold' },
        },
        {
          name: 'B',
          type: 'bar',
          stack: 'total',
          data: [1, 0, 0, 1, 0],
          itemStyle: { color: '#52c41a' },
          label: { show: true, position: 'inside', formatter: function(p) { return p.value === 1 ? '✓' : ''; }, fontSize: 14, fontWeight: 'bold' },
        },
        {
          name: 'C',
          type: 'bar',
          stack: 'total',
          data: [0, 1, 0, 0, 1],
          itemStyle: { color: '#faad14' },
          label: { show: true, position: 'inside', formatter: function(p) { return p.value === 1 ? '✓' : ''; }, fontSize: 14, fontWeight: 'bold' },
        },
        {
          name: 'D',
          type: 'bar',
          stack: 'total',
          data: [0, 0, 0, 0, 0],
          itemStyle: { color: '#1890ff' },
          label: { show: true, position: 'inside', formatter: function(p) { return p.value === 1 ? '✓' : ''; }, fontSize: 14, fontWeight: 'bold' },
        },
      ],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initAbilityCharts() {
    initAbilityChartA();
    initAbilityChartB();
  }

  function initAbilityChartA() {
    if (!abilityChartARef.value) return;
    const chart = echarts.init(abilityChartARef.value);
    const option = {
      tooltip: { trigger: 'axis' },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['第1周', '第2周', '第3周', '第4周', '第5周', '第6周'],
      },
      yAxis: {
        type: 'value',
        name: '质量评分',
        max: 100,
      },
      series: [{
        type: 'line',
        data: [65, 68, 72, 78, 75, 80],
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.05)' },
          ]),
        },
        lineStyle: { color: '#667eea', width: 3 },
        itemStyle: { color: '#667eea' },
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }

  function initAbilityChartB() {
    if (!abilityChartBRef.value) return;
    const chart = echarts.init(abilityChartBRef.value);
    const option = {
      tooltip: { trigger: 'axis' },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['第1周', '第2周', '第3周', '第4周', '第5周', '第6周'],
      },
      yAxis: {
        type: 'value',
        name: '质量评分',
        max: 100,
      },
      series: [{
        type: 'line',
        data: [55, 58, 60, 62, 92, 95],
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(255, 77, 79, 0.3)' },
            { offset: 1, color: 'rgba(255, 77, 79, 0.05)' },
          ]),
        },
        lineStyle: { color: '#ff4d4f', width: 3 },
        itemStyle: { color: '#ff4d4f' },
        markPoint: {
          data: [
            { type: 'max', name: '可疑跃迁点' },
          ],
          itemStyle: { color: '#ff4d4f' },
          label: { formatter: 'AI代写嫌疑', fontSize: 10 },
        },
      }],
    };
    chart.setOption(option);
    window.addEventListener('resize', () => chart.resize());
  }
</script>

<style scoped lang="less">
@import '/@/styles/glass-theme.less';

.teaching-analysis-page {
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
}

.analysis-content-area {
  display: flex;
  gap: 20px;
  min-height: calc(100vh - 160px);
}

.analysis-sidebar {
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

.analysis-main {
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

  .main-body {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }
}

.analysis-section {
  margin-bottom: 24px;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #1a1a2e;
    margin-bottom: 14px;

    :deep(.anticon) {
      color: #667eea;
      font-size: 16px;
    }
  }
}

.radar-chart-container {
  display: flex;
  gap: 20px;
  align-items: flex-start;

  .echarts-radar {
    width: 400px;
    height: 350px;
    flex-shrink: 0;
  }

  .radar-insights {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
}

.insight-card {
  padding: 12px 16px;
  border-radius: 8px;
  display: flex;
  align-items: flex-start;
  gap: 10px;

  .insight-icon {
    font-size: 18px;
    flex-shrink: 0;
    margin-top: 2px;
  }

  .insight-content {
    font-size: 13px;
    line-height: 1.6;

    strong {
      font-weight: 600;
    }
  }

  &.warning {
    background: rgba(255, 77, 79, 0.08);
    border: 1px solid rgba(255, 77, 79, 0.2);

    .insight-icon {
      color: #ff4d4f;
    }

    .insight-content {
      color: #cf1322;
    }
  }

  &.info {
    background: rgba(102, 126, 234, 0.08);
    border: 1px solid rgba(102, 126, 234, 0.2);

    .insight-icon {
      color: #667eea;
    }

    .insight-content {
      color: #1a1a2e;
    }
  }
}

.mastery-chart {
  .echarts-mastery {
    width: 100%;
    height: 300px;
  }
}

.weak-points {
  .echarts-weak {
    width: 100%;
    height: 250px;
  }
}

.echarts-resource {
  width: 100%;
  height: 300px;
}

.stats-cards-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;

  .stat-card {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 16px;
    display: flex;
    align-items: center;
    gap: 12px;
    border: 1px solid rgba(0, 0, 0, 0.06);

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      color: #fff;
      flex-shrink: 0;

      &.participation {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.accuracy {
        background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
      }

      &.active {
        background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
      }

      &.attention {
        background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
      }
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
        margin-top: 4px;
      }
    }
  }
}

.echarts-answer-stats {
  width: 100%;
  height: 350px;
}

.alert-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.alert-card {
  padding: 14px 16px;
  border-radius: 10px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  border: 1px solid;

  .alert-icon {
    font-size: 20px;
    flex-shrink: 0;
    margin-top: 2px;
  }

  .alert-content {
    flex: 1;

    .alert-title {
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 4px;
    }

    .alert-desc {
      font-size: 13px;
      line-height: 1.5;
    }
  }

  &.error {
    background: rgba(255, 77, 79, 0.08);
    border-color: rgba(255, 77, 79, 0.2);

    .alert-icon {
      color: #ff4d4f;
    }

    .alert-title {
      color: #cf1322;
    }

    .alert-desc {
      color: #820014;
    }
  }

  &.warning {
    background: rgba(250, 173, 20, 0.08);
    border-color: rgba(250, 173, 20, 0.2);

    .alert-icon {
      color: #faad14;
    }

    .alert-title {
      color: #d48806;
    }

    .alert-desc {
      color: #876800;
    }
  }

  &.success {
    background: rgba(82, 196, 26, 0.08);
    border-color: rgba(82, 196, 26, 0.2);

    .alert-icon {
      color: #52c41a;
    }

    .alert-title {
      color: #389e0d;
    }

    .alert-desc {
      color: #135200;
    }
  }
}

.ability-charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;

  .ability-chart {
    .chart-label {
      font-size: 13px;
      font-weight: 600;
      color: #1a1a2e;
      margin-bottom: 8px;
    }

    .echarts-ability {
      width: 100%;
      height: 250px;
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

.empty-sessions,
.loading-sessions {
  padding: 40px 0;
}
</style>
