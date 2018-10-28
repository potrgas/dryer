<template>
  <div class="box">
    <van-cell-group>
      <van-cell value="待取回" @click="gotodetail(1)" icon="shop" is-link >
        <view slot="title">
          <span class="van-cell-text">2018年12月13日</span>
        </view>
      </van-cell>
    
     <van-cell value="完成" @click="gotodetail(1)" icon="info-o" is-link>
        <view slot="title">
          <span class="van-cell-text">2018年12月14日</span>
        </view>
      </van-cell>

       <van-cell value="取消" @click="gotodetail(1)" icon="like-o" is-link>
        <view slot="title">
          <span class="van-cell-text">2018年12月15日</span>
        </view>
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
    gotodetail(key) {
      const url = "../details/main";
      wx.navigateTo({
        url
      });
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
