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
      
        <van-row>
          <van-col v-if="currentOrder.orderState==0" span="8" offset="8">
            <van-button @click="got" type="primary">确认接收</van-button>
          </van-col>
        </van-row>
      </van-cell-group>
    </van-row>
  </div>

</template>
<script>
// Use Vuex
import { mapGetters } from "vuex";
import { get, post, put } from "@/utils/api";
export default {
  data() {
    return {
      currentOrder: {}
    };
  },

  components: {},
  computed: {
    ...mapGetters(["order"])
  },
  methods: {
    //接受货物 更改状态 返回首页
    got() {
      if (this.currentOrder.id) {
        let params = { url: "api/chat/order/" + this.order + "/1" };
        put(params).then(r => {
          if (r.result == "00000000") {
            wx.switchTab({
              url: "../index/main"
            });
          }
        });
      }
    }
  },
  mounted() {
    if (this.order) {
      let params = { url: "api/chat/order/" + this.order };
      get(params).then(r => {
        if (r.result == "00000000") {
          this.currentOrder = r.data;
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

.boxshow {
  font-size: 12;
  margin-bottom: 2;
  font-stretch: condensed;
  background-color: aqua;
  width: 100%;
  height: 200px;
}
</style>
