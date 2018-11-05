<template>
  <div class="box">
    <van-row>
      <div class="textshow">
        <p>上门取件说明介绍文案
          需要包含单次上门费用</p>
          {{order.customerName}}
          {{order.mobile}}
          {{order.area}}
          {{order.community}}
          {{order.address}}
      </div>
    </van-row>
    <van-row>
      <van-col  offset="8">
        <van-button @click="payfor" type="primary">确认支付({{order.price}}元)</van-button>
      </van-col>
    </van-row>
  </div>

</template>

<script>
// Use Vuex
import { post } from "@/utils/api";
import { mapGetters } from "vuex";
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
          //小程序发起微信支付
          wx.requestPayment({
            timeStamp: r.data.data.timeStamp, //记住，这边的timeStamp一定要是字符串类型的，不然会报错，我这边在java后端包装成了字符串类型了
            nonceStr: r.data.data.nonceStr,
            package: r.data.data.package,
            signType: "MD5",
            paySign: r.data.data.paySign,
            success: function(event) {
              // success
              console.log(event);
              wx.navigateTo({
                url: "../index/main"
              });
            },
            fail: function(error) {
              // fail
              console.log("支付失败");
              console.log(error);
            },
            complete: function() {
              // complete
              console.log("pay complete");
            }
          });
        }
      });
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
