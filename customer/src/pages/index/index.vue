<template>
  <div class="box">
    <van-row>
      <van-col span="16"><text class="textshow">选择待烘干织物种类</text> </van-col>
    </van-row>
    <van-row>
      <van-radio-group :value="customer.radio">
        <van-cell-group>
          <van-cell title="纯棉" clickable data-name="1" @click="onSelectRadio">
            <van-radio name="1" />
          </van-cell>
          <van-cell title="化纤" clickable data-name="2" @click="onSelectRadio">
            <van-radio name="2" />
          </van-cell>
          <van-cell title="其他" clickable data-name="3" @click="onSelectRadio">
            <van-radio name="3" />
          </van-cell>
        </van-cell-group>
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
        <van-radio-group :value="customer.type">
          <van-cell title="剩余次数(4次)" clickable data-name="times" @click="onSelectType">
            <van-radio name="times" />
          </van-cell>
          <van-cell title="微信支付" clickable data-name="paynow" @click="onSelectType">
            <van-radio name="paynow" />
          </van-cell>
        </van-radio-group>
        <van-row>
          <van-col span="8" offset="8">
            <van-button @click="gotopayfor" type="primary">确认提交</van-button>
          </van-col>
        </van-row>

      </van-cell-group>
    </van-row>



    <van-dialog use-slot async-close :show="showBox" show-cancel-button confirm-button-open-type="getUserInfo"
      @close="onClose" @getuserinfo="auth"  @getphonenumber="auth">
      <img src="../../../static/images/grunt.png"/>
      是否授权获取您的信息
    </van-dialog>
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
    ...mapMutations({
      set_userInfo: "set_userinfo"
    }),
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
    onClose() {
      this.showBox = false;
    },
    auth(source) {
      console.log(source);
      var mo = source.target.userInfo;
      this.set_userInfo(mo);
      this.customer.name = mo.nickName;
      this.customer.mobile = "1181651";
      this.customer.address = mo.nickName;
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

.textshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
}
</style>
