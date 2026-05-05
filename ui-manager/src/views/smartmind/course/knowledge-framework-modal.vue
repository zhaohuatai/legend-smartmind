<template>
  <a-modal
    :open="visible"
    title="编辑知识结构"
    :footer="null"
    @cancel="onClose"
    :confirmLoading="confirmLoading"
    :width="'100%'"
    :style="{ top: 0, height: '100vh', maxHeight: '100vh', padding: 0 }"
    :bodyStyle="{ height: 'calc(100vh - 75px)', padding: '16px', overflow: 'hidden' }"
  >
    <div class="knowledge-framework-container">
      <!-- AI对话区域 -->
      <div v-if="!showEditor" class="chat-section">
        <div class="chat-messages" ref="chatMessagesRef">
          <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
            <div class="message-content">
              <a-avatar v-if="msg.role === 'assistant'" class="avatar" :style="{ backgroundColor: '#1890ff' }">
                AI
              </a-avatar>
              <a-avatar v-else class="avatar" :style="{ backgroundColor: '#52c41a' }">
                {{ userName }}
              </a-avatar>
              <div class="content">
                <div v-if="msg.role === 'assistant' && msg.isStreaming" class="streaming-content">
                  <MarkdownRender
                    :custom-id="'chat-message-' + index"
                    :nodes="msg.nodes"
                    :final="msg.isFinal"
                  />
                </div>
                <div v-else-if="msg.role === 'assistant' && msg.canEdit" class="final-content">
                  <pre class="markdown-raw">{{ msg.rawText }}</pre>
                  <div class="edit-actions">
                    <a-button type="primary" size="small" @click="editMessage(msg)">
                      <EditOutlined /> 编辑内容
                    </a-button>
                    <a-button type="primary" size="small" @click="saveMessageDirectly(msg)" :loading="saving">
                      <SaveOutlined /> 直接保存
                    </a-button>
                  </div>
                </div>
                <div v-else>{{ msg.content }}</div>
              </div>
            </div>
          </div>
          
          <!-- 已有知识框架提示 -->
          <div v-if="hasKnowledgeFramework" class="existing-framework-notice">
            <a-alert
              message="该课程已有知识框架"
              description="您之前已保存过知识框架，可以点击下方按钮查看或继续生成新的框架"
              type="info"
              show-icon
            >
              <template #action>
                <a-button type="primary" size="small" @click="viewExistingFramework">
                  <EyeOutlined /> 查看已有框架
                </a-button>
              </template>
            </a-alert>
          </div>
        </div>
        
        <div class="chat-input-wrapper">
          <div class="chat-input">
            <a-textarea
              v-model:value="inputMessage"
              :rows="3"
              placeholder="请输入您的需求，例如：请为七年级数学课程生成一个包含代数、几何和统计的知识框架大纲"
              @pressEnter="handleSend"
            />
            <a-button 
              type="primary" 
              @click="handleSend" 
              :loading="sending"
              :disabled="!inputMessage.trim()"
              class="send-btn"
            >
              发送
            </a-button>
          </div>
          
          <div class="quick-actions">
            <span class="label">快速生成：</span>
            <a-button size="small" @click="useQuickPrompt('基础大纲')">基础大纲</a-button>
            <a-button size="small" @click="useQuickPrompt('详细大纲')">详细大纲</a-button>
            <a-button size="small" @click="useQuickPrompt('思维导图')">思维导图</a-button>
          </div>
        </div>
      </div>

      <!-- Markdown编辑器区域 - 左右布局 -->
      <div v-else class="editor-section">
        <div class="editor-header">
          <a-button @click="backToChat">
            <template #icon><LeftOutlined /></template>
            返回对话
          </a-button>
          <span class="editor-title">知识框架编辑</span>
          <a-button type="primary" @click="saveFramework" :loading="saving">
            <template #icon><SaveOutlined /></template>
            保存
          </a-button>
        </div>
        
        <div class="editor-body">
          <!-- 左侧编辑区 -->
          <div class="editor-left">
            <div class="editor-label">Markdown 编辑</div>
            <a-textarea
              v-model:value="markdownContent"
              class="markdown-editor"
              placeholder="在这里编辑知识框架..."
            />
          </div>
          
          <!-- 右侧预览区 -->
          <div class="editor-right">
            <div class="editor-label">实时预览</div>
            <div class="preview-content" v-html="renderedMarkdown"></div>
          </div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
  import { ref, computed, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { LeftOutlined, SaveOutlined, EditOutlined, EyeOutlined } from '@ant-design/icons-vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useUserStore } from '/@/store/modules/system/user.js';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import { aiApi } from '/@/api/smartmind/ai-api.js';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { MarkdownRender, getMarkdown, parseMarkdownToStructure } from 'markstream-vue';
  import { marked } from 'marked';
  import 'markstream-vue/index.css';

  const emit = defineEmits(['reloadList']);
  const userStore = useUserStore();
  const dictStore = useDictStore();
  
  // markstream-vue markdown 实例
  const md = getMarkdown('chat-message');
  
  const visible = ref(false);
  const confirmLoading = ref(false);
  const sending = ref(false);
  const saving = ref(false);
  const showEditor = ref(false);
  const showPreview = ref(false);
  const inputMessage = ref('');
  const messages = ref([]);
  const markdownContent = ref('');
  const chatMessagesRef = ref(null);
  const currentCourse = ref(null);
  
  const userName = computed(() => userStore.userInfo?.realName || userStore.userInfo?.username || '用户');
  
  const renderedMarkdown = computed(() => {
    if (!markdownContent.value) return '';
    return marked(markdownContent.value);
  });

  // 判断是否有已有知识框架
  const hasKnowledgeFramework = computed(() => {
    return !!currentCourse.value?.knowledgeFramework;
  });

  // 快速提示词
  const quickPrompts = {
    '基础大纲': '请为{grade}{subject}生成一个基础的知识框架大纲，包含主要章节和知识点。',
    '详细大纲': '请为{grade}{subject}生成一个详细的知识框架大纲，包含章节、知识点、学习目标和建议课时。',
    '思维导图': '请为{grade}{subject}生成一个思维导图形式的知识框架，使用Markdown列表格式，体现知识点的层级关系。'
  };

  // 获取字典标签
  function getDictLabel(dictType, value) {
    if (!value) return '';
    const dictData = dictStore.getDictData(dictType);
    const item = dictData.find(d => d.dataValue === value);
    return item ? item.dataLabel : value;
  }

  function showModal(course) {
    currentCourse.value = course;
    visible.value = true;
    showEditor.value = false;
    showPreview.value = false;
    messages.value = [];
    inputMessage.value = '';
    
    // 获取字典显示名称
    const gradeLevelName = getDictLabel('grade_level', course.gradeLevel);
    const subjectTypeName = getDictLabel('subject_type', course.subjectType);
    
    // 始终添加系统欢迎消息
    messages.value.push({
      role: 'assistant',
      content: `您好！我是您的AI课程助手。我可以帮您生成${gradeLevelName}${subjectTypeName}课程的知识框架大纲。\n\n请告诉我您的需求，比如：\n- 生成基础大纲\n- 生成详细大纲\n- 生成思维导图形式`,
      isStreaming: false,
      isFinal: true,
      rawText: '',
      nodes: []
    });
    
    // 如果有已有的知识框架，加载内容并直接显示编辑器
    if (course.knowledgeFramework) {
      markdownContent.value = course.knowledgeFramework;
      showEditor.value = true;
    }
  }

  function onClose() {
    visible.value = false;
    currentCourse.value = null;
  }

  function useQuickPrompt(type) {
    const course = currentCourse.value;
    const grade = getDictLabel('grade_level', course?.gradeLevel) || '该';
    const subject = getDictLabel('subject_type', course?.subjectType) || '课程';
    const prompt = quickPrompts[type]
      .replace('{grade}', grade)
      .replace('{subject}', subject);
    inputMessage.value = prompt;
  }

  async function handleSend(e) {
    if (e && !e.ctrlKey && e.key === 'Enter') {
      e.preventDefault();
    }
    
    const message = inputMessage.value.trim();
    if (!message || sending.value) return;

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: message,
      isStreaming: false,
      isFinal: true,
      rawText: '',
      nodes: []
    });
    
    inputMessage.value = '';
    sending.value = true;
    
    // 添加AI占位消息（使用响应式对象以便动态更新）
    const aiMessageIndex = messages.value.length;
    const aiMessage = {
      role: 'assistant',
      content: '',
      isStreaming: true,
      isFinal: false,
      rawText: ''
    };
    messages.value.push(aiMessage);

    try {
      // 构建上下文
      const context = buildContext();
      
      // 调用流式API
      await aiApi.streamChat({
        message,
        context,
        courseId: currentCourse.value?.id,
        onMessage: (chunk) => {
          // 追加原始文本
          messages.value[aiMessageIndex].rawText += chunk;
          // 更新 nodes（响应式自动触发重新计算）
          messages.value[aiMessageIndex].nodes = parseMarkdownToStructure(
            messages.value[aiMessageIndex].rawText,
            md,
            { final: false }
          );
          scrollToBottom();
        },
        onComplete: () => {
          const msg = messages.value[aiMessageIndex];
          msg.isStreaming = false;
          msg.isFinal = true;
          msg.content = msg.rawText;
          // 最终解析
          msg.nodes = parseMarkdownToStructure(msg.rawText, md, { final: true });
          // 设置可编辑状态
          msg.canEdit = msg.rawText.includes('#');
          
          // 如果生成了markdown内容，自动提取到编辑器
          if (msg.rawText.includes('#')) {
            extractMarkdown(msg.rawText);
          }
        },
        onError: (error) => {
          const msg = messages.value[aiMessageIndex];
          msg.rawText += '\n\n[发生错误，请重试]';
          msg.content = msg.rawText;
          msg.isStreaming = false;
          msg.isFinal = true;
          msg.nodes = parseMarkdownToStructure(msg.rawText, md, { final: true });
          smartSentry.captureError(error);
        }
      });
    } catch (e) {
      smartSentry.captureError(e);
      message.error('发送失败，请重试');
    } finally {
      sending.value = false;
    }
  }

  function buildContext() {
    const course = currentCourse.value;
    if (!course) return '';

    return `课程信息：
- 课程名称：${course.courseName || '未命名'}
- 学科类型：${getDictLabel('subject_type', course.subjectType) || '未指定'}
- 面向年级：${getDictLabel('grade_level', course.gradeLevel) || '未指定'}
- 学年：${getDictLabel('school_year', course.schoolYear) || '未指定'}
- 学期：${getDictLabel('semester', course.semester) || '未指定'}
- 描述：${course.courseDesc || '无'}

请根据以上课程信息，生成markdown格式的知识框架大纲。要求：
1. 使用标准的markdown格式
2. 包含清晰的层级结构
3. 知识点完整且有条理`;
  }

  function extractMarkdown(content) {
    // 尝试从AI回复中提取markdown代码块
    const codeBlockMatch = content.match(/```markdown\s*\n?([\s\S]*?)\n?```/);
    if (codeBlockMatch) {
      markdownContent.value = codeBlockMatch[1].trim();
    } else {
      // 如果没有代码块，直接使用全部内容
      markdownContent.value = content.trim();
    }
  }

  function backToChat() {
    showEditor.value = false;
    showPreview.value = false;
  }

  // 查看已有知识框架
  function viewExistingFramework() {
    if (currentCourse.value?.knowledgeFramework) {
      markdownContent.value = currentCourse.value.knowledgeFramework;
      showEditor.value = true;
    }
  }

  // 编辑消息内容
  function editMessage(msg) {
    // 提取markdown内容到编辑器
    if (msg.rawText.includes('#')) {
      extractMarkdown(msg.rawText);
      showEditor.value = true;
    } else {
      message.info('没有可编辑的Markdown内容');
    }
  }

  // 直接保存消息内容
  async function saveMessageDirectly(msg) {
    if (!currentCourse.value?.id) {
      message.error('课程信息错误');
      return;
    }
    
    saving.value = true;
    try {
      // 提取markdown内容
      let content = msg.rawText;
      const codeBlockMatch = content.match(/```markdown\s*\n?([\s\S]*?)\n?```/);
      if (codeBlockMatch) {
        content = codeBlockMatch[1].trim();
      }
      
      await courseApi.updateKnowledgeFramework({
        id: currentCourse.value.id,
        knowledgeFramework: content
      });
      message.success('保存成功');
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
      message.error('保存失败');
    } finally {
      saving.value = false;
    }
  }

  async function saveFramework() {
    if (!currentCourse.value?.id) {
      message.error('课程信息错误');
      return;
    }
    
    saving.value = true;
    try {
      await courseApi.updateKnowledgeFramework({
        id: currentCourse.value.id,
        knowledgeFramework: markdownContent.value
      });
      message.success('保存成功');
      emit('reloadList');
      onClose();
    } catch (e) {
      smartSentry.captureError(e);
      message.error('保存失败');
    } finally {
      saving.value = false;
    }
  }

  function scrollToBottom() {
    nextTick(() => {
      if (chatMessagesRef.value) {
        chatMessageRef.value.scrollTop = chatMessagesRef.value.scrollHeight;
      }
    });
  }

  defineExpose({
    showModal
  });
