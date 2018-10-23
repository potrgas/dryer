<template>
  <div class="box">
    <van-row>
      <van-col span="12">服务一次 </van-col>
      <van-col span="12">服务六次 </van-col>
    </van-row>
   <van-row>
      <van-col span="12">服务13次 </van-col>
    </van-row>
     <van-row>
          <van-col span="8" offset="8">
            <van-button type="primary">确认支付</van-button>
          </van-col>
        </van-row>
  </div>

</template>

<script>
// Use Vuex
export default {
  data() {
    return {
      customer: {},
      radio: "1",
      userInfo: {}
    };
  },

  components: {},

  methods: {
    onClick(e) {
      this.radio = e;
      console.log(e);
    },
    onChange() {
      console.log(2);
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
.textshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
}
</style>
