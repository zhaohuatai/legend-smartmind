<template>
  <a-modal
    v-model:open="visible"
    :title="modalTitle"
    width="900px"
    :footer="null"
  >
    <div class="student-detail-content" v-if="detailData">
      <!-- 基本信息卡片 -->
      <div class="info-section">
        <div class="info-card">
          <div class="info-item">
            <span class="label">学生姓名</span>
            <span class="value">{{ detailData.studentName }}</span>
          </div>
          <div class="info-item">
            <span class="label">活动名称</span>
            <span class="value">{{ detailData.activityName }}</span>
          </div>
          <div class="info-item">
            <span class="label">活动类型</span>
            <span class="value">
              <a-tag :color="getActivityTypeColor(detailData.activityType)">
                {{ getActivityTypeName(detailData.activityType) }}
              </a-tag>
            </span>
          </div>
          <div class="info-item">
            <span class="label">提交状态</span>
            <span class="value">
              <a-tag :color="getStatusColor(detailData.status)">
                {{ getStatusName(detailData.status) }}
              </a-tag>
            </span>
          </div>
          <div class="info-item">
            <span class="label">得分</span>
            <span class="value score-value">
              {{ detailData.score !== null && detailData.score !== undefined ? detailData.score : '-' }}
              <span class="score-total" v-if="detailData.activityScore">/ {{ detailData.activityScore }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">提交时间</span>
            <span class="value">{{ detailData.submitTime ? formatDateTime(detailData.submitTime) : '-' }}</span>
          </div>
        </div>
      </div>

      <!-- 随堂测试/随堂练习：题目结果 -->
      <div class="result-section" v-if="shouldShowQuestions">
        <h3 class="section-title">
          <FileTextOutlined />
          <span>答题详情</span>
          <a-tag class="count-tag" color="blue">{{ detailData.questionResults?.length || 0 }} 题</a-tag>
        </h3>
        <div class="question-list">
          <div
            v-for="(question, index) in (detailData.questionResults || [])"
            :key="question.questionId"
            class="question-card"
          >
            <div class="question-header">
              <span class="question-number">第 {{ index + 1 }} 题</span>
              <a-tag :color="getQuestionTypeColor(question.questionType)">
                {{ getQuestionTypeName(question.questionType) }}
              </a-tag>
              <span class="question-score">
                {{ question.score !== null && question.score !== undefined ? question.score : '-' }}
                <span class="score-total" v-if="question.fullScore">/ {{ question.fullScore }}</span>
              </span>
            </div>

            <div class="question-body">
              <div class="question-content">
                <div class="label">题目内容</div>
                <div class="content-text">{{ question.questionContent }}</div>
              </div>

              <div class="options-section" v-if="shouldShowOptions(question.questionType)">
                <div class="label">选项列表</div>
                <div class="options-list">
                  <div
                    v-for="opt in parseOptions(question.options)"
                    :key="opt.key"
                    class="option-item"
                    :class="getOptionClass(opt.key, question)"
                  >
                    <span class="option-key">{{ opt.key }}</span>
                    <span class="option-value">{{ opt.value }}</span>
                    <span class="option-badges">
                      <a-tag v-if="isCorrectAnswer(opt.key, question.answer)" color="green" size="small">正确答案</a-tag>
                      <a-tag v-if="isStudentAnswer(opt.key, question.answerContent)" color="blue" size="small">你的选择</a-tag>
                    </span>
                  </div>
                </div>
              </div>

              <div class="knowledge-points" v-if="question.knowledgePoints">
                <div class="label">知识点</div>
                <div class="tags">
                  <a-tag v-for="(tag, idx) in question.knowledgePoints.split(',')" :key="idx" size="small">
                    {{ tag }}
                  </a-tag>
                </div>
              </div>

              <div class="answer-section">
                <div class="label">学生答案</div>
                <div class="content-text student-answer" :class="getAnswerClass(question)">
                  {{ question.answerContent || '未作答' }}
                </div>
              </div>

              <div class="answer-section" v-if="question.answer">
                <div class="label">参考答案</div>
                <div class="content-text reference-answer">{{ question.answer }}</div>
              </div>

              <div class="answer-section" v-if="question.answerAnalysis">
                <div class="label">答案解析</div>
                <div class="content-text analysis">{{ question.answerAnalysis }}</div>
              </div>

              <div class="evaluations" v-if="shouldShowEvaluations(question)">
                <div class="evaluation-item" v-if="question.aiEvaluate">
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval">{{ question.aiEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else-if="isSubjectiveQuestion(question.questionType)">
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval empty-eval">暂无AI评价</div>
                </div>
                <div class="evaluation-item" v-if="question.teacherEvaluate">
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval">{{ question.teacherEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else-if="isSubjectiveQuestion(question.questionType)">
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval empty-eval">暂无教师评价</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 课堂实验：实验指导书结果 -->
      <div class="result-section" v-if="shouldShowGuides">
        <h3 class="section-title">
          <ExperimentOutlined />
          <span>实验报告</span>
        </h3>
        <div class="guide-list">
          <div
            v-for="guide in (detailData.guideResults || [])"
            :key="guide.guideId"
            class="guide-card"
          >
            <div class="guide-header">
              <h4 class="guide-name">{{ guide.guideName }}</h4>
              <span class="guide-score">
                {{ guide.score !== null && guide.score !== undefined ? guide.score : '-' }}
                <span class="score-total" v-if="guide.fullScore">/ {{ guide.fullScore }}</span>
              </span>
            </div>

            <div class="guide-body">
              <div class="guide-info" v-if="guide.experimentObjectives">
                <div class="label">实验目的</div>
                <div class="content-text">{{ guide.experimentObjectives }}</div>
              </div>

              <div class="guide-info" v-if="guide.experimentPrinciple">
                <div class="label">实验原理</div>
                <div class="content-text">{{ guide.experimentPrinciple }}</div>
              </div>

              <div class="guide-info" v-if="guide.experimentEquipment">
                <div class="label">实验器材</div>
                <div class="content-text">{{ guide.experimentEquipment }}</div>
              </div>

              <div class="guide-info" v-if="guide.experimentSteps">
                <div class="label">实验步骤</div>
                <div class="content-text steps">{{ guide.experimentSteps }}</div>
              </div>

              <div class="guide-info" v-if="guide.precautions">
                <div class="label">注意事项</div>
                <div class="content-text">{{ guide.precautions }}</div>
              </div>

              <div class="guide-info" v-if="guide.reportRequirements">
                <div class="label">报告要求</div>
                <div class="content-text">{{ guide.reportRequirements }}</div>
              </div>

              <div class="answer-section">
                <div class="label">学生实验报告</div>
                <div class="content-text student-answer" :class="getAnswerClass(guide)">
                  {{ guide.answerContent || '未作答' }}
                </div>
              </div>

              <div class="evaluations" v-if="shouldShowGuideEvaluations(guide)">
                <div class="evaluation-item" v-if="guide.aiEvaluate">
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval">{{ guide.aiEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else>
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval empty-eval">暂无AI评价</div>
                </div>
                <div class="evaluation-item" v-if="guide.teacherEvaluate">
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval">{{ guide.teacherEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else>
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval empty-eval">暂无教师评价</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 话题讨论：话题结果 -->
      <div class="result-section" v-if="shouldShowTopics">
        <h3 class="section-title">
          <CommentOutlined />
          <span>讨论详情</span>
        </h3>
        <div class="topic-list">
          <div
            v-for="topic in (detailData.topicResults || [])"
            :key="topic.topicId"
            class="topic-card"
          >
            <div class="topic-header">
              <h4 class="topic-name">{{ topic.topicName }}</h4>
              <a-tag :color="getTopicTypeColor(topic.topicType)">
                {{ getTopicTypeName(topic.topicType) }}
              </a-tag>
              <span class="topic-score">
                {{ topic.score !== null && topic.score !== undefined ? topic.score : '-' }}
                <span class="score-total" v-if="topic.fullScore">/ {{ topic.fullScore }}</span>
              </span>
            </div>

            <div class="topic-body">
              <div class="topic-info" v-if="topic.topicContent">
                <div class="label">话题内容</div>
                <div class="content-text">{{ topic.topicContent }}</div>
              </div>

              <div class="topic-info" v-if="topic.backgroundMaterial">
                <div class="label">背景材料</div>
                <div class="content-text">{{ topic.backgroundMaterial }}</div>
              </div>

              <div class="topic-info" v-if="topic.guidanceTips">
                <div class="label">引导提示</div>
                <div class="content-text">{{ topic.guidanceTips }}</div>
              </div>

              <div class="topic-info" v-if="topic.expectedAnswers">
                <div class="label">预期答案要点</div>
                <div class="content-text">{{ topic.expectedAnswers }}</div>
              </div>

              <div class="topic-info" v-if="topic.evaluationCriteria">
                <div class="label">评价标准</div>
                <div class="content-text">{{ topic.evaluationCriteria }}</div>
              </div>

              <div class="answer-section">
                <div class="label">学生回答</div>
                <div class="content-text student-answer" :class="getAnswerClass(topic)">
                  {{ topic.answerContent || '未作答' }}
                </div>
              </div>

              <div class="evaluations" v-if="shouldShowTopicEvaluations(topic)">
                <div class="evaluation-item" v-if="topic.aiEvaluate">
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval">{{ topic.aiEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else>
                  <div class="label">
                    <RobotOutlined /> AI评价
                  </div>
                  <div class="content-text ai-eval empty-eval">暂无AI评价</div>
                </div>
                <div class="evaluation-item" v-if="topic.teacherEvaluate">
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval">{{ topic.teacherEvaluate }}</div>
                </div>
                <div class="evaluation-item" v-else>
                  <div class="label">
                    <UserOutlined /> 教师评价
                  </div>
                  <div class="content-text teacher-eval empty-eval">暂无教师评价</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 未提交提示 -->
      <a-empty v-if="!hasAnyResults" description="该学生尚未提交任何内容" />
    </div>
  </a-modal>
</template>

<script setup>
  import { computed, ref, watch } from 'vue';
  import dayjs from 'dayjs';
  import {
    FileTextOutlined,
    ExperimentOutlined,
    CommentOutlined,
    RobotOutlined,
    UserOutlined,
  } from '@ant-design/icons-vue';

  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    student: {
      type: Object,
      default: null,
    },
    activityType: {
      type: String,
      default: '',
    },
    detailData: {
      type: Object,
      default: null,
    },
  });

  const emit = defineEmits(['update:open']);

  const visible = computed({
    get: () => props.open,
    set: (val) => emit('update:open', val),
  });

  const modalTitle = computed(() => {
    return `${props.detailData?.studentName || '学生'} - ${props.detailData?.activityName || '活动详情'}`;
  });

  const hasAnyResults = computed(() => {
    const data = props.detailData;
    if (!data) return false;
    return (
      (data.questionResults && data.questionResults.length > 0) ||
      (data.guideResults && data.guideResults.length > 0) ||
      (data.topicResults && data.topicResults.length > 0)
    );
  });

  const shouldShowQuestions = computed(() => {
    return props.detailData?.questionResults && props.detailData.questionResults.length > 0;
  });

  const shouldShowGuides = computed(() => {
    return props.detailData?.guideResults && props.detailData.guideResults.length > 0;
  });

  const shouldShowTopics = computed(() => {
    return props.detailData?.topicResults && props.detailData.topicResults.length > 0;
  });

  function formatDateTime(date) {
    if (!date) return '-';
    return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
  }

  function getActivityTypeName(activityType) {
    const typeMap = {
      '1': '随堂测试',
      '2': '随堂练习',
      '3': '课堂实验',
      '4': '话题讨论',
    };
    return typeMap[activityType] || '未知';
  }

  function getActivityTypeColor(activityType) {
    const colorMap = {
      '1': 'blue',
      '2': 'green',
      '3': 'purple',
      '4': 'orange',
    };
    return colorMap[activityType] || 'default';
  }

  function getStatusName(status) {
    const statusMap = {
      '0': '未提交',
      '1': '已提交',
      '2': '已评分',
    };
    return statusMap[status] || '未知';
  }

  function getStatusColor(status) {
    const colorMap = {
      '0': 'default',
      '1': 'blue',
      '2': 'green',
    };
    return colorMap[status] || 'default';
  }

  function getQuestionTypeName(questionType) {
    const typeMap = {
      '1': '单选题',
      '2': '多选题',
      '3': '判断题',
      '4': '填空题',
      '5': '简答题',
      '6': '计算题',
      '7': '应用题',
      '8': '综合题',
    };
    return typeMap[questionType] || '未知';
  }

  function getQuestionTypeColor(questionType) {
    const colorMap = {
      '1': 'blue',
      '2': 'cyan',
      '3': 'green',
      '4': 'orange',
      '5': 'purple',
      '6': 'magenta',
      '7': 'volcano',
      '8': 'geekblue',
    };
    return colorMap[questionType] || 'default';
  }

  function getTopicTypeName(topicType) {
    const typeMap = {
      1: '开放讨论',
      2: '辩论赛',
      3: '案例分析',
      4: '小组研讨',
      5: '头脑风暴',
    };
    return typeMap[topicType] || '未知';
  }

  function getTopicTypeColor(topicType) {
    const colorMap = {
      1: 'blue',
      2: 'cyan',
      3: 'green',
      4: 'purple',
      5: 'orange',
    };
    return colorMap[topicType] || 'default';
  }

  function getAnswerClass(item) {
    if (!item.answerContent) return 'not-answered';
    if (item.status === '2') return 'graded';
    if (item.status === '1') return 'answered';
    return '';
  }

  function shouldShowOptions(questionType) {
    return questionType === '1' || questionType === '2';
  }

  function parseOptions(optionsStr) {
    if (!optionsStr) return [];
    try {
      const options = JSON.parse(optionsStr);
      if (Array.isArray(options)) {
        return options.map(opt => {
          const match = opt.match(/^([A-Z])[\.\、]\s*(.+)$/);
          if (match) {
            return { key: match[1], value: match[2] };
          }
          return { key: opt.charAt(0), value: opt };
        });
      }
      return [];
    } catch (e) {
      return [];
    }
  }

  function isCorrectAnswer(optionKey, correctAnswer) {
    if (!correctAnswer || !optionKey) return false;
    return correctAnswer.includes(optionKey);
  }

  function isStudentAnswer(optionKey, studentAnswer) {
    if (!studentAnswer || !optionKey) return false;
    return studentAnswer.includes(optionKey);
  }

  function getOptionClass(optionKey, question) {
    const classes = [];
    const isCorrect = isCorrectAnswer(optionKey, question.answer);
    const isStudent = isStudentAnswer(optionKey, question.answerContent);

    if (isCorrect && isStudent) {
      classes.push('option-correct-student');
    } else if (isCorrect) {
      classes.push('option-correct');
    } else if (isStudent) {
      classes.push('option-wrong-student');
    }

    return classes.join(' ');
  }

  function isSubjectiveQuestion(questionType) {
    return ['5', '6', '7', '8'].includes(questionType);
  }

  function shouldShowEvaluations(question) {
    if (question.aiEvaluate || question.teacherEvaluate) return true;
    return isSubjectiveQuestion(question.questionType);
  }

  function shouldShowGuideEvaluations(guide) {
    return true;
  }

  function shouldShowTopicEvaluations(topic) {
    return true;
  }
