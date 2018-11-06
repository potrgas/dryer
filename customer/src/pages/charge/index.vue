<template>
  <div class="box">
    <van-row>
      <van-col @click="serviceChange(1)" span="12">
        <div class="boxshow">服务1次(20元)</div>
      </van-col>
      <van-col @click="serviceChange(6)" span="12">
        <div class="boxshow">服务6次(100元)</div>
      </van-col>
    </van-row>
    <van-row>
      <van-col @click="serviceChange(13)" span="12">
        <div class="boxshow">服务13次(200元)</div>
      </van-col>
    </van-row>
    <van-row>
      <van-col span="8" offset="3">
        <van-button type="primary">使用次数(4次)</van-button>
      </van-col>
      <van-col span="8" offset="3">
        <van-button type="primary">在线支付</van-button>
      </van-col>
    </van-row>
  
  </div>
</template>
<script>
// Use Vuex
// Use Vuex
import { mapMutations } from "vuex";
export default {
  data() {
    return {
      payType: 1,
      serviceType: null,
      radio: "1",
      userInfo: {},
      showBox: true
    };
  },
  components: {},
  methods: {
    ...mapMutations({
      set_userInfo: "set_userinfo"
    }),
    onClick(e) {
      this.radio = e;
      console.log(e);
    },
    onChange() {
      console.log(2);
    },
    onClose() {
      this.showBox = false;
    },
    serviceChange(key) {
      console.log(key);
      this.serviceType = key;
    },
    auth(source) {
      console.log(source);
      var mo = source.target.userInfo;
      this.set_userInfo(mo);
    },
    bindViewTap() {
      const url = "../logs/main";
      wx.navigateTo({
        url
      });
    },

    getUserInfo() {
      // 调用登录接口
      wx.login({
        success: () => {
          wx.getUserInfo({
            success: res => {
              this.userInfo = res.userInfo;
            }
          });
        }
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

.boxshow {
  font-size: 12;
  margin-bottom: 2;
  margin: 3 3;
  font-stretch: condensed;
  background-color: aqua;
  width: 100%;
  height: 160px;
}
</style>
