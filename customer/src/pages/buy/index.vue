<template>
  <div class="box">
    <van-row>
      <van-col span="12">
        <div class="boxshow">服务1次</div> </van-col>
      <van-col span="12"><div class="boxshow">服务6次</div> </van-col>
    </van-row>
   <van-row>
      <van-col span="12"><div class="boxshow">服务13次</div></van-col>
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
.boxshow {
  font-size: 12;
  margin-bottom: 2;
  padding: 3 3 3 3;
  font-stretch: condensed;
  background-color: aqua;
  width: 100%;
  height: 160px;
}
</style>
