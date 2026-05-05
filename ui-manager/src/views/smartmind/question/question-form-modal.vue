<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑题目' : '新建题目'"
    ok-text="确认"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
    width="1360px"
    :confirmLoading="confirmLoading"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row>
        <a-col :span="6">
          <a-form-item label="题目标识码" name="questionCode">
            <a-input v-model:value="form.questionCode" placeholder="请输入题目标识码"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题" name="questionType">
            <a-select v-model:value="form.questionType" placeholder="请选择题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('question_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="所属课程ID" name="courseId">
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
        <a-col :span="6">
          <a-form-item label="课程名称" name="courseName">
            <a-input v-model:value="form.courseName" placeholder="请输入课程名称"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="所属单元ID" name="unitId">
            <a-select 
              v-model:value="form.unitId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdLearningUnit" 
              :loading="smdLearningUnitLoading"
              @focus="remoteSelectSmdLearningUnit('')"
              allowClear
            >
              <a-select-option v-for="item in smdLearningUnitOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="单元名称" name="unitName">
            <a-input v-model:value="form.unitName" placeholder="请输入单元名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难" name="difficultyLevel">
            <a-select v-model:value="form.difficultyLevel" placeholder="请选择难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('difficulty_level')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="认知层次: 1-识记, 2-理解, 3-应用, 4-分析, 5-综合, 6-评价" name="cognitiveLevel">
            <a-select v-model:value="form.cognitiveLevel" placeholder="请选择认知层次: 1-识记, 2-理解, 3-应用, 4-分析, 5-综合, 6-评价" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('cognitive_level')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="来源类型: 1-系统题库, 2-教师自建, 3-AI生成, 4-导入" name="sourceType">
            <a-select v-model:value="form.sourceType" placeholder="请选择来源类型: 1-系统题库, 2-教师自建, 3-AI生成, 4-导入" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('source_type')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态: 0-失效, 1-有效" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态: 0-失效, 1-有效" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('common_status')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="创建人" name="createBy">
            <a-input v-model:value="form.createBy" placeholder="请输入创建人"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="更新人" name="updateBy">
            <a-input v-model:value="form.updateBy" placeholder="请输入更新人"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="备注" name="remark">
            <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="题目内容" name="questionContent">
            <a-textarea v-model:value="form.questionContent" :rows="3" placeholder="请输入题目内容" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="知识点标签列表" name="knowledgePoints">
            <a-textarea v-model:value="form.knowledgePoints" :rows="3" placeholder="请输入知识点标签列表" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="参考答案" name="answer">
            <a-textarea v-model:value="form.answer" :rows="3" placeholder="请输入参考答案" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="答案解析" name="answerAnalysis">
            <a-textarea v-model:value="form.answerAnalysis" :rows="3" placeholder="请输入答案解析" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI分析标签(易错点/考察重点等)" name="aiAnalysisTags">
            <a-textarea v-model:value="form.aiAnalysisTags" :rows="3" placeholder="请输入AI分析标签(易错点/考察重点等)" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="AI推荐相似题" name="aiSimilarQuestions">
            <a-textarea v-model:value="form.aiSimilarQuestions" :rows="3" placeholder="请输入AI推荐相似题" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="选项列表JSON结构" name="options">
            <a-textarea v-model:value="form.options" :rows="3" placeholder="请输入选项列表JSON结构" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref, nextTick } from 'vue';
  import { questionApi } from '/@/api/smartmind/question-api.js';
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
    questionCode: undefined,
    questionType: undefined,
    courseId: undefined,
    courseName: undefined,
    unitId: undefined,
    unitName: undefined,
    difficultyLevel: undefined,
    cognitiveLevel: undefined,
    sourceType: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
    questionContent: undefined,
    knowledgePoints: undefined,
    answer: undefined,
    answerAnalysis: undefined,
    aiAnalysisTags: undefined,
    aiSimilarQuestions: undefined,
    options: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    questionCode: [{ required: true, message: '请输入题目标识码' }],
    questionType: [{ required: true, message: '请输入题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题, 6-计算题, 7-应用题, 8-综合题' }],
    courseId: [{ required: true, message: '请输入所属课程ID' }],
    difficultyLevel: [{ required: true, message: '请输入难度等级: 1-容易, 2-较易, 3-中等, 4-较难, 5-困难' }],
    status: [{ required: true, message: '请输入状态: 0-失效, 1-有效' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
    questionContent: [{ required: true, message: '请输入题目内容' }],
  };

  const smdCourseOptions = ref([]);
  const smdCourseLoading = ref(false);
  const smdLearningUnitOptions = ref([]);
  const smdLearningUnitLoading = ref(false);

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
        await questionApi.update(form);
        message.success('修改成功');
      } else {
        await questionApi.add(form);
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
  async function remoteSelectSmdLearningUnit(keywords) {
    smdLearningUnitLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdLearningUnitApi.querySelectVo({ keywords });
      smdLearningUnitOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdLearningUnitLoading.value = false;
    }
  }

  defineExpose({
    showModal,
  });
</script>
