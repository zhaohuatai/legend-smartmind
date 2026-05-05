<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="学生ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.studentId" placeholder="请输入学生ID" />
        </a-form-item>
        <a-form-item label="学生姓名" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.studentName" placeholder="请输入学生姓名" />
        </a-form-item>
        <a-form-item label="课程ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.courseId" placeholder="请输入课程ID" />
        </a-form-item>
        <a-form-item label="起始单元ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.fromUnitId" placeholder="请输入起始单元ID" />
        </a-form-item>
        <a-form-item label="目标单元ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.toUnitId" placeholder="请输入目标单元ID" />
        </a-form-item>
        <a-form-item label="分析时间" class="smart-query-form-item">
          <a-date-picker v-model:value="queryForm.analysisTime" placeholder="请选择分析时间" style="width: 200px" />
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
            新建能力跃迁
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

    <AbilityTransitionFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { abilityTransitionApi } from '/@/api/smartmind/abilityTransition-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import AbilityTransitionFormModal from './abilityTransition-form-modal.vue';

  const dictStore = useDictStore();

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '学生ID',
      dataIndex: 'studentId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '课程ID',
      dataIndex: 'courseId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '起始单元ID',
      dataIndex: 'fromUnitId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '目标单元ID',
      dataIndex: 'toUnitId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '分析时间',
      dataIndex: 'analysisTime',
      width: 192,
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
    studentId: undefined,
    studentName: undefined,
    courseId: undefined,
    fromUnitId: undefined,
    toUnitId: undefined,
    analysisTime: undefined,
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
      let res = await abilityTransitionApi.queryPage(queryForm);
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
      await abilityTransitionApi.delete(record.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
