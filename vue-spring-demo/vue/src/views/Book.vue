<template>
  <div class="home">
    <div class="button" v-if="user.role==='1'">
      <el-button type="primary" @click="add">新增</el-button>

      <el-popconfirm title="确定要批量删除吗？" @confirm="deleteBatch">
        <template #reference>
          <el-button type="danger">批量删除</el-button>
        </template>
      </el-popconfirm>


      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>

    </div>
    <div class="searchInput">
      <el-input v-model="searchWord" placeholder="请输入搜素内容" style="width: 20%;" clearable></el-input>
      <el-button type="primary" style="margin: 10px 10px;" @click="load()">查询</el-button>
    </div>


    <el-table :data="tableData" stripe border style="width: 100%"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="name" label="书名"  />
      <el-table-column prop="price" label="价格"  />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="userId" label="拥有者ID" />
      <el-table-column prop="cover" label="封面">

      <template #default="scope">
        <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.cover"
            :preview-src-list="[scope.row.cover]"

        >
        </el-image>
      </template>
        </el-table-column>




      <el-table-column  label="操作" >
        <template #default="scope">
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

    <el-dialog v-model="dialogVisible" title="提示" width="30%">
      <el-form  :model="form" label-width="120px">
        <el-form-item label="书名">
          <el-input v-model="form.name" style="width:80%"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" style="width:80%"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" style="width:80%"></el-input>
        </el-form-item>

        <el-form-item label="出版时间">
          <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" clearable></el-date-picker>
        </el-form-item>

        <el-form-item label="拥有者ID">
          <el-input v-model="form.userId" style="width:80%"></el-input>
        </el-form-item>

        <el-form-item label="封面">

          <el-upload ref="clearUpload" action="http://localhost:9090/files/upload" :on-success="fileUploadSuccess">
            <el-button type="primary">Click to upload</el-button>
          </el-upload>

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
  name: 'Book',
  components: {
    HelloWorld,
    ElMessage
  },
  data(){
    return{
      ids:[],
      form:{},
      searchWord:'',
      currentPage:1,
      total:0,
      dialogVisible:false,
      pageNum: 1,
      pageSize:10,
      tableData:[
      ],
    }
  },
  created() {
    this.load()

  },
  props:['user'],
  methods:{
    handleSelectionChange(val){
      console.log(JSON.parse(JSON.stringify(val)))
      this.ids = val.map(item=> item.id)
      // console.log(JSON.parse(JSON.stringify(this.ids)))
    },
    deleteBatch(){
      if (!this.ids.length){
        ElMessage({
          type: 'warning',
          message: '请选择数据',
          duration:2000,
        })
      }
      request.post("/api/book/deleteBatch",this.ids).then(res=>{
        if (res.code ==='0'){
          ElMessage({
            type: 'success',
            message: '批量删除成功',
            duration:2000,
          })
          this.load()
        }else{
          ElMessage({
            type: 'success',
            message: res.msg,
            duration:2000,
          })
        }

      })
    },
    fileUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data;
    },
    load(){
      request.get("/api/book", {
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
    },
    add(){
      this.dialogVisible=true;
      this.form={};

      this.$nextTick(()=>{
        if ( this.$refs['clearUpload']){
          this.$refs['clearUpload'].clearFiles()
        }
      })

    },
    save(){
      //有ID=>更新，没ID=>创建
      if(this.form.id){
        request.put("/api/book",this.form).then(res=>{
          console.log(res)
          if(res.code==='0'){
            ElMessage({
              type: 'success',
              message: '更新成功',
              duration:2000,
            })
          }else{
            ElMessage({
              type: 'error',
              message: res.msg,
              duration:2000,
            })
          }

        })
      }else{
        request.post("/api/book",this.form).then(res=>{
          console.log(res)

          if(res.code==='0'){
            ElMessage({
              type: 'success',
              message: '新增成功',
              duration:2000,
            })
            this.load()
            this.dialogVisible=false;
          }else{
            ElMessage({
              type: 'error',
              message: res.msg,
              duration:2000,
            })
            this.load()
            this.dialogVisible=false;

            this.$nextTick(()=>{
              if ( this.$refs['clearUpload']){
                this.$refs['clearUpload'].clearFiles()
              }
            })

          }

        })
      }
      this.load()
      this.dialogVisible=false;
      this.$nextTick(()=>{
        if ( this.$refs['clearUpload']){
          this.$refs['clearUpload'].clearFiles()
        }
      })

    },
    handleEdit(row){
      this.form=JSON.parse(JSON.stringify(row));
      this.dialogVisible=true;

    },
    handleDelete(id){
      console.log(id);
      request.delete("/api/book/"+id).then(res=>{
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