</script>

<style lang="less" scoped>
  .student-detail-content {
    padding: 16px 0;
    max-height: 70vh;
    overflow-y: auto;
  }

  .info-section {
    margin-bottom: 24px;
  }

  .info-card {
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
    border: 1px solid rgba(102, 126, 234, 0.15);
    border-radius: 12px;
    padding: 16px;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
  }

  .info-item {
    display: flex;
    flex-direction: column;
    gap: 6px;

    .label {
      font-size: 12px;
      color: #6b7280;
      font-weight: 500;
    }

    .value {
      font-size: 14px;
      color: #1a1a2e;
      font-weight: 600;

      &.score-value {
        color: #667eea;
        font-size: 18px;

        .score-total {
          font-size: 14px;
          color: #9ca3af;
          font-weight: 400;
        }
      }
    }
  }

  .result-section {
    margin-bottom: 24px;
  }

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: #1a1a2e;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 2px solid rgba(102, 126, 234, 0.2);

    .anticon {
      font-size: 18px;
      color: #667eea;
    }

    .count-tag {
      margin-left: auto;
      font-size: 12px;
    }
  }

  .question-list,
  .guide-list,
  .topic-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .question-card,
  .guide-card,
  .topic-card {
    background: rgba(255, 255, 255, 0.8);
    border: 1px solid rgba(0, 0, 0, 0.08);
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 16px rgba(102, 126, 234, 0.15);
      border-color: rgba(102, 126, 234, 0.3);
    }
  }

  .question-header,
  .guide-header,
  .topic-header {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
    border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  }

  .question-number {
    font-size: 14px;
    font-weight: 600;
    color: #667eea;
  }

  .guide-name,
  .topic-name {
    font-size: 15px;
    font-weight: 600;
    color: #1a1a2e;
    margin: 0;
    flex: 1;
  }

  .question-score,
  .guide-score,
  .topic-score {
    font-size: 16px;
    font-weight: 700;
    color: #667eea;

    .score-total {
      font-size: 13px;
      color: #9ca3af;
      font-weight: 400;
    }
  }

  .question-body,
  .guide-body,
  .topic-body {
    padding: 16px;
  }

  .question-content,
  .knowledge-points,
  .answer-section,
  .guide-info,
  .topic-info,
  .evaluations {
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .label {
    font-size: 12px;
    color: #6b7280;
    font-weight: 600;
    margin-bottom: 6px;
    display: flex;
    align-items: center;
    gap: 6px;

    .anticon {
      font-size: 14px;
      color: #667eea;
    }
  }

  .content-text {
    font-size: 14px;
    color: #2d3748;
    line-height: 1.6;
    padding: 10px 12px;
    background: rgba(255, 255, 255, 0.6);
    border: 1px solid rgba(0, 0, 0, 0.06);
    border-radius: 8px;
    white-space: pre-wrap;
    word-break: break-word;

    &.student-answer {
      background: rgba(102, 126, 234, 0.05);
      border-color: rgba(102, 126, 234, 0.2);
      color: #1a1a2e;
      font-weight: 500;

      &.not-answered {
        color: #9ca3af;
        font-style: italic;
      }

      &.answered {
        border-color: rgba(82, 196, 26, 0.3);
        background: rgba(82, 196, 26, 0.05);
      }

      &.graded {
        border-color: rgba(102, 126, 234, 0.4);
        background: rgba(102, 126, 234, 0.08);
      }
    }

    &.reference-answer {
      background: rgba(82, 196, 26, 0.05);
      border-color: rgba(82, 196, 26, 0.2);
      color: #059669;
    }

    &.analysis {
      background: rgba(250, 173, 20, 0.05);
      border-color: rgba(250, 173, 20, 0.2);
      color: #d97706;
    }

    &.ai-eval {
      background: rgba(118, 75, 162, 0.05);
      border-color: rgba(118, 75, 162, 0.2);
      color: #7c3aed;
    }

    &.teacher-eval {
      background: rgba(24, 144, 255, 0.05);
      border-color: rgba(24, 144, 255, 0.2);
      color: #1890ff;
    }

    &.steps {
      white-space: pre-line;
    }
  }

  .tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  .options-section {
    .options-list {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }

    .option-item {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      padding: 12px 14px;
      background: rgba(255, 255, 255, 0.8);
      border: 2px solid rgba(0, 0, 0, 0.08);
      border-radius: 10px;
      transition: all 0.2s ease;

      .option-key {
        flex-shrink: 0;
        width: 28px;
        height: 28px;
        border-radius: 50%;
        background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
        color: #667eea;
        font-weight: 700;
        font-size: 14px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .option-value {
        flex: 1;
        font-size: 14px;
        color: #2d3748;
        line-height: 1.5;
      }

      .option-badges {
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        gap: 4px;
        align-items: flex-end;
      }

      &.option-correct-student {
        background: rgba(82, 196, 26, 0.1);
        border-color: rgba(82, 196, 26, 0.4);

        .option-key {
          background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
          color: #fff;
        }
      }

      &.option-correct {
        background: rgba(82, 196, 26, 0.05);
        border-color: rgba(82, 196, 26, 0.3);

        .option-key {
          background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
          color: #fff;
        }
      }

      &.option-wrong-student {
        background: rgba(255, 77, 79, 0.08);
        border-color: rgba(255, 77, 79, 0.3);

        .option-key {
          background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
          color: #fff;
        }
      }
    }
  }

  .evaluations {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .evaluation-item {
    .label {
      margin-bottom: 6px;
    }

    .empty-eval {
      color: #999;
      font-style: italic;
      background: rgba(0, 0, 0, 0.02);
    }
  }
</style>
