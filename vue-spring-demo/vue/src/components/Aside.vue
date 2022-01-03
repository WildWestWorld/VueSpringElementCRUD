<template>
  <div>
    <el-menu

        default-active="2"
        class="el-menu-vertical-demo"

    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon>
            <location/>
          </el-icon>
          <span>系统管理</span>
        </template>


        <el-sub-menu index="1-4">
          <template #title>用户管理</template>

          <el-menu-item index="1-4-1">item one</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-menu-item index="2">
        <el-icon>
          <icon-menu/>
        </el-icon>
        <span>Navigator Two</span>
      </el-menu-item>
      <el-menu-item index="3" >
        <el-icon>
          <document/>
        </el-icon>
        <span>Navigator Three</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon>
          <setting/>
        </el-icon>
        <span>Navigator Four</span>
      </el-menu-item>
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