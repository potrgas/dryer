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
        <van-button @click="payfor" type="primary">确认支付({{order.price}}元)</van-button>
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
      return {};
    },
    computed: {
      ...mapGetters(["order"])
    },
    components: {},

    methods: {
      //调用支付功能
      payfor() {
        let params = {
          url: "api/chat/make",
          data: this.order
        };
        post(params).then(r => {
          console.log(r);
          if (r.data.result) {
            wx.navigateTo({
              url: "../payfor/main"
            });
          }
        })
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
