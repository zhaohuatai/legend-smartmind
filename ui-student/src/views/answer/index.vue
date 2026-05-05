<template>
  <div class="page answer-page">
    <header class="answer-header">
      <button class="btn-back" @click="goBack">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
      </button>
      <div class="header-center">
        <h1 class="activity-title">{{ activityName }}</h1>
        <span class="activity-type-tag">{{ activityTypeName }}</span>
      </div>
      <button v-if="activityStatus === '1'" class="btn-submit" @click="showSubmitConfirm = true">提交</button>
      <div v-else class="status-tag" :class="'status-' + activityStatus">
        {{ activityStatusText }}
      </div>
    </header>

    <div v-if="activityStatus === '0'" class="empty-state">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <circle cx="12" cy="12" r="10"/>
          <path d="M12 6v6l4 2"/>
        </svg>
      </div>
      <h3>活动尚未开始</h3>
      <p>请等待教师发起活动</p>
      <p v-if="activityStartTime" class="time-info">预计开始时间：{{ formatTime(activityStartTime) }}</p>
    </div>

    <div v-else-if="activityStatus === '2' || activityStatus === '3'" class="result-page">
      <div class="result-summary">
        <div class="summary-card">
          <div class="summary-item">
            <span class="label">得分</span>
            <span class="value score">{{ scoreText }}</span>
          </div>
          <div class="summary-item">
            <span class="label">状态</span>
            <span class="value">{{ submitStatusText }}</span>
          </div>
          <div class="summary-item">
            <span class="label">提交时间</span>
            <span class="value">{{ submitTimeText }}</span>
          </div>
        </div>
      </div>

      <div class="question-list" v-if="questionList.length > 0">
        <div class="question-card" v-for="(q, idx) in questionList" :key="q.id">
          <div class="question-header">
            <span class="question-number">第 {{ idx + 1 }} 题</span>
            <span class="type-badge" :class="q.typeClass">{{ q.typeName }}</span>
            <span class="score-badge">
              {{ q.score !== null && q.score !== undefined ? q.score : '-' }}
              <span class="score-total" v-if="q.fullScore">/ {{ q.fullScore }}</span>
            </span>
          </div>

          <div class="question-content">{{ q.content }}</div>

          <div class="options-area" v-if="q.options && q.options.length > 0">
            <div
              v-for="opt in q.options"
              :key="opt.key"
              class="option-item"
              :class="getOptionClass(opt.key, q)"
            >
              <span class="option-key">{{ opt.key }}</span>
              <span class="option-text">{{ opt.value }}</span>
              <span class="option-badges">
                <span v-if="isCorrectAnswer(opt.key, q.correctAnswer)" class="badge correct">正确答案</span>
                <span v-if="isStudentAnswer(opt.key, q.studentAnswer)" class="badge student">你的选择</span>
              </span>
            </div>
          </div>

          <div class="answer-section">
            <div class="answer-label">学生答案</div>
            <div class="answer-content" :class="getAnswerClass(q)">
              {{ q.studentAnswer || '未作答' }}
            </div>
          </div>

          <div class="answer-section" v-if="q.correctAnswer">
            <div class="answer-label">参考答案</div>
            <div class="answer-content reference">{{ q.correctAnswer }}</div>
          </div>

          <div class="answer-section" v-if="q.analysis">
            <div class="answer-label">答案解析</div>
            <div class="answer-content analysis">{{ q.analysis }}</div>
          </div>

          <div class="evaluation-section" v-if="q.aiEvaluate || q.teacherEvaluate">
            <div class="eval-item" v-if="q.aiEvaluate">
              <div class="eval-label">AI评价</div>
              <div class="eval-content">{{ q.aiEvaluate }}</div>
            </div>
            <div class="eval-item" v-if="q.teacherEvaluate">
              <div class="eval-label">教师评价</div>
              <div class="eval-content">{{ q.teacherEvaluate }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <template v-else>
      <div class="answer-progress">
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: progressPercent + '%' }"></div>
        </div>
        <div class="progress-info">
          <span>第 {{ currentIndex + 1 }} 题</span>
          <span>共 {{ questionList.length }} 题</span>
        </div>
      </div>

      <div class="question-indicators">
        <button
          v-for="(q, idx) in questionList"
          :key="q.id"
          class="indicator"
          :class="{
            active: currentIndex === idx,
            answered: isAnswered(q),
            current: currentIndex === idx,
          }"
          @click="jumpToQuestion(idx)"
        >
          {{ idx + 1 }}
        </button>
      </div>

      <main class="question-area">
        <div class="question-card" v-if="currentQuestion">
          <div class="question-meta">
            <span class="type-badge" :class="currentQuestion.typeClass">{{ currentQuestion.typeName }}</span>
            <span class="score-badge">{{ currentQuestion.score }} 分</span>
          </div>

          <h2 class="question-content">{{ currentQuestion.content }}</h2>

          <div class="options-area">
            <template v-if="currentQuestion.type === 'single'">
              <button
                v-for="opt in currentQuestion.options"
                :key="opt.key"
                class="option-btn"
                :class="{ selected: currentQuestion.answer === opt.key }"
                @click="selectSingle(opt.key)"
              >
                <span class="option-letter">{{ opt.key }}</span>
                <span class="option-text">{{ opt.value }}</span>
                <span class="check-icon" v-if="currentQuestion.answer === opt.key">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                    <path d="M5 13l4 4L19 7"/>
                  </svg>
                </span>
              </button>
            </template>

            <template v-else-if="currentQuestion.type === 'multiple'">
              <button
                v-for="opt in currentQuestion.options"
                :key="opt.key"
                class="option-btn"
                :class="{ selected: currentQuestion.answerList.includes(opt.key) }"
                @click="toggleMultiple(opt.key)"
              >
                <span class="option-letter">{{ opt.key }}</span>
                <span class="option-text">{{ opt.value }}</span>
                <span class="check-icon" v-if="currentQuestion.answerList.includes(opt.key)">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                    <path d="M5 13l4 4L19 7"/>
                  </svg>
                </span>
              </button>
            </template>

            <template v-else-if="currentQuestion.type === 'judge'">
              <div class="judge-options">
                <button
                  class="judge-btn"
                  :class="{ selected: currentQuestion.answer === 'T' }"
                  @click="currentQuestion.answer = 'T'"
                >
                  <div class="judge-icon correct">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                      <path d="M5 13l4 4L19 7"/>
                    </svg>
                  </div>
                  <span>正确</span>
                </button>
                <button
                  class="judge-btn"
                  :class="{ selected: currentQuestion.answer === 'F' }"
                  @click="currentQuestion.answer = 'F'"
                >
                  <div class="judge-icon wrong">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                      <path d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                  </div>
                  <span>错误</span>
                </button>
              </div>
            </template>

            <template v-else-if="currentQuestion.type === 'fill' || currentQuestion.type === 'essay'">
              <div class="guide-info" v-if="currentQuestion.experimentObjectives || currentQuestion.experimentSteps">
                <div class="guide-section" v-if="currentQuestion.experimentObjectives">
                  <div class="guide-label">实验目的</div>
                  <div class="guide-content">{{ currentQuestion.experimentObjectives }}</div>
                </div>
                <div class="guide-section" v-if="currentQuestion.experimentPrinciple">
                  <div class="guide-label">实验原理</div>
                  <div class="guide-content">{{ currentQuestion.experimentPrinciple }}</div>
                </div>
                <div class="guide-section" v-if="currentQuestion.experimentEquipment">
                  <div class="guide-label">实验器材</div>
                  <div class="guide-content">{{ currentQuestion.experimentEquipment }}</div>
                </div>
                <div class="guide-section" v-if="currentQuestion.experimentSteps">
                  <div class="guide-label">实验步骤</div>
                  <div class="guide-content">{{ currentQuestion.experimentSteps }}</div>
                </div>
                <div class="guide-section" v-if="currentQuestion.precautions">
                  <div class="guide-label">注意事项</div>
                  <div class="guide-content">{{ currentQuestion.precautions }}</div>
                </div>
                <div class="guide-section" v-if="currentQuestion.reportRequirements">
                  <div class="guide-label">报告要求</div>
                  <div class="guide-content">{{ currentQuestion.reportRequirements }}</div>
                </div>
              </div>
              <textarea
                v-model="currentQuestion.answer"
                class="text-input"
                :placeholder="currentQuestion.type === 'fill' ? '请输入答案...' : '请输入详细答案...'"
                :rows="currentQuestion.type === 'essay' ? 8 : 4"
              ></textarea>
            </template>
          </div>
        </div>
      </main>

      <footer class="answer-footer">
        <button class="footer-btn" :disabled="currentIndex === 0" @click="prevQuestion">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M15 19l-7-7 7-7"/>
          </svg>
          上一题
        </button>
        <button class="footer-btn primary" :disabled="currentIndex === questionList.length - 1" @click="nextQuestion">
          下一题
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 5l7 7-7 7"/>
          </svg>
        </button>
      </footer>
    </template>

    <div class="modal-overlay" v-if="showSubmitConfirm" @click.self="showSubmitConfirm = false">
      <div class="confirm-modal">
        <div class="modal-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <h3>确认提交答案？</h3>
        <p>提交后将无法修改</p>
        <div class="modal-stats">
          <span>已答 <b>{{ answeredCount }}</b> / {{ questionList.length }}</span>
          <span>未答 <b>{{ questionList.length - answeredCount }}</b></span>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showSubmitConfirm = false">继续答题</button>
          <button class="btn-confirm" @click="submitAnswer">确认提交</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '@/api'
