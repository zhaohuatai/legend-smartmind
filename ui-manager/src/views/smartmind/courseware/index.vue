<template>
  <div class="courseware-page">
    <!-- 顶部：课程和单元信息 -->
    <div class="header-info">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <router-link to="/smartmind/course">课程列表</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>
          <router-link :to="`/smartmind/course/detail?courseId=${courseId}`">{{ courseInfo?.courseName || '课程详情' }}</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>课件管理</a-breadcrumb-item>
      </a-breadcrumb>
      <div class="unit-info" v-if="currentUnit">
        <span class="unit-label">当前单元</span>
        <span class="unit-name">{{ currentUnit.unitName }}</span>
        <span class="unit-code">({{ currentUnit.unitCode }})</span>
      </div>
    </div>

    <!-- 主体内容：左侧 AI Chat + 中间 Markdown 编辑 + 右侧 iframe PPT 预览 -->
    <div class="main-content">
      <!-- 左侧：AI Chat -->
      <div class="left-panel">
        <!-- AI Chat -->
        <div class="aichat-section">
          <div class="section-header">
            <RobotOutlined />
            <span>AI 助手</span>
          </div>
          <AIChat
            ref="aiChatRef"
            :course-id="courseId"
            :unit-id="currentUnit?.id"
            :unit-code="unitCode"
            @generate="onAIGenerate"
            @apply="onAIApply"
          />
        </div>
      </div>

      <!-- 中间：Markdown 编辑区 -->
      <div class="editor-panel">
        <div class="editor-toolbar">
          <span class="toolbar-title"><FileTextOutlined /> Markdown 编辑</span>
          <div class="toolbar-actions">
            <a-button type="primary" size="small" @click="syncToIframe">
              <SyncOutlined /> 同步
            </a-button>
            <a-button type="primary" size="small" @click="saveCourseware">
              <SaveOutlined /> 保存
            </a-button>
          </div>
        </div>
        <a-textarea
          ref="markdownTextareaRef"
          v-model:value="markdownContent"
          class="markdown-editor"
          placeholder="在此输入Markdown内容..."
          @input="onEditorInput"
          @mouseup="onEditorClick"
          @keyup="onEditorClick"
        />
      </div>

      <!-- 右侧：iframe 加载 PPT 预览 -->
      <div class="right-panel">
        <iframe
          ref="iframeRef"
          :src="iframeSrc"
          frameborder="0"
          class="courseware-iframe"
        ></iframe>
      </div>
    </div>

    <!-- 新增/编辑模态框 -->
    <CoursewareFormModal ref="formModalRef" @reloadList="loadCoursewareList" />
  </div>
</template>

