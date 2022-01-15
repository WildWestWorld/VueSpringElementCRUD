<template>
  <div class="bgc-container">
    <div>
      <div>
        <div class="loginText">欢迎登录</div>
        <el-form ref="formRef" :model="form" size="large" :rules="rules">
          <el-form-item prop="username" >
            <el-input v-model="form.username">

              <template #prefix>
                <el-icon class="el-input__icon">
                  <UserFilled/>
                </el-icon>
              </template>

            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="form.password" show-password >

              <template #prefix>
                <el-icon class="el-input__icon">
                  <Lock/>
                </el-icon>
              </template>

            </el-input>
          </el-form-item>


          <div style="display:flex;">
          <el-form-item prop="validCode">
            <el-input v-model="form.validCode"  @keyup.enter.native="login('formRef')" >

              <template #prefix>
                <el-icon class="el-input__icon">
                  <Message/>
                </el-icon>
              </template>

            </el-input>
          </el-form-item>

            <ValidCode  @input="createValidCode" ref="ValidCode"></ValidCode>
          </div>



          <el-form-item>
            <el-button style="width: 100%;" type="primary" @click="login('formRef')">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import {Search, UserFilled, Lock,Message} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {reactive} from 'vue';
import ValidCode from "@/components/ValidCode";
import {activeRouter} from "@/utils/permission";


export default {
  name: "Login",
  components: {
    ValidCode,
    Search,
    UserFilled,
    Lock,
    Message,
  },
  data() {
    return {
      form: {},
      validCode:'',
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
        validCode: [
          {
            required: true,
            message: '请输入验证码',
            trigger: 'blur',
          },
        ],

      }),

    }

  },
  methods: {
    createValidCode(data){
      this.validCode = data
    },

    login(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

        if(!this.form.validCode){
          ElMessage({
            type: 'error',
            message: '请输入验证码',
            duration: 2000,
          })
          return
        }

          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()){
            ElMessage({
              type: 'error',
              message: '验证码错误',
              duration: 2000,
            })
            this.$refs.ValidCode.refreshCode();
            return
          }


          request.post("/api/user/login", this.form).then(res => {
            if (res.code === '0') {
              ElMessage({
                type: 'success',
                message: '登录成功',
                duration: 2000,
              })
              console.log(res)
              sessionStorage.setItem("user",JSON.stringify(res.data))
              sessionStorage.setItem("token",JSON.stringify(res.data.token))
              sessionStorage.setItem("permissions",JSON.stringify(res.data.permissions))

              const permission = res.data.permissions;
              //初始化路由信息
              activeRouter(permission)

              this.$router.push("/");

            } else {
              ElMessage({
                type: 'error',
                message: res.msg,
                duration: 2000,
              })
              this.$refs.ValidCode.refreshCode();
              return
            }

          })
        }
      })


    },
  }
}

</script>

<style scoped>
.bgc-container {
  width: 100%;
  height: 100vh;
  background-color: darkslateblue;
  overflow: hidden;
  display: flex;
}

.bgc-container > div {
  width: 100%;
  justify-content: center;
  display: flex;
}

.bgc-container > div > div {
  color: #cccccc;
  font-size: 30px;
  text-align: center;
  margin-top: 150px;
  width: 400px;
}

.loginText {
  padding-bottom: 30px;
}
</style>