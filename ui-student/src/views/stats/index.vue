<template>
  <div class="page stats-page">
    <PageHeader title="学习统计" />

    <div class="stats-overview">
      <div class="overview-card card-main">
        <div class="card-header">
          <span class="card-label">综合得分</span>
          <span class="card-badge">优秀</span>
        </div>
        <div class="score-circle">
          <svg viewBox="0 0 100 100" class="circle-svg">
            <circle cx="50" cy="50" r="40" class="circle-bg" />
            <circle cx="50" cy="50" r="40" class="circle-progress" :style="{ strokeDashoffset: circleOffset }" />
          </svg>
          <div class="score-text">
            <span class="score-num">{{ overallScore }}</span>
            <span class="score-unit">分</span>
          </div>
        </div>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-item">
        <div class="stat-icon icon-courses">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 016.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/>
          </svg>
        </div>
        <span class="stat-value">{{ totalCourses }}</span>
        <span class="stat-label">在学课程</span>
      </div>
      <div class="stat-item">
        <div class="stat-icon icon-activities">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12 6 12 12 16 14"/>
          </svg>
        </div>
        <span class="stat-value">{{ totalActivities }}</span>
        <span class="stat-label">参与活动</span>
      </div>
      <div class="stat-item">
        <div class="stat-icon icon-completed">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <span class="stat-value">{{ completedActivities }}</span>
        <span class="stat-label">已完成</span>
      </div>
      <div class="stat-item">
        <div class="stat-icon icon-accuracy">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 20V4M12 4l-4 4M12 4l4 4"/>
          </svg>
        </div>
        <span class="stat-value">{{ accuracyRate }}%</span>
        <span class="stat-label">正确率</span>
      </div>
    </div>

    <div class="section-card">
      <h2 class="section-title">题型表现</h2>
      <div class="type-bars">
        <div v-for="item in typeStats" :key="item.type" class="type-bar">
          <div class="bar-info">
            <span class="bar-name">{{ item.name }}</span>
            <span class="bar-value">{{ item.accuracy }}%</span>
          </div>
          <div class="bar-track">
            <div class="bar-fill" :style="{ width: item.accuracy + '%', background: item.color }"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="section-card">
      <h2 class="section-title">最近7天学习时长</h2>
      <div class="week-chart">
        <div v-for="day in weekData" :key="day.date" class="day-col">
          <div class="day-bar-wrapper">
            <div class="day-bar" :style="{ height: day.height + 'px' }"></div>
          </div>
          <span class="day-value">{{ day.minutes }}分钟</span>
          <span class="day-label">{{ day.label }}</span>
        </div>
      </div>
    </div>

    <div class="section-card">
      <h2 class="section-title">课程完成度</h2>
      <div class="course-progress-list">
        <div v-for="course in courseProgress" :key="course.id" class="progress-item">
          <div class="progress-info">
            <span class="course-name">{{ course.name }}</span>
            <span class="progress-percent">{{ course.progress }}%</span>
          </div>
          <div class="progress-track">
            <div class="progress-fill" :style="{ width: course.progress + '%' }"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '@/api'
import PageHeader from '@/components/PageHeader.vue'

const overallScore = ref(0)
const totalCourses = ref(0)
const totalActivities = ref(0)
const completedActivities = ref(0)
const accuracyRate = ref(0)
const loading = ref(false)

const typeStats = ref([])
const weekData = ref([])
const courseProgress = ref([])

const circleOffset = computed(() => {
  const circumference = 2 * Math.PI * 40
  return circumference - (overallScore.value / 100) * circumference
})

onMounted(() => {
  loadStatsData()
})

async function loadStatsData() {
  try {
    loading.value = true
    const [overviewRes, typeRes, weekRes, courseRes] = await Promise.all([
      api.getStatsOverview(),
      api.getTypeStats(),
      api.getWeekStudyTime(),
      api.getCourseProgress(),
    ])

    const overview = overviewRes.data || {}
    overallScore.value = overview.overallScore || 0
    totalCourses.value = overview.totalCourses || 0
    totalActivities.value = overview.totalActivities || 0
    completedActivities.value = overview.completedActivities || 0
    accuracyRate.value = overview.accuracyRate || 0

    const typeData = typeRes.data || []
    const typeColors = [
      'linear-gradient(90deg, #667eea, #764ba2)',
      'linear-gradient(90deg, #4facfe, #00f2fe)',
      'linear-gradient(90deg, #43e97b, #38f9d7)',
      'linear-gradient(90deg, #fa709a, #fee140)',
      'linear-gradient(90deg, #a18cd1, #fbc2eb)',
    ]
    typeStats.value = typeData.map((item, index) => ({
      type: item.type || '',
      name: item.typeName || item.type || '未知',
      accuracy: item.accuracy || 0,
      color: typeColors[index % typeColors.length],
    }))

    const weekRaw = weekRes.data || []
    const maxMinutes = Math.max(...weekRaw.map(d => d.studyMinutes || 0), 1)
    weekData.value = weekRaw.map(item => ({
      date: item.date || '',
      label: item.dayLabel || '',
      minutes: item.studyMinutes || 0,
      height: Math.max((item.studyMinutes || 0) / maxMinutes * 120, 4),
    }))

    const courseRaw = courseRes.data || []
    courseProgress.value = courseRaw.map(item => ({
      id: item.id || 0,
      name: item.name || '',
      progress: item.progress || 0,
    }))
  } catch (e) {
    console.error('加载统计数据失败', e)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 16px;
  padding-bottom: 80px;
  background: #f5f7fa;
}

.stats-overview {
  margin-bottom: 20px;
}

.overview-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 20px;
}

.card-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.card-badge {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
}

.score-circle {
  position: relative;
  width: 140px;
  height: 140px;
}

.circle-svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.circle-bg {
  fill: none;
  stroke: rgba(255, 255, 255, 0.2);
  stroke-width: 8;
}

.circle-progress {
  fill: none;
  stroke: #fff;
  stroke-width: 8;
  stroke-linecap: round;
  stroke-dasharray: 251.2;
  transition: stroke-dashoffset 0.5s ease;
}

.score-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.score-num {
  font-size: 40px;
  font-weight: 800;
  color: #fff;
  line-height: 1;
}

.score-unit {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-left: 2px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.stat-item {
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.stat-icon svg {
  width: 20px;
  height: 20px;
  color: #fff;
}

.icon-courses { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.icon-activities { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.icon-completed { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.icon-accuracy { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }

.stat-value {
  font-size: 22px;
  font-weight: 800;
  color: #1f2937;
  line-height: 1.2;
}

.stat-label {
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
}

.section-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 16px;
}

.type-bars {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.type-bar {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.bar-info {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.bar-name {
  color: #374151;
  font-weight: 500;
}

.bar-value {
  color: #6b7280;
  font-weight: 600;
}

.bar-track {
  height: 8px;
  background: #f3f4f6;
  border-radius: 4px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.week-chart {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 160px;
  padding-top: 10px;
}

.day-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
}

.day-bar-wrapper {
  height: 120px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.day-bar {
  width: 24px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 6px 6px 0 0;
  transition: height 0.5s ease;
}

.day-value {
  font-size: 10px;
  color: #6b7280;
  font-weight: 600;
}

.day-label {
  font-size: 11px;
  color: #9ca3af;
}

.course-progress-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.progress-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.course-name {
  color: #374151;
  font-weight: 500;
}

.progress-percent {
  color: #667eea;
  font-weight: 700;
}

.progress-track {
  height: 6px;
  background: #f3f4f6;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
  transition: width 0.5s ease;
}
</style>
