<template>
  <div class="page home-page">
    <div class="welcome-section">
      <div class="welcome-bg">
        <div class="bg-circle c1"></div>
        <div class="bg-circle c2"></div>
        <div class="bg-circle c3"></div>
      </div>
      <div class="welcome-content">
        <h1 class="welcome-title">
          <span class="wave">👋</span>
          <span>你好，{{ userName }}</span>
        </h1>
        <p class="welcome-subtitle">开始今天的学习之旅吧</p>
        <div class="stats-row">
          <div class="stat-item">
            <span class="stat-num">{{ courseCount }}</span>
            <span class="stat-label">在学课程</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-num">{{ activityCount }}</span>
            <span class="stat-label">待完成</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-num">{{ completedCount }}</span>
            <span class="stat-label">已完成</span>
          </div>
        </div>
      </div>
    </div>

    <div class="quick-entry">
      <div class="entry-card" @click="goToCourses">
        <div class="entry-icon icon-courses">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 016.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/>
          </svg>
        </div>
        <span>我的课程</span>
      </div>
      <div class="entry-card" @click="goToActivities">
        <div class="entry-icon icon-activities">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12 6 12 12 16 14"/>
          </svg>
        </div>
        <span>课堂活动</span>
      </div>
      <div class="entry-card" @click="goToStats">
        <div class="entry-icon icon-stats">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M18 20V10"/>
            <path d="M12 20V4"/>
            <path d="M6 20v-6"/>
          </svg>
        </div>
        <span>学习统计</span>
      </div>
    </div>

    <div class="section recent-section">
      <div class="section-header">
        <h2 class="section-title">最近课程</h2>
        <span class="section-more" @click="goToCourses">查看全部</span>
      </div>
      <div class="course-list">
        <div
          v-for="course in courseList"
          :key="course.id"
          class="course-card"
          @click="goToCourseDetail(course)"
        >
          <div class="course-cover" :style="{ background: course.coverGradient }">
            <div class="cover-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"/>
              </svg>
            </div>
            <div class="course-badge" v-if="course.hasNewActivity">
              <span class="pulse-dot"></span>
              新活动
            </div>
          </div>
          <div class="course-info">
            <h3 class="course-name">{{ course.courseName }}</h3>
            <p class="course-teacher">{{ course.teacherName }}</p>
            <div class="course-progress">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: course.progress + '%' }"></div>
              </div>
              <span class="progress-text">{{ course.progress }}%</span>
            </div>
          </div>
          <div class="course-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '@/api'

const router = useRouter()

const userName = ref('同学')
const courseCount = ref(0)
const activityCount = ref(0)
const completedCount = ref(0)
const courseList = ref([])
const loading = ref(false)

const gradients = [
  'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
  'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
  'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
]

onMounted(() => {
  loadHomeData()
})

async function loadHomeData() {
  try {
    loading.value = true
    const [coursesRes, statsRes] = await Promise.all([
      api.getMyCourses(),
      api.getStatsOverview(),
    ])

    const courses = coursesRes.data || []
    const stats = statsRes.data || {}

    courseList.value = courses.map((item, index) => ({
      id: item.id,
      courseName: item.courseName || '',
      teacherName: item.teacherName || '加载中...',
      progress: item.progress || 0,
      hasNewActivity: item.hasNewActivity || false,
      coverGradient: gradients[index % gradients.length],
    }))

    courseCount.value = stats.totalCourses || courses.length
    activityCount.value = stats.totalActivities || 0
    completedCount.value = stats.completedActivities || 0
  } catch (e) {
    console.error('加载首页数据失败', e)
  } finally {
    loading.value = false
  }
}

function goToCourses() {
  router.push('/courses')
}

function goToActivities() {
  router.push('/activities')
}

function goToStats() {
  router.push('/stats')
}

function goToCourseDetail(course) {
  router.push({ path: '/course-sessions', query: { courseId: course.id, courseName: course.courseName } })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 20px;
  width: 100%;
}

@media (min-width: 1024px) {
  .page {
    padding-bottom: 0;
  }
}

.welcome-section {
  position: relative;
  padding: 32px 20px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 0 0 32px 32px;
  overflow: hidden;
  width: 100%;
}

@media (min-width: 1024px) {
  .welcome-section {
    border-radius: 20px;
    padding: 40px 32px 48px;
  }
}

.welcome-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
}

.c1 {
  width: 200px;
  height: 200px;
  top: -60px;
  right: -40px;
  animation: float 8s ease-in-out infinite;
}

.c2 {
  width: 120px;
  height: 120px;
  bottom: -20px;
  left: 20px;
  animation: float 6s ease-in-out infinite reverse;
}

.c3 {
  width: 80px;
  height: 80px;
  top: 40px;
  left: 40%;
  animation: float 10s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.05); }
}

.welcome-content {
  position: relative;
  z-index: 1;
}

