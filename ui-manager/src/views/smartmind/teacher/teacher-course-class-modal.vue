<template>
  <a-modal
    v-model:open="visible"
    title="配置教学课程"
    ok-text="关闭"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="900px"
    :confirmLoading="confirmLoading"
  >
    <div class="teacher-info" v-if="teacher">
      <div class="info-item">
        <span class="label">教师：</span>
        <span class="value">{{ teacher.teacherName }}</span>
      </div>
      <div class="info-item">
        <span class="label">工号：</span>
        <span class="value">{{ teacher.teacherNo }}</span>
      </div>
    </div>

    <!-- 添加新关联 -->
    <div class="add-section">
      <!-- 横向步骤条 -->
      <div class="steps-horizontal">
        <div class="step-h-item" :class="{ active: true, completed: selectedSemester }">
          <div class="step-h-icon">
            <span v-if="selectedSemester" class="completed-icon">✓</span>
            <span v-else>1</span>
          </div>
          <div class="step-h-content">
            <div class="step-h-name">选择学期</div>
          </div>
        </div>
        <div class="step-h-line" :class="{ active: selectedCourses.length > 0, completed: selectedSemester }"></div>
        <div class="step-h-item" :class="{ active: selectedSemester, completed: selectedCourses.length > 0 }">
          <div class="step-h-icon">
            <span v-if="selectedCourses.length > 0" class="completed-icon">✓</span>
            <span v-else>2</span>
          </div>
          <div class="step-h-content">
            <div class="step-h-name">选择课程</div>
            <div class="step-h-desc" v-if="selectedCourses.length > 0">已选{{ selectedCourses.length }}门</div>
          </div>
        </div>
        <div class="step-h-line" :class="{ active: selectedRelations.length > 0, completed: selectedCourses.length > 0 }"></div>
        <div class="step-h-item" :class="{ active: selectedCourses.length > 0, completed: selectedRelations.length > 0 }">
          <div class="step-h-icon">
            <span v-if="selectedRelations.length > 0" class="completed-icon">✓</span>
            <span v-else>3</span>
          </div>
          <div class="step-h-content">
            <div class="step-h-name">选择班级</div>
            <div class="step-h-desc" v-if="selectedRelations.length > 0">已选{{ selectedRelations.length }}个</div>
          </div>
        </div>
      </div>

      <div class="section-title">添加新关联</div>
      
      <div class="add-content-wrapper">
        <!-- 左侧：步骤内容 -->
        <div class="add-content-left">
          <!-- 第一步：选择学期 -->
          <div class="step-section">
            <div class="step-label">1. 选择学期</div>
        <div class="semester-select">
          <a-select
            v-model:value="selectedSemester"
            placeholder="请选择学期"
            style="width: 280px"
            @change="onSemesterChange"
          >
            <a-select-option v-for="item in dictStore.getDictData('semester')" :key="item.dataValue" :value="item.dataValue">
              {{ item.dataLabel }}
            </a-select-option>
          </a-select>
        </div>
      </div>

      <!-- 第二步：选择课程 -->
      <div class="step-section" v-if="selectedSemester">
        <div class="step-label">2. 选择课程（可多选）</div>
        <div class="course-list">
          <div 
            v-for="course in courseOptions" 
            :key="course.id" 
            class="course-item"
            :class="{ selected: isCourseSelected(course.id) }"
            @click="toggleCourse(course)"
          >
            <div class="course-checkbox">
              <a-checkbox 
                :checked="isCourseSelected(course.id)" 
                @click.stop 
              />
            </div>
            <div class="course-content">
              <div class="course-name">{{ course.courseName }}</div>
              <div class="course-info" v-if="course.subjectType">
                {{ dictStore.getDataLabels('subject_type', course.subjectType) }}
              </div>
            </div>
          </div>
          <div v-if="courseOptions.length === 0 && !courseLoading" class="empty-tip">
            该学期暂无课程
          </div>
        </div>
      </div>

      <!-- 第三步：选择班级 -->
      <div class="step-section" v-if="selectedCourses.length > 0">
        <div class="step-label">3. 选择班级（可多选）</div>
        <!-- 已选课程标签 -->
        <div class="selected-courses-bar" v-if="selectedCourses.length > 0">
          <span class="bar-label">已选课程：</span>
          <a-tag 
            v-for="course in selectedCourses" 
            :key="course.id"
            :class="['course-tag', { active: activeCourseId === course.id }]"
            @click="selectCourseForClass(course)"
          >
            {{ course.courseName }}
          </a-tag>
        </div>
        <!-- 班级列表 -->
        <div class="class-list" v-if="activeCourseId">
          <div 
            v-for="clazz in classOptions" 
            :key="clazz.id" 
            class="class-item"
            :class="{ selected: isClassSelected(activeCourseId, clazz.id) }"
            @click="toggleClass(activeCourseId, clazz.id, clazz.className)"
          >
            <div class="class-checkbox">
              <a-checkbox 
                :checked="isClassSelected(activeCourseId, clazz.id)" 
                @click.stop 
              />
            </div>
            <div class="class-content">
              <div class="class-name">{{ clazz.className }}</div>
              <div class="class-info" v-if="clazz.gradeLevel">
                {{ dictStore.getDataLabels('grade_level', clazz.gradeLevel) }}
              </div>
            </div>
          </div>
          <div v-if="classOptions.length === 0 && !classLoading" class="empty-tip">
            该课程未关联班级
          </div>
        </div>
        <div v-else class="empty-tip">
          请点击上方课程标签查看对应班级
        </div>

        <!-- 已选择的关联预览 -->
        <div class="selected-preview" v-if="selectedRelations.length > 0">
          <div class="preview-title">已选择的关联：</div>
          <div class="preview-list">
            <a-tag 
              v-for="(item, index) in selectedRelations" 
              :key="index"
              closable
              @close="removeRelation(item)"
              class="preview-tag"
            >
              {{ item.courseName }} - {{ item.className }}
            </a-tag>
          </div>
        </div>
        
        <!-- 保存按钮 -->
        <div class="save-actions" v-if="selectedRelations.length > 0">
          <span class="selected-info">已选择 {{ selectedRelations.length }} 个关联</span>
          <div class="action-btns">
            <a-button type="primary" @click="saveRelations" :loading="saving">
              保存关联
            </a-button>
            <a-button @click="clearAllSelection" style="margin-left: 8px">清空</a-button>
          </div>
        </div>
      </div>
      <!-- 左侧内容结束 -->


    </div>
    <!-- 内容包装器结束 -->
  </div>
  <!-- add-section 结束 -->