<script setup>
  import { ref, onMounted, computed, onUnmounted, reactive, nextTick } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { getRequest, postRequest } from '/@/lib/axios';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { chapterApi } from '/@/api/smartmind/chapter-api.js';
  import {
    FolderOutlined,
    PlusOutlined,
    FilePptOutlined,
    FileTextOutlined,
    FileOutlined,
    RobotOutlined,
    SyncOutlined,
    SaveOutlined,
  } from '@ant-design/icons-vue';
  import AIChat from './components/AIChat.vue';
  import CoursewareFormModal from './courseware-form-modal.vue';

  const route = useRoute();
  const router = useRouter();

  // 路由参数
  const courseId = ref(route.query.courseId);
  const unitCode = ref(route.query.unitCode);

  // 课程和单元信息
  const courseInfo = ref(null);
  const currentUnit = ref(null);

  // 课件列表数据（从后端加载）
  const coursewareList = ref([]);

  // ========== 响应式数据 ==========
  const selectedCourseware = ref(null);
  const aiChatRef = ref(null);
  const iframeRef = ref(null);
  const formModalRef = ref(null);
  const markdownTextareaRef = ref(null);

  // Markdown 编辑内容
  const markdownContent = ref('');

  // 配置选项
  const config = reactive({
    ratio: '4:3'
  });

  // 自动同步定时器
  let autoSyncTimer = null;

  // iframe 路径 - 使用相对路径访问 public 目录下的静态文件
  const iframeSrc = computed(() => {
    return `./remark-it/index6.html?courseId=${courseId.value}&unitCode=${unitCode.value}`;
  });

  // 全局固定头部
  const GLOBAL_HEADER = 'layout: true\nclass: typo, typo-selection\n---\n';

  // ========== 初始化 ==========
  onMounted(() => {
    loadCourseInfo();
    loadUnitInfo();
    loadCoursewareList();

    // 监听 iframe 消息
    window.addEventListener('message', handleIframeMessage);
  });

  // 加载课程信息
  async function loadCourseInfo() {
    if (!courseId.value) return;
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      console.error('加载课程信息失败', e);
    }
  }

  // 加载单元信息
  async function loadUnitInfo() {
    if (!courseId.value || !unitCode.value) return;
    try {
      const res = await chapterApi.queryByCourseId(courseId.value);
      const chapters = res.data || [];
      // 根据 unitCode 查找对应的章节
      const unit = chapters.find(c => c.unitCode === unitCode.value);
      if (unit) {
        currentUnit.value = unit;
      }
    } catch (e) {
      console.error('加载单元信息失败', e);
    }
  }

  onUnmounted(() => {
    window.removeEventListener('message', handleIframeMessage);
    if (autoSyncTimer) {
      clearTimeout(autoSyncTimer);
    }
  });

  // ========== 方法 ==========

  // 获取课件类型名称
  function getCoursewareTypeName(type) {
    const typeMap = {
      1: '课件',
      2: '教案',
      3: '学案',
      4: '习题',
      5: '素材'
    };
    return typeMap[type] || '其他';
  }

  // 将课件列表拼接为完整 markdown（每条记录是一页 PPT）
  function buildFullMarkdown(list) {
    if (!list || list.length === 0) return '';
    const pages = list.map(item => item.markdownContent || '').filter(Boolean);
    return GLOBAL_HEADER + pages.join('\n\n---\n\n');
  }

  // 选择课件
  function selectCourseware(item) {
    selectedCourseware.value = item;
    // 编辑器显示全部内容（所有页面拼接）
    markdownContent.value = buildFullMarkdown(coursewareList.value);
    
    // 同步到 iframe
    syncToIframe();
  }

  // 发送消息到 iframe
  function sendToIframe(data) {
    if (iframeRef.value && iframeRef.value.contentWindow) {
      iframeRef.value.contentWindow.postMessage(data, '*');
    }
  }

  // 根据光标位置计算当前页码
  function getCurrentSlideIndex() {
    // 使用 nextTick 确保 DOM 已更新
    const allTextareas = document.querySelectorAll('textarea');
    console.log('[slide] all textareas count:', allTextareas.length);
    
    // 找到 markdown 编辑器内的 textarea
    let textarea = null;
    for (let i = 0; i < allTextareas.length; i++) {
      const ta = allTextareas[i];
      const parent = ta.closest('.editor-panel');
      console.log('[slide] textarea', i, 'parent:', parent ? 'found' : 'null');
      if (parent) {
        textarea = ta;
        break;
      }
    }
    
    console.log('[slide] found textarea:', textarea);
    if (!textarea) return null;

    const cursorPos = textarea.selectionStart;
    const content = markdownContent.value;
    console.log('[slide] cursorPos:', cursorPos, 'content length:', content ? content.length : 0);
    if (!content) return null;

    // 去掉全局头部
    let body = content;
    let headerOffset = 0;
    if (body.startsWith('layout: true')) {
      const idx = body.indexOf('\n---\n');
      if (idx !== -1) {
        headerOffset = idx + 5;
        body = body.substring(headerOffset);
      }
    }

    // 计算光标在正文中的位置
    const cursorInBody = Math.max(0, cursorPos - headerOffset);

    // 按 --- 分割，计算光标在第几页
    const separators = [];
    let searchStart = 0;
    while (true) {
      const idx = body.indexOf('\n---\n', searchStart);
      if (idx === -1) break;
      separators.push(idx);
      searchStart = idx + 5;
    }

    let pageIndex = 0;
    for (let i = 0; i < separators.length; i++) {
      if (cursorInBody < separators[i]) {
        pageIndex = i;
        break;
      }
      pageIndex = i + 1;
    }

    console.log('[slide] headerOffset:', headerOffset, 'cursorInBody:', cursorInBody, 'separators:', separators, 'pageIndex:', pageIndex + 1);
    return pageIndex + 1; // 页码从 1 开始
  }

  // 同步到 iframe（直接发送编辑器内容 + 当前页码）
  function syncToIframe() {
    const targetSlide = getCurrentSlideIndex();
    console.log('[sync] syncToIframe, targetSlide:', targetSlide);
    sendToIframe({
      type: 'UPDATE_MARKDOWN',
      content: markdownContent.value,
      targetSlide: targetSlide
    });
  }

  // 编辑器输入事件（自动同步）
  function onEditorInput() {
    if (autoSyncTimer) {
      clearTimeout(autoSyncTimer);
    }
    autoSyncTimer = setTimeout(() => {
      syncToIframe();
    }, 500);
  }

  // 编辑器点击事件（同步光标位置到 PPT）
  function onEditorClick() {
    const targetSlide = getCurrentSlideIndex();
    if (targetSlide) {
      sendToIframe({
        type: 'GOTO_PAGE',
        pageIndex: targetSlide
      });
    }
  }

  // 处理 iframe 消息
  function handleIframeMessage(event) {
    const data = event.data;
    
    if (data.type === 'IFRAME_READY') {
      // iframe 准备好后，发送当前选中的课件内容
      if (coursewareList.value.length > 0) {
        syncToIframe();
      }
    } else if (data.type === 'SAVE_COURSEWARE') {
      // iframe 请求保存
      saveCourseware();
    }
  }

  // AI 生成回调
  function onAIGenerate(content) {
    // 将 AI 生成的内容更新到 markdown 编辑区
    markdownContent.value = content;
    // 同步到 iframe
    syncToIframe();
    message.success('AI 生成完成');
  }

  // AI 应用回调
  function onAIApply(content) {
    // 将 AI 内容更新到 markdown 编辑区
    markdownContent.value = content;
    // 同步到 iframe
    syncToIframe();
    message.success('内容已应用');
  }

  // 将编辑器内容解析回课件列表（按 --- 分割）
  function parseMarkdownToPages(content) {
    if (!content) return [];
    // 去掉全局头部
    let body = content;
    if (body.startsWith('layout: true')) {
      const idx = body.indexOf('\n---\n');
      if (idx !== -1) {
        body = body.substring(idx + 5);
      }
    }
    // 按 --- 分割页面
    const pages = body.split(/\n---\n/).map(p => p.trim()).filter(Boolean);
    return pages;
  }

  // 保存课件
  async function saveCourseware() {
    if (!markdownContent.value) {
      message.warning('没有可保存的内容');
      return;
    }
    
    // 将编辑器内容解析回各页
    const pages = parseMarkdownToPages(markdownContent.value);
    
    if (pages.length === 0) {
      message.warning('没有有效的PPT页面');
      return;
    }
    
    // 构建课件列表
    const listToSave = pages.map((page, index) => ({
      id: selectedCourseware.value?.id || null,
      coursewareCode: `CW-${courseId.value}-${unitCode.value}-${String(index + 1).padStart(3, '0')}`,
      coursewareName: `${currentUnit.value?.unitName || '课件'} - 第${index + 1}页`,
      coursewareType: 1,
      courseId: courseId.value,
      courseName: courseInfo.value?.courseName,
      unitId: currentUnit.value?.id,
      unitName: currentUnit.value?.unitName,
      unitCode: unitCode.value,
      markdownContent: page,
      sortOrder: index + 1,
      status: '1'
    }));
    
    try {
      await postRequest('/manage/smartmind/courseware/batchSave', listToSave);
      message.success('保存成功');
      // 重新加载列表
      await loadCoursewareList();
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    }
    
    // 通知 iframe 保存成功
    sendToIframe({
      type: 'SAVE_SUCCESS'
    });
  }

  // 新增课件
  function toAdd() {
    formModalRef.value?.showModal({
      courseId: courseId.value,
      courseName: courseInfo.value?.courseName,
      unitId: currentUnit.value?.id,
      unitName: currentUnit.value?.unitName,
      unitNo: currentUnit.value?.unitCode,
    });
  }

  // 加载课件列表（从后端 API）
  async function loadCoursewareList() {
    try {
      const params = { courseId: courseId.value };
      if (unitCode.value) {
        params.unitCode = unitCode.value;
      }
      const res = await getRequest('/manage/smartmind/courseware/loadByCourseAndUnit', params);
      if (res && res.data) {
        coursewareList.value = res.data;
        // 默认选中第一个
        if (coursewareList.value.length > 0) {
          selectCourseware(coursewareList.value[0]);
        }
      }
    } catch (e) {
      console.error('加载课件列表失败', e);
      message.error('加载课件列表失败');
    }
  }
