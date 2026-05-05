import request from '@/utils/request'

export const studentLoginApi = {
  login: (param) => request.post('/api/auth/login', param),

  logout: () => request.get('/api/auth/logout'),

  getCaptcha: () => request.get(`/api/open/captcha/applay?t=${Date.now()}`),

  userInfo: () => request.post('/api/service/webapi/userInfo'),
}
