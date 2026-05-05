<!--
  * 菜单 表单弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-12 20:11:39
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal
    :open="visible"
    :title="form.id ? '编辑' : '添加'"
    :width="600"
    destroyOnClose
    @cancel="onClose"
    @ok="onSubmit(false)"
  >
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit(false)">提交</a-button>
      <!-- <a-button v-if="!form.id" type="primary" @click="onSubmit(true)">提交并添加下一个</a-button> -->
    </template>

    <a-form ref="formRef" :labelCol="{ span: 5 }" :labelWrap="true" :model="form" :rules="rules">
      <a-form-item label="菜单类型" name="menuType">
        <a-radio-group v-model:value="form.menuType" button-style="solid" :disabled="!!form.id">
          <template v-for="item in MENU_TYPE_ENUM" :key="item.value">
            <a-radio-button v-if="item.value !== MENU_TYPE_ENUM.POINTS.value" :value="item.value">
              {{ item.desc }}
            </a-radio-button>
          </template>
        </a-radio-group>
      </a-form-item>
      <a-form-item :label="form.menuType === MENU_TYPE_ENUM.CATALOG.value ? '上级目录' : '上级菜单'">
        <MenuTreeSelect ref="parentMenuTreeSelect" v-model:value="form.parentId" />
      </a-form-item>
      
      <a-form-item label="菜单名称" name="name">
        <a-input v-model:value="form.name" placeholder="请输入菜单名称" />
      </a-form-item>
      <a-form-item label="菜单图标" name="iconCls">
        <IconSelect @updateIcon="selectIcon">
          <template #iconSelect>
            <a-input v-model:value="form.iconCls" placeholder="请输入菜单图标" style="width: 200px" />
            <component :is="$antIcons[form.iconCls]" class="smart-margin-left15" style="font-size: 20px" />
          </template>
        </IconSelect>
      </a-form-item>

      <!-- 菜单类型特有字段 -->
      <template v-if="form.menuType === MENU_TYPE_ENUM.MENU.value">
        <a-form-item label="路由地址" name="path">
          <a-input v-model:value="form.path" placeholder="请输入路由地址" />
        </a-form-item>
        
        <a-form-item v-if="form.frameFlag" label="外链地址" name="frameUrl">
          <a-input v-model:value="form.frameUrl" placeholder="请输入外链地址" />
        </a-form-item>
        <a-form-item v-else label="组件地址" name="component">
          <div style="display: flex; align-items: center">
            <a-input v-model:value="form.component" placeholder="请选择组件地址" style="flex: 1" />
            <a-button type="primary" style="margin-left: 10px" @click="showPermissionModal">选择权限</a-button>
          </div>
        </a-form-item>
        
        <!-- <a-form-item label="是否缓存" name="cacheFlag">
          <a-switch v-model:checked="form.cacheFlag" checked-children="开启缓存" un-checked-children="不缓存" />
        </a-form-item> -->
        <a-form-item label="是否外链" name="frameFlag">
          <a-switch v-model:checked="form.frameFlag" checked-children="是外链" un-checked-children="不是外链" />
        </a-form-item>
        <!-- <a-form-item label="显示状态" name="hide">
          <a-switch v-model:checked="form.hide" :checkedValue="0" :unCheckedValue="1" checked-children="显示" un-checked-children="不显示" />
        </a-form-item> -->
      </template>

      <a-form-item label="禁用状态" name="status">
        <a-switch
          v-model:checked="form.status"
          checkedValue="1"
          unCheckedValue="0"
          checked-children="启用"
          un-checked-children="禁用"
        />
      </a-form-item>

      <a-form-item label="排序" name="showIndex" help="值越小越靠前">
        <a-input-number v-model:value="form.showIndex" :min="0" placeholder="请输入排序" style="width: 100px" />
      </a-form-item>
    </a-form>
    <PermissionSelectModal ref="permissionSelectModal" @select="onPermissionSelect" />
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { nextTick, reactive, ref, computed } from 'vue';
  import MenuTreeSelect from './menu-tree-select.vue';
  import PermissionSelectModal from './permission-select-modal.vue';
  import { menuApi } from '/@/api/system/menu-api';
  import IconSelect from '/@/components/framework/icon-select/index.vue';
  import { MENU_DEFAULT_PARENT_ID, MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  // ----------------------- 展开、隐藏编辑窗口 ------------------------

  // 是否展示
  const visible = ref(false);

  const contextMenuTreeSelect = ref();
  const parentMenuTreeSelect = ref();

  //展开编辑窗口
  async function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      
      if (form.parentId === MENU_DEFAULT_PARENT_ID) {
        form.parentId = null;
      }
    }
    visible.value = true;
    refreshParentAndContext();
  }

  function refreshParentAndContext() {
    nextTick(() => {
      if (contextMenuTreeSelect.value) {
        contextMenuTreeSelect.value.queryMenuTree();
      }
      if (parentMenuTreeSelect.value) {
        parentMenuTreeSelect.value.queryMenuTree();
      }
    });
  }

  // 隐藏窗口
  function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
  }

  // ----------------------- form表单相关操作 ------------------------

  const formRef = ref();
  const formDefault = {
    id: undefined,
    name: undefined,
    menuType: MENU_TYPE_ENUM.CATALOG.value,
    iconCls: undefined,
    parentId: undefined,
    path: undefined, // 统一使用 path
    showIndex: undefined,
    hide: 0,
    cacheFlag: false,
    component: undefined,
    status: '1',
    frameFlag: false,
    frameUrl: undefined,
    permId: undefined,
  };
  let form = reactive({ ...formDefault });

  function continueResetForm() {
    refreshParentAndContext();
    const menuType = form.menuType;
    const parentId = form.parentId;
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    form.menuType = menuType;
    form.parentId = parentId;
  }

  const rules = {
    menuType: [{ required: true, message: '菜单类型不能为空' }],
    name: [
      { required: true, message: '菜单名称不能为空' },
      { max: 20, message: '菜单名称不能大于20个字符', trigger: 'blur' },
    ],
    frameUrl: [
      { required: true, message: '外链地址不能为空' },
      { max: 500, message: '外链地址不能大于500个字符', trigger: 'blur' },
    ],
    path: [
      { required: true, message: '路由地址不能为空' },
      { max: 100, message: '路由地址不能大于100个字符', trigger: 'blur' },
    ],
  };

  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  const onSubmit = async (continueFlag) => {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    SmartLoading.show();
    try {
      let params = _.cloneDeep(form);
      // 若无父级ID 默认设置为0
      if (!params.parentId) {
        params.parentId = 0;
      }
      if (params.id) {
        await menuApi.updateMenu(params);
      } else {
        await menuApi.addMenu(params);
      }
      message.success(`${params.id ? '修改' : '添加'}成功`);
      if (continueFlag) {
        continueResetForm();
      } else {
        onClose();
      }
      emit('reloadList');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  };

  function selectIcon(icon) {
    form.iconCls = icon;
  }

  // ----------------------- 权限选择 ------------------------
  const permissionSelectModal = ref();
  function showPermissionModal() {
    permissionSelectModal.value.show();
  }

  function onPermissionSelect(perm) {
    console.log('Selected Permission:', perm);
    if (perm) {
      form.component = perm.component || '';
      form.permId = perm.id;
    }
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
</script>
<style lang="less" scoped>
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: left;
    z-index: 1;
  }
</style>
