<template>
  <div class="remark-slide-viewer">
    <!-- PPT 渲染容器 -->
    <div class="slideshow-wrapper">
      <!-- 占位符 -->
      <div class="slideshow-placeholder" v-if="!hasContent">
        <div class="placeholder-content">
          <DesktopOutlined class="placeholder-icon" />
          <p>请在中间编辑器输入 Markdown 内容</p>
          <p class="placeholder-hint">使用 --- 分隔幻灯片</p>
        </div>
      </div>

      <!-- iframe 加载独立页面 -->
      <iframe
        v-show="hasContent"
        ref="iframeRef"
        class="slideshow-iframe"
        src="/remark-it/index3.html"
        frameborder="0"
        sandbox="allow-scripts allow-same-origin"
      ></iframe>
    </div>

    <!-- 控制栏 -->
    <div class="slide-controls" v-if="hasContent && isReady">
      <a-space>
        <a-button size="small" @click="prevSlide" :disabled="currentSlide <= 1">
          <LeftOutlined />上页
        </a-button>
        <span class="page-indicator">{{ currentSlide }} / {{ totalSlides }}</span>
        <a-button size="small" @click="nextSlide" :disabled="currentSlide >= totalSlides">
          下页<RightOutlined />
        </a-button>
        <a-divider type="vertical" />
        <a-button size="small" @click="toggleTheme">
          <BulbOutlined />{{ isDarkTheme ? '亮' : '暗' }}
        </a-button>
        <a-button size="small" @click="toggleRatio">
          <ExpandOutlined />{{ ratio === '4:3' ? '16:9' : '4:3' }}
        </a-button>
      </a-space>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, computed } from 'vue';
import { LeftOutlined, RightOutlined, BulbOutlined, ExpandOutlined, DesktopOutlined } from '@ant-design/icons-vue';

const props = defineProps({
  markdownContent: {
    type: String,
    default: '',
  },
  courseId: {
    type: [String, Number],
    default: null,
  },
  unitId: {
    type: [String, Number],
    default: null,
  },
});

// Refs
const iframeRef = ref(null);
const isReady = ref(false);
const currentSlide = ref(1);
const totalSlides = ref(1);
const isDarkTheme = ref(true);
const ratio = ref('4:3');

// 计算属性：是否有内容
const hasContent = computed(() => {
  return props.markdownContent && props.markdownContent.trim().length > 0;
});

// 设置父窗口变量供 iframe 读取
function setParentWindowContent() {
  if (hasContent.value) {
    window.__markdownContent = props.markdownContent;
    console.log('已设置 window.__markdownContent, 长度:', props.markdownContent.length);
  }
}

// 发送内容到 iframe
function sendContentToIframe() {
  if (!iframeRef.value || !iframeRef.value.contentWindow) return;
  
  iframeRef.value.contentWindow.postMessage({
    type: 'UPDATE_CONTENT',
    content: props.markdownContent
  }, '*');
}

// 监听 iframe 消息
function handleMessage(event) {
  // 只处理来自 iframe 的消息
  if (event.source !== iframeRef.value?.contentWindow) {
    return;
  }

  if (event.data.type === 'IFRAME_READY') {
    isReady.value = true;
    // iframe 准备好了，发送当前内容
    if (hasContent.value) {
      sendContentToIframe();
    }
  } else if (event.data.type === 'SLIDE_COUNT') {
    totalSlides.value = event.data.count || 1;
  } else if (event.data.type === 'CURRENT_SLIDE') {
    currentSlide.value = event.data.current || 1;
  } else if (event.data.type === 'REQUEST_CONTENT') {
    // iframe 请求内容，立即发送
    console.log('收到 iframe 内容请求');
    sendContentToIframe();
  }
}

// 监听内容变化
watch(() => props.markdownContent, () => {
  // 同步更新父窗口变量
  setParentWindowContent();
  if (isReady.value) {
    sendContentToIframe();
  }
});

// iframe 加载前设置内容
watch(() => iframeRef.value, (newVal) => {
  if (newVal) {
    setParentWindowContent();
  }
});

onMounted(() => {
  // 组件挂载时设置初始内容
  setParentWindowContent();
  window.addEventListener('message', handleMessage);
});

onUnmounted(() => {
  window.removeEventListener('message', handleMessage);
});

// 上一页
function prevSlide() {
  if (iframeRef.value && iframeRef.value.contentWindow) {
    iframeRef.value.contentWindow.postMessage({ type: 'PREV_SLIDE' }, '*');
    if (currentSlide.value > 1) currentSlide.value--;
  }
}

// 下一页
function nextSlide() {
  if (iframeRef.value && iframeRef.value.contentWindow) {
    iframeRef.value.contentWindow.postMessage({ type: 'NEXT_SLIDE' }, '*');
    if (currentSlide.value < totalSlides.value) currentSlide.value++;
  }
}

// 切换主题
function toggleTheme() {
  isDarkTheme.value = !isDarkTheme.value;
  if (iframeRef.value && iframeRef.value.contentWindow) {
    iframeRef.value.contentWindow.postMessage({ 
      type: 'TOGGLE_THEME',
      isDark: isDarkTheme.value 
    }, '*');
  }
}

// 切换比例
function toggleRatio() {
  ratio.value = ratio.value === '4:3' ? '16:9' : '4:3';
  if (iframeRef.value && iframeRef.value.contentWindow) {
    iframeRef.value.contentWindow.postMessage({ 
      type: 'SET_RATIO',
      ratio: ratio.value,
      content: props.markdownContent
    }, '*');
  }
}

// 刷新 iframe（重新加载页面）
function reloadIframe() {
  if (iframeRef.value) {
    // 添加时间戳强制刷新
    const baseSrc = '/remark-it/index3.html';
    iframeRef.value.src = baseSrc + '?t=' + Date.now();
    // 重置状态
    isReady.value = false;
    currentSlide.value = 1;
    totalSlides.value = 1;
  }
}

// 暴露方法
defineExpose({
  refresh: sendContentToIframe,
  reload: reloadIframe,
  prevSlide,
  nextSlide,
});
</script>

<style scoped lang="less">
.remark-slide-viewer {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #1a1a1a;

  .slideshow-wrapper {
    flex: 1;
    overflow: hidden;
    position: relative;

    .slideshow-iframe {
      width: 100%;
      height: 100%;
      border: none;
    }

    .slideshow-placeholder {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #666;
      background: #1a1a1a;

      .placeholder-content {
        text-align: center;
        padding: 20px;

        .placeholder-icon {
          font-size: 64px;
          color: #444;
          margin-bottom: 16px;
        }

        p {
          margin: 8px 0;
          font-size: 14px;
          color: #666;
        }

        .placeholder-hint {
          font-size: 12px;
          color: #555;
        }
      }
    }
  }

  .slide-controls {
    padding: 10px 16px;
    background: #262626;
    border-top: 1px solid #444;
    display: flex;
    justify-content: center;
    align-items: center;

    .page-indicator {
      color: #fff;
      font-size: 13px;
      min-width: 60px;
      text-align: center;
    }
  }
}
</style>
