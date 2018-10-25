<template>
  <div class="box">
    <van-row>
      <van-col span="16"><text class="textshow">织物订单信息</text> </van-col>
    </van-row>
    <van-row>
      <van-radio-group :value="customer.radio">
          <van-cell title="纯棉" clickable data-name="1" @click="onSelectRadio">
            <van-radio name="1" />
          </van-cell>
      </van-radio-group>
    </van-row>

    <van-row>
      <van-col span="16"><text class="textshow">确认个人信息</text></van-col>
    </van-row>
    <van-row>
      <van-cell-group>
        <van-field :value="customer.name" required clearable label="姓名" placeholder="请输入姓名" />
        <van-field :value="customer.mobile" required clearable label="联系电话" placeholder="请输入联系电话" />
        <van-field :value="customer.address" required clearable label="取件地址" placeholder="请输入取件地址" />
       <van-row>
         <div class="boxshow">
      客服识别用二维码（待取状态显示）
         </div>
       </van-row>
        <van-row>
          <van-col span="8" offset="8">
            <van-button @click="gotopayfor" type="primary">确认接收</van-button>
          </van-col>
        </van-row>
      </van-cell-group>
    </van-row>
  </div>

</template>
<script>
// Use Vuex
import { mapMutations } from "vuex";
export default {
  data() {
    return {
      showBox: true,
      customer: {
        radio: "1",
        type: "paynow",
        name: "",
        name: "",
        address: ""
      },
      userInfo: {}
    };
  },

  components: {},

  methods: {
    gotopayfor() {
      wx.navigateTo({
        url: "../payfor/main"
      });
    },
    onSelectRadio(e) {
      this.customer.radio = e.currentTarget.dataset.name;
    },
    onSelectType(e) {
      this.customer.type = e.currentTarget.dataset.name;
    },
    bindViewTap() {
      const url = "../logs/main";
      wx.navigateTo({
        url
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
  font-stretch: condensed;
  background-color: aqua;
  width: 100%;
  height: 200px;
}
</style>
