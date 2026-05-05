<template>
  <header class="page-header">
    <button class="btn-back" @click="goBack">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M19 12H5M12 19l-7-7 7-7"/>
      </svg>
    </button>
    <div class="header-title">{{ title }}</div>
    <div class="header-placeholder"></div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

defineProps({
  title: {
    type: String,
    default: '',
  },
})

function goBack() {
  router.back()
}
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.btn-back {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  border-radius: 8px;
  color: #374151;
  transition: background 0.2s;
}

.btn-back:hover {
  background: #f3f4f6;
}

.btn-back svg {
  width: 20px;
  height: 20px;
}

.header-title {
  font-size: 17px;
  font-weight: 700;
  color: #1f2937;
  flex: 1;
  text-align: center;
}

.header-placeholder {
  width: 36px;
}
</style>
