<template>
  <div class="headerContainer">
    <div>后台管理</div>
    <div></div>
    <div>
      <el-dropdown>
    <span class="el-dropdown-link">
      {{user.nikeName}}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="exit">退出系统</el-dropdown-item>

          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script >
import {ArrowDown} from "@element-plus/icons";
// import request from "@/utils/request";


export default {
  name: "Header",
  components:{
    ArrowDown
  },
  props:['user'],
  data(){
    return{
      form:{}
    }
  },
  created() {
    let str =sessionStorage.getItem("user")||"{}"
    this.form=JSON.parse(str)
  },
  methods:{
      exit(){
        sessionStorage.clear();
        this.$router.push('/login')

      }
  },


}
</script>

<style scoped>
.headerContainer {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #ccc;
  display: flex;
}

.headerContainer > div:nth-of-type(1) {
  width: 200px;
  padding-left: 30px;
  font-weight: bold;
  color: dodgerblue;
}

.headerContainer > div:nth-of-type(2) {
  flex: 0.94;
  line-height: 50px;
}

.headerContainer > div:nth-of-type(3) {
  flex: 0.06;
  width: 100px;

}
</style>
