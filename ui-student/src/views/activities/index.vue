<template>
  <div class="page activities-page">
    <PageHeader :title="sessionName" />

    <div class="filter-tabs">
      <div
        v-for="tab in filterTabs"
        :key="tab.value"
        class="filter-tab"
        :class="{ active: currentFilter === tab.value }"
        @click="currentFilter = tab.value"
      >
        {{ tab.label }}
      </div>
    </div>

    <div class="activity-list">
      <div
        v-for="activity in filteredActivities"
        :key="activity.id"
        class="activity-card"
        @click="goToAnswer(activity)"
      >
        <div class="activity-icon" :class="activity.typeClass">
          <svg v-if="activity.activityType === '1'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"/>
          </svg>
          <svg v-else-if="activity.activityType === '2'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"/>
          </svg>
          <svg v-else-if="activity.activityType === '3'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M11.42 15.17l-5.384 3.174A1 1 0 014.5 17.5V6.5a1 1 0 011.536-.848l5.384 3.174a1 1 0 010 1.688zM12.58 15.17l5.384 3.174A1 1 0 0019.5 17.5V6.5a1 1 0 00-1.536-.848l-5.384 3.174a1 1 0 000 1.688z"/>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M7.5 8.25h9m-9 3H12m-9.75 3h7.5M3 19.5h18a2.25 2.25 0 002.25-2.25V6.75A2.25 2.25 0 0021 4.5H3A2.25 2.25 0 00.75 6.75v10.5A2.25 2.25 0 003 19.5z"/>
          </svg>
        </div>
        <div class="activity-info">
          <h3 class="activity-name">{{ activity.activityName }}</h3>
          <p class="activity-type">{{ activity.typeName }}</p>
          <div class="activity-meta">
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
                <path d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ activity.duration }}分钟
            </span>
            <span class="meta-item">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
                <path d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.563.563 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z"/>
              </svg>
              {{ activity.score }}分
            </span>
          </div>
        </div>
        <div class="activity-status" :class="activity.statusClass">
          <span class="status-dot"></span>
          {{ activity.statusText }}
        </div>
        <div class="activity-arrow">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
          </svg>
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="filteredActivities.length === 0">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"/>
        </svg>
      </div>
      <p class="empty-text">暂无活动</p>
      <p class="empty-hint">老师还未发布课堂活动</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '@/api'
import PageHeader from '@/components/PageHeader.vue'

const route = useRoute()
const router = useRouter()

const courseName = ref(route.query.courseName || '课程')
const sessionName = ref(route.query.sessionName || '课堂')
const currentFilter = ref('all')
const loading = ref(false)

const filterTabs = [
  { label: '全部', value: 'all' },
  { label: '随堂测试', value: '1' },
  { label: '随堂练习', value: '2' },
  { label: '课堂实验', value: '3' },
  { label: '话题讨论', value: '4' },
]

const activityList = ref([])

const typeMap = {
  '1': { name: '随堂测试', class: 'type-quiz' },
  '2': { name: '随堂练习', class: 'type-exercise' },
  '3': { name: '课堂实验', class: 'type-experiment' },
  '4': { name: '话题讨论', class: 'type-discussion' },
}

const statusMap = {
  '0': { text: '未开始', class: 'status-pending' },
  '1': { text: '进行中', class: 'status-active' },
  '2': { text: '已结束', class: 'status-ended' },
  '3': { text: '已取消', class: 'status-ended' },
}

const filteredActivities = computed(() => {
  if (currentFilter.value === 'all') return activityList.value
  return activityList.value.filter(a => a.activityType === currentFilter.value)
})

onMounted(() => {
  loadActivities()
})

async function loadActivities() {
  try {
    loading.value = true
    const sessionId = route.query.sessionId
    if (!sessionId) return
    const res = await api.getActivityList(sessionId)
    const list = res.data || []
    activityList.value = list.map(item => {
      const type = typeMap[item.activityType] || { name: '未知', class: 'type-quiz' }
      const status = statusMap[item.status] || { text: '未知', class: 'status-ended' }
      return {
        id: item.id,
        activityName: item.activityName || '',
        activityType: item.activityType || '1',
        typeName: type.name,
        typeClass: type.class,
        duration: item.duration || 0,
        score: item.score || 0,
        status: item.status || '0',
        statusText: status.text,
        statusClass: status.class,
      }
    })
  } catch (e) {
    console.error('加载活动列表失败', e)
  } finally {
    loading.value = false
  }
}

function goToAnswer(activity) {
  router.push({
    path: '/answer',
    query: {
      activityId: activity.id,
      activityName: activity.activityName,
      activityType: activity.activityType,
    },
  })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding-bottom: 20px;
}

.page-header {
  position: sticky;
  top: 0;
  z-index: 100;
  height: 64px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 2px 12px rgba(102, 126, 234, 0.3);
}

.header-back, .header-placeholder {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 12px;
  transition: background 0.2s;
}

.header-back:hover {
  background: rgba(255, 255, 255, 0.15);
}

.header-back svg {
  width: 24px;
  height: 24px;
  color: #fff;
}

.header-info {
  text-align: center;
}

.header-title {
  font-size: 16px;
  font-weight: 700;
  color: #fff;
}

.header-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
}

.filter-tabs {
  display: flex;
  gap: 8px;
  padding: 14px 16px;
  overflow-x: auto;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.filter-tab {
  flex-shrink: 0;
  padding: 6px 14px;
  border-radius: 20px;
  background: #f3f4f6;
  font-size: 13px;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.filter-tab:hover {
  background: #e5e7eb;
}

.filter-tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.activity-list {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16px;
  padding: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.activity-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.activity-card:active {
  transform: scale(0.98);
}

.activity-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-right: 12px;
}

.activity-icon svg {
  width: 24px;
  height: 24px;
  color: #fff;
}

.type-quiz { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.type-exercise { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.type-experiment { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.type-discussion { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }

.activity-info {
  flex: 1;
  min-width: 0;
}

.activity-name {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.activity-type {
  font-size: 12px;
  color: #6b7280;
  margin: 0 0 6px;
}

.activity-meta {
  display: flex;
  gap: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  color: #9ca3af;
}

.activity-status {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 600;
  flex-shrink: 0;
  margin-right: 8px;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.status-pending {
  background: #fef3c7;
  color: #d97706;
}
.status-pending .status-dot { background: #d97706; }

.status-active {
  background: #d1fae5;
  color: #059669;
}
.status-active .status-dot {
  background: #059669;
  animation: pulse 2s ease-in-out infinite;
}

.status-ended {
  background: #f3f4f6;
  color: #6b7280;
}
.status-ended .status-dot { background: #6b7280; }

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}

.activity-arrow {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d1d5db;
  flex-shrink: 0;
}

.activity-arrow svg {
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
