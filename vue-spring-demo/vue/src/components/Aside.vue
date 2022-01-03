<template>
  <div>
    <el-menu
        default-active="user"
        class="el-menu-vertical-demo"

    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon>
            <location/>
          </el-icon>
          <span>系统管理</span>
        </template>

        <el-menu-item index="user" :route="{path:'/'}">用户管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="data" :route="{path:'/'}">数据管理</el-menu-item>


    </el-menu>
  </div>
</template>

<script>
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons'

export default {
  name: "Aside",
  components: {Location, Document, IconMenu, Setting}
}
</script>

<style scoped>
.el-menu-vertical-demo {
  width: 200px;
  height: calc(100vh - 50px);
}
</style>