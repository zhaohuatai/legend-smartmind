<template>
  <Teleport to="body">
    <TransitionGroup name="toast">
      <div
        v-for="item in toasts"
        :key="item.id"
        class="toast-overlay"
      >
        <div class="toast-card" :class="'toast-' + item.type">
          <div class="toast-icon-wrap">
            <svg v-if="item.type === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 12.75L11.25 15 15 9.75"/>
              <circle cx="12" cy="12" r="9"/>
            </svg>
            <svg v-else-if="item.type === 'error'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="9"/>
              <path d="M15 9l-6 6M9 9l6 6"/>
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="9"/>
              <path d="M12 8v4M12 16h.01"/>
            </svg>
          </div>
          <div class="toast-title">{{ item.title }}</div>
          <div class="toast-message">{{ item.message }}</div>
        </div>
      </div>
    </TransitionGroup>
  </Teleport>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useToast } from '@/utils/toast.js'

const toasts = ref([])

onMounted(() => {
  const { subscribe } = useToast()
  const unsubscribe = subscribe((newToasts) => {
    toasts.value = newToasts
  })
  onUnmounted(unsubscribe)
})
</script>

<style scoped>
.toast-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
  animation: fadeIn 0.2s ease;
}

.toast-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px 28px 24px;
  min-width: 300px;
  max-width: 380px;
  text-align: center;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  animation: cardIn 0.3s ease;
}

.toast-icon-wrap {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.toast-icon-wrap svg {
  width: 32px;
  height: 32px;
}

.toast-success .toast-icon-wrap {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
}

.toast-error .toast-icon-wrap {
  background: linear-gradient(135deg, #f56565, #e53e3e);
  color: #fff;
}

.toast-warning .toast-icon-wrap {
  background: linear-gradient(135deg, #f6ad55, #ed8936);
  color: #fff;
}

.toast-info .toast-icon-wrap {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
}

.toast-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 6px;
}

.toast-message {
  font-size: 14px;
  color: #718096;
  line-height: 1.5;
}

.toast-enter-active {
  animation: cardIn 0.3s ease;
}

.toast-leave-active {
  animation: cardOut 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes cardIn {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes cardOut {
  from {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
  to {
    opacity: 0;
    transform: scale(0.9) translateY(20px);
  }
}
</style>
