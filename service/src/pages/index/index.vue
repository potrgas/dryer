<template>
  <div class="box">
    <div class="scan"></div>
    <van-row>
      <van-col span="12" offset="8">
        <van-button @click="scan" type="primary">开始扫码</van-button>
      </van-col>
    </van-row>
    <van-dialog use-slot :asyncClose="true" :show="showBox" show-cancel-button confirm-button-open-type="getUserInfo"
      @getuserinfo="auth" @getphonenumber="auth">
      <van-field :value="account.userName" label="用户名" placeholder="请输入用户名" />
      <van-field :value="account.password" type="password" label="密码" :border="false" placeholder="请输入密码" />
    </van-dialog>
  </div>

</template>

<script>
// Use Vuex
import { mapMutations } from "vuex";
export default {
  data() {
    return {
      showBox: true,
      customer: {
        radio: "1",
        type: "paynow",
        name: "",
        name: "",
        address: ""
      },
      userInfo: {},
      account: {}
    };
  },

  components: {},

  methods: {
    scan() {
      // 只允许从相机扫码
      wx.scanCode({
        onlyFromCamera: true,
        success(res) {
          console.log(res);
        }
      });
    },
    ...mapMutations({
      set_userInfo: "set_userinfo"
    })
  },
  created() {}
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
