<template>
  <div >

    <div class="userinfo" @click="bindViewTap">
      <img class="userinfo-avatar" v-if="operater.avatarUrl" :src="operater.avatarUrl"
       background-size="cover" />
      <div class="userinfo-nickname">
         {{operater.account}}
      </div>
    </div>
    <van-cell-group>
      <van-cell :value="operater.orderCount+'条'"  is-link url="../myorders/main"  icon="location" title="我的订单" border="false">
        <van-icon slot="right-icon" name="search" class="van-cell__right-icon" />
      </van-cell>
    </van-cell-group>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      motto: "Hello World"
    };
  },
  computed: {
    ...mapGetters(["operater"])
  },
  components: {},

  methods: {},
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
