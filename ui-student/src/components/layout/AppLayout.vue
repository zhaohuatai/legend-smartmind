<template>
  <div class="app-layout" :class="layoutClass">
    <!-- PC端侧边栏 -->
    <aside v-if="isPc" class="sidebar">
      <div class="sidebar-header">
        <div class="sidebar-logo">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 016.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z" />
          </svg>
          <span>智析课堂</span>
        </div>
      </div>
      <nav class="sidebar-nav">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: currentPath === item.path }"
        >
          <div class="nav-icon">
            <component :is="item.icon" />
          </div>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </nav>
    </aside>

    <!-- 平板端/手机端底部导航 -->
    <nav v-if="isMobile || isTablet" class="bottom-nav">
      <router-link
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        class="nav-item"
        :class="{ active: currentPath === item.path }"
      >
        <div class="nav-icon">
          <component :is="item.icon" />
        </div>
        <span class="nav-label">{{ item.label }}</span>
      </router-link>
    </nav>

    <div class="main-wrapper">
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, h } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const deviceType = ref('mobile')

const currentPath = computed(() => route.path)

const isPc = computed(() => deviceType.value === 'pc')
const isTablet = computed(() => deviceType.value === 'tablet')
const isMobile = computed(() => deviceType.value === 'mobile')

const layoutClass = computed(() => `layout-${deviceType.value}`)

const navItems = [
  { path: '/home', label: '主页', icon: HomeIcon },
  { path: '/courses', label: '课程', icon: CoursesIcon },
  { path: '/profile', label: '我的', icon: ProfileIcon },
]

function checkDevice() {
  const width = window.innerWidth
  if (width >= 1024) {
    deviceType.value = 'pc'
  } else if (width >= 768) {
    deviceType.value = 'tablet'
  } else {
    deviceType.value = 'mobile'
  }
}

onMounted(() => {
  checkDevice()
  window.addEventListener('resize', checkDevice)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkDevice)
})

function HomeIcon() {
  return h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '2' }, [
    h('path', { d: 'M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z' }),
    h('polyline', { points: '9 22 9 12 15 12 15 22' }),
  ])
}

function CoursesIcon() {
  return h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '2' }, [
    h('path', { d: 'M4 19.5A2.5 2.5 0 016.5 17H20' }),
    h('path', { d: 'M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z' }),
  ])
}

function ProfileIcon() {
  return h('svg', { viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '2' }, [
    h('path', { d: 'M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2' }),
    h('circle', { cx: '12', cy: '7', r: '4' }),
  ])
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  min-height: 100dvh;
  background: #f5f7fa;
}

.main-wrapper {
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

/* PC端 - 左侧边栏 */
.layout-pc {
  display: flex;
}

.layout-pc .main-wrapper {
  flex: 1;
  margin-left: 240px;
  padding: 24px 32px;
}

.layout-pc .main-content {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  width: 240px;
  background: #fff;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  z-index: 100;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 700;
  color: #667eea;
}

.sidebar-logo svg {
  width: 28px;
  height: 28px;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.sidebar-nav .nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  text-decoration: none;
  color: #666;
  transition: all 0.2s ease;
}

.sidebar-nav .nav-item:hover {
  background: #f5f7fa;
  color: #667eea;
}

.sidebar-nav .nav-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.sidebar-nav .nav-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-nav .nav-icon svg {
  width: 20px;
  height: 20px;
}

.sidebar-nav .nav-label {
  font-size: 14px;
  font-weight: 500;
}

/* 平板端/手机端 - 底部导航 */
.layout-tablet .main-wrapper,
.layout-mobile .main-wrapper {
  padding-bottom: 60px;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.06);
  border-top: 1px solid #f0f0f0;
  z-index: 1000;
}

.bottom-nav .nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  padding: 6px 20px;
  text-decoration: none;
  color: #9ca3af;
  transition: all 0.2s ease;
  flex: 1;
  -webkit-tap-highlight-color: transparent;
}

.bottom-nav .nav-item:hover {
  color: #667eea;
}

.bottom-nav .nav-item.active {
  color: #667eea;
}

.bottom-nav .nav-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bottom-nav .nav-icon svg {
  width: 22px;
  height: 22px;
}

.bottom-nav .nav-label {
  font-size: 10px;
  font-weight: 500;
}
</style>
