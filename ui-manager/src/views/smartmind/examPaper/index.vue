<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="试卷编码" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.paperCode" placeholder="请输入试卷编码" />
        </a-form-item>
        <a-form-item label="试卷名称" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.paperName" placeholder="请输入试卷名称" />
        </a-form-item>
        <a-form-item label="所属课程ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.courseId" placeholder="请输入所属课程ID" />
        </a-form-item>
        <a-form-item label="所属单元ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.unitId" placeholder="请输入所属单元ID" />
        </a-form-item>
        <a-form-item label="关联教案ID" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.planId" placeholder="请输入关联教案ID" />
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
            新建试卷
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
          <template v-else-if="column.dataIndex === 'paperType'">
            {{ dictStore.getDataLabels('paper_type', record.paperType) }}
          </template>
          <template v-else-if="column.dataIndex === 'aiGenerateMode'">
            {{ dictStore.getDataLabels('ai_generate_mode', record.aiGenerateMode) }}
          </template>
          <template v-else-if="column.dataIndex === 'sourceType'">
            {{ dictStore.getDataLabels('source_type', record.sourceType) }}
          </template>
          <template v-else-if="column.dataIndex === 'status'">
            {{ dictStore.getDataLabels('paper_status', record.status) }}
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

    <ExamPaperFormModal ref="formModalRef" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { examPaperApi } from '/@/api/smartmind/examPaper-api.js';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useDictStore } from '/@/store/modules/system/dict.js';
  import ExamPaperFormModal from './examPaper-form-modal.vue';

  const dictStore = useDictStore();

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: '试卷编码',
      dataIndex: 'paperCode',
      ellipsis: true,
    },
    {
      title: '试卷名称',
      dataIndex: 'paperName',
      ellipsis: true,
    },
    {
      title: '试卷类型: 1-课前预习, 2-课堂测验, 3-课后作业, 4-单元测试, 5-期中期末, 9-其他',
      dataIndex: 'paperType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '所属课程ID',
      dataIndex: 'courseId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '所属单元ID',
      dataIndex: 'unitId',
      width: 192,
      ellipsis: true,
    },
    {
      title: '关联教案ID',
      dataIndex: 'planId',
      width: 192,
      ellipsis: true,
    },
    {
      title: 'AI生成模式',
      dataIndex: 'aiGenerateMode',
      width: 120,
      ellipsis: true,
    },
    {
      title: '来源类型',
      dataIndex: 'sourceType',
      width: 120,
      ellipsis: true,
    },
    {
      title: '状态: 0-草稿, 1-已发布, 2-已归档',
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
    paperCode: undefined,
    paperName: undefined,
    courseId: undefined,
    unitId: undefined,
    planId: undefined,
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
      let res = await examPaperApi.queryPage(queryForm);
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
      await examPaperApi.delete(record.id);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
