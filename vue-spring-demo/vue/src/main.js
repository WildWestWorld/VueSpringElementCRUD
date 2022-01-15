import { createApp } from 'vue'

import 'element-plus/dist/index.css'
import App from './App.vue';
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import {Location, Document, Menu as IconMenu, Setting,} from '@element-plus/icons';
import '@/assets/css/global.css'

import * as echarts from 'echarts'
import * as ElIcons  from '@element-plus/icons'


const  app =createApp(App).use(store).use(router).use(ElementPlus).use(echarts)    .mount('#app')




app.echarts = echarts

