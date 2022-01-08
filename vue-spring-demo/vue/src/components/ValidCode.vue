<template>
  <div
      class="ValidCode "
      :style="`width:${width}; height:${height}`"
      @click="refreshCode"
  >

    <span
        v-for="(item, index) in codeList"
        :key="index"
        :style="getStyle(item)"
    >{{ item.code }}</span>

  </div>
</template>

<script>
export default {
  name: 'ValidCode',
  model: {
    prop:['value','width','height'] ,
    event: 'input'
  },
  props: {
    width: {
      type: String,
      default: '40%'
    },
    height: {
      type: String,
      default: '40px'
    },


    ValidCodeLength: {
      type: Number,
      default: 4
    },


  },
  data () {
    return {
      codeList: []
    }
  },
  watch: {
    refreshCode () {
      this.createdCode()
    }
  },
  mounted () {
    this.createdCode()
  },
  methods: {
    refreshCode () {
      this.createdCode()
    },


    createdCode () {

      const codeList = []
      const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789'
      const charsLen = chars.length
      // 生成
      for (let i = 0; i < this.ValidCodeLength; i++) {
        //产生rgb的值 rbg值的示例 [100,90,50]
        const rgb = [Math.round(Math.random() * 220), Math.round(Math.random() * 240), Math.round(Math.random() * 200)]

        codeList.push({
          //String.charat(Num)=选中String中num位的字符,
          // Math.floor(Math.random() * charsLen) =生成0-charslen范围的随机数

          //在chars字符集中随机一个字符
          code: chars.charAt(Math.floor(Math.random() * charsLen)),
          //字体的颜色随机
          color: `rgb(${rgb})`,
          //字体的大小随机
          fontSize: `${13 + (+[Math.floor(Math.random() * 10)] + 6)}px`,
          //字的padding随机
          padding: `${[Math.floor(Math.random() * 10)]}px`,
          //旋转角度随机
          transform: `rotate(${Math.floor(Math.random() * 90) - Math.floor(Math.random() * 90)}deg)`
        })
      }
      // 赋值
      this.codeList = codeList

      //   console.log(codeList)
      //
      //   console.log(
      //     codeList.map((item,index)=>{
      //   return item.code
      // })
      // )
      // console.log(codeList.map(item => item.code))

      // 将当前数据派发出去
      this.$emit('input', codeList.map(item => item.code).join(''))
    },



    getStyle (data) {
      return `color: ${data.color}; font-size: ${data.fontSize}; padding: ${data.padding}; transform: ${data.transform}`
    }

  }
}
</script>

<style scoped>
.ValidCode{
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background-color: #fff;
  margin-left: 20px;
  border-radius: 5px;
}
.ValidCode>span{
  display: inline-block;
}
</style>
