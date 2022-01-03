<template>
  <el-config-provider :locale="locale">

    <div >
      <Header/>
      <div class="asideContainer">
        <Aside/>
        <router-view style="flex:1"></router-view>
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
import {ElConfigProvider} from 'element-plus'
import zhCn from 'element-plus/lib/locale/lang/zh-cn'

import Header from "@/components/Header.vue";
import Aside from "@/components/Aside";
import Home from "@/views/Home";

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
    }
  }
}
</script>