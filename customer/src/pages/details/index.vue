<template>
  <div class="box">
    <van-row>
      <van-col span="16"><text class="textshow">织物订单信息</text> </van-col>
    </van-row>
    <van-row>
      <van-col v-if="currentOrder.dryType==1" span="8"><text class="textshow">纯棉</text>√
      </van-col>
      <van-col v-if="currentOrder.dryType==2" span="8"><text class="textshow">化纤</text>√ </van-col>
      <van-col v-if="currentOrder.dryType==3" span="8"><text class="textshow">其他</text>√ </van-col>
    </van-row>


    <van-row>
      <van-col span="16"><text class="textshow">确认个人信息</text></van-col>
    </van-row>
    <van-row>
      <van-cell-group>
        <van-field :value="currentOrder.customerName" required readonly label="姓名" placeholder="请输入姓名" />
        <van-field :value="currentOrder.mobile" required readonly label="联系电话" placeholder="请输入联系电话" />
        <van-field :value="currentOrder.address" required readonly label="取件地址" placeholder="请输入取件地址" />
        <van-row v-if="currentOrder.orderState==0" >
            <canvas class='boxshow' canvas-id='canvas' bindlongtap='save'></canvas>
        </van-row>
      </van-cell-group>
    </van-row>
  </div>

</template>
<script>
// Use Vuex
import { mapGetters } from "vuex";
import QRCode from "@/utils/weapp-qrcode";
export default {
  data() {
    return {};
  },

  components: {},
  computed: {
    ...mapGetters(["currentOrder"])
  },
  methods: {
    buildQrcode(order) {
      //传入wxml中二维码canvas的canvas-id
      var qrcode = new QRCode("canvas", {
        // usingIn: this,
        text: order.id,
        width: 150,
        height: 150,
        colorDark: "#000000",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.H
      });
    },
    gotomy() {
      wx.switchTab({
        url: "../my/main"
      });
    }
  },
  mounted() {
    console.log(this.currentOrder);
    if (this.currentOrder) {
      this.buildQrcode(this.currentOrder);
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
  font-stretch: condensed;
  background-color: aqua;
  width: 100%;
  height: 200px;
}
</style>