import { toastSuccess } from '@/utils/toast.js'

const route = useRoute()
const router = useRouter()

const activityName = ref(route.query.activityName || '答题')
const activityType = ref(route.query.activityType || '1')
const activityId = ref(route.query.activityId)
const submitId = ref(null)
const currentIndex = ref(0)
const showSubmitConfirm = ref(false)
const questionList = ref([])
const loading = ref(false)
const activityStatus = ref('1')
const activityStartTime = ref(null)
const activityEndTime = ref(null)
const submitStatus = ref('0')
const submitTime = ref(null)
const totalScore = ref(null)
const activityScore = ref(null)

const activityTypeName = computed(() => {
  const map = { '1': '随堂测试', '2': '随堂练习', '3': '课堂实验', '4': '话题讨论' }
  return map[activityType.value] || '答题'
})

const activityStatusText = computed(() => {
  const map = { '0': '未开始', '1': '进行中', '2': '已结束', '3': '已取消' }
  return map[activityStatus.value] || ''
})

const submitStatusText = computed(() => {
  const map = { '0': '未提交', '1': '已提交', '2': '已评分' }
  return map[submitStatus.value] || '未提交'
})

const scoreText = computed(() => {
  if (totalScore.value !== null && totalScore.value !== undefined) {
    if (activityScore.value) {
      return `${totalScore.value} / ${activityScore.value}`
    }
    return String(totalScore.value)
  }
  return '-'
})

