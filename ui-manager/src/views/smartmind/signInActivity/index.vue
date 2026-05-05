<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="签到编码" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.signCode" placeholder="请输入签到编码" />
        </a-form-item>
        <a-form-item label="签到名称" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.signName" placeholder="请输入签到名称" />
        </a-form-item>
        <a-form-item label="课程ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.courseId" placeholder="请输入课程ID" />
        </a-form-item>
        <a-form-item label="班级ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.classId" placeholder="请输入班级ID" />
        </a-form-item>
        <a-form-item label="发布教师ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.teacherId" placeholder="请输入发布教师ID" />
        </a-form-item>
        <a-form-item label="教师姓名" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.teacherName" placeholder="请输入教师姓名" />
        </a-form-item>
        <a-form-item label="开始时间" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.startTime" placeholder="请选择开始时间" style="width: 200px" />
        </a-form-item>
        <a-form-item label="结束时间" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.endTime" placeholder="请选择结束时间" style="width: 200px" />
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery">
              <template #icon>
                <ReloadOutlined />
              </template>
              重置
            </a-button>
          </a-button-group>
          <a-button @click="toAdd" type="primary" class="smart-margin-left20">
            <template #icon>
              <PlusOutlined />
            </template>
            新建签到活动
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="id" :pagination="false">
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.dataIndex === 'seq'">
            {{ index + 1 }}
          </template>
          <template v-else-if="column.dataIndex === 'signType'">
            {{ dictStore.getDataLabels('sign_type', record.signType) }}
          </template>
          <template v-else-if="column.dataIndex === 'signStatus'">
            {{ dictStore.getDataLabels('sign_status', record.signStatus) }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            {{ dictStore.getDataLabels('common_status', record.status) }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button @click="toEdit(record)" type="link">编辑</a-button>
              <a-popconfirm title="确定要删除吗?" @confirm="toDelete(record)">
                <a-button type="link" danger>删除</a-button>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <SignInActivityFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { signInActivityApi } from '/@/api/smartmind/signInActivity-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import SignInActivityFormModal from './signInActivity-form-modal.vue';

  const dictStore = useDictStore();

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '签到编码',
      dataIndex: 'signCode',
      ellipsis: true,
    },
    {
      title: '签到名称',
      dataIndex: 'signName',
      ellipsis: true,
    },
    {
      title: '签到类型: 1-普通签到, 2-位置签到, 3-手势签到, 4-扫码签到',
      dataIndex: 'signType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '课程ID',
      dataIndex: 'courseId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '班级ID',
      dataIndex: 'classId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '发布教师ID',
      dataIndex: 'teacherId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '开始时间',
      dataIndex: 'startTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '签到状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消',
      dataIndex: 'signStatus',
      width: 120,
      ellipsis: true,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 56,
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 192,
      ellipsis: true,
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      ellipsis: true,
    },
    {
      title: '更新人',
      dataIndex: 'updateBy',
      ellipsis: true,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 150,
    },
  ]);

  const queryFormState = {
    signCode: undefined,
    signName: undefined,
    courseId: undefined,
    classId: undefined,
    teacherId: undefined,
    teacherName: undefined,
    startTime: undefined,
    endTime: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const formModalRef = ref();

  onMounted(() => {
    queryData();
  });

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  function resetQuery() {
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let res = await signInActivityApi.queryPage(queryForm);
      tableData.value = res.data?.rows || res.data?.list || [];
      total.value = res.data?.total || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function toAdd() {
    formModalRef.value.showModal();
  }

  function toEdit(record) {
    formModalRef.value.showModal(record);
  }

  async function toDelete(record) {
    try {
      await signInActivityApi.delete(record.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