</script>

<style scoped lang="less">
  .knowledge-framework-container {
    height: 100%;
    display: flex;
    flex-direction: column;
  }

  .chat-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;
    
    .chat-messages {
      flex: 1;
      overflow-y: auto;
      padding: 16px;
      background: #f5f5f5;
      border-radius: 8px;
      margin-bottom: 16px;
      
      .message {
        margin-bottom: 16px;
        
        .message-content {
          display: flex;
          gap: 12px;
          
          .avatar {
            flex-shrink: 0;
          }
          
          .content {
            flex: 1;
            padding: 12px 16px;
            border-radius: 8px;
            background: #fff;
            white-space: pre-wrap;
            word-break: break-word;
            
            .streaming-content {
              /* markstream-vue 内部处理样式 */
            }
            
            .final-content {
              .markdown-raw {
                margin: 0;
                padding: 12px;
                background: #f6f8fa;
                border: 1px solid #e1e4e8;
                border-radius: 6px;
                font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
                font-size: 14px;
                line-height: 1.6;
                color: #24292e;
                white-space: pre-wrap;
                word-wrap: break-word;
                overflow-x: auto;
              }
              
              .edit-actions {
                display: flex;
                gap: 8px;
                margin-top: 12px;
                padding-top: 12px;
                border-top: 1px solid #e8e8e8;
              }
            }
          }
        }
        
        &.user {
          .message-content {
            flex-direction: row-reverse;
            
            .content {
              background: #1890ff;
              color: #fff;
            }
          }
        }
      }
      
      .existing-framework-notice {
        margin-top: 16px;
        
        :deep(.ant-alert) {
          .ant-alert-action {
            margin-left: 16px;
          }
        }
      }
    }
    
    .chat-input-wrapper {
      position: sticky;
      bottom: 0;
      left: 0;
      right: 0;
      background: #fff;
      border-top: 1px solid #e8e8e8;
      padding: 12px 16px;
      z-index: 10;
      
      .chat-input {
        display: flex;
        gap: 12px;
        margin-bottom: 12px;
        
        .send-btn {
          align-self: flex-end;
        }
      }
      
      .quick-actions {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .label {
          color: #666;
        }
      }
    }
  }

  .editor-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: hidden;
    
    .editor-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      flex-shrink: 0;
      
      .editor-title {
        font-size: 16px;
        font-weight: 500;
      }
    }
    
    .editor-body {
      flex: 1;
      display: flex;
      gap: 16px;
      overflow: hidden;
      
      .editor-left,
      .editor-right {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        
        .editor-label {
          font-size: 14px;
          font-weight: 500;
          color: #666;
          margin-bottom: 8px;
          flex-shrink: 0;
        }
        
        .markdown-editor {
          flex: 1;
          font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
          resize: none;
        }
        
        .preview-content {
          flex: 1;
          overflow-y: auto;
          padding: 24px 32px;
          background: #f5f5f5;
          border-radius: 8px;
          border: 1px solid #e8e8e8;
          
          h1, h2, h3, h4, h5, h6 {
            margin-top: 16px;
            margin-bottom: 8px;
          }
          
          ul, ol {
            padding-left: 24px;
          }
          
          li {
            margin-bottom: 4px;
          }
          
          p {
            margin-bottom: 8px;
          }
        }
      }
    }
  }
</style>
