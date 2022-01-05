<template>
<div>
  <router-view></router-view>
</div>
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
import Home from "@/views/User";

export default {
  name:"App",
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