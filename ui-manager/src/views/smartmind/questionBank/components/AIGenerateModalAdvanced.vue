<template>
  <a-modal
    v-model:visible="visibleInner"
    title="AI 智能出题"
    @ok="handleCreate"
    @cancel="handleCancel"
    width="1200px"
    :confirm-loading="creatingLoading"
    ok-text="批量创建"
    :ok-button-disabled="selectedCount === 0"
    :mask-closable="false"
  >
    <div class="ai-generate-advanced">
      <a-row :gutter="20">
        <!-- 左侧：出题配置区 -->
        <a-col :span="8">
          <div class="config-panel">
            <div class="panel-title">
              <BookOutlined /> 出题配置
            </div>

            <a-form :model="configForm" layout="vertical">
              <!-- 题目类型 -->
              <a-form-item label="题目类型">
                <div class="type-selector">
                  <a-checkbox-group v-model:value="configForm.questionTypes">
                    <a-checkbox v-for="item in questionTypeOptions" :key="item.value" :value="item.value" class="type-checkbox">
                      {{ item.label }}
                    </a-checkbox>
                  </a-checkbox-group>
                </div>
              </a-form-item>

              <!-- 出题总数 -->
              <a-form-item label="出题总数">
                <div class="total-count-wrapper">
                  <a-input-number v-model:value="configForm.totalCount" :min="1" :max="50" class="total-count-input" />
                  <div class="quick-count-btns">
                    <a-button size="small" @click="configForm.totalCount = 5">5</a-button>
                    <a-button size="small" @click="configForm.totalCount = 10">10</a-button>
                    <a-button size="small" @click="configForm.totalCount = 15">15</a-button>
                    <a-button size="small" @click="configForm.totalCount = 20">20</a-button>
                  </div>
                </div>
              </a-form-item>

              <!-- 难度等级选择模式 -->
              <a-form-item label="难度配置模式">
                <a-radio-group v-model:value="configForm.difficultyMode" button-style="solid" size="small">
                  <a-radio-button value="distribution">分布模式</a-radio-button>
                  <a-radio-button value="range">范围模式</a-radio-button>
                </a-radio-group>
              </a-form-item>

              <!-- 难度分布 -->
              <a-form-item v-if="configForm.difficultyMode === 'distribution'" label="难度分布">
                <div class="distribution-section">
                  <div class="distribution-header">
                    <a-button type="link" size="small" @click="averageDifficulty">
                      <ThunderboltOutlined /> 一键平均分布
                    </a-button>
                  </div>
                  <div v-for="item in difficultyOptions" :key="item.value" class="distribution-item">
                    <span class="dist-label">{{ item.label }}</span>
                    <a-slider
                      v-model:value="configForm.difficultyDistribution[item.value]"
                      :min="0"
                      :max="configForm.totalCount"
                      class="dist-slider"
                      @change="onDifficultyChange"
                    />
                    <span class="dist-value">{{ configForm.difficultyDistribution[item.value] || 0 }}题</span>
                  </div>
                  <div class="distribution-total">
                    合计：<span :class="{ 'total-error': difficultyTotal !== configForm.totalCount }">{{ difficultyTotal }}</span> / {{ configForm.totalCount }}题
                  </div>
                </div>
              </a-form-item>

              <!-- 难度等级范围 -->
              <a-form-item v-if="configForm.difficultyMode === 'range'" label="难度等级范围">
                <a-select
                  v-model:value="configForm.difficultyLevels"
                  mode="multiple"
                  placeholder="选择难度等级范围"
                  :options="difficultyOptions"
                />
              </a-form-item>

              <!-- 认知层次选择模式 -->
              <a-form-item label="认知层次配置模式">
                <a-radio-group v-model:value="configForm.cognitiveMode" button-style="solid" size="small">
                  <a-radio-button value="distribution">分布模式</a-radio-button>
                  <a-radio-button value="range">范围模式</a-radio-button>
                </a-radio-group>
              </a-form-item>

              <!-- 认知层次分布 -->
              <a-form-item v-if="configForm.cognitiveMode === 'distribution'" label="认知层次分布">
                <div class="distribution-section">
                  <div class="distribution-header">
                    <a-button type="link" size="small" @click="averageCognitive">
                      <ThunderboltOutlined /> 一键平均分布
                    </a-button>
                  </div>
                  <div v-for="item in cognitiveOptions" :key="item.value" class="distribution-item">
                    <span class="dist-label">{{ item.label }}</span>
                    <a-slider
                      v-model:value="configForm.cognitiveDistribution[item.value]"
                      :min="0"
                      :max="configForm.totalCount"
                      class="dist-slider"
                      @change="onCognitiveChange"
                    />
                    <span class="dist-value">{{ configForm.cognitiveDistribution[item.value] || 0 }}题</span>
                  </div>
                  <div class="distribution-total">
                    合计：<span :class="{ 'total-error': cognitiveTotal !== configForm.totalCount }">{{ cognitiveTotal }}</span> / {{ configForm.totalCount }}题
                  </div>
                </div>
              </a-form-item>

              <!-- 认知层次范围 -->
              <a-form-item v-if="configForm.cognitiveMode === 'range'" label="认知层次范围">
                <a-select
                  v-model:value="configForm.cognitiveLevels"
                  mode="multiple"
                  placeholder="选择认知层次范围"
                  :options="cognitiveOptions"
                />
              </a-form-item>

              <!-- 内容说明 -->
              <a-form-item label="内容说明">
                <a-textarea
                  v-model:value="configForm.contentDescription"
                  placeholder="请输入出题要求，如：重点考察集合的基本运算"
                  :rows="3"
                  :maxlength="500"
                  show-count
                />
              </a-form-item>

              <!-- 生成按钮 -->
              <a-button type="primary" @click="handleGenerate" :loading="generatingLoading" block size="large">
                <ThunderboltOutlined /> AI 生成题目
              </a-button>
            </a-form>
          </div>
        </a-col>

        <!-- 右侧：生成结果区 -->
        <a-col :span="16">
          <div class="result-panel">
            <div class="panel-header">
              <div class="panel-title">
                <FileTextOutlined /> 生成结果
                <a-badge :count="selectedCount" :number-style="{ backgroundColor: '#52c41a' }" />
              </div>
              <div class="panel-actions">
                <a-checkbox v-model:checked="selectAll" @change="onSelectAllChange">全选</a-checkbox>
                <a-button danger size="small" :disabled="selectedCount === 0" @click="batchDelete">
                  <DeleteOutlined /> 删除选中
                </a-button>
                <a-button size="small" @click="clearAll">
                  <ClearOutlined /> 清空
                </a-button>
              </div>
            </div>

            <!-- 空状态 -->
            <a-empty v-if="generatedQuestions.length === 0" description="点击左侧「AI 生成题目」按钮开始出题" class="empty-state" />

            <!-- 题目列表 -->
            <div v-else class="question-list">
              <div
                v-for="(q, idx) in generatedQuestions"
                :key="idx"
                class="question-card"
                :class="{ 
                  'question-deleted': q.deleted,
                  'question-selected': q.selected && !q.deleted
                }"
              >
                <div class="question-card-header">
                  <a-checkbox v-model:checked="q.selected" :disabled="q.deleted" @change="onSelectChange" />
                  <span class="question-index">第{{ idx + 1 }}题</span>
                  <div class="question-tags">
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
                  <div class="question-actions">
                    <a-button type="link" size="small" @click="toggleEdit(idx)">
                      <EditOutlined />
                    </a-button>
                    <a-button type="link" danger size="small" @click="deleteQuestion(idx)">
                      <DeleteOutlined />
                    </a-button>
                  </div>
                </div>

                <!-- 题目内容 -->
                <div class="question-content">
                  <div class="content-text">{{ q.questionContent }}</div>
                  
                  <!-- 选项展示 -->
                  <div v-if="q.options && q.options.length > 0" class="options-list">
                    <div v-for="(opt, optIdx) in q.options" :key="optIdx" class="option-item">
                      <span class="option-label">{{ String.fromCharCode(65 + optIdx) }}.</span>
                      <span class="option-text">{{ opt }}</span>
                    </div>
                  </div>
                </div>

                <!-- 答案和解析 -->
                <div class="question-footer">
                  <div class="footer-item">
                    <span class="footer-label">答案：</span>
                    <span class="footer-value answer-value">{{ q.answer }}</span>
                  </div>
                  <div class="footer-item">
                    <span class="footer-label">分值：</span>
                    <span class="footer-value">{{ q.score }}分</span>
                  </div>
                  <div v-if="q.answerAnalysis" class="footer-item analysis-item">
                    <span class="footer-label">解析：</span>
                    <span class="footer-value">{{ q.answerAnalysis }}</span>
                  </div>
                </div>

                <!-- 行内编辑表单 -->
                <div v-if="q.editing" class="edit-form">
                  <a-divider>编辑题目</a-divider>
                  <a-form :model="q" layout="vertical">
                    <a-form-item label="题目内容">
                      <a-textarea v-model:value="q.questionContent" :rows="3" />
                    </a-form-item>
                    <a-form-item v-if="q.options && q.options.length > 0" label="选项">
                      <a-textarea
                        v-for="(opt, optIdx) in q.options"
                        :key="optIdx"
                        v-model:value="q.options[optIdx]"
                        :placeholder="'选项' + String.fromCharCode(65 + optIdx)"
                        :rows="1"
                        class="option-edit-input"
                      />
                    </a-form-item>
                    <a-form-item label="答案">
                      <a-input v-model:value="q.answer" />
                    </a-form-item>
                    <a-form-item label="分值">
                      <a-input-number v-model:value="q.score" :min="1" :max="20" style="width: 100%" />
                    </a-form-item>
                    <a-form-item label="解析">
                      <a-textarea v-model:value="q.answerAnalysis" :rows="2" />
                    </a-form-item>
                    <a-form-item>
                      <a-button type="primary" size="small" @click="saveEdit(idx)">保存</a-button>
                      <a-button size="small" style="margin-left: 8px" @click="cancelEdit(idx)">取消</a-button>
                    </a-form-item>
                  </a-form>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script setup>
  import { ref, computed, watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { 
    ThunderboltOutlined, 
    DeleteOutlined, 
    EditOutlined, 
    ClearOutlined,
    BookOutlined,
    FileTextOutlined 
  } from '@ant-design/icons-vue';
  import { postRequest } from '/@/lib/axios';
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
  
  const questionTypeOptions = computed(() => {
    const data = dictStore.getDictData('question_type');
    const seen = new Set();
    return data.filter(item => {
      if (seen.has(item.dataValue)) return false;
      seen.add(item.dataValue);
      return true;
    }).map(item => ({ label: item.dataLabel, value: item.dataValue }));
  });
  
  const difficultyOptions = computed(() => {
    const data = dictStore.getDictData('difficulty_level');
    const seen = new Set();
    return data.filter(item => {
      if (seen.has(item.dataValue)) return false;
      seen.add(item.dataValue);
      return true;
    }).map(item => ({ label: item.dataLabel, value: item.dataValue }));
  });
  
  const cognitiveOptions = computed(() => {
    const data = dictStore.getDictData('cognitive_level');
    const seen = new Set();
    return data.filter(item => {
      if (seen.has(item.dataValue)) return false;
      seen.add(item.dataValue);
      return true;
    }).map(item => ({ label: item.dataLabel, value: item.dataValue }));
  });

  const configForm = ref({
    questionTypes: ['1'],
    totalCount: 5,
    difficultyMode: 'distribution',
    difficultyDistribution: { '1': 0, '2': 0, '3': 0, '4': 0, '5': 0 },
    difficultyLevels: [],
    cognitiveMode: 'distribution',
    cognitiveDistribution: { '1': 0, '2': 0, '3': 0, '4': 0, '5': 0, '6': 0 },
    cognitiveLevels: [],
    contentDescription: '',
  });

  const generatingLoading = ref(false);
  const creatingLoading = ref(false);
  const generatedQuestions = ref([]);
  const selectAll = ref(false);

  const difficultyTotal = computed(() => {
    return Object.values(configForm.value.difficultyDistribution).reduce((sum, val) => sum + (val || 0), 0);
  });

  const cognitiveTotal = computed(() => {
    return Object.values(configForm.value.cognitiveDistribution).reduce((sum, val) => sum + (val || 0), 0);
  });

  const selectedCount = computed(() => {
    return generatedQuestions.value.filter(q => q.selected && !q.deleted).length;
  });

  watch(() => configForm.value.totalCount, (newVal) => {
    const currentDifficultyTotal = difficultyTotal.value;
    if (currentDifficultyTotal !== newVal && currentDifficultyTotal > 0) {
      const ratio = newVal / currentDifficultyTotal;
      Object.keys(configForm.value.difficultyDistribution).forEach(key => {
        configForm.value.difficultyDistribution[key] = Math.round(
          (configForm.value.difficultyDistribution[key] || 0) * ratio
        );
      });
    }

    const currentCognitiveTotal = cognitiveTotal.value;
    if (currentCognitiveTotal !== newVal && currentCognitiveTotal > 0) {
      const ratio = newVal / currentCognitiveTotal;
      Object.keys(configForm.value.cognitiveDistribution).forEach(key => {
        configForm.value.cognitiveDistribution[key] = Math.round(
          (configForm.value.cognitiveDistribution[key] || 0) * ratio
        );
      });
    }
  });

  function onDifficultyChange() {
    if (difficultyTotal.value !== configForm.value.totalCount) {
      message.warning(`难度分布合计 ${difficultyTotal.value} 题，与出题总数 ${configForm.value.totalCount} 不一致`);
    }
  }

  function onCognitiveChange() {
    if (cognitiveTotal.value !== configForm.value.totalCount) {
      message.warning(`认知层次分布合计 ${cognitiveTotal.value} 题，与出题总数 ${configForm.value.totalCount} 不一致`);
    }
  }

  function averageDifficulty() {
    const count = configForm.value.totalCount;
    const keys = Object.keys(configForm.value.difficultyDistribution);
    const avg = Math.floor(count / keys.length);
    let remainder = count - avg * keys.length;
    keys.forEach(key => {
      configForm.value.difficultyDistribution[key] = avg + (remainder > 0 ? 1 : 0);
      remainder--;
    });
    message.success('已平均分配难度分布');
  }

  function averageCognitive() {
    const count = configForm.value.totalCount;
    const keys = Object.keys(configForm.value.cognitiveDistribution);
    const avg = Math.floor(count / keys.length);
    let remainder = count - avg * keys.length;
    keys.forEach(key => {
      configForm.value.cognitiveDistribution[key] = avg + (remainder > 0 ? 1 : 0);
      remainder--;
    });
    message.success('已平均分配认知层次分布');
  }

  async function handleGenerate() {
    if (!configForm.value.questionTypes || configForm.value.questionTypes.length === 0) {
      message.warning('请至少选择一种题目类型');
      return;
    }
    if (!configForm.value.totalCount || configForm.value.totalCount < 1) {
      message.warning('请输入出题总数');
      return;
    }
    if (configForm.value.difficultyMode === 'distribution' && difficultyTotal.value !== configForm.value.totalCount) {
      message.warning('难度分布合计题数必须与出题总数一致');
      return;
    }
    if (configForm.value.difficultyMode === 'range' && (!configForm.value.difficultyLevels || configForm.value.difficultyLevels.length === 0)) {
      message.warning('请至少选择一个难度等级');
      return;
    }
    if (configForm.value.cognitiveMode === 'distribution' && cognitiveTotal.value !== configForm.value.totalCount) {
      message.warning('认知层次分布合计题数必须与出题总数一致');
      return;
    }
    if (configForm.value.cognitiveMode === 'range' && (!configForm.value.cognitiveLevels || configForm.value.cognitiveLevels.length === 0)) {
      message.warning('请至少选择一个认知层次');
      return;
    }

    generatingLoading.value = true;
    try {
      const requestData = {
        courseId: props.courseId,
        unitCode: props.unitCode,
        questionTypes: configForm.value.questionTypes,
        totalCount: configForm.value.totalCount,
        contentDescription: configForm.value.contentDescription,
      };

      if (configForm.value.difficultyMode === 'distribution') {
        requestData.difficultyDistribution = configForm.value.difficultyDistribution;
      } else {
        requestData.difficultyLevels = configForm.value.difficultyLevels;
      }

      if (configForm.value.cognitiveMode === 'distribution') {
        requestData.cognitiveDistribution = configForm.value.cognitiveDistribution;
      } else {
        requestData.cognitiveLevels = configForm.value.cognitiveLevels;
      }

      const res = await postRequest('/api/smartmind/ai/question/generate', requestData, { timeout: 180000 });
      if (res && res.data && Array.isArray(res.data)) {
        generatedQuestions.value = res.data.map((q, idx) => ({
          ...q,
          options: q.options || [],
          selected: true,
          deleted: false,
          editing: false,
          _originalState: null,
        }));
        selectAll.value = true;
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

  function deleteQuestion(idx) {
    generatedQuestions.value[idx].deleted = true;
    generatedQuestions.value[idx].selected = false;
    updateSelectAllState();
  }

  function batchDelete() {
    const count = generatedQuestions.value.filter(q => q.selected && !q.deleted).length;
    if (count === 0) {
      message.warning('请先选择要删除的题目');
      return;
    }
    generatedQuestions.value.forEach(q => {
      if (q.selected) {
        q.deleted = true;
        q.selected = false;
      }
    });
    selectAll.value = false;
    message.success(`已删除 ${count} 道题目`);
  }

  function clearAll() {
    generatedQuestions.value = [];
    selectAll.value = false;
  }

  function toggleEdit(idx) {
    const q = generatedQuestions.value[idx];
    if (q.editing) {
      cancelEdit(idx);
    } else {
      q._originalState = JSON.parse(JSON.stringify({
        questionContent: q.questionContent,
        options: [...(q.options || [])],
        answer: q.answer,
        score: q.score,
        answerAnalysis: q.answerAnalysis,
      }));
      q.editing = true;
    }
  }

  function saveEdit(idx) {
    const q = generatedQuestions.value[idx];
    q.editing = false;
    q._originalState = null;
    message.success('保存成功');
  }

  function cancelEdit(idx) {
    const q = generatedQuestions.value[idx];
    if (q._originalState) {
      q.questionContent = q._originalState.questionContent;
      q.options = q._originalState.options;
      q.answer = q._originalState.answer;
      q.score = q._originalState.score;
      q.answerAnalysis = q._originalState.answerAnalysis;
    }
    q.editing = false;
    q._originalState = null;
  }

  function onSelectChange() {
    updateSelectAllState();
  }

  function onSelectAllChange(e) {
    const checked = e.target.checked;
    generatedQuestions.value.forEach(q => {
      if (!q.deleted) {
        q.selected = checked;
      }
    });
  }

  function updateSelectAllState() {
    const validQuestions = generatedQuestions.value.filter(q => !q.deleted);
    if (validQuestions.length === 0) {
      selectAll.value = false;
    } else {
      selectAll.value = validQuestions.every(q => q.selected);
    }
  }

  async function handleCreate() {
    const validQuestions = generatedQuestions.value.filter(q => q.selected && !q.deleted);
    if (validQuestions.length === 0) {
      message.warning('请至少选择一道题目');
      return;
    }

    creatingLoading.value = true;
    try {
      const questions = validQuestions.map(q => {
        const formattedOptions = (q.options || []).map((opt, idx) => {
          return String.fromCharCode(65 + idx) + '. ' + opt;
        });
        return {
          questionType: q.questionType,
          courseId: props.courseId,
          unitCode: props.unitCode,
          difficultyLevel: q.difficultyLevel,
          cognitiveLevel: q.cognitiveLevel,
          questionContent: q.questionContent,
          options: JSON.stringify(formattedOptions),
          answer: q.answer,
          score: q.score,
          knowledgePoints: q.knowledgePoints || '',
          answerAnalysis: q.answerAnalysis || '',
          sourceType: '3',
          status: '1',
        };
      });

      const res = await postRequest('/manage/smartmind/question/batchCreate', { questions });
      creatingLoading.value = false;
      message.success(`成功创建 ${questions.length} 道题目`);
      visibleInner.value = false;
      generatedQuestions.value = [];
      selectAll.value = false;
      emit('created');
    } catch (e) {
      creatingLoading.value = false;
      console.error('批量创建失败:', e);
      message.error('批量创建失败');
    }
  }

  function handleCancel() {
    visibleInner.value = false;
    generatedQuestions.value = [];
    selectAll.value = false;
  }

  function getQuestionTypeColor(type) {
    const map = { '1': 'blue', '2': 'purple', '3': 'green', '4': 'orange', '5': 'cyan', '6': 'magenta', '7': 'geekblue', '8': 'volcano' };
    return map[type] || 'default';
  }

  function getDifficultyColor(level) {
    const map = { '1': 'green', '2': 'lime', '3': 'blue', '4': 'orange', '5': 'red' };
    return map[level] || 'default';
  }
</script>

<style scoped lang="less">
  .ai-generate-advanced {
    min-height: 600px;

    .config-panel {
      background: #fafafa;
      border-radius: 8px;
      padding: 16px;
      height: 600px;
      overflow-y: auto;

      .panel-title {
        font-size: 15px;
        font-weight: 500;
        color: #262626;
        margin-bottom: 16px;
        display: flex;
        align-items: center;
        gap: 6px;
      }

      :deep(.ant-form-item) {
        margin-bottom: 16px;
      }

      .type-selector {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;

        :deep(.ant-checkbox-wrapper) {
          margin-right: 0;
          margin-bottom: 0;
        }

        :deep(.ant-checkbox-wrapper:hover) {
          background: transparent;
        }

        :deep(.ant-checkbox-wrapper:hover::before) {
          background: transparent;
        }
      }

      .total-count-wrapper {
        display: flex;
        gap: 8px;
        align-items: center;

        .total-count-input {
          flex: 1;
        }

        .quick-count-btns {
          display: flex;
          gap: 4px;
        }
      }

      .distribution-section {
        .distribution-header {
          margin-bottom: 8px;
        }

        .distribution-item {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 8px;

          .dist-label {
            width: 50px;
            font-size: 13px;
            color: #595959;
            flex-shrink: 0;
          }

          .dist-slider {
            flex: 1;
          }

          .dist-value {
            width: 40px;
            text-align: right;
            font-size: 13px;
            color: #1890ff;
            font-weight: 500;
            flex-shrink: 0;
          }
        }

        .distribution-total {
          margin-top: 8px;
          padding-top: 8px;
          border-top: 1px dashed #d9d9d9;
          font-size: 13px;
          color: #8c8c8c;
          text-align: right;

          .total-error {
            color: #ff4d4f;
            font-weight: 500;
          }
        }
      }
    }

    .result-panel {
      background: #fff;
      border: 1px solid #f0f0f0;
      border-radius: 8px;
      padding: 16px;
      height: 600px;
      display: flex;
      flex-direction: column;

      .panel-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 1px solid #f0f0f0;

        .panel-title {
          font-size: 15px;
          font-weight: 500;
          color: #262626;
          display: flex;
          align-items: center;
          gap: 8px;
        }

        .panel-actions {
          display: flex;
          align-items: center;
          gap: 12px;
        }
      }

      .empty-state {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .question-list {
        flex: 1;
        overflow-y: auto;
        padding-right: 8px;

        .question-card {
          padding: 12px;
          border: 1px solid #f0f0f0;
          border-radius: 6px;
          margin-bottom: 12px;
          background: #fff;
          transition: all 0.3s;

          &:hover {
            border-color: #1890ff;
            box-shadow: 0 2px 8px rgba(24, 144, 255, 0.15);
          }

          &.question-selected {
            border-color: #52c41a;
            background: #f6ffed;
          }

          &.question-deleted {
            opacity: 0.5;
            background: #fafafa;
          }

          .question-card-header {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;

            .question-index {
              font-size: 13px;
              font-weight: 500;
              color: #1890ff;
              flex-shrink: 0;
            }

            .question-tags {
              display: flex;
              gap: 4px;
              flex: 1;
            }

            .question-actions {
              display: flex;
              gap: 4px;
              flex-shrink: 0;
            }
          }

          .question-content {
            margin-bottom: 8px;

            .content-text {
              font-size: 14px;
              line-height: 1.6;
              color: #262626;
              margin-bottom: 8px;
            }

            .options-list {
              .option-item {
                display: flex;
                gap: 4px;
                margin-bottom: 4px;
                font-size: 13px;
                color: #595959;

                .option-label {
                  font-weight: 500;
                  flex-shrink: 0;
                }

                .option-text {
                  flex: 1;
                }
              }
            }
          }

          .question-footer {
            display: flex;
            flex-wrap: wrap;
            gap: 16px;
            padding-top: 8px;
            border-top: 1px dashed #f0f0f0;

            .footer-item {
              font-size: 12px;
              color: #8c8c8c;

              .footer-label {
                color: #595959;
                font-weight: 500;
              }

              .footer-value {
                color: #262626;
              }

              .answer-value {
                color: #52c41a;
                font-weight: 500;
              }
            }

            .analysis-item {
              flex-basis: 100%;
              margin-top: 4px;

              .footer-value {
                color: #595959;
                line-height: 1.5;
              }
            }
          }

          .edit-form {
            margin-top: 12px;
            padding-top: 12px;
            border-top: 1px solid #f0f0f0;

            .option-edit-input {
              margin-bottom: 8px;
            }
          }
        }
      }
    }
  }
</style>
