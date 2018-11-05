<template>
  <div class="box">
    <van-row>
      <div class="textshow">
        <p>上门取件说明介绍文案
          需要包含单次上门费用</p>
      </div>
    </van-row>
    <van-row>
      <van-col span="8" offset="8">
        <van-button @click="payfor" type="primary">确认支付(10元)</van-button>
      </van-col>
    </van-row>
  </div>

</template>

<script>
  // Use Vuex
  import {
    post
  } from "@/utils/api";
  import {
    mapGetters
  } from "vuex";
  export default {
    data() {
      return {
        customer: {},
        radio: "1",
        userInfo: {}
      };
    },
    computed: {
      ...mapGetters(["order"])
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
      //调用支付功能
      payfor() {

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
    background-color: aqua;
    width: 100%;
    height: 400px;
  }

</style>
