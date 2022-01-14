<template>
  <div style="padding: 10px">
    <el-card>
      <div id="myChart" :style="{width: '100%', height: '600px'}"></div>
    </el-card>

  </div>
</template>

<script>
import request from "@/utils/request";

import * as echarts from 'echarts'

export default {
  name: "Echart",
  data() {
    return {}
  },
  mounted() {
    this.drawLine();
  },
  methods: {
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = echarts.init(document.getElementById('myChart'))

      let option = {
        //标题
        title: {
          text: '各地区用户比例统计图',//大标题
          subtext: '虚拟数据',//小标题
          left: 'left', //标题放置位置

        },
        // tooltip:提示框，鼠标悬浮交互时的信息提示
        tooltip: {
          trigger: 'item',//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
          //显示的数据格式
          //{a}是分别表示系列名 来源自下面的name，
          //{b}:是数据名
          //{c}:数据值
          //{d}%:数据百分比
          formatter: "{a} <br/>{b} : {c} ({d}%)"

        },
        //图例，每个图表最多仅有一个图例
        legend: {
          left: 'left',  //left:"center",组件离容器左侧的距离,'left','center','right','20%'
          top: 'bottom',// top: 'bottom'，组件离容器上侧的距离,'top','middle','bottom','20%'

        },
        //toolbox:工具栏 就是右上角的东西
        toolbox: {
          show: true, //是否显示工具栏组件
          itemSize:15,

          feature: {
            mark: {   //   mark: {show: true}, '辅助线开关'
              show: true
            },

            dataView: {           // 右上角的第一个按钮  dataView:数据视图工具，可以展现当前图表所用的数据，编辑后可以动态更新
              show: true,          //是否显示该工具。
              readOnly: false    //是否不可编辑（只读）
            },
            restore: {          // 右上角的第二个按钮 配置项还原。
              show: true,

            },
            saveAsImage: {        //右上角的第三个按钮 保存为图片。
              show: true,
              title:"另存为",
            },


          }
        },
        series: [
          {
            name: '用户比例',      //系列名称，用于tooltip的显示； 用于a的使用
            type: 'pie',          //饼图类型
            radius: [50, 250],    //  radius: [50, 250], 50：内圆半径，250：外圈半径
            center: ['50%', '50%'],  // center: ['50%', '50%'], 第一个50：内圆的中心点位置，   第二个50：外圆的中心点位置，

            roseType: 'area',       // roseType: 'area',
                                    // # 是否展示成南丁格尔图，通过半径区分数据大小，有'radius'和'area'两种模式。
                                      //# radius：扇区圆心角展现数据的百分比，半径展现数据的大小
                                      //  # area：所有扇区圆心角相同，仅通过半径展现数据大小
            itemStyle: {           //图表面积
              borderRadius: 8    //图表面积的圆角大小
            },
            data: []    //数据，我们自己从后台取，要是没有我们就要自己写了
          }
        ]
      }

      request.get("/api/user/count").then(res => {
        if (res.code === '0') {
          res.data.forEach(item => {
            option.series[0].data.push({name: item.address, value: item.count})
          })
          console.log(res)
          // 绘制图表
          myChart.setOption(option);
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
