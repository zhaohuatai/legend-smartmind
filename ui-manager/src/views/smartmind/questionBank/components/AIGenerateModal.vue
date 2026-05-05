<template>
  <a-modal
    v-model:visible="visibleInner"
    title="AI 智能出题"
    @ok="handleCreate"
    @cancel="handleCancel"
    width="900px"
    :confirm-loading="creatingLoading"
    ok-text="批量创建"
  >
    <div class="ai-generate-modal">
      <!-- 出题配置区 -->
      <div class="config-section">
        <a-form :model="configForm" layout="horizontal" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="题目类型">
                <a-select
                  v-model:value="configForm.questionTypes"
                  mode="multiple"
                  placeholder="选择题目类型"
                  :options="questionTypeOptions"
                />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="出题总数">
                <a-input-number v-model:value="configForm.totalCount" :min="1" :max="50" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="难度等级">
                <DictSelect v-model:value="configForm.difficultyLevel" dict-code="difficulty_level" placeholder="自动分配" width="100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="认知层次">
                <a-select
                  v-model:value="configForm.cognitiveLevels"
                  mode="multiple"
                  placeholder="自动分配"
                  :options="cognitiveLevelOptions"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="内容说明">
                <a-input v-model:value="configForm.contentDescription" placeholder="请输入出题要求，如：重点考察集合的基本运算" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <a-button type="primary" @click="handleGenerate" :loading="generatingLoading" block size="large">
          <ThunderboltOutlined /> AI 生成题目
        </a-button>
      </div>

      <!-- 生成结果列表 -->
      <div class="result-section" v-if="generatedQuestions.length > 0">
        <div class="result-header">
          <span class="result-title">生成结果（{{ generatedQuestions.length }}题）</span>
          <a-button danger size="small" @click="clearGenerated">
            <DeleteOutlined /> 清空
          </a-button>
        </div>
        <div class="result-list">
          <div
            v-for="(q, idx) in generatedQuestions"
            :key="idx"
            class="result-item"
            :class="{ deleted: q._deleted }"
          >
            <div class="result-item-header">
              <span class="item-index">第{{ idx + 1 }}题</span>
              <div class="item-tags">
                <a-tag :color="getQuestionTypeColor(q.questionType)" size="small">
                  <DictLabel dict-code="question_type" :data-value="q.questionType" />
                </a-tag>
                <a-tag :color="getDifficultyColor(q.difficultyLevel)" size="small">
                  <DictLabel dict-code="difficulty_level" :data-value="q.difficultyLevel" />
                </a-tag>
                <a-tag color="purple" size="small">
                  <DictLabel dict-code="cognitive_level" :data-value="q.cognitiveLevel" />
                </a-tag>
              </div>
              <a-button type="link" danger size="small" @click="removeQuestion(idx)">
                <DeleteOutlined />
              </a-button>
            </div>
            <div class="result-item-content">{{ q.questionContent }}</div>
            <div class="result-item-footer">
              <span class="item-meta">分值：{{ q.score }}</span>
              <span class="item-meta">答案：{{ q.answer }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
  import { ref, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { ThunderboltOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { getRequest, postRequest } from '/@/lib/axios';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import DictLabel from '/@/components/support/dict-label/index.vue';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const props = defineProps({
    visible: {
      type: Boolean,
      default: false,
    },
    courseId: {
      type: [String, Number],
      default: null,
    },
    unitCode: {
      type: String,
      default: null,
    },
  });

  const emit = defineEmits(['update:visible', 'created']);

  const visibleInner = computed({
    get: () => props.visible,
    set: (val) => emit('update:visible', val),
  });

  const dictStore = useDictStore();
  const questionTypeOptions = computed(() => 
    dictStore.getDictData('question_type').map(item => ({ label: item.dataLabel, value: item.dataValue }))
  );
  const cognitiveLevelOptions = computed(() => 
    dictStore.getDictData('cognitive_level').map(item => ({ label: item.dataLabel, value: item.dataValue }))
  );

  const configForm = ref({
    questionTypes: ['1', '3'],
    totalCount: 5,
    difficultyLevel: undefined,
    cognitiveLevels: [],
    contentDescription: '',
  });

  const generatingLoading = ref(false);
  const creatingLoading = ref(false);
  const generatedQuestions = ref([]);

  async function handleGenerate() {
    if (!configForm.value.questionTypes || configForm.value.questionTypes.length === 0) {
      message.warning('请至少选择一种题目类型');
      return;
    }
    if (!configForm.value.totalCount || configForm.value.totalCount < 1) {
      message.warning('请输入出题总数');
      return;
    }

    generatingLoading.value = true;
    try {
      const requestData = {
        courseId: props.courseId,
        unitCode: props.unitCode,
        questionTypes: configForm.value.questionTypes,
        totalCount: configForm.value.totalCount,
        difficultyLevel: configForm.value.difficultyLevel,
        cognitiveLevels: configForm.value.cognitiveLevels,
        contentDescription: configForm.value.contentDescription,
      };

      const res = await postRequest('/api/smartmind/ai/question/generate', requestData);
      if (res && res.data && Array.isArray(res.data)) {
        generatedQuestions.value = res.data.map(q => ({ ...q, _deleted: false }));
        message.success(`成功生成 ${res.data.length} 道题目`);
      } else {
        message.error('生成失败，请重试');
      }
    } catch (e) {
      console.error('AI出题异常:', e);
      message.error('生成失败：' + (e.message || '未知错误'));
    } finally {
      generatingLoading.value = false;
    }
  }

  function removeQuestion(idx) {
    generatedQuestions.value[idx]._deleted = true;
  }

  function clearGenerated() {
    generatedQuestions.value = [];
  }

  async function handleCreate() {
    const validQuestions = generatedQuestions.value.filter(q => !q._deleted);
    if (validQuestions.length === 0) {
      message.warning('没有可创建的题目');
      return;
    }

    creatingLoading.value = true;
    try {
      const questions = validQuestions.map(q => ({
        questionType: q.questionType,
        courseId: props.courseId,
        unitCode: props.unitCode,
        difficultyLevel: q.difficultyLevel,
        cognitiveLevel: q.cognitiveLevel,
        questionContent: q.questionContent,
        options: q.options || '[]',
        answer: q.answer,
        score: q.score,
        knowledgePoints: q.knowledgePoints || '',
        answerAnalysis: q.answerAnalysis || '',
        sourceType: '3',
        status: '1',
      }));

      await postRequest('/manage/smartmind/question/batchCreate', { questions });
      message.success(`成功创建 ${questions.length} 道题目`);
      visibleInner.value = false;
      generatedQuestions.value = [];
      emit('created');
    } catch (e) {
      console.error('批量创建失败:', e);
      message.error('批量创建失败');
    } finally {
      creatingLoading.value = false;
    }
  }

  function handleCancel() {
    visibleInner.value = false;
    generatedQuestions.value = [];
  }

  function getQuestionTypeLabel(type) {
    const map = { '1': '单选题', '2': '多选题', '3': '判断题', '4': '填空题', '5': '简答题', '6': '计算题' };
    return map[type] || type;
  }

  function getQuestionTypeColor(type) {
    const map = { '1': 'blue', '2': 'purple', '3': 'green', '4': 'orange', '5': 'cyan', '6': 'magenta' };
    return map[type] || 'default';
  }

  function getDifficultyLabel(level) {
    const map = { '1': '容易', '2': '较易', '3': '中等', '4': '较难', '5': '困难' };
    return map[level] || level;
  }

  function getDifficultyColor(level) {
    const map = { '1': 'green', '2': 'lime', '3': 'blue', '4': 'orange', '5': 'red' };
    return map[level] || 'default';
  }

  function getCognitiveLabel(level) {
    const map = { '1': '识记', '2': '理解', '3': '应用', '4': '分析', '5': '综合', '6': '评价' };
    return map[level] || level;
  }
</script>

<style scoped lang="less">
  .ai-generate-modal {
    .config-section {
      margin-bottom: 20px;

      :deep(.ant-form-item) {
        margin-bottom: 12px;
      }
    }

    .result-section {
      .result-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .result-title {
          font-size: 14px;
          font-weight: 500;
          color: #333;
        }
      }

      .result-list {
        max-height: 400px;
        overflow-y: auto;

        .result-item {
          padding: 12px;
          border: 1px solid #f0f0f0;
          border-radius: 6px;
          margin-bottom: 12px;
          background: #fff;

          &.deleted {
            opacity: 0.5;
            text-decoration: line-through;
          }

          .result-item-header {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;

            .item-index {
              font-size: 13px;
              font-weight: 500;
              color: #1890ff;
            }

            .item-tags {
              display: flex;
              gap: 4px;
              flex: 1;
            }
          }

          .result-item-content {
            font-size: 13px;
            line-height: 1.6;
            color: #595959;
            margin-bottom: 8px;
          }

          .result-item-footer {
            display: flex;
            gap: 16px;

            .item-meta {
              font-size: 12px;
              color: #8c8c8c;
            }
          }
        }
      }
    }
  }
</style>
