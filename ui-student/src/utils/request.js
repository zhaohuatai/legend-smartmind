import axios from 'axios'
import { toastError } from '@/utils/toast.js'

const request = axios.create({
  baseURL: '',
  timeout: 15000,
  withCredentials: true,
})

request.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data.code === 200) {
      return data
    }
    if (data.code === 401 || data.code === 403) {
      window.location.hash = '#/login'
      return Promise.reject(data)
    }
    if (data.code === 300) {
      toastError(data.msg || '请求参数错误')
      return Promise.reject(data)
    }
    if (data.code === 500) {
      toastError(data.msg || '服务器内部错误，请稍后重试')
      return Promise.reject(data)
    }
    toastError(data.msg || '请求失败，请稍后重试')
    return Promise.reject(data)
  },
  (err) => {
    if (err.response) {
      const status = err.response.status
      if (status === 401 || status === 403) {
        window.location.hash = '#/login'
      } else if (status === 500) {
        toastError('服务器内部错误，请稍后重试')
      } else if (status >= 400 && status < 500) {
        toastError('请求错误，请检查网络或参数')
      } else {
        toastError('网络异常，请稍后重试')
      }
    } else {
      toastError('网络连接失败，请检查网络')
    }
    return Promise.reject(err)
  }
)

export default request
