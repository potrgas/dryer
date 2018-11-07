<template>
  <div class="box">
    <div class="scan"></div>
    <van-row>
      <van-col span="12" offset="8">
        <van-button @click="scan" type="primary">开始扫码</van-button>
      </van-col>
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
      userInfo: {},
      account: {}
    };
  },

  components: {},

  methods: {
    scan() {
      // 只允许从相机扫码
      wx.scanCode({
        onlyFromCamera: true,
        success(res) {
          if (res && res.result) {
            this.setOrder(res.result);
          }
        }
      });
    },
    ...mapMutations({
      setOrder: "setOrder"
    })
  },
  created() {}
};
</script>

<style scoped>
.box {
  padding: 2%;
}

.scan {
  width: 80%;
  height: 300px;
  background-color: aqua;
  margin: auto auto;
}

.textshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
}
</style>
