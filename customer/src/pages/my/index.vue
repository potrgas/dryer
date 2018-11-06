<template>
  <div  >
    <div class="userinfo" @click="bindViewTap">
      <img class="userinfo-avatar" v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl"
       background-size="cover" />
      <div class="userinfo-nickname">
         {{userInfo.nickName}}
      </div>
    </div>
    <van-cell-group>
      <van-cell :value="balance+'次'" icon="shop" url="../buy/main" is-link >
        <view slot="title">
          <span class="van-cell-text">剩余次数</span>
        </view>
      </van-cell>
      <van-cell :value="order+'条'"  is-link url="../myorders/main"  icon="location" title="我的订单" border="false">
        <van-icon slot="right-icon" name="search" class="van-cell__right-icon" />
      </van-cell>
    </van-cell-group>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { post, get } from "@/utils/api";
export default {
  data() {
    return {
      order: 0,
      balance: 0
    };
  },
  computed: {
    ...mapGetters(["userInfo"])
  },
  components: {},
  methods: {
    getLessMore() {
      var obj = wx.getStorageSync("openId");
      let param = { url: "api/chat/info/" + obj };
      get(param).then(r => {
        if (r.result == "00000000") {
          this.order = r.data.order;
          this.balance = r.data.balance;
        }
      });
    }
  },
  mounted() {
    this.getLessMore();
  },
  created() {}
};
</script>

<style scoped>
.userinfo {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userinfo-avatar {
  width: 128rpx;
  height: 128rpx;
  margin: 20rpx;
  border-radius: 50%;
}

.userinfo-nickname {
  color: #aaa;
}

.usermotto {
  margin-top: 150px;
}

.form-control {
  display: block;
  padding: 0 12px;
  margin-bottom: 5px;
  border: 1px solid #ccc;
}

.counter {
  display: inline-block;
  margin: 10px auto;
  padding: 5px 10px;
  color: blue;
  border: 1px solid blue;
}
</style>
