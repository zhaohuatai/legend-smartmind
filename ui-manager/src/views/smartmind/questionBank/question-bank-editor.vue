<template>
  <div class="question-bank-page">
    <!-- 顶部：面包屑 + 操作区 -->
    <div class="header-bar">
      <a-breadcrumb>
        <a-breadcrumb-item>
          <router-link :to="`/smartmind/course/detail?courseId=${courseId}`">{{ courseInfo?.courseName || '课程详情' }}</router-link>
        </a-breadcrumb-item>
        <a-breadcrumb-item>{{ currentUnit?.unitName || '单元' }}</a-breadcrumb-item>
        <a-breadcrumb-item>题库</a-breadcrumb-item>
      </a-breadcrumb>

      <div class="header-actions">
        <a-button type="primary" @click="openAIGenerateModal">
          <ThunderboltOutlined /> AI 出题
        </a-button>
        <a-button @click="openCreateModal" style="margin-left: 8px">
          <PlusOutlined /> 新建题目
        </a-button>
      </div>
    </div>

    <!-- 主体内容：左侧（问题列表） + 右侧（问题详情） -->
    <div class="main-content">
      <!-- 左侧：问题列表 -->
      <div class="left-panel">
        <div class="list-header">
          <span class="list-title">题目列表（{{ questionList.length }}）</span>
          <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索题目内容"
            size="small"
            style="width: 200px"
            @search="onSearch"
          />
        </div>
        <div class="question-list">
          <div
            v-for="item in questionList"
            :key="item.id"
            class="question-item"
            :class="{ active: selectedQuestion?.id === item.id }"
            @click="selectQuestion(item)"
          >
            <div class="question-item-header">
              <a-tag :color="getQuestionTypeColor(item.questionType)" size="small">
                <DictLabel dict-code="question_type" :data-value="item.questionType" />
              </a-tag>
              <a-tag :color="getDifficultyColor(item.difficultyLevel)" size="small">
                <DictLabel dict-code="difficulty_level" :data-value="item.difficultyLevel" />
              </a-tag>
            </div>
            <div class="question-item-content">{{ truncateText(item.questionContent, 50) }}</div>
            <div class="question-item-footer">
              <span class="question-item-meta">分值：{{ item.score || '-' }}</span>
              <span class="question-item-meta">使用：{{ item.usageCount || 0 }}次</span>
            </div>
          </div>
          <a-empty v-if="questionList.length === 0" description="暂无题目" class="empty-list" />
        </div>
      </div>

      <!-- 右侧：问题详情 -->
      <div class="right-panel">
        <div class="detail-header" v-if="selectedQuestion">
          <div class="detail-title-row">
            <h3 class="detail-title">题目详情</h3>
            <div class="detail-actions">
              <a-button size="small" @click="openEditModal">
                <EditOutlined /> 编辑
              </a-button>
              <a-popconfirm title="确定删除该题目吗？" @confirm="deleteQuestion">
                <a-button danger size="small" style="margin-left: 8px">
                  <DeleteOutlined /> 删除
                </a-button>
              </a-popconfirm>
            </div>
          </div>
        </div>

        <div class="detail-content" v-if="selectedQuestion">
          <div class="detail-section">
            <div class="section-label">题目信息</div>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">题目类型：</span>
                <span class="info-value"><DictLabel dict-code="question_type" :data-value="selectedQuestion.questionType" /></span>
              </div>
              <div class="info-item">
                <span class="info-label">难度等级：</span>
                <span class="info-value"><DictLabel dict-code="difficulty_level" :data-value="selectedQuestion.difficultyLevel" /></span>
              </div>
              <div class="info-item">
                <span class="info-label">认知层次：</span>
                <span class="info-value"><DictLabel dict-code="cognitive_level" :data-value="selectedQuestion.cognitiveLevel" /></span>
              </div>
              <div class="info-item">
                <span class="info-label">题目分值：</span>
                <span class="info-value">{{ selectedQuestion.score || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">来源类型：</span>
                <span class="info-value">{{ getSourceLabel(selectedQuestion.sourceType) }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">知识点标签：</span>
                <span class="info-value">{{ selectedQuestion.knowledgePoints || '-' }}</span>
              </div>
            </div>
          </div>

          <div class="detail-section">
            <div class="section-label">题目内容</div>
            <div class="content-display">{{ selectedQuestion.questionContent || '暂无内容' }}</div>
          </div>

          <div class="detail-section" v-if="selectedQuestion.options">
            <div class="section-label">选项</div>
            <div class="options-display">
              <div v-for="(opt, idx) in parseOptions(selectedQuestion.options)" :key="idx" class="option-item">
                <span class="option-label">{{ String.fromCharCode(65 + idx) }}.</span>
                <span class="option-text">{{ opt }}</span>
              </div>
            </div>
          </div>

          <div class="detail-section">
            <div class="section-label">参考答案</div>
            <div class="content-display answer">{{ selectedQuestion.answer || '暂无答案' }}</div>
          </div>

          <div class="detail-section" v-if="selectedQuestion.answerAnalysis">
            <div class="section-label">答案解析</div>
            <div class="content-display">{{ selectedQuestion.answerAnalysis }}</div>
          </div>
        </div>

        <div class="empty-detail" v-else>
          <a-empty description="请在左侧选择题目查看详情" />
        </div>
      </div>
    </div>

    <!-- AI出题弹窗 -->
    <AIGenerateModalAdvanced
      v-model:visible="aiGenerateModalVisible"
      :course-id="courseId"
      :unit-code="unitCode"
      @created="loadQuestions"
    />

    <!-- 题目编辑/新建弹窗 -->
    <a-modal
      v-model:visible="editModalVisible"
      :title="isEditModalCreate ? '新建题目' : '编辑题目'"
      @ok="handleModalSubmit"
      @cancel="editModalVisible = false"
      width="720px"
      :confirm-loading="modalSubmitLoading"
    >
      <a-form
        :model="editModalForm"
        :rules="modalFormRules"
        ref="modalFormRef"
        layout="horizontal"
        :label-col="{ style: { width: '80px' } }"
        :wrapper-col="{ style: { flex: 1 } }"
        class="question-form"
      >
        <!-- 第一行：题目类型、难度、认知 -->
        <div class="form-row">
          <a-form-item label="题目类型" name="questionType" class="form-item-type">
            <DictSelect v-model:value="editModalForm.questionType" dict-code="question_type" placeholder="请选择题目类型" width="100%" @change="onQuestionTypeChange" />
          </a-form-item>
          <a-form-item label="难度" name="difficultyLevel" class="form-item-difficulty">
            <DictSelect v-model:value="editModalForm.difficultyLevel" dict-code="difficulty_level" placeholder="请选择" width="100%" />
          </a-form-item>
          <a-form-item label="认知" name="cognitiveLevel" class="form-item-cognitive">
            <DictSelect v-model:value="editModalForm.cognitiveLevel" dict-code="cognitive_level" placeholder="请选择" width="100%" />
          </a-form-item>
        </div>

        <!-- 题目内容 -->
        <a-form-item label="题目内容" name="questionContent" class="form-item-content">
          <a-textarea v-model:value="editModalForm.questionContent" :rows="4" placeholder="请输入题目内容" />
        </a-form-item>

        <!-- 单选题模板 -->
        <template v-if="editModalForm.questionType === '1'">
          <a-form-item label="选项" class="form-item-options">
            <div class="options-list">
              <div v-for="(opt, idx) in singleOptions" :key="idx" class="option-row">
                <span class="option-prefix">{{ String.fromCharCode(65 + idx) }}.</span>
                <a-input v-model:value="singleOptions[idx]" placeholder="请输入选项内容" />
              </div>
            </div>
          </a-form-item>
          <a-form-item label="正确答案" name="answer" class="form-item-answer">
            <a-radio-group v-model:value="singleAnswer" button-style="solid">
              <a-radio-button value="A">A</a-radio-button>
              <a-radio-button value="B">B</a-radio-button>
              <a-radio-button value="C">C</a-radio-button>
              <a-radio-button value="D">D</a-radio-button>
            </a-radio-group>
          </a-form-item>
        </template>

        <!-- 多选题模板 -->
        <template v-if="editModalForm.questionType === '2'">
          <a-form-item label="选项" class="form-item-options">
            <div class="options-list">
              <div v-for="(opt, idx) in multiOptions" :key="idx" class="option-row">
                <span class="option-prefix">{{ String.fromCharCode(65 + idx) }}.</span>
                <a-input v-model:value="multiOptions[idx]" placeholder="请输入选项内容" />
                <a-checkbox v-model:checked="multiAnswer[idx]" class="option-checkbox">正确答案</a-checkbox>
              </div>
            </div>
          </a-form-item>
        </template>

        <!-- 判断题模板 -->
        <template v-if="editModalForm.questionType === '3'">
          <a-form-item label="正确答案" name="answer" class="form-item-answer">
            <a-radio-group v-model:value="editModalForm.answer">
              <a-radio value="正确">正确</a-radio>
              <a-radio value="错误">错误</a-radio>
            </a-radio-group>
          </a-form-item>
        </template>

        <!-- 填空题模板 -->
        <template v-if="editModalForm.questionType === '4'">
          <a-form-item label="填空答案" name="answer" class="form-item-answer">
            <a-input v-model:value="editModalForm.answer" placeholder="多个答案用逗号分隔，如：答案1,答案2" />
          </a-form-item>
        </template>

        <!-- 简答题模板 -->
        <template v-if="editModalForm.questionType === '5'">
          <a-form-item label="参考答案" name="answer" class="form-item-answer">
            <a-textarea v-model:value="editModalForm.answer" :rows="4" placeholder="请输入参考答案要点" />
          </a-form-item>
        </template>

        <!-- 计算题模板 -->
        <template v-if="editModalForm.questionType === '6'">
          <a-form-item label="参考答案" name="answer" class="form-item-answer">
            <a-input v-model:value="editModalForm.answer" placeholder="请输入最终答案" />
          </a-form-item>
          <a-form-item label="解题步骤" class="form-item-steps">
            <a-textarea v-model:value="editModalForm.answerAnalysis" :rows="4" placeholder="请输入详细解题步骤" />
          </a-form-item>
        </template>

        <!-- 应用题/综合题模板 -->
        <template v-if="['7', '8'].includes(editModalForm.questionType)">
          <a-form-item label="参考答案" name="answer" class="form-item-answer">
            <a-textarea v-model:value="editModalForm.answer" :rows="4" placeholder="请输入参考答案" />
          </a-form-item>
        </template>

        <!-- 底部：分值、知识点、解析 -->
        <div class="form-row-bottom">
          <a-form-item label="分值" name="score" class="form-item-score">
            <a-input-number v-model:value="editModalForm.score" :min="0" :step="0.5" style="width: 120%" />
          </a-form-item>
          <a-form-item label="知识点" class="form-item-knowledge">
            <a-input v-model:value="editModalForm.knowledgePoints" placeholder="多个标签用逗号分隔" />
          </a-form-item>
        </div>

        <a-form-item label="答案解析" class="form-item-analysis">
          <a-textarea v-model:value="editModalForm.answerAnalysis" :rows="3" placeholder="请输入答案解析" v-if="editModalForm.questionType !== '6'" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { Empty } from 'ant-design-vue';
  import { getRequest, postRequest } from '/@/lib/axios';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import DictLabel from '/@/components/support/dict-label/index.vue';
  import {
    ThunderboltOutlined,
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
  } from '@ant-design/icons-vue';
  import AIGenerateModalAdvanced from './components/AIGenerateModalAdvanced.vue';

  const route = useRoute();
  const router = useRouter();

  const courseId = ref(route.query.courseId);
  const unitCode = ref(route.query.unitCode);

  const courseInfo = ref(null);
  const currentUnit = ref(null);

  const questionList = ref([]);
  const selectedQuestion = ref(null);
  const searchKeyword = ref('');

  const aiGenerateModalVisible = ref(false);

  const editModalVisible = ref(false);
  const isEditModalCreate = ref(false);
  const modalSubmitLoading = ref(false);
  const modalFormRef = ref(null);

  const editModalForm = ref({
    id: null,
    questionType: '1',
    difficultyLevel: '3',
    cognitiveLevel: '2',
    questionContent: '',
    optionsText: '',
    answer: '',
    score: 5,
    knowledgePoints: '',
    answerAnalysis: '',
  });

  const singleOptions = ref(['', '', '', '']);
  const singleAnswer = ref('A');
  const multiOptions = ref(['', '', '', '']);
  const multiAnswer = ref([false, false, false, false]);

  const modalFormRules = {
    questionType: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
    questionContent: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  };

  const shouldShowOptions = computed(() => {
    const type = editModalForm.value.questionType;
    return ['1', '2', '3'].includes(type);
  });

  onMounted(async () => {
    await loadCourseInfo();
    await loadUnitInfo();
    await loadQuestionList();
  });

  async function loadCourseInfo() {
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      console.error('加载课程信息失败', e);
    }
  }

  async function loadUnitInfo() {
    try {
      const res = await getRequest('/manage/smartmind/learningunit/loadByUnitCode', { unitCode: unitCode.value });
      if (res && res.data) {
        currentUnit.value = res.data;
      }
    } catch (e) {
      console.error('加载单元信息失败', e);
    }
  }

  async function loadQuestionList() {
    try {
      const res = await postRequest('/manage/smartmind/question/loadByCourseAndUnit', {
        courseId: courseId.value,
        unitCode: unitCode.value,
      });
      if (res && res.data) {
        questionList.value = res.data;
      }
    } catch (e) {
      console.error('加载题目列表失败', e);
    }
  }

  function selectQuestion(item) {
    selectedQuestion.value = item;
  }

  function onSearch() {
    if (!searchKeyword.value) {
      loadQuestionList();
      return;
    }
    selectedQuestion.value = null;
  }

  function openAIGenerateModal() {
    aiGenerateModalVisible.value = true;
  }

  function onAICreated() {
    loadQuestionList();
  }

  function openCreateModal() {
    isEditModalCreate.value = true;
    editModalForm.value = {
      id: null,
      questionType: '1',
      difficultyLevel: '3',
      cognitiveLevel: '2',
      questionContent: '',
      optionsText: '',
      answer: '',
      score: 5,
      knowledgePoints: '',
      answerAnalysis: '',
    };
    singleOptions.value = ['', '', '', ''];
    singleAnswer.value = 'A';
    multiOptions.value = ['', '', '', ''];
    multiAnswer.value = [false, false, false, false];
    editModalVisible.value = true;
  }

  function onQuestionTypeChange() {
    singleOptions.value = ['', '', '', ''];
    singleAnswer.value = 'A';
    multiOptions.value = ['', '', '', ''];
    multiAnswer.value = [false, false, false, false];
  }

  function openEditModal() {
    if (!selectedQuestion.value) {
      message.warning('请先选择题目');
      return;
    }
    isEditModalCreate.value = false;
    const q = selectedQuestion.value;
    editModalForm.value = {
      id: q.id,
      questionType: q.questionType,
      difficultyLevel: q.difficultyLevel,
      cognitiveLevel: q.cognitiveLevel,
      questionContent: q.questionContent || '',
      optionsText: q.options || '[]',
      answer: q.answer || '',
      score: q.score || 5,
      knowledgePoints: q.knowledgePoints || '',
      answerAnalysis: q.answerAnalysis || '',
    };

    const opts = parseOptions(q.options);
    if (q.questionType === '1') {
      singleOptions.value = opts.length >= 4 ? opts.slice(0, 4) : [...opts, '', '', '', ''].slice(0, 4);
      singleAnswer.value = q.answer || 'A';
    } else if (q.questionType === '2') {
      multiOptions.value = opts.length >= 4 ? opts.slice(0, 4) : [...opts, '', '', '', ''].slice(0, 4);
      const answerArr = (q.answer || '').split(',').map(a => a.trim());
      multiAnswer.value = [false, false, false, false].map((_, i) => answerArr.includes(String.fromCharCode(65 + i)));
    }
    editModalVisible.value = true;
  }

  async function handleModalSubmit() {
    try {
      await modalFormRef.value.validateFields();
    } catch (e) {
      return;
    }

    modalSubmitLoading.value = true;
    try {
      let optionsJson = '[]';
      let answer = editModalForm.value.answer;

      const type = editModalForm.value.questionType;
      if (type === '1') {
        optionsJson = JSON.stringify(singleOptions.value.filter(o => o.trim()));
        answer = singleAnswer.value;
      } else if (type === '2') {
        optionsJson = JSON.stringify(multiOptions.value.filter(o => o.trim()));
        answer = multiAnswer.value.map((checked, i) => checked ? String.fromCharCode(65 + i) : '').filter(Boolean).join(',');
      } else if (type === '3') {
        optionsJson = JSON.stringify(['正确', '错误']);
      }

      const data = {
        questionType: type,
        courseId: courseId.value,
        courseName: courseInfo.value?.courseName,
        unitId: currentUnit.value?.id,
        unitName: currentUnit.value?.unitName,
        unitCode: unitCode.value,
        difficultyLevel: editModalForm.value.difficultyLevel,
        cognitiveLevel: editModalForm.value.cognitiveLevel,
        questionContent: editModalForm.value.questionContent,
        options: optionsJson,
        answer: answer,
        score: editModalForm.value.score,
        knowledgePoints: editModalForm.value.knowledgePoints,
        answerAnalysis: editModalForm.value.answerAnalysis,
        sourceType: '2',
        status: '1',
      };

      if (isEditModalCreate.value) {
        await postRequest('/manage/smartmind/question/create', data);
        message.success('创建成功');
      } else {
        data.id = editModalForm.value.id;
        await postRequest('/manage/smartmind/question/update', data);
        message.success('更新成功');
      }
      editModalVisible.value = false;
      await loadQuestionList();
    } catch (e) {
      console.error('保存失败', e);
      message.error('保存失败');
    } finally {
      modalSubmitLoading.value = false;
    }
  }

  async function deleteQuestion() {
    if (!selectedQuestion.value) {
      message.warning('请先选择题目');
      return;
    }
    try {
      await postRequest('/manage/smartmind/question/setStatus', {
        id: selectedQuestion.value.id,
        status: '0',
      });
      message.success('删除成功');
      selectedQuestion.value = null;
      await loadQuestionList();
    } catch (e) {
      console.error('删除失败', e);
      message.error('删除失败');
    }
  }

  function getQuestionTypeLabel(type) {
    const map = { '1': '单选题', '2': '多选题', '3': '判断题', '4': '填空题', '5': '简答题', '6': '计算题', '7': '应用题', '8': '综合题' };
    return map[type] || type;
  }

  function getQuestionTypeColor(type) {
    const map = { '1': 'blue', '2': 'purple', '3': 'green', '4': 'orange', '5': 'cyan', '6': 'magenta', '7': 'volcano', '8': 'geekblue' };
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

  function getSourceLabel(type) {
    const map = { '1': '系统题库', '2': '教师自建', '3': 'AI生成', '4': '导入' };
    return map[type] || type;
  }

  function truncateText(text, len) {
    if (!text) return '';
    return text.length > len ? text.substring(0, len) + '...' : text;
  }

  function parseOptions(optionsStr) {
    if (!optionsStr) return [];
    try {
      const arr = JSON.parse(optionsStr);
      return Array.isArray(arr) ? arr : [];
    } catch {
      return [];
    }
  }

  function formatOptionsText(optionsStr) {
    if (!optionsStr) return '';
    try {
      const arr = JSON.parse(optionsStr);
      return Array.isArray(arr) ? arr.join('\n') : '';
    } catch {
      return optionsStr;
    }
  }

  function parseOptionsToJson(text) {
    if (!text) return '[]';
    const lines = text.split('\n').filter(l => l.trim());
    return JSON.stringify(lines);
  }
</script>

<style scoped lang="less">
  .question-bank-page {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background: #f5f5f5;
    padding: 16px;

    .header-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      background: #fff;
      border-radius: 8px;
      margin-bottom: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    }

    .main-content {
      flex: 1;
      display: flex;
      gap: 16px;
      overflow: hidden;

      .left-panel {
        width: 360px;
        background: #fff;
        border-radius: 8px;
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .list-header {
          padding: 12px 16px;
          border-bottom: 1px solid #f0f0f0;
          display: flex;
          justify-content: space-between;
          align-items: center;

          .list-title {
            font-size: 14px;
            font-weight: 500;
            color: #333;
          }
        }

        .question-list {
          flex: 1;
          overflow-y: auto;
          padding: 8px;

          .question-item {
            padding: 12px;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.2s;
            margin-bottom: 8px;
            border: 1px solid #f0f0f0;

            &:hover {
              background: #f5f5f5;
            }

            &.active {
              background: #e6f7ff;
              border-color: #1890ff;
            }

            .question-item-header {
              display: flex;
              gap: 8px;
              margin-bottom: 8px;
            }

            .question-item-content {
              font-size: 13px;
              color: #595959;
              line-height: 1.5;
              margin-bottom: 8px;
            }

            .question-item-footer {
              display: flex;
              gap: 12px;

              .question-item-meta {
                font-size: 12px;
                color: #8c8c8c;
              }
            }
          }

          .empty-list {
            padding: 40px 0;
          }
        }
      }

      .right-panel {
        flex: 1;
        background: #fff;
        border-radius: 8px;
        display: flex;
        flex-direction: column;
        overflow: hidden;

        .detail-header {
          padding: 12px 16px;
          border-bottom: 1px solid #f0f0f0;

          .detail-title-row {
            display: flex;
            justify-content: space-between;
            align-items: center;

            .detail-title {
              margin: 0;
              font-size: 16px;
              font-weight: 500;
            }
          }
        }

        .detail-content {
          flex: 1;
          overflow-y: auto;
          padding: 16px;

          .detail-section {
            margin-bottom: 20px;

            .section-label {
              font-size: 14px;
              font-weight: 500;
              color: #333;
              margin-bottom: 8px;
              padding-bottom: 4px;
              border-bottom: 1px dashed #d9d9d9;
            }

            .info-grid {
              display: grid;
              grid-template-columns: repeat(2, 1fr);
              gap: 12px;

              .info-item {
                .info-label {
                  color: #8c8c8c;
                  font-size: 13px;
                }

                .info-value {
                  color: #333;
                  font-size: 13px;
                }
              }
            }

            .content-display {
              font-size: 14px;
              line-height: 1.8;
              color: #595959;
              white-space: pre-wrap;

              &.answer {
                color: #52c41a;
              }
            }

            .options-display {
              .option-item {
                padding: 8px 12px;
                background: #fafafa;
                border-radius: 4px;
                margin-bottom: 8px;

                .option-label {
                  font-weight: 500;
                  margin-right: 8px;
                  color: #1890ff;
                }

                .option-text {
                  color: #595959;
                }
              }
            }
          }
        }

        .empty-detail {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }

  .question-form {
    :deep(.form-row) {
      display: flex;
      gap: 12px;
      margin-bottom: 0;

      .form-item-type {
        flex: 2;
        margin-bottom: 16px;
      }

      .form-item-difficulty {
        flex: 1;
        margin-bottom: 16px;
      }

      .form-item-cognitive {
        flex: 1;
        margin-bottom: 16px;
      }
    }

    :deep(.form-item-content) {
      margin-bottom: 16px;
    }

    :deep(.form-item-options) {
      margin-bottom: 16px;

      .options-list {
        .option-row {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 8px;

          .option-prefix {
            font-weight: 500;
            color: #1890ff;
            min-width: 20px;
            text-align: center;
          }

          :deep(.ant-input) {
            flex: 1;
          }

          .option-radio,
          .option-checkbox {
            white-space: nowrap;
          }
        }
      }
    }

    :deep(.form-item-answer) {
      margin-bottom: 16px;
    }

    :deep(.form-item-steps) {
      margin-bottom: 16px;
    }

    :deep(.form-row-bottom) {
      display: flex;
      gap: 12px;
      margin-bottom: 0;

      .form-item-score {
        width: 120px;
        margin-bottom: 16px;
      }

      .form-item-knowledge {
        flex: 1;
        margin-bottom: 16px;
      }
    }

    :deep(.form-item-analysis) {
      margin-bottom: 0;
    }

    :deep(.ant-form-item-label) {
      label {
        font-size: 13px;
        color: #333;
      }
    }
  }
</style>
