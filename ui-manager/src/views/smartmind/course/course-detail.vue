<template>
  <div class="glass-page course-detail-page">
    <!-- 玻璃风格背景 -->
    <div class="glass-bg">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
    </div>

    <!-- 第一层：课程基本信息 -->
    <div class="course-header-card" v-if="courseInfo">
      <div class="course-header-content">
        <div class="course-icon">
          <BookOutlined />
        </div>
        <div class="course-info">
          <div class="course-title-row">
            <h1 class="course-name">{{ courseInfo.courseName }}</h1>
            <a-tag :class="['status-tag', getStatusClass(courseInfo.status)]">
              {{ dictStore.getDataLabels('course_status', courseInfo.status) }}
            </a-tag>
          </div>
          <div class="course-code">课程编码：{{ courseInfo.courseCode }}</div>
          <div class="course-meta">
            <span class="meta-item">
              <TagOutlined />
              {{ dictStore.getDataLabels('subject_type', courseInfo.subjectType) }}
            </span>
            <span class="meta-item" v-if="courseInfo.semester">
              <CalendarOutlined />
              {{ dictStore.getDataLabels('semester', courseInfo.semester) }}
            </span>
            <span class="meta-item" v-if="courseInfo.className">
              <TeamOutlined />
              {{ courseInfo.className }}
            </span>
          </div>
          <p class="course-desc" v-if="courseInfo.courseDesc">{{ courseInfo.courseDesc }}</p>
        </div>
      </div>
    </div>

    <!-- 第二层：章节树 + 资源展示 -->
    <div class="course-content-area">
      <!-- 左侧：章节树形结构 -->
      <div class="chapter-sidebar">
        <div class="sidebar-header">
          <ApartmentOutlined />
          <span>课程章节</span>
        </div>
        <div class="chapter-tree-container">
          <a-tree
            v-if="chapterTree.length > 0"
            :tree-data="chapterTree"
            :selected-keys="selectedKeys"
            :default-expanded-keys="expandedKeys"
            @select="onChapterSelect"
            class="chapter-tree"
          >
            <template #title="{ title, level }">
              <span class="tree-node-title" :class="'level-' + level">
                {{ title }}
              </span>
            </template>
          </a-tree>
          <a-empty v-else description="暂无章节数据" class="empty-chapters" />
        </div>
      </div>

      <!-- 右侧：章节资源展示 -->
      <div class="resource-main">
        <div class="resource-header">
          <div class="current-chapter">
            <span class="chapter-label">当前章节</span>
            <h2 class="chapter-name">{{ currentChapter?.title || '请选择章节' }}</h2>
          </div>
        </div>

        <div class="resource-grid" v-if="currentChapter">
          <!-- 教案 -->
          <div class="resource-card">
            <div class="resource-icon lesson-plan">
              <FileTextOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">教案</div>
              <div class="resource-desc">查看本章节教学设计</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToTeachingPlan">
              查看
            </a-button>
          </div>

          <!-- 课件 -->
          <div class="resource-card">
            <div class="resource-icon courseware">
              <VideoCameraOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">课件</div>
              <div class="resource-desc">查看多媒体课件资源</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToCourseware">
              查看
            </a-button>
          </div>

          <!-- 实验指导书 -->
          <div class="resource-card">
            <div class="resource-icon experiment">
              <ExperimentOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">实验指导书</div>
              <div class="resource-desc">查看实验操作指南</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToExperimentGuide">
              查看
            </a-button>
          </div>

          <!-- 题库 -->
          <div class="resource-card">
            <div class="resource-icon question-bank">
              <DatabaseOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">课程题库</div>
              <div class="resource-desc">建设本章节题库</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToQuestionBank">
              查看
            </a-button>
          </div>

       
          <!-- 讨论活动 -->
          <div class="resource-card">
            <div class="resource-icon discussion">
              <MessageOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">讨论活动话题</div>
              <div class="resource-desc">话题讨论与交流</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToDiscussionTopic">
              查看
            </a-button>
          </div>

          <!-- 课堂教学 -->
          <div class="resource-card">
            <div class="resource-icon class-session">
              <DesktopOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">课堂教学</div>
              <div class="resource-desc">进入课堂活动管理</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToClassSession">
              查看
            </a-button>
          </div>

          <!-- 教学分析 -->
          <div class="resource-card">
            <div class="resource-icon class-session">
              <BarChartOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">教学分析</div>
              <div class="resource-desc">课堂数据分析与洞察</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn" @click="goToTeachingAnalysis">
              查看
            </a-button>
          </div>

             <!-- 随堂问题 -->
          <div class="resource-card">
            <div class="resource-icon questions">
              <QuestionCircleOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">随堂问题</div>
              <div class="resource-desc">课堂互动问答</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn">
              查看
            </a-button>
          </div>

          <!-- 随堂测试  -->
          <div class="resource-card">
            <div class="resource-icon quiz">
              <FormOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">随堂测试</div>
              <div class="resource-desc">章节知识点检测</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn">
              查看
            </a-button>
          </div>

          <!-- 课后作业 -->
          <div class="resource-card">
            <div class="resource-icon homework">
              <EditOutlined />
            </div>
            <div class="resource-info">
              <div class="resource-name">课后作业</div>
              <div class="resource-desc">课后巩固练习</div>
            </div>
            <a-button type="primary" size="small" class="resource-btn">
              查看
            </a-button>
          </div>


        </div>

        <div class="empty-resources" v-else>
          <a-empty description="请在左侧选择章节查看资源">
            <template #image>
              <FolderOpenOutlined style="font-size: 64px; color: #d1d5db;" />
            </template>
          </a-empty>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { message } from 'ant-design-vue';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { chapterApi } from '/@/api/smartmind/chapter-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import {
    BookOutlined,
    TagOutlined,
    CalendarOutlined,
    TeamOutlined,
    ApartmentOutlined,
    FileTextOutlined,
    VideoCameraOutlined,
    ExperimentOutlined,
    QuestionCircleOutlined,
    FormOutlined,
    EditOutlined,
    MessageOutlined,
    FolderOpenOutlined,
    DesktopOutlined,
    BarChartOutlined,
  } from '@ant-design/icons-vue';

  const route = useRoute();
  const router = useRouter();
  const dictStore = useDictStore();

  // 课程ID
  const courseId = ref(null);
  // 课程信息
  const courseInfo = ref(null);
  // 章节树数据
  const chapterTree = ref([]);
  // 当前选中的章节
  const currentChapter = ref(null);
  // 树选择keys
  const selectedKeys = ref([]);
  // 默认展开的keys
  const expandedKeys = ref([]);

  onMounted(() => {
    // 从路由query获取课程ID
    courseId.value = route.query.courseId;
    if (courseId.value) {
      loadCourseInfo();
      loadChapters();
    }
  });

  // 加载课程信息
  async function loadCourseInfo() {
    try {
      const res = await courseApi.getDetail(courseId.value);
      courseInfo.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 加载章节树
  async function loadChapters() {
    try {
      const res = await chapterApi.queryByCourseId(courseId.value);
      const chapters = res.data || [];
      chapterTree.value = buildChapterTree(chapters);
      
      // 默认展开第一级
      if (chapterTree.value.length > 0) {
        expandedKeys.value = chapterTree.value.map(item => item.key);
        // 默认选中第一个章节
        const firstChapter = findFirstChapter(chapterTree.value);
        if (firstChapter) {
          selectedKeys.value = [firstChapter.key];
          currentChapter.value = firstChapter;
        }
      }
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 构建章节树
  function buildChapterTree(chapters) {
    const map = {};
    const roots = [];

    // 先构建map
    chapters.forEach(chapter => {
      const key = `chapter-${chapter.id}`;
      map[chapter.id] = {
        key: key,
        title: chapter.unitName,
        level: parseInt(chapter.unitLevel) || 1,
        children: [],
        data: chapter,
      };
    });

    // 构建树形结构
    chapters.forEach(chapter => {
      const node = map[chapter.id];
      if (chapter.parentId && map[chapter.parentId]) {
        map[chapter.parentId].children.push(node);
      } else {
        roots.push(node);
      }
    });

    return roots;
  }

  // 查找第一个章节
  function findFirstChapter(tree) {
    for (const node of tree) {
      if (node.children && node.children.length > 0) {
        const firstChild = findFirstChapter(node.children);
        if (firstChild) return firstChild;
      }
      return node;
    }
    return null;
  }

  // 跳转到课件管理页面
  function goToCourseware() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    router.push({
      path: '/smartmind/courseware',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到教案管理页面
  function goToTeachingPlan() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    const chapterData = currentChapter.value.data;
    if (chapterData && chapterData.children && chapterData.children.length > 0) {
      message.warning('请选择叶子节点章节');
      return;
    }
    router.push({
      path: '/smartmind/teaching-plan',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到实验指导书管理页面
  function goToExperimentGuide() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    const chapterData = currentChapter.value.data;
    if (chapterData && chapterData.children && chapterData.children.length > 0) {
      message.warning('请选择叶子节点章节');
      return;
    }
    router.push({
      path: '/smartmind/experiment-guide',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到题库管理页面
  function goToQuestionBank() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    const chapterData = currentChapter.value.data;
    if (chapterData && chapterData.children && chapterData.children.length > 0) {
      message.warning('请选择叶子节点章节');
      return;
    }
    router.push({
      path: '/smartmind/question-bank',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到讨论活动话题管理页面
  function goToDiscussionTopic() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    const chapterData = currentChapter.value.data;
    if (chapterData && chapterData.children && chapterData.children.length > 0) {
      message.warning('请选择叶子节点章节');
      return;
    }
    router.push({
      path: '/smartmind/discussion-topic',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到教学分析页面
  function goToTeachingAnalysis() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    router.push({
      path: '/smartmind/teaching-analysis',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 跳转到课堂教学页面
  function goToClassSession() {
    if (!currentChapter.value) {
      message.warning('请先选择单元章节');
      return;
    }
    const unitCode = currentChapter.value.data?.unitCode;
    if (!unitCode) {
      message.warning('当前章节没有单元编码');
      return;
    }
    router.push({
      path: '/smartmind/class-session',
      query: {
        courseId: courseId.value,
        unitCode: unitCode,
      },
    });
  }

  // 章节选择
  function onChapterSelect(keys, { node }) {
    if (keys.length > 0) {
      selectedKeys.value = keys;
      currentChapter.value = {
        key: node.key,
        title: node.title,
        data: node.data,
      };
    }
  }

  // 获取状态样式
  function getStatusClass(status) {
    switch (status) {
      case '1':
        return 'active';
      case '0':
        return 'draft';
      case '2':
        return 'archived';
      default:
        return '';
    }
  }
</script>

<style scoped lang="less">
  // 玻璃拟态背景
  .glass-bg {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    overflow: hidden;

    .shape {
      position: absolute;
      border-radius: 50%;
      filter: blur(80px);
      opacity: 0.6;
    }

    .shape-1 {
      width: 400px;
      height: 400px;
      background: rgba(255, 255, 255, 0.2);
      top: -100px;
      right: -100px;
    }

    .shape-2 {
      width: 300px;
      height: 300px;
      background: rgba(118, 75, 162, 0.3);
      bottom: 10%;
      left: -50px;
    }

    .shape-3 {
      width: 200px;
      height: 200px;
      background: rgba(255, 255, 255, 0.15);
      top: 40%;
      right: 10%;
    }

    .shape-4 {
      width: 250px;
      height: 250px;
      background: rgba(102, 126, 234, 0.2);
      bottom: -50px;
      right: 20%;
    }
  }

  .course-detail-page {
    min-height: 100vh;
    padding: 20px;
  }

  // 课程头部卡片
  .course-header-card {
    background: rgba(255, 255, 255, 0.7);
    border-radius: 16px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.5);

    .course-header-content {
      display: flex;
      gap: 20px;
      align-items: flex-start;
    }

    .course-icon {
      width: 80px;
      height: 80px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 36px;
      color: #fff;
      flex-shrink: 0;
    }

    .course-info {
      flex: 1;

      .course-title-row {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 8px;

        .course-name {
          font-size: 24px;
          font-weight: 700;
          color: #1f2937;
          margin: 0;
        }

        .status-tag {
          font-size: 12px;
          padding: 2px 10px;
          border-radius: 12px;

          &.active {
            background: #dcfce7;
            color: #166534;
            border-color: #86efac;
          }

          &.draft {
            background: #fef9c3;
            color: #854d0e;
            border-color: #fde047;
          }

          &.archived {
            background: #f3f4f6;
            color: #6b7280;
            border-color: #d1d5db;
          }
        }
      }

      .course-code {
        font-size: 13px;
        color: #6b7280;
        margin-bottom: 12px;
      }

      .course-meta {
        display: flex;
        gap: 20px;
        margin-bottom: 12px;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 13px;
          color: #4b5563;

          :deep(.anticon) {
            color: #667eea;
          }
        }
      }

      .course-desc {
        font-size: 14px;
        color: #6b7280;
        line-height: 1.6;
        margin: 0;
      }
    }
  }

  // 内容区域
  .course-content-area {
    display: flex;
    gap: 20px;
    min-height: calc(100vh - 280px);

    // 左侧章节侧边栏
    .chapter-sidebar {
      width: 280px;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.5);
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .sidebar-header {
        padding: 16px 20px;
        border-bottom: 1px solid #e5e7eb;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 600;
        color: #1f2937;
        font-size: 15px;

        :deep(.anticon) {
          color: #667eea;
        }
      }

      .chapter-tree-container {
        flex: 1;
        overflow-y: auto;
        padding: 12px;

        .chapter-tree {
          :deep(.ant-tree-treenode) {
            padding: 4px 0;
          }

          :deep(.ant-tree-node-content-wrapper) {
            padding: 6px 8px;
            border-radius: 6px;
            transition: all 0.2s;

            &:hover {
              background: rgba(102, 126, 234, 0.1);
            }

            &.ant-tree-node-selected {
              background: rgba(102, 126, 234, 0.15);
            }
          }

          .tree-node-title {
            font-size: 13px;

            &.level-1 {
              font-weight: 600;
              color: #1f2937;
            }

            &.level-2 {
              font-weight: 500;
              color: #374151;
            }

            &.level-3 {
              color: #4b5563;
            }
          }
        }

        .empty-chapters {
          margin-top: 40px;
        }
      }
    }

    // 右侧资源主区域
    .resource-main {
      flex: 1;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.5);
      padding: 24px;
      overflow-y: auto;

      .resource-header {
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 1px solid #e5e7eb;

        .current-chapter {
          .chapter-label {
            font-size: 12px;
            color: #9ca3af;
            text-transform: uppercase;
            letter-spacing: 1px;
          }

          .chapter-name {
            font-size: 20px;
            font-weight: 600;
            color: #1f2937;
            margin: 8px 0 0;
          }
        }
      }

      .resource-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 16px;

        .resource-card {
          background: rgba(255, 255, 255, 0.7);
          backdrop-filter: blur(20px);
          border: 1px solid rgba(255, 255, 255, 0.5);
          border-radius: 12px;
          padding: 20px;
          display: flex;
          align-items: center;
          gap: 16px;
          transition: all 0.3s;
          cursor: pointer;
          box-shadow: 0 8px 32px rgba(31, 38, 135, 0.1);

          &:hover {
            transform: translateY(-4px);
            box-shadow: 0 25px 50px rgba(31, 38, 135, 0.2);
            border-color: rgba(102, 126, 234, 0.3);
            background: rgba(255, 255, 255, 0.85);
          }

          .resource-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            flex-shrink: 0;

            &.lesson-plan {
              background: #dbeafe;
              color: #2563eb;
            }

            &.courseware {
              background: #fce7f3;
              color: #db2777;
            }

            &.experiment {
              background: #d1fae5;
              color: #059669;
            }

            &.questions {
              background: #fef3c7;
              color: #d97706;
            }

            &.quiz {
              background: #ede9fe;
              color: #7c3aed;
            }

            &.homework {
              background: #ffe4e6;
              color: #e11d48;
            }

            &.discussion {
              background: #cffafe;
              color: #0891b2;
            }
          }

          .resource-info {
            flex: 1;
            min-width: 0;

            .resource-name {
              font-size: 15px;
              font-weight: 600;
              color: #1f2937;
              margin-bottom: 4px;
            }

            .resource-desc {
              font-size: 12px;
              color: #9ca3af;
            }
          }

          .resource-btn {
            flex-shrink: 0;
          }
        }
      }

      .empty-resources {
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 400px;
      }
    }
  }
</style>
