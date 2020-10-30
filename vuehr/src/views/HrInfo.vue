<template>
  <div v-if="hr">
    <el-card class="box-card" style="width: 400px">
      <div slot="header" class="clearfix">
        <span>{{ hr.name }}</span>
      </div>
      <div>
        <div style="display:flex;justify-content: center;">
          <el-upload
            :show-file-list="false"
            :on-success="onSuccess"
            :data="hr"
            action="/hr/userface"
          >
            <img
              title="点击修改用户头像"
              :src="ht.userface"
              style="width:100px; height: 100px; border-radius: 50px"
              alt=""
            />
          </el-upload>
        </div>
        <div>
          电话号码： <el-tag>{{ hr.telephone }}</el-tag>
        </div>
        <div>
          手机号码： <el-tag>{{ hr.phone }}</el-tag>
        </div>
        <div>
          居住地址： <el-tag>{{ hr.address }}</el-tag>
        </div>
        <div>
          用户标签：
          <el-tag
            type="success"
            style="margin-right: 5px;"
            v-for="(r, index) in hr.roles"
            :key="index"
            >{{ r.nameZh }}</el-tag
          >
        </div>
        <div
          style="display:flex; justify-content:space-around; margin-top: 10px"
        >
          <el-button type="primary" @click="showUpdateHrInfoView"
            >修改信息</el-button
          >
          <el-button type="danger" @click="showUpdatePasswordView"
            >修改密码</el-button
          >
        </div>
      </div>
    </el-card>
    <el-dialog>
      <div>
        <table>
          <tr>
            <td>
              <el-tag>用户昵称：</el-tag>
            </td>
            <td>
              <el-input v-model="hr2.name"></el-input>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>电话号码：</el-tag>
            </td>
            <td>
              <el-input v-model="hr2.telephone"></el-input>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>手机号码：</el-tag>
            </td>
            <td>
              <el-input v-model="hr2.phone"></el-input>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>用户地址：</el-tag>
            </td>
            <td>
              <el-input v-model="hr2.address"></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateHrInfo">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="passwdDialogVisible" width="30%">
      <div>
        <el-form
          :model="ruleForm"
          status-icon
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
            <el-form-item label="请输入旧密码" prop="oldpadd">
                <el-input type="password" v-model="ruleForm.oldpass" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="请输入新密码" prop="pass">
                <el-input type="password" v-model="ruleForm.pass" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="checkPass">
                <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
    name: "HrInfo",
    data() {
        let validatePass = (rule, value, callback) => {
            if(value == "") {
                callback(new Error("请输入密码"))
            } else {
                if(this.ruleForm.checkPass !== "") {
                    this.$refs.ruleForm.validateField("checkPass")
                }
                callback()
            }
        }
        let validatePass2 = (rule, value, callback) => {
            if(value == "") {
                callback(new Error("请再次输入密码"))
            } else if(value !== this.ruleForm.pass) {
                callback(new Error("两次密码输入不一致!"))
            } else {
                callback()
            }
        }
        return {
            ruleForm: {
                oldpass: "",
                pass: "",
                checkPass: ""
            },
            rules: {
                oldpass: [{validator: validatePass, tigger: "blur"}],
                pass: [{validator: validatePass, tigger: "blur"}],
                checkPass: [{validator: validatePass2, tigger: "blur"}]
            },
            hr: null,
            hr2: null,
            dialogVisible: false,
            passwdDialogVisible: false
        }
    },
    mounted() {
        this.initHr()
    },
    methods: {
        onSuccess() {
            this.initHr()
        },
        updateHrInfo() {
            this.putRequest('hr/info', this.hr2).then((resp) => {
                if(resp) {
                    console.log(resp)
                    this.dialogVisible = false
                    this.initHr()
                }
            })
        },
        showUpdateHrInfoView() {
            this.dialogVisible = true
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if(valid) {
                    this.ruleForm.hrid = this.hr.id
                    this.putRequest('hr/pass', this.ruleForm).then((resp) => {
                        if(resp) {
                            console.log(resp)
                            this.getRequest("/logout")
                            window.sessionStorage.removeItem("user")
                            this.$store.commit("initRoutes", [])
                            this.$router.replace("/")
                        }
                    })
                } else {
                    return false
                }
            })
        },
        resetForm(formName) {
            this.$refs[formName].resetFields()
        },
        showUpdatePasswordView() {
            this.passwdDialogVisible = true
        },
        initHr() {
            this.getRequest('/hr/info').then((resp) => {
                if(resp) {
                    this.hr = resp;
                    this.hr2 = Object.assign({}, this.hr)
                    window.sessionStorage.setItem("user", JSON.stringify(resp))
                    this.$store.commit("INIT_CURRENTHR", resp)
                }
            })
        }
    }
}
</script>
