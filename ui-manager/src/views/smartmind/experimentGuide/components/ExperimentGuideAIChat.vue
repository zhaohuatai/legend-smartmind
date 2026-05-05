<template>
  <div class="experiment-guide-ai-chat">
    <div class="input-wrapper">
      <a-textarea
        v-model:value="inputMessage"
        :rows="3"
        placeholder="输入实验指导书需求，如：生成细胞膜观察实验的指导书..."
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
</template>

<script setup>
  import { ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { SendOutlined } from '@ant-design/icons-vue';

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
    guideData: {
      type: Object,
      default: () => ({}),
    },
    guideId: {
      type: [String, Number],
      default: null,
    },
  });

  const emit = defineEmits(['generate', 'apply']);

  const inputMessage = ref('');
  const isGenerating = ref(false);

  async function handleSend(e) {
    if (e.shiftKey) return;
    e.preventDefault();

    const content = inputMessage.value.trim();
    if (!content) {
      message.warning('请输入内容');
      return;
    }

    if (!props.guideId) {
      message.warning('请先生成实验指导书基础信息并保存');
      return;
    }

    inputMessage.value = '';
    isGenerating.value = true;

    try {
      const url = `/api/smartmind/ai/stream/experiment-guide?guideId=${props.guideId}&courseId=${props.courseId}&unitCode=${props.unitCode}&userPrompt=${encodeURIComponent(content)}`;

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
      let fullContent = '';

      while (true) {
        const { done, value } = await reader.read();

        if (done) {
          break;
        }

        const chunk = decoder.decode(value, { stream: true });
        buffer += chunk;

        const lines = buffer.split('\n');
        buffer = lines.pop() || '';

        for (const line of lines) {
          const trimmedLine = line.trim();

          if (!trimmedLine || trimmedLine.startsWith(':')) {
            continue;
          }

          if (trimmedLine.startsWith('data:')) {
            let data = trimmedLine.substring(5).trimStart();

            if (data.startsWith('"') && data.endsWith('"')) {
              data = data.slice(1, -1);
            }

            data = data.replace(/\\n/g, '\n');

            const dataLines = data.split('\n');
            const cleanedLines = dataLines.map(line => {
              const trimmed = line.trimStart();
              if (trimmed.startsWith('data:')) {
                return trimmed.substring(5).trimStart();
              }
              return line;
            });
            data = cleanedLines.join('\n');

            if (data === '[DONE]') {
              clearTimeout(timeoutId);
              let cleanedContent = fullContent;
              cleanedContent = cleanedContent.replace(/^```markdown\s*/, '');
              cleanedContent = cleanedContent.replace(/^```\s*/, '');
              cleanedContent = cleanedContent.replace(/\s*```\s*$/, '');
              cleanedContent = cleanedContent.trim();

              emit('generate', cleanedContent);
              isGenerating.value = false;
              return;
            }

            if (data) {
              fullContent += data;
              emit('apply', fullContent);
            }
          }
        }
      }
    } catch (e) {
      console.error('实验指导书生成异常:', e);
      message.error('生成失败：' + e.message);
      isGenerating.value = false;
    }
  }
</script>

<style scoped lang="less">
  .experiment-guide-ai-chat {
    height: 100%;
    padding: 8px;
    background: #fff;
    display: flex;
    flex-direction: column;

    .input-wrapper {
      flex: 1;
      position: relative;

      .chat-textarea {
        width: 100%;
        height: 100%;
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
</style>