</div>

    <!-- 已配置列表 -->
    <div class="section-title" style="margin-top: 24px">
      <span>已配置课程班级</span>
    </div>

    <a-table
      :dataSource="relationList"
      :columns="columns"
      rowKey="id"
      size="small"
      :pagination="false"
      class="relation-table"
      :scroll="{ y: 200 }"
    >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'semester'">
          {{ dictStore.getDataLabels('semester', record.semester) || '-' }}
        </template>
        <template v-else-if="column.dataIndex === 'status'">
          <a-tag :class="['status-tag', record.status === '1' ? 'active' : 'inactive']">
            {{ record.status === '1' ? '有效' : '失效' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <a-popconfirm title="确定要删除此关联吗?" @confirm="deleteRelation(record)">
            <a-button type="link" danger size="small">删除</a-button>
          </a-popconfirm>
        </template>
      </template>
      <template #emptyText>
        <div class="empty-text">暂无配置</div>
      </template>
    </a-table>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { ref, computed } from 'vue';
  import { teacherCourseClassApi } from '/@/api/smartmind/teacherCourseClass-api.js';
  import { courseApi } from '/@/api/smartmind/course-api.js';
  import { classCourseApi } from '/@/api/smartmind/classCourse-api.js';
  import { clazzApi } from '/@/api/smartmind/clazz-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const saving = ref(false);
  const teacher = ref(null);
  const relationList = ref([]);

  // 学期选择
  const selectedSemester = ref(undefined);

  // 课程选择（多选）
  const selectedCourses = ref([]);
  const courseOptions = ref([]);
  const courseLoading = ref(false);
  // 当前查看班级的课程
  const activeCourseId = ref(null);

  // 班级选择
  const classOptions = ref([]);
  const classLoading = ref(false);

  // 选中的关联列表 { teacherId, courseId, classId, courseName, className }
  const selectedRelations = ref([]);

  // 表格列
  const columns = [
    {
      title: '课程',
      dataIndex: 'courseName',
      ellipsis: true,
    },
    {
      title: '班级',
      dataIndex: 'className',
      width: 120,
    },
    {
      title: '学期',
      dataIndex: 'semester',
      width: 150,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      align: 'center',
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 80,
      align: 'center',
    },
  ];

  // 显示弹窗
  async function showModal(rowData) {
    teacher.value = rowData;
    visible.value = true;
    resetForm();
    await loadRelationList();
  }

  // 重置表单
  function resetForm() {
    selectedSemester.value = undefined;
    selectedCourses.value = [];
    activeCourseId.value = null;
    courseOptions.value = [];
    classOptions.value = [];
    selectedRelations.value = [];
  }

  // 加载关联列表
  async function loadRelationList() {
    try {
      const res = await teacherCourseClassApi.queryPage({
        teacherId: teacher.value.id,
        pageNum: 1,
        pageSize: 100,
      });
      relationList.value = res.data?.rows || res.data?.list || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 学期改变
  async function onSemesterChange(semester) {
    selectedCourses.value = [];
    activeCourseId.value = null;
    classOptions.value = [];
    if (semester) {
      await loadCourseBySemester(semester);
    } else {
      courseOptions.value = [];
    }
  }

  // 根据学期加载课程
  async function loadCourseBySemester(semester) {
    courseLoading.value = true;
    try {
      const res = await courseApi.queryPage({
        semester: semester,
        status: '1',
        pageNum: 1,
        pageSize: 1000,
      });
      courseOptions.value = res.data?.rows || res.data?.list || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      courseLoading.value = false;
    }
  }

  // 判断是否已选中课程
  function isCourseSelected(courseId) {
    return selectedCourses.value.some(c => c.id === courseId);
  }

  // 切换课程选择
  async function toggleCourse(course) {
    const index = selectedCourses.value.findIndex(c => c.id === course.id);
    if (index > -1) {
      // 取消选择，移除相关班级关联
      selectedCourses.value.splice(index, 1);
      selectedRelations.value = selectedRelations.value.filter(
        r => r.courseId !== course.id
      );
      // 如果当前查看的是这个课程，切换到其他课程或清空
      if (activeCourseId.value === course.id) {
        activeCourseId.value = selectedCourses.value.length > 0 ? selectedCourses.value[0].id : null;
        if (activeCourseId.value) {
          await loadClassByCourse(activeCourseId.value);
        } else {
          classOptions.value = [];
        }
      }
    } else {
      selectedCourses.value.push(course);
      // 如果是第一个选择的课程，自动加载其班级
      if (selectedCourses.value.length === 1) {
        activeCourseId.value = course.id;
        await loadClassByCourse(course.id);
      }
    }
  }

  // 选择课程查看班级
  async function selectCourseForClass(course) {
    activeCourseId.value = course.id;
    await loadClassByCourse(course.id);
  }

  // 根据课程加载班级
  async function loadClassByCourse(courseId) {
    classLoading.value = true;
    try {
      const res = await classCourseApi.queryPage({
        courseId: courseId,
        pageNum: 1,
        pageSize: 1000,
      });
      const classCourseList = res.data?.rows || res.data?.list || [];
      // 提取班级ID
      const classIds = classCourseList.map(item => item.classId).filter(id => id);
      
      // 批量查询班级详情
      let clazzMap = {};
      if (classIds.length > 0) {
        const clazzRes = await clazzApi.queryPage({
          ids: classIds,
          pageNum: 1,
          pageSize: classIds.length,
        });
        const clazzList = clazzRes.data?.rows || clazzRes.data?.list || [];
        clazzMap = clazzList.reduce((map, clazz) => {
          map[clazz.id] = clazz;
          return map;
        }, {});
      }
      
      // 组装班级信息
      classOptions.value = classCourseList.map(item => {
        const clazz = clazzMap[item.classId] || {};
        return {
          id: item.classId,
          className: item.className || clazz.className || `班级${item.classId}`,
          gradeLevel: item.gradeLevel || clazz.gradeLevel,
        };
      });
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      classLoading.value = false;
    }
  }

  // 判断是否已选中班级
  function isClassSelected(courseId, classId) {
    return selectedRelations.value.some(
      r => r.courseId === courseId && r.classId === classId
    );
  }

  // 切换班级选择
  function toggleClass(courseId, classId, className) {
    const index = selectedRelations.value.findIndex(
      r => r.courseId === courseId && r.classId === classId
    );
    if (index > -1) {
      selectedRelations.value.splice(index, 1);
    } else {
      const course = selectedCourses.value.find(c => c.id === courseId);
      selectedRelations.value.push({
        teacherId: teacher.value.id,
        courseId: courseId,
        classId: classId,
        courseName: course?.courseName || '',
        className: className,
      });
    }
  }

  // 移除关联
  function removeRelation(item) {
    const index = selectedRelations.value.findIndex(
      r => r.courseId === item.courseId && r.classId === item.classId
    );
    if (index > -1) {
      selectedRelations.value.splice(index, 1);
    }
  }

  // 清空所有选择
  function clearAllSelection() {
    selectedRelations.value = [];
  }

  // 保存关联
  async function saveRelations() {
    if (selectedRelations.value.length === 0) {
      message.warning('请至少选择一个关联');
      return;
    }

    saving.value = true;
    SmartLoading.show();
    
    try {
      // 构建请求参数
      const items = selectedRelations.value.map(r => ({
        teacherId: r.teacherId,
        courseId: r.courseId,
        classId: r.classId,
      }));
      
      await teacherCourseClassApi.addBatch({ items });
      
      message.success('关联成功');
      resetForm();
      await loadRelationList();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      saving.value = false;
      SmartLoading.hide();
    }
  }

  // 删除关联
  async function deleteRelation(record) {
    try {
      await teacherCourseClassApi.delete(record.id);
      message.success('删除成功');
      await loadRelationList();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function onClose() {
    visible.value = false;
    teacher.value = null;
    resetForm();
  }

  function onSubmit() {
    onClose();
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less">
  .teacher-info {
    display: flex;
    gap: 24px;
    margin-bottom: 20px;
    padding: 16px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 8px;
    color: #fff;

    .info-item {
      display: flex;
      align-items: center;
      gap: 8px;

      .label {
        opacity: 0.9;
        font-size: 13px;
      }

      .value {
        font-weight: 500;
        font-size: 15px;
      }
    }
  }

  // 添加新关联内容包装器
  .add-content-wrapper {
    display: flex;
    gap: 20px;
  }

  .add-content-left {
    flex: 1;
    min-width: 0;
  }

  .add-content-right {
    display: none;
  }

  // 横向步骤条
  .steps-horizontal {
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12px;
    padding: 16px 24px;
    margin-bottom: 20px;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);

    .step-h-item {
      display: flex;
      align-items: center;
      gap: 8px;
      transition: all 0.3s;
      opacity: 0.5;

      &.active {
        opacity: 1;
      }

      &.completed {
        opacity: 1;
      }

      .step-h-icon {
        width: 28px;
        height: 28px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 600;
        font-size: 13px;
        color: #fff;
        flex-shrink: 0;
        transition: all 0.3s;

        .completed-icon {
          color: #4ade80;
          font-size: 14px;
        }
      }

      &.active .step-h-icon {
        background: #fff;
        color: #667eea;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
      }

      &.completed .step-h-icon {
        background: #4ade80;
        color: #fff;
      }

      .step-h-content {
        .step-h-name {
          font-size: 14px;
          font-weight: 600;
          color: #fff;
        }

        .step-h-desc {
          font-size: 11px;
          color: rgba(255, 255, 255, 0.85);
          margin-top: 2px;
        }
      }
    }

    .step-h-line {
      width: 60px;
      height: 2px;
      background: rgba(255, 255, 255, 0.2);
      margin: 0 12px;
      transition: all 0.3s;
      position: relative;

      &.completed {
        background: #4ade80;
      }

      &.active {
        background: linear-gradient(to right, #4ade80, rgba(255, 255, 255, 0.2));
      }

      &::after {
        content: '';
        position: absolute;
        right: 0;
        top: -3px;
        width: 0;
        height: 0;
        border-left: 6px solid rgba(255, 255, 255, 0.2);
        border-top: 4px solid transparent;
        border-bottom: 4px solid transparent;
      }

      &.completed::after {
        border-left-color: #4ade80;
      }
    }
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid #e5e7eb;
  }

  .step-section {
    margin-bottom: 20px;
  }

  .step-label {
    font-size: 14px;
    font-weight: 500;
    color: #4b5563;
    margin-bottom: 12px;
  }

  .semester-select {
    display: flex;
    align-items: center;
  }

  .course-list, .class-list {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    max-height: 250px;
    overflow-y: auto;
    padding: 4px;
  }

  .course-item, .class-item {
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 12px 16px;
    min-width: 180px;
    flex: 0 0 calc(33.333% - 8px);
    transition: all 0.2s;
    cursor: pointer;
    display: flex;
    align-items: flex-start;
    gap: 10px;

    &:hover {
      border-color: #667eea;
      background: rgba(102, 126, 234, 0.05);
    }

    &.selected {
      border-color: #667eea;
      background: rgba(102, 126, 234, 0.1);
      box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
    }

    .course-checkbox, .class-checkbox {
      flex-shrink: 0;
      padding-top: 2px;
    }

    .course-content, .class-content {
      flex: 1;
      min-width: 0;
    }

    .course-name, .class-name {
      font-weight: 600;
      color: #1f2937;
      font-size: 15px;
      margin-bottom: 4px;
    }

    .course-info, .class-info {
      font-size: 12px;
      color: #6b7280;
    }
  }

  .empty-tip {
    width: 100%;
    text-align: center;
    color: #9ca3af;
    padding: 40px 0;
    font-size: 14px;
  }

  .selected-preview {
    margin-top: 16px;
    padding: 12px;
    background: #f9fafb;
    border-radius: 8px;

    .preview-title {
      font-size: 13px;
      color: #6b7280;
      margin-bottom: 8px;
    }

    .preview-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }

    .preview-tag {
      margin: 0;
    }
  }

  .save-actions {
    margin-top: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 16px;
    border-top: 1px solid #e5e7eb;

    .selected-info {
      font-size: 14px;
      color: #667eea;
      font-weight: 500;
    }

    .action-btns {
      display: flex;
    }
  }

  .selected-courses-bar {
    margin-bottom: 12px;
    padding: 12px;
    background: #f9fafb;
    border-radius: 8px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;

    .bar-label {
      font-size: 13px;
      color: #6b7280;
      flex-shrink: 0;
    }

    .course-tag {
      cursor: pointer;
      transition: all 0.2s;

      &:hover {
        opacity: 0.8;
      }

      &.active {
        background: #667eea;
        color: #fff;
        border-color: #667eea;
      }
    }
  }

  .relation-table {
    margin-bottom: 20px;
  }

  .empty-text {
    color: #9ca3af;
    padding: 20px 0;
  }

  .status-tag {
    padding: 2px 10px;
    border-radius: 10px;
    font-size: 12px;
    font-weight: 500;
  }

  .status-tag.active {
    background: rgba(16, 185, 129, 0.1);
    color: #059669;
    border: 1px solid rgba(16, 185, 129, 0.2);
  }

  .status-tag.inactive {
    background: rgba(156, 163, 175, 0.1);
    color: #6b7280;
    border: 1px solid rgba(156, 163, 175, 0.2);
  }
</style>
