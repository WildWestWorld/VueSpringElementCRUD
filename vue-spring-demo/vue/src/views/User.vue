<template>
  <div class="home">
    <div class="button">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>

    </div>
    <div class="searchInput">
      <el-input v-model="searchWord" placeholder="请输入搜素内容" style="width: 20%;" clearable></el-input>
      <el-button type="primary" style="margin: 10px 10px;" @click="load()">查询</el-button>
    </div>
    <el-table :data="tableData" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="username" label="用户名"  />
      <el-table-column prop="password" label="密码" />
      <el-table-column prop="nikeName" label="昵称" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="address" label="地址" />


      <el-table-column label="角色列表" width="300">
        <template #default="scope">
          <el-select v-model="scope.row.roles" multiple placeholder="请选择" style="width: 80%">
            <el-option v-for="item in roles" :key="item.id" :label="item.comment" :value="item.id"></el-option>
          </el-select>
        </template>
      </el-table-column>

      <el-table-column  label="操作"  width="400">
        <template #default="scope">
          <el-button  type="primary" @click="handleChange(scope.row)">保存权限菜单</el-button>
          <el-button   type="success" plain @click="showBooks( scope.row.bookList)">查看书籍列表</el-button>
          <el-button   @click="handleEdit( scope.row)">编辑</el-button>

          <el-popconfirm title="你确定要删除吗?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" >删除</el-button>
            </template>
          </el-popconfirm>


        </template>
      </el-table-column>
    </el-table>
    <div class="page">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size=pageSize
          layout="total, sizes, prev, pager, next, jumper"
          :total=total
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>

    <el-dialog v-model="bookVis" title="用户拥有的图书列表" width="30%">
      <el-table :data="bookList" stripe border style="width: 100%">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="name" label="书名"  />
        <el-table-column prop="price" label="价格" />
      </el-table>
    </el-dialog>



    <el-dialog v-model="dialogVisible" title="提示" width="30%">
    <el-form  :model="form" label-width="120px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" style="width:80%"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nikeName" style="width:80%"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age" style="width:80%"></el-input>
      </el-form-item>

      <el-form-item label="性别">
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
        <el-radio v-model="form.sex" label="未知">未知</el-radio>
      </el-form-item>





      <el-form-item label="地址">
        <el-input type="textarea" v-model="form.address" style="width:80%"></el-input>
      </el-form-item>
    </el-form>

      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>

        <el-button type="primary" @click="save">
          确定
        </el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>


<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import request from "@/utils/request";
import { h } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'Home',
  components: {
    HelloWorld,
    ElMessage
  },
  data(){
    return{
      roles:{},
      bookList:[],
      form:{},
      searchWord:'',
      currentPage:1,
      total:0,
      dialogVisible:false,
      bookVis:false,
      pageNum: 1,
      pageSize:10,
      tableData:[

      ],
    }
  },
  created() {
    this.load()
    this.$emit("userInfo")
  },
  methods:{
    handleChange(row) {
      request.put("/api/user/changeRole", row).then(res => {
        if (res.code === '0') {
          this.$message.success("更新成功")
          if (res.data) {
            this.$router.push("/login")
          }
        }
      })
    },
    showBooks(bookList){
      console.log(JSON.parse(JSON.stringify(bookList)));
      this.bookList=bookList;
      this.bookVis=true;
    },
    load(){
      request.get("/api/user", {
        params: {
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          searchWord:this.searchWord
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records;
        this.total=res.data.total
      })
      request.get("/api/role/all").then(res => {
        this.roles = res.data
      })
    },
    add(){
      this.dialogVisible=true;
      this.form={};
    },
    save(){
      //有ID=>更新，没ID=>创建
      if(this.form.id){
        request.put("/api/user",this.form).then(res=>{
          console.log(res)
          if(res.code==='0'){
            ElMessage({
              type: 'success',
              message: '更新成功',
              duration:2000,

            })
            this.load()
            this.$emit("userInfo");
          }else{
            ElMessage({
              type: 'error',
              message: res.msg,
              duration:2000,
            })
          }

        })
      }else{
      request.post("/api/user",this.form).then(res=>{


        if(res.code==='0'){
          ElMessage({
            type: 'success',
            message: '新增成功',
            duration:2000,
          })
          this.load()
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
            duration:2000,
          })
        }

      })
      }
      this.load()
      this.$emit("userInfo")
      this.dialogVisible=false;

    },
    handleEdit(row){

      this.form=JSON.parse(JSON.stringify(row));
      console.log(this.form.role);
      this.dialogVisible=true;
    },
    handleDelete(id){
      console.log(id);
      request.delete("/api/user/"+id).then(res=>{
        if(res.code==='0'){
          ElMessage({
            type: 'success',
            message: '删除成功',
            duration:2000,
          })
        }else{
          ElMessage({
            type: 'error',
            message: res.msg,
            duration:2000,
          })
        }
        this.load();
      })

    },
    handleSizeChange(pageSize){
      this.pageSize =pageSize;
      this.load();
    },
    handleCurrentChange(pageNum){
      this.currentPage =pageNum;
      this.load();
    }
  }
}
</script>

<style>
.home{
  width: 100%;
}
.button{
  margin: 10px 10px;
}

.page{
  margin: 10px 0;
}
</style>