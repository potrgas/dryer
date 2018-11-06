<template>
  <div class="box">
    <van-cell-group>
      <van-cell value="待取回" @click="gotodetail(1)" icon="shop" is-link >
        <view slot="title">
          <span class="van-cell-text">2018年12月13日</span>
        </view>
      </van-cell>
     <van-cell value="完成" @click="gotodetail(1)" icon="info-o" is-link>
        <view slot="title">
          <span class="van-cell-text">2018年12月14日</span>
        </view>
      </van-cell>

       <van-cell value="取消" @click="gotodetail(1)" icon="like-o" is-link>
        <view slot="title">
          <span class="van-cell-text">2018年12月15日</span>
        </view>
      </van-cell>
    </van-cell-group>
  </div>

</template>

<script>
import { post, get } from "@/utils/api";
// Use Vuex
export default {
  data() {
    return {
      list: []
    };
  },

  components: {},

  methods: {
    loadList() {},
    gotodetail(key) {
      const url = "../details/main";
      wx.navigateTo({
        url
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
}
</style>
