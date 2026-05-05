import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/',
    component: () => import('@/components/layout/AppLayout.vue'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页', isMain: true },
      },
      {
        path: '/courses',
        name: 'Courses',
        component: () => import('@/views/courses/index.vue'),
        meta: { title: '我的课程', isMain: true },
      },
      {
        path: '/course-sessions',
        name: 'CourseSessions',
        component: () => import('@/views/courseSessions/index.vue'),
        meta: { title: '课堂列表', isMain: false },
      },
      {
        path: '/activities',
        name: 'Activities',
        component: () => import('@/views/activities/index.vue'),
        meta: { title: '课堂活动', isMain: false },
      },
      {
        path: '/answer',
        name: 'Answer',
        component: () => import('@/views/answer/index.vue'),
        meta: { title: '答题', isMain: false },
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', isMain: true },
      },
      {
        path: '/stats',
        name: 'Stats',
        component: () => import('@/views/stats/index.vue'),
        meta: { title: '学习统计', isMain: false },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

let isUserInfoLoaded = false

router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title || '智析课堂'
  
  if (to.path === '/login') {
    next()
    return
  }
  
  if (isUserInfoLoaded) {
    next()
    return
  }
  
  try {
    const { studentLoginApi } = await import('@/api/login')
    await studentLoginApi.userInfo()
    isUserInfoLoaded = true
    next()
  } catch (e) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  }
})

export default router