</script>

<style scoped lang="less">
  .courseware-page {
    height: calc(100vh - 100px);
    display: flex;
    flex-direction: column;
    padding: 16px;
    background: #f5f7fa;

    .header-info {
      background: #fff;
      padding: 12px 20px;
      border-radius: 8px;
      margin-bottom: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

      .unit-info {
        margin-top: 8px;
        display: flex;
        align-items: center;
        gap: 8px;

        .unit-label {
          color: #8c8c8c;
          font-size: 13px;
        }

        .unit-name {
          font-size: 15px;
          font-weight: 500;
          color: #262626;
        }

        .unit-code {
          color: #8c8c8c;
          font-size: 12px;
        }
      }
    }

    .main-content {
      flex: 1;
      display: flex;
      gap: 12px;
      min-height: 0;

      .left-panel {
        width: 330px;
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        gap: 12px;

        .material-section {
          background: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
          display: flex;
          flex-direction: column;
          height: 45%;
          overflow: hidden;

          .material-list {
            flex: 1;
            overflow-y: auto;
            padding: 8px;

            .material-item {
              display: flex;
              align-items: center;
              padding: 10px;
              border-radius: 6px;
              cursor: pointer;
              transition: all 0.3s;
              margin-bottom: 6px;

              &:hover {
                background: #f5f7fa;
              }

              &.active {
                background: #e6f7ff;
                border: 1px solid #1890ff;
              }

              .item-icon {
                font-size: 20px;
                margin-right: 10px;
                color: #1890ff;
              }

              .item-info {
                flex: 1;
                min-width: 0;

                .item-name {
                  font-size: 13px;
                  color: #262626;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                }

                .item-type {
                  font-size: 11px;
                  color: #8c8c8c;
                  margin-top: 2px;
                }
              }
            }
          }
        }

        .aichat-section {
          background: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
          display: flex;
          flex-direction: column;
          flex: 1;
          overflow: hidden;
        }
      }

      .editor-panel {
        width: 470px;
        flex-shrink: 0;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .editor-toolbar {
          padding: 10px 12px;
          border-bottom: 1px solid #f0f0f0;
          display: flex;
          justify-content: space-between;
          align-items: center;
          background: #fafafa;

          .toolbar-title {
            font-size: 14px;
            color: #333;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 6px;
          }

          .toolbar-actions {
            display: flex;
            gap: 8px;
          }
        }

        .markdown-editor {
          flex: 1;
          min-height: 0;
          border: none;
          resize: none;
          background: #fff;

          :deep(.ant-input) {
            height: 100% !important;
            border: none !important;
            box-shadow: none !important;
            resize: none !important;
            background: #fff !important;
          }

          :deep(textarea) {
            height: 100% !important;
            padding: 12px !important;
            resize: none !important;
            overflow-y: auto !important;
            font-family: 'Consolas', 'Monaco', 'Courier New', monospace !important;
            font-size: 13px !important;
            line-height: 1.7 !important;
            color: #333 !important;
            background: #fff !important;
            border: none !important;
            box-shadow: none !important;
          }
        }
      }

      .right-panel {
        flex: 1;
        min-width: 0;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

        .courseware-iframe {
          width: 100%;
          height: 100%;
          border: none;
          background: #fff;
        }
      }
    }

    .section-header {
      padding: 10px 12px;
      border-bottom: 1px solid #f0f0f0;
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 500;
      color: #262626;
      font-size: 14px;
      flex-shrink: 0;

      :deep(.ant-btn) {
        margin-left: auto;
        padding: 0 8px;
        height: 24px;
        font-size: 12px;
      }
    }
  }
</style>
