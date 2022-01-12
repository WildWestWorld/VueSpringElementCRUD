<template>
  <div>
    <el-menu
        :default-active="routePath"
        class="el-menu-vertical-demo"
        router
    >
      <el-sub-menu index="1" v-if="user.role==='1'">
        <template #title>
          <el-icon>
            <location/>
          </el-icon>
          <span>系统管理</span>
        </template>

        <el-menu-item index="/user" >用户管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/book" >书籍管理</el-menu-item>
      <el-menu-item index="/map" >百度地图</el-menu-item>
      <el-menu-item index="/order" >订单管理</el-menu-item>

      <el-menu-item index="/category" >分类管理</el-menu-item>


      <el-menu-item index="/news" >新闻管理</el-menu-item>

      <el-menu-item index="/message" >留言板</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {Location, Document, Menu as IconMenu, Setting,} from '@element-plus/icons';
// import request from "@/utils/request";




export default {
  name: "Aside",
  components: {Location, Document, IconMenu, Setting},
  data(){
    return{
      routePath:this.$route.path,

    }
  },
  props:["user"],
  created() {
    let str =sessionStorage.getItem("user")||"{}"
    this.form=JSON.parse(str)

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