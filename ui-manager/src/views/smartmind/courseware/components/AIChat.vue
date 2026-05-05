<template>
  <div class="ai-chat-container">
    <!-- 聊天记录 -->
    <div class="chat-messages" ref="messagesRef">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="message-item"
        :class="{ user: msg.role === 'user', assistant: msg.role === 'assistant' }"
      >
        <div class="message-avatar">
          <UserOutlined v-if="msg.role === 'user'" />
          <RobotOutlined v-else />
        </div>
        <div class="message-content">
          <div class="message-text" v-html="renderMarkdown(msg.content)"></div>
          <div class="message-actions" v-if="msg.role === 'assistant' && msg.isComplete">
            <a-button type="link" size="small" @click="applyContent(msg.content)">
              <CheckOutlined />应用
            </a-button>
          </div>
        </div>
      </div>
      <div v-if="isGenerating" class="message-item assistant">
        <div class="message-avatar">
          <RobotOutlined />
        </div>
        <div class="message-content">
          <a-spin size="small" />
          <span class="generating-text">AI 生成中...</span>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-area">
      <div class="quick-actions">
        <a-space wrap size="small">
          <a-tag
            v-for="prompt in quickPrompts"
            :key="prompt.key"
            class="quick-prompt"
            @click="useQuickPrompt(prompt)"
          >
            {{ prompt.label }}
          </a-tag>
        </a-space>
      </div>
      <div class="input-wrapper">
        <a-textarea
          v-model:value="inputMessage"
          :rows="5"
          placeholder="输入需求，如：生成集合的概念课件..."
          @pressEnter="handleSend"
          :disabled="isGenerating"
          class="chat-textarea"
        />
        <a-button
          type="primary"
          class="send-btn"
          :loading="isGenerating"
          @click="handleSend"
          size="small"
        >
          <SendOutlined />
        </a-button>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { UserOutlined, RobotOutlined, SendOutlined, CheckOutlined } from '@ant-design/icons-vue';
  import { marked } from 'marked';

  const props = defineProps({
    courseId: {
      type: [String, Number],
      default: null,
    },
    unitId: {
      type: [String, Number],
      default: null,
    },
    unitCode: {
      type: String,
      default: null,
    },
  });

  const emit = defineEmits(['generate', 'apply']);

  // 数据
  const messages = ref([]);
  const inputMessage = ref('');
  const isGenerating = ref(false);
  const messagesRef = ref(null);

  // 快捷提示词
  const quickPrompts = [
    { key: 'intro', label: '📖 导入' },
    { key: 'content', label: '📝 新授' },
    { key: 'practice', label: '✏️ 练习' },
    { key: 'summary', label: '📋 小结' },
    { key: 'homework', label: '🏠 作业' },
  ];

  // 渲染 Markdown
  function renderMarkdown(content) {
    if (!content) return '';
    try {
      return marked(content);
    } catch (e) {
      return content;
    }
  }

  // 使用快捷提示
  function useQuickPrompt(prompt) {
    const prompts = {
      intro: '请帮我生成课程导入环节的内容，要求生动有趣，能吸引学生注意力。',
      content: '请帮我生成新授内容，包含核心概念讲解、例题演示、注意事项等。',
      practice: '请帮我生成课堂练习题，包含基础题、提高题、拓展题三个层次。',
      summary: '请帮我生成课堂小结，梳理本节课的重点知识和方法。',
      homework: '请帮我生成课后作业，难度适中，能巩固本节课所学内容。',
    };
    inputMessage.value = prompts[prompt.key];
  }

  // 发送消息
  async function handleSend(e) {
    if (e.shiftKey) return; // Shift+Enter 换行
    e.preventDefault();

    const content = inputMessage.value.trim();
    if (!content) {
      message.warning('请输入内容');
      return;
    }

    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: content,
    });

    inputMessage.value = '';
    isGenerating.value = true;

    // 添加 AI 消息占位
    const aiMessageIndex = messages.value.length;
    messages.value.push({
      role: 'assistant',
      content: '',
      isComplete: false,
    });

    try {
      // 调用后端 API 流式生成
      const courseId = props.courseId;
      const unitCode = props.unitCode;
      const url = `/api/smartmind/ai/stream/ppt?courseId=${courseId}&unitCode=${unitCode}&userPrompt=${encodeURIComponent(content)}`;
      
      console.log('[PPT生成] 请求URL:', url);
      
      const GLOBAL_HEADER = 'layout: true\nclass: typo, typo-selection\n---\n';
      
      // 使用 fetch + ReadableStream 处理 SSE
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 180000); // 3分钟超时
      
      const response = await fetch(url, {
        headers: {
          'Accept': 'text/event-stream'
        },
        signal: controller.signal
      });
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const reader = response.body.getReader();
      const decoder = new TextDecoder();
      let buffer = '';
      let chunkCount = 0;
      
      while (true) {
        const { done, value } = await reader.read();
        
        if (done) {
          console.log('[PPT生成] 流结束，总chunk数:', chunkCount);
          console.log('[PPT生成] 最终内容长度:', messages.value[aiMessageIndex].content.length);
          break;
        }
        
        chunkCount++;
        const chunk = decoder.decode(value, { stream: true });
        buffer += chunk;
        
        // 按行处理 SSE 数据
        const lines = buffer.split('\n');
        buffer = lines.pop() || ''; // 保留最后一行（可能不完整）
        
        for (const line of lines) {
          const trimmedLine = line.trim();
          
          // 跳过空行和注释行
          if (!trimmedLine || trimmedLine.startsWith(':')) {
            continue;
          }
          
          if (trimmedLine.startsWith('data:')) {
            // 去掉 "data:" 前缀（可能是 "data:" 或 "data: "）
            let data = trimmedLine.substring(5).trimStart();
            
            // 如果数据被双引号包裹，去掉引号
            if (data.startsWith('"') && data.endsWith('"')) {
              data = data.slice(1, -1);
            }
            
            // 处理转义字符：将 \n 转换为实际换行符
            data = data.replace(/\\n/g, '\n');
            
            // 按行分割，去掉每一行开头的 "data:" 前缀
            // 这是因为后端错误地将 SSE 前缀包含在内容中
            const lines = data.split('\n');
            const cleanedLines = lines.map(line => {
              const trimmed = line.trimStart();
              if (trimmed.startsWith('data:')) {
                // 去掉 "data:" 前缀，并trim掉可能的空格
                return trimmed.substring(5).trimStart();
              }
              return line;
            });
            data = cleanedLines.join('\n');
            
            if (data === '[DONE]') {
              clearTimeout(timeoutId);
              console.log('[PPT生成] 收到[DONE]标记');
              console.log('[PPT生成] 原始内容长度:', messages.value[aiMessageIndex].content.length);
              
              // 清理内容：移除 markdown 代码块标记
              let cleanedContent = messages.value[aiMessageIndex].content;
              cleanedContent = cleanedContent.replace(/^```markdown\s*/, '');
              cleanedContent = cleanedContent.replace(/^```\s*/, '');
              cleanedContent = cleanedContent.replace(/\s*```\s*$/, '');
              cleanedContent = cleanedContent.trim();
              
              console.log('[PPT生成] 清理后内容长度:', cleanedContent.length);
              console.log('[PPT生成] 清理后内容前100字符:', cleanedContent.substring(0, 100));
              
              // 添加第一页固定设置
              const fullContent = GLOBAL_HEADER + cleanedContent;
              console.log('[PPT生成] 最终内容长度:', fullContent.length);
              
              emit('generate', fullContent);
              scrollToBottom();
              
              messages.value[aiMessageIndex].isComplete = true;
              isGenerating.value = false;
              return;
            }
            
            if (data) {
              messages.value[aiMessageIndex].content += data;
              scrollToBottom();
            }
          }
        }
      }
    } catch (e) {
      console.error('[PPT生成] 异常:', e);
      message.error('生成失败：' + e.message);
      isGenerating.value = false;
    }
  }

  // 模拟生成内容
  function generateMockContent() {
    return `class: center, middle

# AI 生成的课件内容

---

## 学习目标

- 理解核心概念
- 掌握基本方法
- 能够解决相关问题

---

## 引入新课

**思考问题：**

1. 生活中的相关例子
2. 数学中的表现形式
3. 如何解决这类问题

---

## 核心内容

1. **概念讲解**
   - 定义和性质
   - 特点和规律

2. **例题演示**
   - 典型例题分析
   - 解题步骤详解

3. **注意事项**
   - 易错点提醒
   - 解题技巧总结

---

## 课堂练习

**基础题：**
1. 练习题 1
2. 练习题 2

**提高题：**
1. 拓展练习 1
2. 拓展练习 2

---

## 课堂小结

- 重点知识回顾
- 方法技巧总结
- 下节课预告`;
  }

  // 应用内容到编辑器
  function applyContent(content) {
    emit('apply', content);
    message.success('内容已应用');
  }

  // 滚动到底部
  function scrollToBottom() {
    nextTick(() => {
      const container = messagesRef.value;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    });
  }
</script>

<style scoped lang="less">
  .ai-chat-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .chat-messages {
      flex: 1;
      overflow-y: auto;
      padding: 14px;
      background: #fafafa;

      .message-item {
        display: flex;
        gap: 10px;
        margin-bottom: 14px;

        .message-avatar {
          width: 34px;
          height: 34px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 16px;
          flex-shrink: 0;
        }

        .message-content {
          flex: 1;
          min-width: 0;
          padding: 10px 14px;
          border-radius: 8px;
          font-size: 14px;
          line-height: 1.6;

          .message-text {
            :deep(p) {
              margin-bottom: 4px;

              &:last-child {
                margin-bottom: 0;
              }
            }

            :deep(pre) {
              background: #f5f5f5;
              padding: 6px;
              border-radius: 3px;
              overflow-x: auto;
              margin: 4px 0;
              font-size: 11px;
            }

            :deep(code) {
              background: #f5f5f5;
              padding: 1px 3px;
              border-radius: 2px;
              font-family: monospace;
              font-size: 11px;
            }
          }

          .message-actions {
            margin-top: 4px;
            padding-top: 4px;
            border-top: 1px dashed rgba(0, 0, 0, 0.06);

            :deep(.ant-btn) {
              padding: 0;
              font-size: 12px;
              height: 20px;
            }
          }

          .generating-text {
            margin-left: 6px;
            color: #8c8c8c;
            font-size: 12px;
          }
        }

        &.user {
          flex-direction: row-reverse;

          .message-avatar {
            background: #1890ff;
            color: #fff;
          }

          .message-content {
            background: #e6f7ff;
            border: 1px solid #91d5ff;
          }
        }

        &.assistant {
          .message-avatar {
            background: #52c41a;
            color: #fff;
          }

          .message-content {
            background: #f6ffed;
            border: 1px solid #b7eb8f;
          }
        }
      }
    }

    .chat-input-area {
      padding: 8px;
      background: #fff;
      border-top: 1px solid #f0f0f0;

      .quick-actions {
        margin-bottom: 6px;

        .quick-prompt {
          cursor: pointer;
          transition: all 0.3s;
          font-size: 11px;
          padding: 0 6px;
          line-height: 20px;
          margin-bottom: 4px;

          &:hover {
            color: #1890ff;
            border-color: #1890ff;
          }
        }
      }

      .input-wrapper {
        position: relative;

        .chat-textarea {
          padding-right: 40px;
          resize: none;
          font-size: 14px;
        }

        .send-btn {
          position: absolute;
          right: 6px;
          bottom: 6px;
          width: 28px;
          height: 28px;
          padding: 0;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }
</style>
