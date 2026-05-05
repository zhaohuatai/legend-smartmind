<template>
  <div class="login-container">
    <div class="login-header">
      <div class="logo">
        <div class="logo-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 016.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z" />
          </svg>
        </div>
        <span class="logo-text">智析课堂</span>
      </div>
      <p class="welcome-text">欢迎登录学生端</p>
    </div>

    <div class="login-form">
      <div class="form-group">
        <label class="form-label">学号</label>
        <div class="input-wrapper">
          <svg class="input-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
            <circle cx="12" cy="7" r="4" />
          </svg>
          <input
            v-model="loginForm.account"
            type="text"
            placeholder="请输入学号"
            class="form-input"
            @keyup.enter="onLogin"
          />
        </div>
        <span v-if="errors.account" class="error-msg">{{ errors.account }}</span>
      </div>

      <div class="form-group">
        <label class="form-label">密码</label>
        <div class="input-wrapper">
          <svg class="input-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
            <path d="M7 11V7a5 5 0 0110 0v4" />
          </svg>
          <input
            v-model="loginForm.password"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请输入密码"
            class="form-input"
            @keyup.enter="onLogin"
          />
          <button type="button" class="eye-btn" @click="showPassword = !showPassword">
            <svg v-if="showPassword" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
              <circle cx="12" cy="12" r="3" />
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24" />
              <line x1="1" y1="1" x2="23" y2="23" />
            </svg>
          </button>
        </div>
        <span v-if="errors.password" class="error-msg">{{ errors.password }}</span>
      </div>

      <div class="form-group">
        <label class="form-label">验证码</label>
        <div class="captcha-row">
          <input
            v-model="loginForm.captcha"
            type="text"
            placeholder="请输入验证码"
            class="form-input captcha-input"
            @keyup.enter="onLogin"
          />
          <img
            class="captcha-img"
            :src="captchaBase64Image"
            @click="getCaptcha"
            alt="验证码"
          />
        </div>
        <span v-if="errors.captcha" class="error-msg">{{ errors.captcha }}</span>
      </div>

      <button
        type="button"
        class="login-btn"
        :disabled="loading"
        @click="onLogin"
      >
        <span v-if="loading" class="loading-spinner"></span>
        {{ loading ? '登录中...' : '登 录' }}
      </button>
    </div>

    <div v-if="toast.show" class="toast" :class="toast.type">
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { studentLoginApi } from '@/api/login'
import md5 from 'js-md5'

const router = useRouter()
const loading = ref(false)
const showPassword = ref(false)
const captchaBase64Image = ref('')

const loginForm = reactive({
  account: '',
  password: '',
  captcha: '',
  captchaKey: '',
})

const errors = reactive({
  account: '',
  password: '',
  captcha: '',
})

const toast = reactive({
  show: false,
  message: '',
  type: 'success',
})

let refreshCaptchaInterval = null
let toastTimer = null

onMounted(() => {
  getCaptcha()
  document.addEventListener('keyup', handleKeyup)
})

onUnmounted(() => {
  document.removeEventListener('keyup', handleKeyup)
  stopRefreshCaptchaInterval()
  clearTimeout(toastTimer)
})

function handleKeyup(e) {
  if (e.keyCode === 13) {
    onLogin()
  }
}

function showToast(message, type = 'success') {
  toast.show = true
  toast.message = message
  toast.type = type
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => {
    toast.show = false
  }, 2000)
}

function validate() {
  let valid = true
  errors.account = ''
  errors.password = ''
  errors.captcha = ''

  if (!loginForm.account.trim()) {
    errors.account = '请输入学号'
    valid = false
  }
  if (!loginForm.password) {
    errors.password = '请输入密码'
    valid = false
  }
  if (!loginForm.captcha.trim()) {
    errors.captcha = '请输入验证码'
    valid = false
  }
  return valid
}

async function getCaptcha() {
  try {
    const res = await studentLoginApi.getCaptcha()
    captchaBase64Image.value = res.data.image
    if (res.data.captchaKey) {
      loginForm.captchaKey = res.data.captchaKey
    }
    const expireSeconds = res.data.expireSeconds || 60
    beginRefreshCaptchaInterval(expireSeconds)
  } catch (e) {
    console.error('获取验证码失败', e)
  }
}

function beginRefreshCaptchaInterval(expireSeconds) {
  stopRefreshCaptchaInterval()
  refreshCaptchaInterval = setInterval(getCaptcha, (expireSeconds - 5) * 1000)
}

function stopRefreshCaptchaInterval() {
  if (refreshCaptchaInterval) {
    clearInterval(refreshCaptchaInterval)
    refreshCaptchaInterval = null
  }
}

