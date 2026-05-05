<template>
  <div class="page course-sessions-page">
    <PageHeader :title="courseName" />

    <div class="sessions-list">
      <div
        v-for="session in sessionList"
        :key="session.id"
        class="session-card"
        @click="goToSession(session)"
      >
        <div class="session-number">
          <span class="number">{{ session.sessionNo }}</span>
        </div>
        <div class="session-info">
          <h3 class="session-name">{{ session.sessionName }}</h3>
          <div class="session-meta">
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                <line x1="16" y1="2" x2="16" y2="6"/>
                <line x1="8" y1="2" x2="8" y2="6"/>
                <line x1="3" y1="10" x2="21" y2="10"/>
              </svg>
              {{ session.date }}
            </span>
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              {{ session.time }}
            </span>
            <!-- <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/>
                <circle cx="9" cy="7" r="4"/>
                <path d="M23 21v-2a4 4 0 00-3-3.87"/>
                <path d="M16 3.13a4 4 0 010 7.75"/>
              </svg>
              {{ session.activityCount }} 个活动
            </span> -->
          </div>
          <div class="session-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: session.progress + '%' }"></div>
            </div>
            <span class="progress-text">{{ session.progress }}%</span>
          </div>
        </div>
        <div class="session-status" :class="session.statusClass">
          {{ session.statusText }}
        </div>
        <div class="session-arrow">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
          </svg>
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="sessionList.length === 0">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
      </div>
      <p class="empty-text">暂无课堂</p>
      <p class="empty-hint">老师还未创建课堂</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '@/api'
import PageHeader from '@/components/PageHeader.vue'

const route = useRoute()
const router = useRouter()

const courseName = ref(route.query.courseName || '课程')
const sessionList = ref([])
const loading = ref(false)

const statusMap = {
  '0': { text: '未开始', class: 'status-pending' },
  '1': { text: '进行中', class: 'status-active' },
  '2': { text: '已结束', class: 'status-ended' },
  '3': { text: '已取消', class: 'status-ended' },
}

onMounted(() => {
  loadSessions()
})

async function loadSessions() {
  try {
    loading.value = true
    const courseId = route.query.courseId
    if (!courseId) return
    const res = await api.getSessionList(courseId)
    const list = res.data || []
    sessionList.value = list.map(item => {
      const status = statusMap[item.status] || { text: '未知', class: 'status-ended' }
      const date = item.startTime ? item.startTime.split(' ')[0] : ''
      const time = item.startTime ? item.startTime.split(' ')[1] : ''
      return {
        id: item.id,
        sessionNo: item.sessionCode || '',
        sessionName: item.sessionName || '',
        date: date,
        time: time,
        activityCount: item.activityCount || 0,
        progress: item.progress || 0,
        statusText: status.text,
        statusClass: status.class,
      }
    })
  } catch (e) {
    console.error('加载课堂列表失败', e)
  } finally {
    loading.value = false
  }
}

function goToSession(session) {
  router.push({
    path: '/activities',
    query: {
      courseId: route.query.courseId,
      courseName: courseName.value,
      sessionId: session.id,
      sessionName: session.sessionName,
    },
  })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 20px;
}

.sessions-list {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.session-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.session-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.session-card:active {
  transform: scale(0.98);
}

.session-number {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
  flex-shrink: 0;
}

.session-number .number {
  font-size: 20px;
  font-weight: 800;
  color: #fff;
}

.session-info {
  flex: 1;
  min-width: 0;
}

.session-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.session-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #6b7280;
}

.meta-item svg {
  color: #9ca3af;
}

.session-progress {
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

.session-status {
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 600;
  flex-shrink: 0;
  margin-right: 8px;
}

.status-pending {
  background: #fef3c7;
  color: #d97706;
}

.status-active {
  background: #d1fae5;
  color: #059669;
}

.status-ended {
  background: #f3f4f6;
  color: #6b7280;
}

.session-arrow {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d1d5db;
  flex-shrink: 0;
}

.session-arrow svg {
  width: 16px;
  height: 16px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
  color: #d1d5db;
}

.empty-icon svg {
  width: 100%;
  height: 100%;
}

.empty-text {
  font-size: 16px;
  font-weight: 600;
  color: #4b5563;
  margin: 0 0 8px;
}

.empty-hint {
  font-size: 13px;
  color: #9ca3af;
  margin: 0;
}
</style>
