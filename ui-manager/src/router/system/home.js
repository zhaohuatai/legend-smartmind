/*
 * 首页路由
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:51:41
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
import SmartLayout from '/@/layout/index.vue';

export const homeRouters = [
  {
    path: '/',
    name: '_home',
    redirect: { name: HOME_PAGE_NAME },
    component: SmartLayout,
    meta: {
      title: '首页',
      menuType: MENU_TYPE_ENUM.CATALOG.value,
      icon: 'HomeOutlined',
    },
    children: [
      {
        path: '/home',
        name: HOME_PAGE_NAME,
        meta: {
          title: '首页',
          menuType: MENU_TYPE_ENUM.MENU.value,
          icon: 'HomeOutlined',
          parentMenuList: [{ name: '_home', title: '首页' }],
        },
        component: () => import('/@/views/pentest/homeindex.vue'),
      },
      {
        path: '/account',
        name: 'Account',
        component: () => import('/@/views/system/account/index.vue'),
        meta: {
          title: '个人中心',
          hideInMenu: false,
        },
      },
      {
        path: '/course/detail',
        name: 'CourseDetail',
        component: () => import('/@/views/smartmind/course/course-detail.vue'),
        meta: {
          title: '课程详情',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/courseware',
        name: 'Courseware',
        component: () => import('/@/views/smartmind/courseware/index.vue'),
        meta: {
          title: '课件管理',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/teaching-plan',
        name: 'TeachingPlan',
        component: () => import('/@/views/smartmind/teachingPlan/teaching-plan-editor.vue'),
        meta: {
          title: '教案管理',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/experiment-guide',
        name: 'ExperimentGuide',
        component: () => import('/@/views/smartmind/experimentGuide/experiment-guide-editor.vue'),
        meta: {
          title: '实验指导书管理',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/question-bank',
        name: 'QuestionBank',
        component: () => import('/@/views/smartmind/questionBank/question-bank-editor.vue'),
        meta: {
          title: '题库管理',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/discussion-topic',
        name: 'DiscussionTopic',
        component: () => import('/@/views/smartmind/discussionTopic/discussion-topic-editor.vue'),
        meta: {
          title: '讨论活动话题',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/class-session',
        name: 'ClassSession',
        component: () => import('/@/views/smartmind/classSession/index.vue'),
        meta: {
          title: '课堂教学',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/teaching-analysis',
        name: 'TeachingAnalysis',
        component: () => import('/@/views/smartmind/teachingAnalysis/index.vue'),
        meta: {
          title: '教学分析',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/class-activity',
        name: 'ClassActivity',
        component: () => import('/@/views/smartmind/classActivity/index.vue'),
        meta: {
          title: '课堂活动',
          hideInMenu: true,
        },
      },
      {
        path: '/smartmind/class-activity-detail',
        name: 'ClassActivityDetail',
        component: () => import('/@/views/smartmind/classSession/activity-detail.vue'),
        meta: {
          title: '活动详情',
          hideInMenu: true,
        },
      },
    ],
  },
];