async function onLogin() {
  if (!validate()) return

  loading.value = true

  try {
    const salt = '94DABGioQOq2tTUO0AXYow'
    const loginData = {
      account: loginForm.account,
      password: md5(salt + loginForm.password),
      captcha: loginForm.captcha,
      captchaKey: loginForm.captchaKey,
      authType: 'session',
    }

    const res = await studentLoginApi.login(loginData)
    stopRefreshCaptchaInterval()

    showToast('登录成功')
    setTimeout(() => {
      router.replace('/home')
    }, 300)
  } catch (e) {
    loginForm.captcha = ''
    getCaptcha()
    showToast(e.msg || '登录失败', 'error')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  min-height: 100dvh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

@media (max-width: 1024px) {
  .login-container {
    padding: 40px 24px;
  }
}

@media (max-width: 768px) {
  .login-container {
    padding: 24px 16px;
  }
}

.login-header {
  text-align: center;
  margin-bottom: 48px;
}

@media (max-width: 1024px) {
  .login-header {
    margin-bottom: 40px;
  }
}

@media (max-width: 768px) {
  .login-header {
    margin-bottom: 24px;
  }
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 1024px) {
  .logo-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
  }
  .logo-icon svg {
    width: 28px;
    height: 28px;
  }
}

@media (max-width: 768px) {
  .logo-icon {
    width: 40px;
    height: 40px;
  }
  .logo-icon svg {
    width: 24px;
    height: 24px;
  }
}

.logo-text {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
}

@media (max-width: 1024px) {
  .logo-text {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .logo-text {
    font-size: 24px;
  }
}

.welcome-text {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
}

@media (max-width: 768px) {
  .welcome-text {
    font-size: 14px;
  }
}

.login-form {
  width: 100%;
  max-width: 440px;
  background: #fff;
  border-radius: 20px;
  padding: 40px 32px;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
}

@media (max-width: 1024px) {
  .login-form {
    max-width: 400px;
    padding: 32px 24px;
    border-radius: 16px;
  }
}

@media (max-width: 768px) {
  .login-form {
    max-width: 100%;
    padding: 24px 20px;
    border-radius: 12px;
  }
}

.form-group {
  margin-bottom: 24px;
}

@media (max-width: 1024px) {
  .form-group {
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .form-group {
    margin-bottom: 16px;
  }
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  width: 20px;
  height: 20px;
  color: #999;
  pointer-events: none;
}

@media (max-width: 768px) {
  .input-icon {
    left: 12px;
    width: 18px;
    height: 18px;
  }
}

.form-input {
  width: 100%;
  height: 52px;
  padding: 0 14px 0 44px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  transition: border-color 0.2s;
  box-sizing: border-box;
  -webkit-appearance: none;
  appearance: none;
}

@media (max-width: 1024px) {
  .form-input {
    height: 48px;
    padding: 0 12px 0 40px;
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  .form-input {
    height: 44px;
    font-size: 16px;
  }
}

.form-input:focus {
  border-color: #667eea;
}

.eye-btn {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  color: #999;
  -webkit-tap-highlight-color: transparent;
}

@media (max-width: 768px) {
  .eye-btn {
    right: 12px;
  }
}

.eye-btn svg {
  width: 20px;
  height: 20px;
}

.captcha-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-input {
  flex: 1;
  min-width: 0;
}

.captcha-img {
  width: 120px;
  height: 52px;
  border-radius: 10px;
  cursor: pointer;
  border: 1px solid #eee;
  flex-shrink: 0;
}

@media (max-width: 1024px) {
  .captcha-row {
    gap: 12px;
  }
  .captcha-img {
    width: 110px;
    height: 48px;
  }
}

@media (max-width: 768px) {
  .captcha-row {
    gap: 8px;
  }
  .captcha-img {
    width: 100px;
    height: 44px;
  }
}

.error-msg {
  display: block;
  font-size: 12px;
  color: #ff4d4f;
  margin-top: 4px;
}

.login-btn {
  width: 100%;
  height: 52px;
  margin-top: 28px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: opacity 0.2s;
  -webkit-tap-highlight-color: transparent;
}

@media (max-width: 1024px) {
  .login-btn {
    height: 48px;
    margin-top: 24px;
  }
}

@media (max-width: 768px) {
  .login-btn {
    height: 44px;
    margin-top: 20px;
  }
}

.login-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.login-btn:active:not(:disabled) {
  opacity: 0.8;
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@media (max-width: 768px) {
  .loading-spinner {
    width: 18px;
    height: 18px;
  }
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.toast {
  position: fixed;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  padding: 14px 28px;
  border-radius: 10px;
  font-size: 15px;
  z-index: 9999;
  animation: fadeIn 0.3s ease;
  max-width: calc(100vw - 32px);
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 1024px) {
  .toast {
    top: 20px;
    font-size: 14px;
    padding: 12px 24px;
  }
}

@media (max-width: 768px) {
  .toast {
    top: 12px;
    font-size: 13px;
    padding: 10px 20px;
  }
}

.toast.success {
  background: #52c41a;
  color: #fff;
}

.toast.error {
  background: #ff4d4f;
  color: #fff;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateX(-50%) translateY(-10px); }
  to { opacity: 1; transform: translateX(-50%) translateY(0); }
}
</style>
