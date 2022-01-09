<template>
  <el-config-provider :locale="locale">

    <div >
      <Header :user="user"/>
      <div class="asideContainer">
        <Aside  :user="user"></Aside>
        <router-view style="flex:1" @userInfo="refreshUser" :user="user"></router-view>
      </div>
    </div>

  </el-config-provider>
</template>

<style >

.asideContainer{
  display: flex;
}

</style>

<script>
import {ElConfigProvider} from 'element-plus';
import zhCn from 'element-plus/lib/locale/lang/zh-cn';

import Header from "@/components/Header.vue";
import Aside from "@/components/Aside";
import Home from "@/views/User";
import request from "@/utils/request";

export default {
  name:"Layout",
  components:{
    [ElConfigProvider.name]:ElConfigProvider,
    Header,
    Aside,
    Home,
  },
  data(){
    return{
      locale:zhCn,
      user:{},

    }
  },created() {
    this.refreshUser()
  },
  methods:{
    refreshUser(){


      let userJson= sessionStorage.getItem("user");
      if(!userJson){
        return
      }else{
        let userId=JSON.parse(userJson).id;
        request.get("/api/user/"+userId).then(res=>{
          this.user=res.data

          sessionStorage.setItem('user', JSON.stringify(this.user));
        })
      }
    }
  },
}
</script>