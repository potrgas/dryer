<template>
  <div class="box">
    <van-field id="userName" @change="inputValue" :value="model.userName" label="用户名" placeholder="请输入用户名" />
    <van-field id="password" @change="inputValue" :value="model.password" type="password" label="密码" :border="false"
      placeholder="请输入密码" />
    <van-button @click="login" type="primary">登陆</van-button>
  </div>

</template>

<script>
// Use Vuex
import { mapMutations, mapGetters } from "vuex";
import { get, post } from "@/utils/api";
import Toast from "../../../static/vant/toast/toast";
export default {
  data() {
    return {
      model: {
        userName: "zhangsan",
        password: "1234567"
      }
    };
  },
  computed: {
    ...mapGetters(["operater"])
  },
  components: {},

  methods: {
    ...mapMutations({
      setOperater: "setOperater"
    }),
    inputValue(e) {
      this.model[e.mp.target.id] = e.mp.detail;
    },
    login() {
      if (!this.model.userName || !this.model.password) {
        Toast.fail("请完善必填信息");
        return;
      }
      var params = {
        url: "api/operater/login",
        data: this.model
      };
      post(params).then(r => {
        if (r.result == "00000000") {
          this.setOperater(r.data);
          wx.switchTab({
            url: "/pages/index/main"
          });
        }
        console.log(r);
      });
    }
  },
  created() {
    console.log(this.operater);
    if (this.operater.id) {
      wx.switchTab({
        url: "/pages/index/main"
      });
    }
  }
};
</script>

<style scoped>
.box {
  padding: 2%;
}

.scan {
  width: 80%;
  height: 300px;
  background-color: aqua;
  margin: auto auto;
}

.textshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
}
</style>
