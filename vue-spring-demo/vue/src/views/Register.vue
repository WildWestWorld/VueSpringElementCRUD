<template>
  <div class="bgc-container">
    <div>
      <div>
      <div class="loginText">欢迎注册</div>
      <el-form ref="formRef" :model="form" size="large" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="form.username"  >

            <template #prefix>
              <el-icon class="el-input__icon"><UserFilled /></el-icon>
            </template>

          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" show-password>

            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>

          </el-input>
        </el-form-item>

        <el-form-item prop="confirm">
          <el-input v-model="form.confirm" show-password>

            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>

          </el-input>
        </el-form-item>

        <el-form-item >
         <el-button style="width: 100%;" type="primary" @click="register('formRef')">注册</el-button>
        </el-form-item>
      </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { Search,UserFilled,Lock} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {reactive} from "vue";
export default {
  name: "Register",
  components:{
    Search,
    UserFilled,
    Lock,
  },
  data(){
    return {
      form:{},
      rules: reactive({
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
        ],
        confirm: [
          {
            required: true,
            message: '请输入确认密码',
            trigger: 'blur',
          },
        ],
      })
    }

  },
  methods:{
    register(formName){
      if(this.form.password != this.form.confirm){

          ElMessage({
            type: 'error',
            message: '两次密码输入不一致',
            duration:2000,
          })
        return
      }

      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
          request.post("/api/user/register",this.form).then(res =>{
            if(res.code ==='0'){
              ElMessage({
                type: 'success',
                message: '注册成功',
                duration:2000,
              })
              this.$router.push("/login");

            }else{
              ElMessage({
                type: 'error',
                message: res.msg,
                duration:2000,
              })
            }

          })
        }
      })


    },
  }
}

</script>

<style scoped>
.bgc-container{
  width: 100%;
  height: 100vh;
  background-color: darkslateblue;
  overflow: hidden;
  display: flex;
}
.bgc-container>div{
  width: 100%;
  justify-content: center;
  display: flex;
}
.bgc-container>div>div{
  color: #cccccc;
  font-size: 30px;
  text-align: center;
  margin-top: 150px;
  width: 400px;
}
.loginText{
  padding-bottom: 30px;
}
</style>