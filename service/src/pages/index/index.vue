<template>
  <div class="box">
    <div class="scan"></div>
    <van-row>
      <van-col span="12" offset="8">
        <van-button  type="primary">开始扫码</van-button>
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
    ...mapMutations({
      set_userInfo: "set_userinfo"
    }),
    onSelectRadio(e) {
      this.customer.radio = e.currentTarget.dataset.name;
    },
    onSelectType(e) {
      this.customer.type = e.currentTarget.dataset.name;
    },
    onClose() {
      this.showBox = false;
    },
    auth(source) {
      console.log(source);
      var mo = source.target.userInfo;
      this.set_userInfo(mo);
      this.customer.name = mo.nickName;
      this.customer.mobile = "1181651";
      this.customer.address = mo.nickName;
      this.showBox = false;
    },
    bindViewTap() {
      const url = "../logs/main";
      wx.navigateTo({
        url
      });
    },
    clickHandle(msg, ev) {
      console.log("clickHandle:", msg, ev);
    }
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
