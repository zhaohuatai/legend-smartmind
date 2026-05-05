<template>
  <div class="page courses-page">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-bg">
        <div class="bg-circle c1"></div>
        <div class="bg-circle c2"></div>
        <div class="bg-circle c3"></div>
      </div>
      <div class="welcome-content">
        <h1 class="welcome-title">
          <span class="wave"></span>
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

    <!-- 课程列表 -->
    <div class="courses-section">
      <div class="section-header">
        <h2 class="section-title">我的课程</h2>
        <span class="section-count">{{ courseList.length }} 门</span>
      </div>

      <div class="course-list">
        <div
          v-for="course in courseList"
          :key="course.id"
          class="course-card"
          @click="goToActivities(course)"
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
            <p class="course-teacher">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                <path d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/>
              </svg>
              {{ course.teacherName }}
            </p>
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

      <div class="empty-state" v-if="!loading && courseList.length === 0">
        <div class="empty-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"/>
          </svg>
        </div>
        <p class="empty-text">暂无课程</p>
        <p class="empty-hint">请联系老师添加课程</p>
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
  'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
]

onMounted(() => {
  loadCourses()
})

async function loadCourses() {
  try {
    loading.value = true
    const coursesRes = await api.getMyCourses()
    const list = coursesRes.data || []
    courseList.value = list.map((item, index) => ({
      id: item.id,
      courseName: item.courseName || '',
      teacherName: item.createBy || '',
      progress: item.progress || 0,
      hasNewActivity: item.hasNewActivity || false,
      coverGradient: gradients[index % gradients.length],
    }))
    courseCount.value = list.length
    activityCount.value = 0
    completedCount.value = 0
  } catch (e) {
    console.error('加载课程失败', e)
  } finally {
    loading.value = false
  }
}

function goToActivities(course) {
  router.push({ path: '/course-sessions', query: { courseId: course.id, courseName: course.courseName } })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 20px;
}

/* 欢迎区域 */
.welcome-section {
  position: relative;
  padding: 32px 20px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 0 0 32px 32px;
  overflow: hidden;
}

@media (min-width: 1024px) {
  .welcome-section {
    border-radius: 20px;
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

.stats-row {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 16px 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
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

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 4px;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
}

/* 课程列表区域 */
.courses-section {
  padding: 24px 16px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.section-count {
  font-size: 13px;
  color: var(--text-muted);
  background: #f3f4f6;
  padding: 4px 12px;
  border-radius: 20px;
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.course-card {
  display: flex;
  align-items: center;
  background: var(--card-bg);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--shadow);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.course-card:active {
  transform: scale(0.98);
}

.course-cover {
  width: 80px;
  height: 100px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.cover-icon {
  width: 40px;
  height: 40px;
  color: rgba(255, 255, 255, 0.9);
}

.course-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(255, 255, 255, 0.95);
  color: var(--danger);
  font-size: 10px;
  font-weight: 700;
  padding: 3px 8px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.pulse-dot {
  width: 6px;
  height: 6px;
  background: var(--danger);
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(1.5); }
}

.course-info {
  flex: 1;
  padding: 14px 12px;
  min-width: 0;
}

.course-name {
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.course-teacher {
  font-size: 12px;
  color: var(--text-muted);
  margin: 0 0 10px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.course-progress {
  display: flex;
  align-items: center;
  gap: 10px;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background: #f3f4f6;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary), var(--primary-dark));
  border-radius: 3px;
  transition: width 0.6s ease;
}

.progress-text {
  font-size: 12px;
  font-weight: 600;
  color: var(--primary);
  min-width: 36px;
  text-align: right;
}

.course-arrow {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--border);
  margin-right: 8px;
}

.course-arrow svg {
  width: 18px;
  height: 18px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
  color: var(--border);
}

.empty-icon svg {
  width: 100%;
  height: 100%;
}

.empty-text {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-secondary);
  margin: 0 0 8px;
}

.empty-hint {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}
</style>
