<template>
  <div class="role-container">
    <a-row :gutter="10" style="height: 100%">
      <!-- Left: Role List (30%) -->
      <a-col :span="8" style="height: 100%">
        <RoleList @select-role="onSelectRole" />
      </a-col>

      <!-- Right: Tabs (70%) -->
      <a-col :span="16" style="height: 100%">
        <a-card style="height: 100%; display: flex; flex-direction: column;" :bodyStyle="{ flex: 1, overflow: 'auto' }">
            <a-tabs v-model:activeKey="activeKey">
                <a-tab-pane key="1" tab="权限列表">
                    <RolePermission :roleId="selectedRole?.id" />
                </a-tab-pane>
                <a-tab-pane key="2" tab="用户列表">
                    <RoleUser :roleId="selectedRole?.id" />
                </a-tab-pane>
            </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import RoleList from './components/role-list/index.vue';
import RolePermission from './components/role-permission/index.vue';
import RoleUser from './components/role-user/index.vue';

const selectedRole = ref(null);
const activeKey = ref('1');

function onSelectRole(role) {
    selectedRole.value = role;
}
</script>

<style scoped>
.role-container {
    height: 100%;
    padding: 10px;
    background-color: #f0f2f5;
}
</style>
