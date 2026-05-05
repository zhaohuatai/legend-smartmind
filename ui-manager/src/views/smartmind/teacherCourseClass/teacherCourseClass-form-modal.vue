<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑教师课程班级关联' : '新建教师课程班级关联'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="1020px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row>
        <a-col :span="8">
          <a-form-item label="教师ID" name="teacherId">
            <a-select 
              v-model:value="form.teacherId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdTeacher" 
              :loading="smdTeacherLoading"
              @focus="remoteSelectSmdTeacher('')"
              allowClear
            >
              <a-select-option v-for="item in smdTeacherOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="教师姓名" name="teacherName">
            <a-input v-model:value="form.teacherName" placeholder="请输入教师姓名"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="课程ID" name="courseId">
            <a-select 
              v-model:value="form.courseId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdCourse" 
              :loading="smdCourseLoading"
              @focus="remoteSelectSmdCourse('')"
              allowClear
            >
              <a-select-option v-for="item in smdCourseOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="班级ID" name="classId">
            <a-select 
              v-model:value="form.classId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdClazz" 
              :loading="smdClazzLoading"
              @focus="remoteSelectSmdClazz('')"
              allowClear
            >
              <a-select-option v-for="item in smdClazzOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="班级名称" name="className">
            <a-input v-model:value="form.className" placeholder="请输入班级名称"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="学期" name="semester">
            <a-select v-model:value="form.semester" placeholder="请选择学期" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('semester')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="学年" name="schoolYear">
            <a-input v-model:value="form.schoolYear" placeholder="请输入学年"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="教学角色: 1-主讲教师, 2-辅导教师, 3-助教" name="teachingRole">
            <a-select v-model:value="form.teachingRole" placeholder="请选择教学角色: 1-主讲教师, 2-辅导教师, 3-助教" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('teaching_role')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="是否班主任: 0-否, 1-是" name="isHeadTeacher">
            <a-select v-model:value="form.isHeadTeacher" placeholder="请选择是否班主任: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="授课时间(如: 周一第3节)" name="teachingTime">
            <a-input v-model:value="form.teachingTime" placeholder="请输入授课时间(如: 周一第3节)"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="授课教室" name="classroom">
            <a-input v-model:value="form.classroom" placeholder="请输入授课教室"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="状态: 0-失效, 1-有效" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-失效, 1-有效" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { teacherCourseClassApi } from '/@/api/smartmind/teacherCourseClass-api.js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const emit = defineEmits(['reloadList']);
  const dictStore = useDictStore();

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const formDefault = {
    id: undefined,
    teacherId: undefined,
    teacherName: undefined,
    courseId: undefined,
    courseName: undefined,
    classId: undefined,
    className: undefined,
    semester: undefined,
    schoolYear: undefined,
    teachingRole: undefined,
    isHeadTeacher: undefined,
    teachingTime: undefined,
    classroom: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    teacherId: [{ required: true, message: '请输入教师ID' }],
    courseId: [{ required: true, message: '请输入课程ID' }],
    classId: [{ required: true, message: '请输入班级ID' }],
    status: [{ required: true, message: '请输入状态: 0-失效, 1-有效' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdTeacherOptions = ref([]);
  const smdTeacherLoading = ref(false);
  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdClazzOptions = ref([]);
  const smdClazzLoading = ref(false);

  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      nextTick(() => {
        Object.assign(form, rowData);
      });
    }
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
    reset();
  }

  function reset() {
    Object.assign(form, formDefault);
    if (formRef.value) {
      formRef.value.resetFields();
    }
  }

  async function onSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      SmartLoading.show();
      
      if (form.id) {
        await teacherCourseClassApi.update(form);
        message.success('修改成功');
      } else {
        await teacherCourseClassApi.add(form);
        message.success('添加成功');
      }
      
      visible.value = false;
      emit('reloadList');
      reset();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      confirmLoading.value = false;
      SmartLoading.hide();
    }
  }

  async function remoteSelectSmdTeacher(keywords) {
    smdTeacherLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdTeacherApi.querySelectVo({ keywords });
      smdTeacherOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdTeacherLoading.value = false;
    }
  }
  async function remoteSelectSmdCourse(keywords) {
    smdCourseLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdCourseApi.querySelectVo({ keywords });
      smdCourseOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdCourseLoading.value = false;
    }
  }
  async function remoteSelectSmdClazz(keywords) {
    smdClazzLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdClazzApi.querySelectVo({ keywords });
      smdClazzOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdClazzLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
