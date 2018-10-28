import config from "@/configs/config";

function formatNumber(n) {
  const str = n.toString()
  return str[1] ? str : `0${str}`
}

export function formatTime(date) {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  const t1 = [year, month, day].map(formatNumber).join('/')
  const t2 = [hour, minute, second].map(formatNumber).join(':')

  return `${t1} ${t2}`
}
export function getopenId() {
  var obj = wx.getStorageSync("openId");
  console.log("token=" + obj);
  if (obj != null && obj != "") return;
  wx.login({
    success: function (res) {
      if (res.code) {
        var l = 'https://api.weixin.qq.com/sns/jscode2session?appid=' + config.appid + '&secret=' +
          config.appKey + '&js_code=' + res.code + '&grant_type=authorization_code';
        wx.request({
          url: l,
          data: {},
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT  
          // header: {}, // 设置请求的 header  
          success: function (res) {
            console.log(obj);
            wx.setStorageSync('openId', res.data.openid); //存储openid  
          }
        });
      } else {
        console.log('获取用户登录态失败！' + res.errMsg)
      }
    }
  });
}
export default {
  formatNumber,
  formatTime,
  getopenId
}