.welcome-title {
  font-size: 26px;
  font-weight: 800;
  color: #fff;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

@media (min-width: 1024px) {
  .welcome-title {
    font-size: 32px;
  }
}

.wave {
  display: inline-block;
  animation: wave 2s ease-in-out infinite;
  transform-origin: 70% 70%;
}

@keyframes wave {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(20deg); }
  75% { transform: rotate(-15deg); }
}

.welcome-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.75);
  margin: 0 0 24px;
}

@media (min-width: 1024px) {
  .welcome-subtitle {
    font-size: 16px;
    margin-bottom: 32px;
  }
}

.stats-row {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 16px 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

@media (min-width: 1024px) {
  .stats-row {
    padding: 24px 32px;
  }
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 24px;
  font-weight: 800;
  color: #fff;
  line-height: 1.2;
}

@media (min-width: 1024px) {
  .stat-num {
    font-size: 32px;
  }
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 4px;
}

@media (min-width: 1024px) {
  .stat-label {
    font-size: 14px;
  }
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
}

.quick-entry {
  display: flex;
  gap: 12px;
  padding: 20px 16px;
}

@media (min-width: 1024px) {
  .quick-entry {
    padding: 24px 0;
    gap: 16px;
  }
}

.entry-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
  font-weight: 500;
  color: #4b5563;
}

@media (min-width: 1024px) {
  .entry-card {
    padding: 20px 12px;
    border-radius: 20px;
    font-size: 14px;
  }
}

.entry-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.entry-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (min-width: 1024px) {
  .entry-icon {
    width: 52px;
    height: 52px;
    border-radius: 14px;
  }
}

.entry-icon svg {
  width: 22px;
  height: 22px;
  color: #fff;
}

@media (min-width: 1024px) {
  .entry-icon svg {
    width: 26px;
    height: 26px;
  }
}

.icon-courses { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.icon-activities { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.icon-stats { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }

.section {
  padding: 0 16px;
  margin-bottom: 20px;
}

@media (min-width: 1024px) {
  .section {
    padding: 0;
    margin-bottom: 24px;
  }
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

@media (min-width: 1024px) {
  .section-header {
    margin-bottom: 16px;
  }
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

@media (min-width: 1024px) {
  .section-title {
    font-size: 22px;
  }
}

.section-more {
  font-size: 13px;
  color: #667eea;
  cursor: pointer;
}

@media (min-width: 1024px) {
  .section-more {
    font-size: 14px;
  }
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

@media (min-width: 1024px) {
  .course-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 16px;
  }
}

.course-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

@media (min-width: 1024px) {
  .course-card {
    border-radius: 20px;
  }
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.course-card:active {
  transform: scale(0.98);
}

.course-cover {
  width: 72px;
  height: 90px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

@media (min-width: 1024px) {
  .course-cover {
    width: 90px;
    height: 110px;
  }
}

.cover-icon {
  width: 36px;
  height: 36px;
  color: rgba(255, 255, 255, 0.9);
}

@media (min-width: 1024px) {
  .cover-icon {
    width: 44px;
    height: 44px;
  }
}

.course-badge {
  position: absolute;
  top: 6px;
  right: 6px;
  background: rgba(255, 255, 255, 0.95);
  color: #ef4444;
  font-size: 10px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 3px;
}

.pulse-dot {
  width: 5px;
  height: 5px;
  background: #ef4444;
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(1.5); }
}

.course-info {
  flex: 1;
  padding: 12px;
  min-width: 0;
}

@media (min-width: 1024px) {
  .course-info {
    padding: 16px;
  }
}

.course-name {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (min-width: 1024px) {
  .course-name {
    font-size: 17px;
  }
}

.course-teacher {
  font-size: 12px;
  color: #6b7280;
  margin: 0 0 8px;
}

@media (min-width: 1024px) {
  .course-teacher {
    font-size: 13px;
  }
}

.course-progress {
  display: flex;
  align-items: center;
  gap: 8px;
}

.progress-bar {
  flex: 1;
  height: 5px;
  background: #f3f4f6;
  border-radius: 3px;
  overflow: hidden;
}

@media (min-width: 1024px) {
  .progress-bar {
    height: 6px;
  }
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 3px;
  transition: width 0.6s ease;
}

.progress-text {
  font-size: 11px;
  font-weight: 600;
  color: #667eea;
  min-width: 32px;
  text-align: right;
}

@media (min-width: 1024px) {
  .progress-text {
    font-size: 13px;
  }
}

.course-arrow {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d1d5db;
  margin-right: 6px;
}

@media (min-width: 1024px) {
  .course-arrow {
    width: 40px;
    height: 40px;
    margin-right: 8px;
  }
}

.course-arrow svg {
  width: 16px;
  height: 16px;
}

@media (min-width: 1024px) {
  .course-arrow svg {
    width: 18px;
    height: 18px;
  }
}
</style>