const submitTimeText = computed(() => {
  if (!submitTime.value) return '-'
  return formatTime(submitTime.value)
})

const currentQuestion = computed(() => questionList.value[currentIndex.value])

const progressPercent = computed(() => {
  if (questionList.value.length === 0) return 0
  return ((currentIndex.value + 1) / questionList.value.length) * 100
})

const answeredCount = computed(() => {
  return questionList.value.filter(q => isAnswered(q)).length
})

onMounted(() => {
  loadActivityDetail()
})

async function loadActivityDetail() {
  try {
    loading.value = true
    const res = await api.getStudentActivityDetail(activityId.value)
    const detail = res.data || {}
    submitId.value = detail.submitId
    activityStatus.value = detail.activityStatus || '1'
    activityStartTime.value = detail.activityStartTime
    activityEndTime.value = detail.activityEndTime
    submitStatus.value = detail.status || '0'
    submitTime.value = detail.submitTime
    totalScore.value = detail.score
    activityScore.value = detail.activityScore

    const questionResults = detail.questionResults || []
    const topicResults = detail.topicResults || []
    const guideResults = detail.guideResults || []
    const questions = []
    questionResults.forEach(qr => {
      questions.push({
        id: qr.questionId,
        detailId: qr.detailId,
        type: mapQuestionType(qr.questionType),
        typeClass: 'type-' + mapQuestionType(qr.questionType),
        typeName: mapQuestionTypeName(qr.questionType),
        content: qr.questionContent || '',
        score: qr.questionScore || 0,
        fullScore: qr.fullScore,
        options: qr.options ? JSON.parse(qr.options).map((opt, idx) => ({
          key: String.fromCharCode(65 + idx),
          value: opt,
        })) : [],
        answer: qr.answerContent || '',
        answerList: qr.answerContent ? qr.answerContent.split(',') : [],
        studentAnswer: qr.answerContent || '',
        correctAnswer: qr.answer || '',
        analysis: qr.answerAnalysis || '',
        aiEvaluate: qr.aiEvaluate || '',
        teacherEvaluate: qr.teacherEvaluate || '',
        knowledgePoints: qr.knowledgePoints || '',
      })
    })
    topicResults.forEach(tr => {
      questions.push({
        id: tr.topicId,
        detailId: tr.detailId,
        type: 'essay',
        typeClass: 'type-essay',
        typeName: '话题讨论',
        content: tr.topicContent || '',
        score: tr.score || 0,
        fullScore: tr.fullScore,
        options: [],
        answer: tr.answerContent || '',
        answerList: [],
        studentAnswer: tr.answerContent || '',
        correctAnswer: tr.expectedAnswers || '',
        analysis: tr.guidanceTips || '',
        aiEvaluate: tr.aiEvaluate || '',
        teacherEvaluate: tr.teacherEvaluate || '',
        knowledgePoints: '',
      })
    })
    guideResults.forEach(gr => {
      questions.push({
        id: gr.guideId,
        detailId: gr.detailId,
        type: 'essay',
        typeClass: 'type-essay',
        typeName: '实验报告',
        content: gr.guideName || '',
        experimentObjectives: gr.experimentObjectives || '',
        experimentPrinciple: gr.experimentPrinciple || '',
        experimentEquipment: gr.experimentEquipment || '',
        experimentSteps: gr.experimentSteps || '',
        precautions: gr.precautions || '',
        reportRequirements: gr.reportRequirements || '',
        score: gr.score || 0,
        fullScore: gr.fullScore,
        options: [],
        answer: gr.answerContent || '',
        answerList: [],
        studentAnswer: gr.answerContent || '',
        correctAnswer: '',
        analysis: '',
        aiEvaluate: gr.aiEvaluate || '',
        teacherEvaluate: gr.teacherEvaluate || '',
        knowledgePoints: '',
      })
    })
    questionList.value = questions
  } catch (e) {
    console.error('加载活动详情失败', e)
  } finally {
    loading.value = false
  }
}

