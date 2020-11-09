<template>
  <div>
    <div>
      <div style="display:flex; justify-content: space-between">
        <div v-show="showSearch">
          操作人员
          <el-input
            v-model="queryParams.operName"
            placeholder="请输入操作人员"
            clearable
            style="width: 240px; margin-right: 10px"
            size="small"
            @keyup.enter.native="handleQuery"
          ></el-input>
          状态
          <el-select
            v-model="queryParams.status"
            placeholder="操作状态"
            clearable
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabal"
              :value="dict.dictValue"
            >
            </el-option>
          </el-select>
          操作时间
          <el-date-picker
            v-model="dateRange"
            size="small"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
          <el-button
            type="cyan"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </div>
        <div>
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            >删除</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="handleClean"
            >清空</el-button
          >
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            >导出</el-button
          >
        </div>
        <div class="top-right-btn">
          <el-row>
            <el-tooltip
              class="item"
              effect="dark"
              :content="showSearch ? '隐藏搜索' : '显示搜索'"
              placement="top"
            >
              <el-button
                size="mini"
                circle
                icon="el-icon-search"
                @click="showSearch = !showSearch"
              ></el-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="dark"
              :content="'刷新'"
              placement="top"
            >
              <el-button
                size="mini"
                circle
                icon="el-icon-refresh"
                @click="refresh()"
              ></el-button>
            </el-tooltip>
          </el-row>
        </div>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="系统模块" align="center" prop="title" />
      <el-table-column
        label="操作类型"
        align="center"
        prop="businessType"
        :formatter="hasBusinessFormat"
      />
      <el-table-column label="请求方式" align="center" prop="requestMethod" />
      <el-table-column label="操作人员" align="center" prop="operName" />
      <el-table-column label="主机" align="center" prop="operIp" width="130" />
      <!-- <el-table-column
        label="操作地点"
        align="center"
        prop="operLocation"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        label="操作状态"
        align="center"
        prop="status"
        :formatter="hasStatusFormat"
      />
      <el-table-column
        label="操作日期"
        align="center"
        prop="operTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.operTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row, scope.index)"
            >详细</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex;justify-content: flex-end">
      <el-pagination
        background
        @current-change="currentChange"
        @size-change="sizeChange"
        :total="total"
        layout="sizes, prev, pager, next, jumper, ->, total, slot"
      >
      </el-pagination>
    </div>

    <el-dialog
      title="操作日志详细"
      :visible.sync="open"
      width="700px"
      append-to-body
    >
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模块："
              >{{ form.title }} / {{ form }}</el-form-item
            >
            <!-- <el-form-item
              label="登录信息："
            >{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-form-item> -->
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求地址：">{{ form.operUrl }}</el-form-item>
            <el-form-item label="请求方式：">{{
              form.requestMethod
            }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回参数：">{{
              form.jsonResult
            }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作状态：">
              <div v-if="form.status === 0">正常</div>
              <div v-else-if="form.status === 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作时间：">{{ form.operTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status === 1">{{
              form.errorMsg
            }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SysLog",
  data() {
    return {
      loading: true, // 遮罩层
      ids: [], // 选中数组
      multiple: true, //非多个禁用
      showSearch: true, //显示搜索条件
      total: 0, // 总条数
      page: 1,
      size: 10,
      list: [], //表单数据
      open: false, //是否显示弹出层
      statusOptions: [], //类型数据字典
      dateRange: [], //日期范围
      form: {},
      queryParams: {
        page: 1,
        size: 10,
        operName: "",
        status: ""
      }
    };
  },
  mounted() {
    this.initLog();
  },
  methods: {
    refresh() {
      this.initLog();
    },
    initLog(type) {
      this.loading = true;
      let url =
        "/system/basic/syslog/?page=" + this.page + "&size=" + this.size;
      if (this.queryParams.operName) {
        url += "&operName=" + this.queryParams.operName;
      }
      this.getRequest(url).then(resp => {
        if (resp) {
          console.log(resp);
          if (resp.data.businessType == 0) {
            this.list.businessType = "其他";
          }
          this.list = resp.data;
          this.total = resp.total;
          this.loading = false;
        }
      });
    },
    hasBusinessFormat(row, column) {
      if (row.businessType == 0) {
        return "其他";
      }
    },
    hasStatusFormat(row, column) {
      if (row.status == 0) {
        return "成功";
      } else {
        return "失败";
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.initLog();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.multiple = !selection.length;
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initLog();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initLog("advanced");
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids;
      this.$confirm(
        '是否确认删除日志编号为"' + operIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          this.deleteRequest("/system/basic/syslog/", operIds).then(resp => {
            if (resp) {
              this.initLog();
              this.$message({
                type: "info",
                message: "删除成功"
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$confirm("是否确认清空所有操作日志数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(resp => {
          this.deleteRequest("/system/basic/syslog/clean").then(resp => {
            if (resp) {
              this.initLog();
              this.$message({ type: "info", message: "删除成功" });
            }
          });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有操作日志数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(resp => {
          window.open("system/basic/syslog/export", "_parent");
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消" });
        });
    }
  }
};
</script>