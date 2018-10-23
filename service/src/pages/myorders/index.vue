<template>
  <div class="box">
    <van-cell-group>
      <van-cell value="内容" icon="shop" is-link>
        <view slot="title">
          <span class="van-cell-text">单元格</span>
          <van-tag type="danger">标签</van-tag>
        </view>
      </van-cell>
      <van-cell title="单元格" icon="location" is-link />
      <van-cell title="单元格" border="false">
        <van-icon slot="right-icon" name="search" class="van-cell__right-icon" />
      </van-cell>
    </van-cell-group>
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
