<template>
  <div>
    <el-menu
        :default-active="routePath"
        class="el-menu-vertical-demo"
        router
    >
      <el-sub-menu index="1" >
        <template #title>

          <el-icon class="document">
            <Document/>
          </el-icon>

          <span>系统管理</span>
        </template>

        <el-menu-item index="/user" >

          <el-icon class="location">
            <location/>
          </el-icon>

          用户管理
        </el-menu-item>

      </el-sub-menu>






      <el-menu-item :index="m.path"  v-for="m in permission">

<!--        <Location style="width: 1em; height: 1em; margin-right: 8px;" />-->

        <el-icon class="IconMenu">
          <IconMenu/>
        </el-icon>

        {{ m.comment }}

      </el-menu-item>


    </el-menu>
  </div>
</template>

<script>
import {Document,Location, Menu as IconMenu, Setting,} from '@element-plus/icons';

// import request from "@/utils/request";




export default {
  name: "Aside",
  components: { Location,Document, IconMenu, Setting},
  data(){
    return{
      routePath:this.$route.path,
      permission:{},

    }
  },
  props:["user"],
  created() {
    let str =sessionStorage.getItem("user")||"{}"
    let permissions =sessionStorage.getItem("permissions")||"{}"
    this.permission=JSON.parse(permissions)

    // this.html= "<el-icon><arrow-left-bold /></el-icon>"
    // request.get("/api/user/"+this.form.id).then(res=>{
    //   if (res.code ==='0'){
    //     this.form =res.data;
    //   }
    // })


  },


}
</script>

<style scoped>
.el-menu-vertical-demo {
  width: 200px;
  height: calc(100vh - 50px);
}
</style>