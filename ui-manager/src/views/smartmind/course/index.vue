<template>
  <div class="glass-page">
    <!-- 玻璃风格背景 -->
    <div class="glass-bg">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 页面标题区 -->
    <header class="glass-header">
      <div class="header-content">
        <div class="title-section">
          <div class="title-icon">
            <ReadOutlined />
          </div>
          <div class="title-text">
            <h1>课程管理</h1>
            <p>构建知识体系，启发学生智慧</p>
          </div>
        </div>
        <a-button type="primary" @click="toAdd" class="btn-create">
          <PlusOutlined /> 新建课程
        </a-button>
      </div>
    </header>

    <!-- 搜索卡片 -->
    <div class="glass-search">
      <div class="search-content">
        <div class="search-inputs">
          <a-input
            v-model:value="queryForm.courseCode"
            placeholder="课程编码"
            class="search-input"
            allowClear
          >
            <template #prefix><SearchOutlined /></template>
          </a-input>
          <a-input
            v-model:value="queryForm.courseName"
            placeholder="搜索课程名称..."
            class="search-input"
            allowClear
          >
            <template #prefix><BookOutlined /></template>
          </a-input>
          <a-select
            v-model:value="queryForm.subjectType"
            placeholder="学科"
            class="search-select"
            allowClear
          >
            <a-select-option v-for="item in dictStore.getDictData('subject_type')" :key="item.dataValue" :value="item.dataValue">
              {{ item.dataLabel }}
            </a-select-option>
          </a-select>
        </div>
        <div class="search-actions">
          <a-button @click="resetQuery" class="btn-reset">
            <ReloadOutlined /> 重置
          </a-button>
          <a-button type="primary" @click="onSearch" class="btn-search">
            <SearchOutlined /> 查询
          </a-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="glass-stats">
      <div class="stat-card total">
        <div class="stat-icon"><ReadOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ total }}</div>
          <div class="stat-label">全部课程</div>
        </div>
      </div>
      <div class="stat-card active">
        <div class="stat-icon"><PlayCircleOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ activeCount }}</div>
          <div class="stat-label">进行中</div>
        </div>
      </div>
      <div class="stat-card draft">
        <div class="stat-icon"><EditOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ draftCount }}</div>
          <div class="stat-label">草稿</div>
        </div>
      </div>
      <div class="stat-card archived">
        <div class="stat-icon"><InboxOutlined /></div>
        <div class="stat-info">
          <div class="stat-value">{{ archivedCount }}</div>
          <div class="stat-label">已归档</div>
        </div>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="glass-action-bar">
      <div class="section-title">
        <span class="title-text">课程列表</span>
        <span class="title-count">共 {{ total }} 门课程</span>
      </div>
    </div>

    <!-- 课程卡片网格 -->
    <div class="glass-card-grid">
      <div
        v-for="course in tableData"
        :key="course.id"
        class="glass-card"
        :class="{ 'archived': course.status === '2' }"
      >
        <!-- 卡片头部 -->
        <div class="card-header">
          <span class="tag">
            {{ dictStore.getDataLabels('subject_type', course.subjectType) }}
          </span>
          <a-tag :class="['status-tag', getStatusClass(course.status)]">
            {{ dictStore.getDataLabels('course_status', course.status) }}
          </a-tag>
        </div>

        <!-- 卡片主体 -->
        <div class="card-body">
          <div class="code">{{ course.courseCode }}</div>
          <h3 class="card-title">{{ course.courseName }}</h3>
          <div class="meta-row">
             <span class="meta-item">
              <CalendarOutlined />
              {{ dictStore.getDataLabels('school_year', course.schoolYear) }}
            </span>
            <span class="meta-item" v-if="course.className">
              <TeamOutlined />
              {{ course.className }}
            </span>
            <span class="meta-item" v-else>
              <TeamOutlined />
              未关联班级
            </span>
           
          </div>
          <p class="desc" v-if="course.courseDesc">{{ course.courseDesc }}</p>
          <p class="desc empty" v-else>暂无课程描述</p>
        </div>

        <!-- 卡片操作 -->
        <div class="card-actions">

          <a-tooltip title="知识结构">
            <a-button type="text" @click="toEditKnowledgeFramework(course)" class="action-btn">
              <ApartmentOutlined />
              <span class="btn-text">知识</span>
            </a-button>
          </a-tooltip>

           <a-tooltip title="编辑课程">
            <a-button type="text" @click="toEdit(course)" class="action-btn">
              <EditOutlined />
              <span class="btn-text">编辑</span>
            </a-button>
          </a-tooltip>

            <a-tooltip title="章节管理">
            <a-button type="text" @click="toEditChapter(course)" class="action-btn">
              <UnorderedListOutlined />
              <span class="btn-text">章节</span>
            </a-button>
          </a-tooltip>

          <a-tooltip title="课程详情">
            <a-button type="text" @click="toDetail(course)" class="action-btn detail">
              <EyeOutlined />
              <span class="btn-text">详情</span>
            </a-button>
          </a-tooltip>

          <a-tooltip title="课堂教学">
            <a-button type="text" @click="toSelectClazz(course)" class="action-btn">
              <DesktopOutlined />
              <span class="btn-text">课堂</span>
            </a-button>
          </a-tooltip>
         
          <a-tooltip title="教学分析">
            <a-button type="text" @click="toTeachingAnalysis(course)" class="action-btn">
              <BarChartOutlined />
              <span class="btn-text">分析</span>
            </a-button>
          </a-tooltip>

          
          
        
          <!-- <a-popconfirm
            title="确定要归档此课程吗?"
            description="归档后课程将不可编辑"
            @confirm="toArchive(course)"
            placement="topRight"
          >
            <a-tooltip title="归档">
              <a-button type="text" class="action-btn archive">
                <InboxOutlined />
                <span class="btn-text">归档</span>
              </a-button>
            </a-tooltip>
          </a-popconfirm> -->
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <a-empty v-if="tableData.length === 0 && !tableLoading" description="暂无课程数据" class="glass-empty">
      <a-button type="primary" @click="toAdd">创建第一门课程</a-button>
    </a-empty>

    <!-- 分页 -->
    <div class="glass-pagination" v-if="total > 0">
      <a-pagination
        showSizeChanger
        showQuickJumper
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryForm.pageSize"
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryData"
        :show-total="(total) => `共 ${total} 条`"
      />
    </div>

    <!-- 弹窗组件 -->
    <CourseFormModal ref="formModalRef" @reloadList="queryData" />
    <ChapterModal ref="chapterModalRef" @reloadList="queryData" />
    <KnowledgeFrameworkModal ref="knowledgeFrameworkModalRef" @reloadList="queryData" />
    <ClassSelectModal ref="classSelectModalRef" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import CourseFormModal from './course-form-modal.vue';
  import ChapterModal from './chapter-modal.vue';
  import KnowledgeFrameworkModal from './knowledge-framework-modal.vue';
  import ClassSelectModal from './class-select-modal.vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    BookOutlined,
    ReadOutlined,
    PlayCircleOutlined,
    EditOutlined,
    InboxOutlined,
    TeamOutlined,
    CalendarOutlined,
    UnorderedListOutlined,
    ApartmentOutlined,
    EyeOutlined,
    BarChartOutlined,
    DesktopOutlined,
  } from '@ant-design/icons-vue';

  const dictStore = useDictStore();
  const router = useRouter();

  // 查询表单
  const queryForm = reactive({
    courseCode: '',
    courseName: '',
    subjectType: undefined,
    pageNum: 1,
    pageSize: 12,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  // 统计数量
  const activeCount = computed(() => tableData.value.filter(c => c.status === '1').length);
  const draftCount = computed(() => tableData.value.filter(c => c.status === '0').length);
  const archivedCount = computed(() => tableData.value.filter(c => c.status === '2').length);

  // 弹窗引用
  const formModalRef = ref();
  const chapterModalRef = ref();
  const knowledgeFrameworkModalRef = ref();
  const classSelectModalRef = ref();

  onMounted(() => {
    queryData();
  });

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await courseApi.queryPage(queryForm);
      const data = res.data || {};
      tableData.value = data.list || data.rows || [];
      total.value = data.total || data.totalRow || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 搜索
  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  // 重置
  function resetQuery() {
    queryForm.courseCode = '';
    queryForm.courseName = '';
    queryForm.subjectType = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 状态样式
  function getStatusClass(status) {
    const map = {
      '0': 'draft',
      '1': 'active',
      '2': 'archived'
    };
    return map[status] || 'default';
  }

  // 进入课程详情
  function toDetail(record) {
    router.push({
      path: '/course/detail',
      query: { courseId: record.id }
    });
  }

  // 打开编辑弹窗
  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  // 打开新增弹窗
  function toAdd() {
    formModalRef.value.showModal();
  }

  // 进入课堂教学
  function toSelectClazz(record) {
    router.push({
      path: '/smartmind/class-session',
      query: { courseId: record.id }
    });
  }

  // 进入教学分析
  function toTeachingAnalysis(record) {
    router.push({
      path: '/smartmind/teaching-analysis',
      query: { courseId: record.id }
    });
  }

  // 打开章节管理
  function toEditChapter(record) {
    chapterModalRef.value.showModal(record);
  }

  // 打开知识结构
  function toEditKnowledgeFramework(record) {
    knowledgeFrameworkModalRef.value.showModal(record);
  }

  // 归档
  async function toArchive(record) {
    try {
      await courseApi.archive(record.id);
      message.success('归档成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>

<style lang="less">
  // 导入公共玻璃主题
  @import '/@/styles/glass-theme.less';

  // 课程卡片按钮样式覆盖 - 只显示图标，图标更醒目
  .glass-card {
    .card-actions {
      .action-btn {
        min-width: 44px;
        width: 44px;
        height: 44px;
        padding: 0;

        .btn-text {
          display: none;
        }

        :deep(.anticon) {
          font-size: 20px;
        }
      }
    }
  }
</style>