function formatTime(date) {
  if (!date) return ''
  const d = new Date(date)
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
}

function mapQuestionType(type) {
  const map = { '1': 'single', '2': 'multiple', '3': 'judge', '4': 'fill', '5': 'essay' }
  return map[type] || 'single'
}

function mapQuestionTypeName(type) {
  const map = { '1': '单选题', '2': '多选题', '3': '判断题', '4': '填空题', '5': '简答题' }
  return map[type] || '未知'
}

function isAnswered(q) {
  if (q.type === 'multiple') return q.answerList && q.answerList.length > 0
  return q.answer && q.answer !== ''
}

function selectSingle(key) {
  const q = currentQuestion.value
  q.answer = key
}

function toggleMultiple(key) {
  const q = currentQuestion.value
  const idx = q.answerList.indexOf(key)
  if (idx > -1) {
    q.answerList.splice(idx, 1)
  } else {
    q.answerList.push(key)
  }
}

function prevQuestion() {
  if (currentIndex.value > 0) currentIndex.value--
}

function nextQuestion() {
  if (currentIndex.value < questionList.value.length - 1) currentIndex.value++
}

function jumpToQuestion(idx) {
  currentIndex.value = idx
}

function getOptionClass(key, q) {
  const classes = []
  const isCorrect = isCorrectAnswer(key, q.correctAnswer)
  const isStudent = isStudentAnswer(key, q.studentAnswer)
  if (isCorrect && isStudent) classes.push('option-correct-student')
  else if (isCorrect) classes.push('option-correct')
  else if (isStudent) classes.push('option-wrong-student')
  return classes.join(' ')
}

function isCorrectAnswer(key, correctAnswer) {
  if (!correctAnswer || !key) return false
  return correctAnswer.includes(key)
}

function isStudentAnswer(key, studentAnswer) {
  if (!studentAnswer || !key) return false
  return studentAnswer.includes(key)
}

function getAnswerClass(q) {
  if (!q.studentAnswer) return 'not-answered'
  if (q.score !== null && q.score !== undefined) return 'graded'
  return 'answered'
}

