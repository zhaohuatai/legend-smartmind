import request from '@/utils/request'

export const api = {
  // 课程相关
  getMyCourses: () => request.post('/student/course/loadStudentCourses'),
  getCourseDetail: (id) => request.get(`/student/course/loadCourseDetail/${id}`),
  getSessionList: (courseId) => request.get(`/student/course/loadSessions/${courseId}`),

  // 活动相关
  getActivityList: (sessionId) => request.get(`/student/course/loadActivityList/${sessionId}`),
  getStudentActivityDetail: (activityId) => request.post(`/student/course/loadStudentActivityDetail/${activityId}`),

  // 提交相关
  submitAnswer: (params) => request.post('/student/course/submitAnswer', params),
  batchSubmitAnswer: (params) => request.post('/student/course/batchSubmitAnswer', params),

  // 个人中心
  getMyInfo: () => request.get('/student/profile/loadMyInfo'),
  updateMyInfo: (params) => request.post('/student/profile/updateMyInfo', params),

  // 学习统计
  getStatsOverview: () => request.get('/student/stats/loadOverview'),
  getTypeStats: () => request.get('/student/stats/loadTypeStats'),
  getWeekStudyTime: () => request.get('/student/stats/loadWeekStudyTime'),
  getCourseProgress: () => request.get('/student/stats/loadCourseProgress'),
}
