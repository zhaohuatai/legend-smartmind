<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑签到记录' : '新建签到记录'"
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
          <a-form-item label="签到活动ID" name="signId">
            <a-select 
              v-model:value="form.signId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdSignInActivity" 
              :loading="smdSignInActivityLoading"
              @focus="remoteSelectSmdSignInActivity('')"
              allowClear
            >
              <a-select-option v-for="item in smdSignInActivityOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="签到名称" name="signName">
            <a-input v-model:value="form.signName" placeholder="请输入签到名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="学生ID" name="studentId">
            <a-select 
              v-model:value="form.studentId" 
              show-search 
              :filter-option="false" 
              placeholder="请输入关键词搜索" 
              @search="remoteSelectSmdStudent" 
              :loading="smdStudentLoading"
              @focus="remoteSelectSmdStudent('')"
              allowClear
            >
              <a-select-option v-for="item in smdStudentOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="学号" name="studentNo">
            <a-input v-model:value="form.studentNo" placeholder="请输入学号"  />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="学生姓名" name="studentName">
            <a-input v-model:value="form.studentName" placeholder="请输入学生姓名"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
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
        <a-col :span="6">
          <a-form-item label="班级名称" name="className">
            <a-input v-model:value="form.className" placeholder="请输入班级名称"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="签到时间" name="signTime">
            <a-date-picker v-model:value="form.signTime" show-time style="width: 100%" placeholder="请选择签到时间" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假" name="signResult">
            <a-select v-model:value="form.signResult" placeholder="请选择签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('sign_result')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="签到方式: 1-点击签到, 2-位置签到, 3-手势签到, 4-扫码签到" name="signMethod">
            <a-select v-model:value="form.signMethod" placeholder="请选择签到方式: 1-点击签到, 2-位置签到, 3-手势签到, 4-扫码签到" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('sign_method')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="签到位置" name="location">
            <a-input v-model:value="form.location" placeholder="请输入签到位置"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="纬度" name="latitude">
            <a-input-number v-model:value="form.latitude" :min="0" :precision="2" style="width: 100%" placeholder="请输入纬度" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="经度" name="longitude">
            <a-input-number v-model:value="form.longitude" :min="0" :precision="2" style="width: 100%" placeholder="请输入经度" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="IP地址" name="ipAddress">
            <a-input v-model:value="form.ipAddress" placeholder="请输入IP地址"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="设备类型" name="deviceType">
            <a-input v-model:value="form.deviceType" placeholder="请输入设备类型"  />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="是否早退: 0-否, 1-是" name="leaveEarly">
            <a-select v-model:value="form.leaveEarly" placeholder="请选择是否早退: 0-否, 1-是" allowClear>
              <a-select-option v-for="item in dictStore.getDictData('yes_no')" :key="item.dataValue" :value="item.dataValue">{{ item.dataLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="6">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status" placeholder="请选择状态" allowClear>
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
        <a-col :span="6">
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
  import { signInRecordApi } from '/@/api/smartmind/signInRecord-api.js';
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
    signId: undefined,
    signName: undefined,
    studentId: undefined,
    studentNo: undefined,
    studentName: undefined,
    classId: undefined,
    className: undefined,
    signTime: undefined,
    signResult: undefined,
    signMethod: undefined,
    location: undefined,
    latitude: undefined,
    longitude: undefined,
    ipAddress: undefined,
    deviceType: undefined,
    leaveEarly: undefined,
    status: undefined,
    createTime: undefined,
    updateTime: undefined,
    createBy: undefined,
    updateBy: undefined,
    remark: undefined,
  };
  let form = reactive({ ...formDefault });

  const rules = {
    signId: [{ required: true, message: '请输入签到活动ID' }],
    studentId: [{ required: true, message: '请输入学生ID' }],
    classId: [{ required: true, message: '请输入班级ID' }],
    signTime: [{ required: true, message: '请输入签到时间' }],
    signResult: [{ required: true, message: '请输入签到结果: 1-正常, 2-迟到, 3-缺勤, 4-请假' }],
    status: [{ required: true, message: '请输入状态' }],
    createTime: [{ required: true, message: '请输入创建时间' }],
    updateTime: [{ required: true, message: '请输入更新时间' }],
  };

  const smdSignInActivityOptions = ref([]);
  const smdSignInActivityLoading = ref(false);
  const smdStudentOptions = ref([]);
  const smdStudentLoading = ref(false);
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
        await signInRecordApi.update(form);
        message.success('修改成功');
      } else {
        await signInRecordApi.add(form);
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

  async function remoteSelectSmdSignInActivity(keywords) {
    smdSignInActivityLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdSignInActivityApi.querySelectVo({ keywords });
      smdSignInActivityOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdSignInActivityLoading.value = false;
    }
  }
  async function remoteSelectSmdStudent(keywords) {
    smdStudentLoading.value = true;
    try {
      // 调用 SelectVO 查询接口
      const res = await smdStudentApi.querySelectVo({ keywords });
      smdStudentOptions.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      smdStudentLoading.value = false;
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
