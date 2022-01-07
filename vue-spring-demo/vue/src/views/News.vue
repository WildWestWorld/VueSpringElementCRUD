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
      <el-table-column prop="title" label="标题"  />
      <el-table-column prop="author" label="作者"  />
      <el-table-column prop="time" label="时间" />



      <el-table-column  label="操作" >
        <template #default="scope">
          <el-button   @click="contentDetail( scope.row)">详情</el-button>

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

    <el-dialog v-model="dialogVisible" title="提示" width="50%">
      <el-form  :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title" style="width:60%"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" style="width:60%"></el-input>
        </el-form-item>

<!--        <el-form-item label="内容">-->
<!--          <el-input v-model="form.content" style="width:80%"></el-input>-->
<!--        </el-form-item>-->
        <div id="div1"></div>


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



    <el-dialog v-model="contentVis" title="提示" width="50%">
      <el-card style="min-height: 100px">
        <div v-html="detail.content"></div>
      </el-card>
    </el-dialog>

  </div>
</template>


<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import request from "@/utils/request";
import { h } from 'vue'
import { ElMessage } from 'element-plus'
import E from 'wangeditor'
let editor;

export default {
  name: 'News',
  components: {
    HelloWorld,
    ElMessage
  },
  data(){
    return{
      form:{},
      searchWord:'',
      currentPage:1,
      total:0,
      dialogVisible:false,
      pageNum: 1,
      pageSize:10,
      tableData:[

      ],
      detail:{},
      contentVis:false,
    }
  },
  created() {
    this.load()

  },
  methods:{
    contentDetail(row){
      this.detail =JSON.parse(JSON.stringify(row))
      this.contentVis=true
    },

    fileUploadSuccess(res){
      console.log(res)
      this.form.cover=res.data;
    },
    load(){
      request.get("/api/news", {
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

      this.$nextTick(()=>{
        if(!editor){
          editor = new E('#div1')
          editor.config.uploadImgServer="http://localhost:9090/files/editor/upload"
          editor.config.uploadFileName = 'file'
          editor.create()
        }else{
          editor.txt.clear()
        }

      })

    },
    save(){


      this.form.content=editor.txt.html()//获取编辑器里面的值





      //有ID=>更新，没ID=>创建
      if(this.form.id){
        request.put("/api/news",this.form).then(res=>{
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
        request.post("/api/news",this.form).then(res=>{
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
      this.$nextTick(()=>{
        if(!editor){
          editor = new E('#div1')

          editor.create()
        }else{
          editor.txt.clear()
        }
        editor.txt.html(row.content)
      })

    },
    handleDelete(id){
      console.log(id);
      request.delete("/api/news/"+id).then(res=>{
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