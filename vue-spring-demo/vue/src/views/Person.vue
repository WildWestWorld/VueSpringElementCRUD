<template>
  <div>
    <div class="cardContainer">

  <el-card class="box-card">
    <el-form  :model="form" label-width="120px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" style="width:80%" disabled></el-input>
      </el-form-item>

      <el-form-item label="昵称">
        <el-input v-model="form.nikeName" style="width:80%"></el-input>
      </el-form-item>

      <el-form-item label="年龄">
        <el-input v-model="form.age" style="width:80%"></el-input>
      </el-form-item>

      <el-form-item label="性别">
        <el-input v-model="form.sex" style="width:80%"></el-input>
      </el-form-item>



      <el-form-item label="地址">
        <el-input v-model="form.address" style="width:80%"></el-input>
      </el-form-item>

      <el-form-item label="密码">
        <el-input v-model="form.password" style="width:80%"></el-input>
      </el-form-item>

    </el-form>

    <div>
      <el-button type="primary" @click="update">保存</el-button>
    </div>

  </el-card>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Person",
  components:{
    ElMessage

  },
  data(){
    return{
      form:{},
    }
  },
  created(){
    let str =sessionStorage.getItem("user")||"{}"
    this.form =JSON.parse(str)
  },
  methods:{
    update(){
        request.put("/api/user",this.form).then(res=>{
          console.log(res)
          if (res.code==='0'){
            ElMessage({
              type:'success',
              message:'保存成功',
            })

            sessionStorage.setItem("user",JSON.stringify(this.form))
            this.$emit("userInfo");
            this.$router.push('/')
          }else{
            ElMessage({
              type:'error',
              message:res.msg,
            })
          }
        })
    },
  },
}
</script>

<style scoped>
.cardContainer{
  width: 35%;
}

</style>