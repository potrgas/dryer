<template>
  <div class="box">
    <van-row>
      <van-col @click="serviceChange(1,20)" span="12">
        <div class="boxshow">服务1次(20元)</div>
      </van-col>
      <van-col @click="serviceChange(6,100)" span="12">
        <div class="boxshow">服务6次(100元)</div>
      </van-col>
    </van-row>
    <van-row>
      <van-col @click="serviceChange(13,200)" span="12">
        <div class="boxshow">服务13次(200元)</div>
      </van-col>
    </van-row>
    <van-row>
     
      <van-col  offset="3">
        <van-button @click="charge" type="primary">在线支付</van-button>
      </van-col>
    </van-row>
  </div>
</template>
<script>
// Use Vuex
// Use Vuex
import { mapMutations } from "vuex";
import { post, get } from "@/utils/api";
export default {
  data() {
    return {
      order: {
        openId: "",
        price: 0,
        productName: "",
        count: 0
      }
    };
  },
  components: {},
  methods: {
    serviceChange(count, price) {
      this.order.price = price;
      this.order.count = count;
      this.order.productName = `服务${count}次`;
    },

    charge() {
      var obj = wx.getStorageSync("openId");
      this.order.openId = obj;
      let params = { url: "api/chat/charge", data: this.order };
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
  activated() {
    var obj = wx.getStorageSync("openId");
    this.order.openId = obj;
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
