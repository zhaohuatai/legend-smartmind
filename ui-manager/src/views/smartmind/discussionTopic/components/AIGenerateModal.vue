<template>
  <a-modal
    v-model:visible="visibleInner"
    title="AI 智能生成讨论话题"
    @ok="handleCreate"
    @cancel="handleCancel"
    width="800px"
    :confirm-loading="creatingLoading"
    ok-text="创建话题"
    :ok-button-disabled="!generatedTopic"
    :mask-closable="false"
  >
    <div class="ai-generate-topic">
      <!-- 配置区 -->
      <a-form :model="configForm" layout="vertical" class="config-form">
        <!-- 话题类型 -->
        <a-form-item label="话题类型">
          <DictSelect v-model:value="configForm.topicType" dict-code="discussion_topic_type" placeholder="请选择话题类型" width="100%" />
        </a-form-item>

        <!-- 内容说明 -->
        <a-form-item label="内容说明">
          <a-textarea
            v-model:value="configForm.contentDescription"
            :rows="3"
            placeholder="请输入讨论话题相关的主题或知识点，如：细胞膜的功能、光合作用等"
          />
        </a-form-item>

        <!-- 预计时长、小组人数 -->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="预计讨论时长">
              <a-input-number v-model:value="configForm.estimatedDuration" :min="5" :max="120" style="width: 100%" addon-after="分钟" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="建议小组人数">
              <a-input-number v-model:value="configForm.groupSize" :min="2" :max="10" style="width: 100%" addon-after="人" />
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 生成按钮 -->
        <a-form-item>
          <a-button type="primary" block @click="handleGenerate" :loading="generatingLoading" :disabled="!configForm.topicType">
            <ThunderboltOutlined /> 生成话题
          </a-button>
        </a-form-item>
      </a-form>

      <!-- 生成结果区 -->
      <div class="result-panel" v-if="generatedTopic">
        <div class="panel-title">
          <FileTextOutlined /> 生成结果
        </div>

        <div class="topic-card">
          <div class="topic-card-header">
            <a-tag :color="getTopicTypeColor(generatedTopic.topicType)" size="small">
              <DictLabel dict-code="discussion_topic_type" :data-value="generatedTopic.topicType" />
            </a-tag>
            <span class="topic-card-title">{{ generatedTopic.topicName }}</span>
          </div>

          <div class="topic-card-body">
            <div class="topic-field">
              <span class="field-label">话题内容：</span>
              <span class="field-value">{{ generatedTopic.topicContent }}</span>
            </div>
            <div class="topic-field" v-if="generatedTopic.backgroundMaterial">
              <span class="field-label">背景材料：</span>
              <span class="field-value">{{ generatedTopic.backgroundMaterial }}</span>
            </div>
            <div class="topic-field" v-if="generatedTopic.guidanceTips">
              <span class="field-label">引导提示：</span>
              <span class="field-value">{{ generatedTopic.guidanceTips }}</span>
            </div>
            <div class="topic-field" v-if="generatedTopic.expectedAnswers">
              <span class="field-label">预期答案：</span>
              <span class="field-value">{{ generatedTopic.expectedAnswers }}</span>
            </div>
            <div class="topic-field" v-if="generatedTopic.evaluationCriteria">
              <span class="field-label">评价标准：</span>
              <span class="field-value">{{ generatedTopic.evaluationCriteria }}</span>
            </div>
            <div class="topic-meta">
              <span>时长：{{ generatedTopic.estimatedDuration }}分钟</span>
              <span>小组：{{ generatedTopic.groupSize }}人</span>
              <span v-if="generatedTopic.knowledgePoints">知识点：{{ generatedTopic.knowledgePoints }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <a-empty v-else description="配置上方参数后点击「生成话题」按钮开始生成" class="empty-state" />
    </div>
  </a-modal>
</template>

<script setup>
  import { ref, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { postRequest } from '/@/lib/axios';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import DictLabel from '/@/components/support/dict-label/index.vue';
  import {
    ThunderboltOutlined,
    FileTextOutlined,
  } from '@ant-design/icons-vue';

  const props = defineProps({
    visible: {
      type: Boolean,
      default: false,
    },
    courseId: {
      type: [Number, String],
      required: true,
    },
    unitCode: {
      type: String,
      required: true,
    },
  });

  const emit = defineEmits(['update:visible', 'created']);

  const visibleInner = computed({
    get: () => props.visible,
    set: (val) => emit('update:visible', val),
  });

  const generatingLoading = ref(false);
  const creatingLoading = ref(false);

  const configForm = ref({
    topicType: '',
    contentDescription: '',
    estimatedDuration: 15,
    groupSize: 4,
  });

  const generatedTopic = ref(null);

  function getTopicTypeColor(type) {
    const map = { '1': 'blue', '2': 'purple', '3': 'orange', '4': 'cyan', '5': 'magenta' };
    return map[type] || 'default';
  }

  async function handleGenerate() {
    if (!configForm.value.topicType) {
      message.warning('请选择话题类型');
      return;
    }

    generatingLoading.value = true;
    try {
      const requestData = {
        courseId: props.courseId,
        unitCode: props.unitCode,
        topicType: configForm.value.topicType,
        contentDescription: configForm.value.contentDescription,
        estimatedDuration: configForm.value.estimatedDuration,
        groupSize: configForm.value.groupSize,
      };

      const res = await postRequest('/api/smartmind/ai/topic/generate', requestData, { timeout: 180000 });
      if (res && res.data && res.data.topicName) {
        generatedTopic.value = res.data;
        message.success('话题生成成功');
      } else {
        message.warning('未生成有效话题');
      }
    } catch (e) {
      console.error('生成话题失败:', e);
      message.error('生成话题失败，请重试');
    } finally {
      generatingLoading.value = false;
    }
  }

  async function handleCreate() {
    if (!generatedTopic.value) {
      message.warning('请先生成话题');
      return;
    }

    creatingLoading.value = true;
    try {
      const topic = {
        topicName: generatedTopic.value.topicName,
        courseId: props.courseId,
        unitCode: props.unitCode,
        topicType: generatedTopic.value.topicType,
        topicContent: generatedTopic.value.topicContent,
        estimatedDuration: generatedTopic.value.estimatedDuration,
        groupSize: generatedTopic.value.groupSize,
        backgroundMaterial: generatedTopic.value.backgroundMaterial || '',
        guidanceTips: generatedTopic.value.guidanceTips || '',
        expectedAnswers: generatedTopic.value.expectedAnswers || '',
        evaluationCriteria: generatedTopic.value.evaluationCriteria || '',
        knowledgePoints: generatedTopic.value.knowledgePoints || '',
        status: '1',
      };

      await postRequest('/manage/smartmind/discussionTopic/create', topic);
      creatingLoading.value = false;
      message.success('创建成功');
      visibleInner.value = false;
      generatedTopic.value = null;
      emit('created');
    } catch (e) {
      creatingLoading.value = false;
      console.error('创建失败:', e);
      message.error('创建失败');
    }
  }

  function handleCancel() {
    visibleInner.value = false;
    generatedTopic.value = null;
  }
</script>

<style scoped lang="less">
.ai-generate-topic {
  min-height: 400px;
}

.config-form {
  margin-bottom: 24px;
}

.result-panel {
  .panel-title {
    font-size: 16px;
    font-weight: 600;
    color: #262626;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.empty-state {
  padding: 40px 0;
}

.topic-card {
  padding: 16px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  background: #fff;
}

.topic-card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.topic-card-title {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
  color: #262626;
}

.topic-card-body {
  .topic-field {
    margin-bottom: 8px;
    font-size: 13px;
    line-height: 1.6;

    .field-label {
      color: #8c8c8c;
      margin-right: 4px;
    }

    .field-value {
      color: #262626;
    }
  }

  .topic-meta {
    display: flex;
    gap: 16px;
    margin-top: 12px;
    padding-top: 8px;
    border-top: 1px solid #f0f0f0;
    font-size: 12px;
    color: #bfbfbf;
  }
}
</style>