async function submitAnswer() {
  try {
    showSubmitConfirm.value = false
    const answers = []
    for (const q of questionList.value) {
      let answerContent = ''
      if (q.type === 'multiple') {
        answerContent = q.answerList.join(',')
      } else {
        answerContent = q.answer || ''
      }
      answers.push({
        detailId: q.detailId,
        answerContent: answerContent,
      })
    }
    await api.batchSubmitAnswer({
      submitId: submitId.value,
      answers: answers,
    })
    toastSuccess('提交成功！')
    setTimeout(() => {
      loadActivityDetail()
    }, 1500)
  } catch (e) {
    console.error('提交答案失败', e)
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.answer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  position: sticky;
  top: 0;
  z-index: 100;
}

.btn-back {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  border-radius: 8px;
  color: #374151;
}

.btn-back:hover {
  background: #f3f4f6;
}

.btn-back svg {
  width: 20px;
  height: 20px;
}

.header-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.activity-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.activity-type-tag {
  font-size: 11px;
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
  padding: 2px 8px;
  border-radius: 10px;
}

.btn-submit {
  padding: 8px 18px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-submit:hover {
  opacity: 0.9;
}

.status-tag {
  font-size: 13px;
  font-weight: 600;
  padding: 6px 14px;
  border-radius: 20px;
}

.status-0 {
  background: rgba(156, 163, 175, 0.1);
  color: #6b7280;
}

.status-2 {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.status-3 {
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
}

.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
  color: #d1d5db;
}

.empty-icon svg {
  width: 100%;
  height: 100%;
}

.empty-state h3 {
  font-size: 18px;
  font-weight: 700;
  color: #374151;
  margin: 0 0 8px;
}

.empty-state p {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.time-info {
  margin-top: 12px;
  font-size: 13px;
  color: #9ca3af;
}

.result-page {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.result-summary {
  margin-bottom: 20px;
}

.summary-card {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  border: 1px solid rgba(102, 126, 234, 0.15);
  border-radius: 12px;
  padding: 16px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.summary-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.summary-item .label {
  font-size: 12px;
  color: #6b7280;
}

.summary-item .value {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
}

.summary-item .value.score {
  color: #f59e0b;
  font-size: 20px;
}

.question-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.question-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.question-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.question-number {
  font-size: 14px;
  font-weight: 700;
  color: #667eea;
}

.type-badge {
  font-size: 12px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
}

.type-single { background: rgba(102, 126, 234, 0.1); color: #667eea; }
.type-multiple { background: rgba(79, 172, 254, 0.1); color: #4facfe; }
.type-judge { background: rgba(67, 233, 123, 0.1); color: #43e97b; }
.type-fill { background: rgba(250, 112, 154, 0.1); color: #fa709a; }
.type-essay { background: rgba(161, 140, 209, 0.1); color: #a18cd1; }

.score-badge {
  margin-left: auto;
  font-size: 14px;
  font-weight: 700;
  color: #f59e0b;
}

.score-total {
  font-weight: 400;
  color: #9ca3af;
}

.question-content {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.6;
  margin: 0 0 16px;
}

.options-area {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  background: #f9fafb;
  border-radius: 10px;
  border: 2px solid transparent;
}

.option-item.option-correct {
  background: rgba(16, 185, 129, 0.08);
  border-color: #10b981;
}

.option-item.option-correct-student {
  background: rgba(16, 185, 129, 0.12);
  border-color: #10b981;
}

.option-item.option-wrong-student {
  background: rgba(239, 68, 68, 0.08);
  border-color: #ef4444;
}

.option-key {
  width: 26px;
  height: 26px;
  border-radius: 6px;
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #6b7280;
  flex-shrink: 0;
}

.option-correct .option-key,
.option-correct-student .option-key {
  background: #10b981;
  color: #fff;
}

.option-wrong-student .option-key {
  background: #ef4444;
  color: #fff;
}

.option-text {
  flex: 1;
  font-size: 14px;
  color: #1f2937;
}

.option-badges {
  display: flex;
  gap: 6px;
  flex-shrink: 0;
}

.badge {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}

.badge.correct {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.badge.student {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

.answer-section {
  margin-bottom: 12px;
}

.answer-label {
  font-size: 12px;
  font-weight: 600;
  color: #6b7280;
  margin-bottom: 6px;
}

.answer-content {
  font-size: 14px;
  color: #1f2937;
  line-height: 1.6;
  padding: 10px 14px;
  background: #f9fafb;
  border-radius: 8px;
}

.answer-content.not-answered {
  color: #9ca3af;
  font-style: italic;
}

.answer-content.answered {
  background: rgba(102, 126, 234, 0.05);
}

.answer-content.graded {
  background: rgba(16, 185, 129, 0.05);
}

.answer-content.reference {
  background: rgba(16, 185, 129, 0.05);
  color: #059669;
}

.answer-content.analysis {
  background: rgba(245, 158, 11, 0.05);
  color: #92400e;
}

.evaluation-section {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f3f4f6;
}

.eval-item {
  margin-bottom: 10px;
}

.eval-item:last-child {
  margin-bottom: 0;
}

.eval-label {
  font-size: 12px;
  font-weight: 600;
  color: #6b7280;
  margin-bottom: 4px;
}

.eval-content {
  font-size: 13px;
  color: #374151;
  line-height: 1.6;
  padding: 8px 12px;
  background: #f9fafb;
  border-radius: 8px;
}

.answer-progress {
  padding: 12px 16px;
  background: #fff;
}

.progress-track {
  height: 4px;
  background: #e5e7eb;
  border-radius: 2px;
  overflow: hidden;
  margin-bottom: 6px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.3s ease;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #6b7280;
}

.question-indicators {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  overflow-x: auto;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
}

.indicator {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
  background: #f3f4f6;
  color: #6b7280;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.indicator.current {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: #667eea;
}

.indicator.answered {
  background: #d1fae5;
  color: #059669;
}

.indicator.answered.current {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.question-area {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.question-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.option-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  background: #f9fafb;
  border: 2px solid transparent;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  text-align: left;
  width: 100%;
}

.option-btn:hover {
  background: #f3f4f6;
}

.option-btn.selected {
  background: rgba(102, 126, 234, 0.08);
  border-color: #667eea;
}

.option-letter {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  color: #6b7280;
  flex-shrink: 0;
}

.option-btn.selected .option-letter {
  background: #667eea;
  color: #fff;
}

.option-text {
  flex: 1;
  font-size: 15px;
  color: #1f2937;
  line-height: 1.5;
}

.check-icon {
  width: 20px;
  height: 20px;
  color: #667eea;
  flex-shrink: 0;
}

.check-icon svg {
  width: 100%;
  height: 100%;
}

.judge-options {
  display: flex;
  gap: 16px;
}

.judge-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  background: #f9fafb;
  border: 2px solid transparent;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.judge-btn:hover {
  background: #f3f4f6;
}

.judge-btn.selected {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.08);
}

.judge-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.judge-icon svg {
  width: 28px;
  height: 28px;
}

.judge-icon.correct {
  background: #d1fae5;
  color: #059669;
}

.judge-icon.wrong {
  background: #fee2e2;
  color: #dc2626;
}

.text-input {
  width: 100%;
  padding: 14px 16px;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.6;
  color: #1f2937;
  resize: vertical;
  transition: border-color 0.2s;
  margin-top: 12px;
}

.text-input:focus {
  outline: none;
  border-color: #667eea;
  background: #fff;
}

.guide-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.guide-section {
  padding: 12px 14px;
  background: #f9fafb;
  border-radius: 10px;
  border-left: 3px solid #667eea;
}

.guide-label {
  font-size: 13px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 6px;
}

.guide-content {
  font-size: 14px;
  color: #374151;
  line-height: 1.7;
  white-space: pre-wrap;
}

.text-input::placeholder {
  color: #9ca3af;
}

.answer-footer {
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #e5e7eb;
}

.footer-btn {
  flex: 1;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: #f3f4f6;
  color: #374151;
  font-size: 15px;
  font-weight: 600;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.footer-btn:hover:not(:disabled) {
  background: #e5e7eb;
}

.footer-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.footer-btn svg {
  width: 18px;
  height: 18px;
}

.footer-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.footer-btn.primary:hover:not(:disabled) {
  opacity: 0.9;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.confirm-modal {
  background: #fff;
  border-radius: 20px;
  padding: 32px 24px;
  text-align: center;
  max-width: 340px;
  width: 100%;
  animation: modalIn 0.3s ease;
}

@keyframes modalIn {
  from { opacity: 0; transform: scale(0.9) translateY(20px); }
  to { opacity: 1; transform: scale(1) translateY(0); }
}

.modal-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-icon svg {
  width: 32px;
  height: 32px;
  color: #fff;
}

.confirm-modal h3 {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 8px;
}

.confirm-modal p {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 20px;
}

.modal-stats {
  display: flex;
  justify-content: center;
  gap: 24px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 12px;
  margin-bottom: 24px;
  font-size: 14px;
  color: #6b7280;
}

.modal-stats b {
  color: #667eea;
  font-weight: 700;
}

.modal-actions {
  display: flex;
  gap: 12px;
}

.btn-cancel, .btn-confirm {
  flex: 1;
  height: 44px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.btn-cancel {
  background: #f3f4f6;
  color: #374151;
}

.btn-cancel:hover {
  background: #e5e7eb;
}

.btn-confirm {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.btn-confirm:hover {
  opacity: 0.9;
}
</style>
