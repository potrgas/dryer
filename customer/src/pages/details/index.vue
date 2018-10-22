<template>
  <div class="box">
    <van-row>
      <van-col span="16"><text class="textshow">选择待烘干织物种类</text> </van-col>
    </van-row>
    <van-row>
      <van-radio-group :value="radio" @change="onChange">
        <van-cell-group>
          <van-cell title="纯棉" clickable data-name="1" @click="onClick">
            <van-radio name="1" />
          </van-cell>
          <van-cell title="化纤" clickable data-name="2" @click="onClick">
            <van-radio name="2" />
          </van-cell>
          <van-cell title="其他" clickable data-name="3" @click="onClick">
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
        <van-field :value="customer.name" required clearable label="姓名"  placeholder="请输入姓名" />
        <van-field :value="customer.mobile" required clearable label="联系电话"  placeholder="请输入联系电话" />
        <van-field :value="customer.address" required clearable label="取件地址"  placeholder="请输入取件地址" />
        <van-cell title="剩余次数(4次)" clickable data-name="2" @click="onClick">
          <van-radio name="2" />
        </van-cell>
        <van-cell title="微信支付" clickable data-name="3" @click="onClick">
          <van-radio name="3" />
        </van-cell>
        <van-row>
          <van-col span="8" offset="8">
            <van-button type="primary">确认提交</van-button>
          </van-col>
        </van-row>

      </van-cell-group>
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
.textshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
}
</style>
