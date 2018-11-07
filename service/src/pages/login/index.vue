<template>
  <div class="box">
    <van-field id="userName" @change="inputValue" :value="model.userName" label="用户名" placeholder="请输入用户名" />
    <van-field id="password" @change="inputValue" :value="model.password" type="password" label="密码" :border="false" placeholder="请输入密码" />
    <van-button @click="login" type="primary">登陆</van-button>
  </div>

</template>

<script>
// Use Vuex
import { mapMutations } from "vuex";
import { get, post } from "@/utils/api";
export default {
  data() {
    return {
      model: {
        userName: "",
        password: ""
      }
    };
  },

  components: {},

  methods: {
    inputValue(e) {
      this.model[e.mp.target.id] = e.mp.detail;
    },
    login() {
      var params = { url: "api/chat/login", data: this.model };
      post(params).then(r => {
        console.log(r);
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
