<template>
  <div class="page profile-page">
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <h2 class="student-name">{{ studentInfo.name }}</h2>
        <p class="student-id">学号: {{ studentInfo.studentId }}</p>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-value">{{ stats.totalCourses }}</span>
        <span class="stat-label">在学课程</span>
      </div>
      <div class="stat-card">
        <span class="stat-value">{{ stats.totalActivities }}</span>
        <span class="stat-label">参与活动</span>
      </div>
      <div class="stat-card">
        <span class="stat-value">{{ stats.avgScore }}</span>
        <span class="stat-label">平均得分</span>
      </div>
      <div class="stat-card">
        <span class="stat-value">{{ stats.completionRate }}%</span>
        <span class="stat-label">完成率</span>
      </div>
    </div>

    <div class="menu-section">
      <div class="section-title">学习中心</div>
      <div class="menu-list">
        <div class="menu-item" @click="goToCourses">
          <div class="menu-icon icon-courses">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 19.5A2.5 2.5 0 016.5 17H20"/>
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/>
            </svg>
          </div>
          <span class="menu-text">我的课程</span>
          <div class="menu-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
        <div class="menu-item" @click="goToActivities">
          <div class="menu-icon icon-activities">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
          </div>
          <span class="menu-text">课堂活动</span>
          <div class="menu-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
        <div class="menu-item" @click="goToStats">
          <div class="menu-icon icon-stats">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 20V10"/>
              <path d="M12 20V4"/>
              <path d="M6 20v-6"/>
            </svg>
          </div>
          <span class="menu-text">学习统计</span>
          <div class="menu-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
      </div>
    </div>

    <div class="menu-section">
      <div class="section-title">个人设置</div>
      <div class="menu-list">
        <div class="menu-item" @click="goToEditProfile">
          <div class="menu-icon icon-edit">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </div>
          <span class="menu-text">编辑资料</span>
          <div class="menu-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
        <div class="menu-item" @click="goToSettings">
          <div class="menu-icon icon-settings">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3"/>
              <path d="M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-2 2 2 2 0 01-2-2v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83 0 2 2 0 010-2.83l.06-.06a1.65 1.65 0 00.33-1.82 1.65 1.65 0 00-1.51-1H3a2 2 0 01-2-2 2 2 0 012-2h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 010-2.83 2 2 0 012.83 0l.06.06a1.65 1.65 0 001.82.33H9a1.65 1.65 0 001-1.51V3a2 2 0 012-2 2 2 0 012 2v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 0 2 2 0 010 2.83l-.06.06a1.65 1.65 0 00-.33 1.82V9a1.65 1.65 0 001.51 1H21a2 2 0 012 2 2 2 0 01-2 2h-.09a1.65 1.65 0 00-1.51 1z"/>
            </svg>
          </div>
          <span class="menu-text">系统设置</span>
          <div class="menu-arrow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
            </svg>
          </div>
        </div>
        <div class="menu-item" @click="handleLogout">
          <div class="menu-icon icon-logout">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </div>
          <span class="menu-text">退出登录</span>
          <div class="menu-arrow">
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

const studentInfo = ref({
  name: '加载中...',
  studentId: '',
})

const stats = ref({
  totalCourses: 0,
  totalActivities: 0,
  avgScore: 0,
  completionRate: 0,
})

const loading = ref(false)

onMounted(() => {
  loadProfileData()
})

async function loadProfileData() {
  try {
    loading.value = true
    const [infoRes, statsRes] = await Promise.all([
      api.getMyInfo(),
      api.getStatsOverview(),
    ])

    const info = infoRes.data || {}
    const statsData = statsRes.data || {}

    studentInfo.value = {
      name: info.studentName || info.name || '同学',
      studentId: info.studentNo || info.studentId || '',
    }

    stats.value = {
      totalCourses: statsData.totalCourses || 0,
      totalActivities: statsData.totalActivities || 0,
      avgScore: statsData.avgScore || 0,
      completionRate: statsData.completionRate || 0,
    }
  } catch (e) {
    console.error('加载个人信息失败', e)
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

function goToEditProfile() {
}

function goToSettings() {
}

function handleLogout() {
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 20px;
  background: #f5f7fa;
}

.profile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px 30px;
  text-align: center;
  border-radius: 0 0 32px 32px;
}

@media (min-width: 1024px) {
  .profile-header {
    border-radius: 20px;
  }
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.avatar svg {
  width: 40px;
  height: 40px;
  color: #fff;
}

.student-name {
  font-size: 22px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 6px;
}

.student-id {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 20px 16px;
  margin-top: -20px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 16px 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: 800;
  color: #667eea;
  line-height: 1.2;
}

.stat-label {
  font-size: 11px;
  color: #6b7280;
  margin-top: 4px;
}

.menu-section {
  padding: 0 16px;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 12px;
  padding-left: 4px;
}

.menu-list {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: #f9fafb;
}

.menu-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.menu-icon svg {
  width: 18px;
  height: 18px;
  color: #fff;
}

.icon-courses { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.icon-activities { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.icon-stats { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.icon-edit { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
.icon-settings { background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%); }
.icon-logout { background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%); }

.menu-text {
  flex: 1;
  font-size: 15px;
  color: #1f2937;
}

.menu-arrow {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d1d5db;
}

.menu-arrow svg {
  width: 16px;
  height: 16px;
}
</style>
