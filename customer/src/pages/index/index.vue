<template>
  <div class="box">
    <van-row>
      <van-col span="16"><text class="textshow">选择待烘干织物种类</text> </van-col>
    </van-row>
    <van-row>
      <van-col @click="changeType(1)" span="8"><text class="textshow">纯棉</text> {{order.dryType==1?'√':""}}
      </van-col>
      <van-col @click="changeType(2)" span="8"><text class="textshow">化纤</text>{{order.dryType==2?'√':""}} </van-col>
      <van-col @click="changeType(3)" span="8"><text class="textshow">其他</text>{{order.dryType==3?'√':""}} </van-col>
    </van-row>

    <van-row>
      <van-col span="16"><text class="textshow">确认个人信息</text></van-col>
    </van-row>
    <van-row>
      <van-cell-group>
        <van-field id="customerName" @change="inputValue" :value="order.customerName" required clearable label="姓名" placeholder="请输入姓名" />
        <van-field id="mobile" @change="inputValue" :value="order.mobile" required clearable label="电话" placeholder="电话" />
        <van-field :value="order.area" icon="question" @focus="onPop(true)" required clearable label="地区" placeholder="地区" />
        <van-field id="community" @change="inputValue" :value="order.community" required clearable label="小区"
          placeholder="小区" />
        <van-field id="address" @change="inputValue" :value="order.address" required clearable label="取件地址" placeholder="取件地址" />
        <van-radio-group :value="order.type">
          <van-cell title="剩余次数(4次)" clickable data-name="1" @click="selectTime(1,10)">
            <div>10元</div>
            <div v-if="order.isTime==1">√</div>
          </van-cell>
          <van-cell title="微信支付" clickable data-name="2" @click="selectTime(2,30)">
            <div>30元</div>
            <div v-if="order.isTime==2">√</div>
          </van-cell>
        </van-radio-group>

        <van-row>
          <van-col span="8" offset="8">
            <van-button @click="gotopayfor" type="primary">确认提交</van-button>
          </van-col>
        </van-row>
      </van-cell-group>
    </van-row>



    <van-dialog use-slot async-close :show="showBox" show-cancel-button confirm-button-open-type="getUserInfo" @close="onClose"
      @getuserinfo="auth" @getphonenumber="auth">
      <img src="../../../static/images/grunt.png" />
      是否授权获取您的信息
    </van-dialog>

    <van-popup position="bottom" :show="showpop" @close="onPop(false)">
      <van-area @confirm="selectArea" @cancel="onPop(false)" :area-list="areas" />
    </van-popup>
    <van-toast id="van-toast" />

  </div>
</template>
<script>
// Use Vuex
import { mapMutations } from "vuex";
import areaList from "../../utils/area";
import Toast from "../../../static/vant/toast/toast";
export default {
  data() {
    return {
      areas: {
        province_list: {
          "110000": "北京市",
          "120000": "天津市"
        },
        city_list: {
          "110100": "北京市",
          "110200": "县",
          "120100": "天津市",
          "120200": "县"
        },
        county_list: {
          "110101": "东城区",
          "110102": "西城区",
          "110105": "朝阳区",
          "110106": "丰台区",
          "120101": "和平区",
          "120102": "河东区",
          "120103": "河西区",
          "120104": "南开区",
          "120105": "河北区"
        }
      },
      order: {
        dryType: 1,
        isTime: null,
        customerName: "",
        area: "",
        mobile: "",
        community: "",
        address: "",
        price: 0,
        openId: "",
        payType: 1
      },
      showBox: false,
      showpop: false
    };
  },
  computed: {},
  components: {},

  methods: {
    ...mapMutations({
      set_userInfo: "set_userinfo",
      set_order: "set_order"
    }),
    inputValue(e) {
      this.order[e.mp.target.id] = e.mp.detail;
    },
    gotopayfor() {
      //todo vilidate
      console.log(this.order);
      if (
        !this.order.customerName ||
        !this.order.area ||
        !this.order.mobile ||
        !this.order.community ||
        !this.order.address ||
        !this.order.isTime
      ) {
        Toast.fail("请完善必填信息");
        return;
      }
      var obj = wx.getStorageSync("openId");
      if (!obj) {
        Toast.fail("获取用户openId失败");
        return;
      }
      this.order.openId = obj;
      this.set_order(this.order);
      wx.navigateTo({
        url: "../payfor/main"
      });
    },
    selectArea(e) {
      let temp = "";
      if (e.target.values && e.target.values.length > 0) {
        e.target.values.forEach(w => {
          temp += w.name;
        });
        this.order.area = temp;
      }
      this.showpop = false;
    },
    changeType(value) {
      this.order.dryType = value;
    },
    selectTime(e, p) {
      this.order.price = p;
      this.order.isTime = e;
    },
    onPop(e) {
      this.showpop = e;
    },
    onClose() {
      this.showBox = false;
    },
    auth(source) {
      console.log(source);
      var mo = source.target.userInfo;
      this.set_userInfo(mo);
      this.order.customerName = mo.nickName;
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
