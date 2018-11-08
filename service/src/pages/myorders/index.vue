<template>
  <div class="box">
    <van-cell-group>
      <van-cell v-for="item in list" :key="item.id" :value="item.orderState==0?'待取':(item.orderState==1?'待烘干':(item.orderState==3?'待取回':'完成'))"
       @click="gotodetail(item)" :icon="item.dryType==1?'shop':(item.dryType==2?'info-o':'like-o')" is-link >
        <view slot="title">
          <span class="van-cell-text">{{item.creationTime}}</span>
        </view>
      </van-cell>
    </van-cell-group>
  </div>

</template>

<script>
import { post, get } from "@/utils/api";
import { mapMutations, mapGetters } from "vuex";
// Use Vuex
export default {
  data() {
    return {
      total: 0,
      params: {
        index: 1,
        size: 10,
        where: {}
      },
      list: []
    };
  },
  computed: {
    ...mapGetters(["operater"])
  },
  components: {},

  methods: {
    ...mapMutations({
      set_currentOrder: "set_currentOrder"
    }),
    loadList() {
      this.params.where = { operaterId: this.operater.id };
      let p = { url: "api/chat/orders", data: this.params };
      post(p).then(r => {
        if (r.result == "00000000") {
          this.list = r.data.records;
          this.total = r.data.total;
        }
      });
    },
    gotodetail(item) {
      this.set_currentOrder(item);
      const url = "../details/main";
      wx.navigateTo({
        url
      });
    }
  },
  mounted() {
    this.loadList();
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
