package com.monkey.application.Payfor;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.monkey.application.Device.IDeviceService;
import com.monkey.common.base.Constant;
import com.monkey.common.util.DateUtil;
import com.monkey.common.wechatsdk.HttpUtil;
import com.monkey.common.wechatsdk.PayConfig;
import com.monkey.common.wechatsdk.PayToolUtil;
import com.monkey.common.wechatsdk.XMLUtil4jdom;
import com.monkey.core.dtos.DeviceSaleStatical;
import com.monkey.core.dtos.ProductSaleStatical;
import com.monkey.core.dtos.SalePercentDto;
import com.monkey.core.dtos.TodayStatical;
import com.monkey.core.entity.*;
import com.monkey.core.mapper.OrderRepository;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.monkey.core.mapper.PayforRepository;
import com.monkey.core.mapper.ProductRepository;
import com.monkey.web.aspect.WebSocketServer;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.StaticalInput;
import com.monkey.web.controller.dtos.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhaohejing
 * @since 2018-08-13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderRepository, Order> implements IOrderService {
    //创建订单
    @Autowired
    OrderRepository _orderRepository;
    @Autowired
    IDeviceService _deviceService;
    @Autowired
    ProductRepository _productRepository;
    @Autowired
    PayforRepository _payforRepository;
    @Autowired
    ISerialService _serialService;
    protected static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    /**
     * @Description: 发起微信支付
     * @param input 订单
     */
    public SortedMap<String, Object> wxPay(Order input){
        try{
            String currTime = PayToolUtil.getCurrTime();
            String strTime = currTime.substring(8, currTime.length());
            String strRandom = PayToolUtil.buildRandom(4) + "";
            String nonce_str = strTime + strRandom;
            //商品名称
            String body = input.getCustomerName();

            // 获取发起电脑 ip
            String spbill_create_ip = PayConfig.Create_Ip;
            // 回调接口
            //组装参数，用户生成统一下单接口的签名
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            packageParams.put("appid", PayConfig.APPID);
            packageParams.put("mch_id", PayConfig.SHOPID);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no",input.getId());//商户订单号
            packageParams.put("total_fee", input.getPrice().toString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url",PayConfig.Notify_Url);//支付成功后的回调地址
            packageParams.put("trade_type", "JSAPI");//支付方式
            packageParams.put("openid", input.getOpenId());

            String sign = PayToolUtil.createSign("UTF-8", packageParams, PayConfig.PAYFOR);

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + PayConfig.APPID + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + PayConfig.SHOPID + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + PayConfig.Notify_Url + "</notify_url>"
                    + "<openid>" + input.getOpenId() + "</openid>"
                    + "<out_trade_no>" + input.getId() + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + input.getPrice() + "</total_fee>"
                    + "<trade_type>JSAPI</trade_type>"
                    + "<sign>" + sign + "</sign>"
                    + "</xml>";

            System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);

            //调用统一下单接口，并接受返回的结果
            String result =HttpUtil.postData(PayConfig.AliNotify_url,xml);
            //PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);
            System.out.println("调试模式_统一下单接口 返回XML数据：" + result);
            // 将解析结果存储在HashMap中
            Map map = XMLUtil4jdom.doXMLParse(result);
            logger.warn(JSON.toJSONString(map));

            String return_code = (String) map.get("return_code");//返回状态码

            SortedMap<String, Object> response = new TreeMap<String, Object>();
            if(return_code.equals("SUCCESS")){
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + PayConfig.APPID + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayToolUtil.createSign("UTF-8", packageParams, PayConfig.PAYFOR);
                response.put("paySign", paySign);
            }
            response.put("appid", PayConfig.APPID);
            return response;

        }catch(Exception e){
           logger.error(e.getMessage());
        }
        return null;
    }
    /*
     * 创建订单*/
    @Override
    public Order insertOrder(OrderInput input) throws Exception {
        Order o = new Order();
        o.setPayType(input.payType);
        //  o.setDeviceId(d.getId());
        o.setOrderState(0);
        o.setPayState(0);
        o.setAddress(input.address);
        o.setArea(input.area);
        o.setCommunity(input.community);
        o.setCustomerName(input.customerName);
        o.setDryType(input.dryType);
        o.setIsTime(input.isTime);
        o.setMobile(input.mobile);
        o.setOpenId(input.openId);
        o.setPrice(input.payType == 1 ? input.price : input.price / 100);
        _orderRepository.insert(o);
        return o;
    }
    /*微信支付*/
    @Override
    public String weixinPay(Order input) throws Exception {
        EntityWrapper ew = new EntityWrapper();
        List<Payfor> list = _payforRepository.selectList(ew);
        Payfor p = list.get(0);
        if (list.isEmpty() || p == null) throw new Exception("该商户支付信息不存在");
        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）
        // 账号信息
        String appid = p.getWechatpayId();  // appid
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
        String mch_id = p.getWechatpayAgent(); // 商业号
        String key = p.getWechatpayKey(); // key

        String currTime = PayToolUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayToolUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;

        // 获取发起电脑 ip
        String spbill_create_ip = PayConfig.Create_Ip;
        // 回调接口
        String notify_url = PayConfig.Notify_Url;
        String trade_type = "NATIVE";

        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", input.getDryType());  //（调整为自己的名称）
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", input.getPrice().toString()); //价格的单位为分
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        String sign = PayToolUtil.createSign("UTF-8", packageParams, key);
        packageParams.put("sign", sign);

        String requestXML = PayToolUtil.getRequestXml(packageParams);
        System.out.println(requestXML);

        String resXml = HttpUtil.postData(PayConfig.Ufdoder_Url, requestXML);

        Map map = XMLUtil4jdom.doXMLParse(resXml);
        logger.warn(JSON.toJSONString(map));
        String urlCode = (String) map.get("code_url");

        return urlCode;
    }

    /*
     * 阿里云支付*/
    @Override
    public String aliPay(Order product) throws Exception {
        EntityWrapper ew = new EntityWrapper();
        List<Payfor> list = _payforRepository.selectList(ew);
        Payfor p = list.get(0);
        if (list.isEmpty() || p == null) throw new Exception("该商户支付信息不存在");
        String out_trade_no = product.getId(); //订单号 （调整为自己的生产逻辑）

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", p.getAlipayId(), p.getAlipayKey(),
                "json", "UTF-8", p.getAlipayAgent(), "RSA2"); //获得初始化的AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        Map<String, String> r = new HashMap<>();
        r.put("out_trade_no", product.getId());
        r.put("total_amount", product.getPrice() + "");
        r.put("store_id", "");
        r.put("timeout_express", "120m");
        String w = JSON.toJSONString(r);
        request.setNotifyUrl(PayConfig.AliNotify_url);
        request.setBizContent(w);
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
        //根据response中的结果继续业务逻辑处理
        return response.getQrCode();
    }

    /*
     * 阿里云退款*/
    @Override
    public String aliback(Order input) throws Exception {
        EntityWrapper ew = new EntityWrapper();
        List<Payfor> list = _payforRepository.selectList(ew);
        Payfor p = list.get(0);
        if (list.isEmpty() || p == null) throw new Exception("该商户支付信息不存在");
        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", p.getAlipayId(), p.getAlipayKey(),
                "json", "UTF-8", p.getAlipayAgent(), "RSA2");
        //获得初始化的AlipayClient
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
        Map<String, String> r = new HashMap<>();
        r.put("out_trade_no", input.getId());
        r.put("out_request_no", System.currentTimeMillis() + "");
        r.put("refund_amount", input.getPrice() + "");
        String w = JSON.toJSONString(r);
        request.setBizContent(w); //设置业务参数
        //   request.setNotifyUrl(PayConfig.Alibackurl);
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        //通过alipayClient调用API，获得对应的response类
        System.out.print(response.getBody());
        String to = response.getRefundFee();
        if (!to.isEmpty()) {
            _orderRepository.updateOrderState(input.getId(), null, 2, response.getOutTradeNo());
            insertSerial(input);
        }
        return to;
    }

    ///插入流水表
    private void insertSerial(Order order) {
        Serial s = new Serial();
        s.setDeviceId(order.getDeviceId());
        s.setOrder(order.getId());
        s.setPrice(order.getPrice());
        s.setType(1);
        s.setPrice(order.getPrice());
        s.setBackOrder("");
        _serialService.insert(s);
    }

    @Override
    public Page<DeviceSaleStatical> getDeviceSaleStatical(Page<DeviceSaleStatical> page, StaticalInput input) {
        List<DeviceSaleStatical> t = _orderRepository.getDeviceSaleStatical(input.getTenantId(), page, input.deviceName, input.pointName, input.start, input.end);
        return page.setRecords(t);
    }

    @Override
    public Page<ProductSaleStatical> getProductSaleStatical(Page<ProductSaleStatical> page, StaticalInput input) {
        List<ProductSaleStatical> t = _orderRepository.getProductSaleStatical(input.getTenantId(), page, input.productName, input.deviceName, input.start, input.end);
        return page.setRecords(t);
    }

    @Override
    public Map<String, Object> getDashboard(Integer tenantId) {
        Map<String, Object> result = new HashMap<>();
        List<SalePercentDto> dr = _orderRepository.getTodaySalePercent(tenantId, DateUtil.getStartTime(), DateUtil.getEndTime());
        List<SalePercentDto> mr = _orderRepository.getMonthSalePercent(tenantId, DateUtil.getBeginDayOfMonth(), DateUtil.getEndDayOfMonth());
        List<SalePercentDto> pr = _orderRepository.getPayTypePercent(tenantId, DateUtil.getBeginDayOfMonth(), DateUtil.getEndDayOfMonth());
        List<SalePercentDto> topdr = _orderRepository.getPointSalePercent(tenantId, DateUtil.getStartTime(), DateUtil.getEndTime());
        List<SalePercentDto> topmr = _orderRepository.getPointSalePercent(tenantId, DateUtil.getBeginDayOfMonth(), DateUtil.getEndDayOfMonth());
        result.put("todaySale", getDayKeysAndValues(dr));
        result.put("monthSale", getMonthKeysAndValues(mr));
        result.put("payType", pr);
        result.put("todayTop", getKeysAndValues(topdr));
        result.put("monthTop", getKeysAndValues(topmr));
        return result;
    }

    @Override
    public Map<String, Object> getStaticial(Integer tenantId, Date start, Date end) {
        Map<String, Object> result = new HashMap<>();
        TodayStatical tr = _orderRepository.getOrderStatical(tenantId, DateUtil.getStartTime(), DateUtil.getEndTime());
        TodayStatical mr = _orderRepository.getOrderStatical(tenantId, DateUtil.getStartTime(start), DateUtil.getEndTime(end));
        Integer count = WebSocketServer.getOnlineCount();
        tr.setDevice(count);
        result.put("today", tr);
        result.put("month", mr);
        return result;
    }

    @Override
    public Payfor getPayforByOrder(String appId, String mch_id) {
        return _orderRepository.getPayforByOrder(appId, mch_id);
    }

    @Override
    public Payfor getPayforByAppId(String appId) {
        return _orderRepository.getPayforByAppId(appId);
    }

    @Override
    public void updateOrderStatte(String orderNum, Integer orderState, Integer payState, String backNum) {
        _orderRepository.updateOrderState(orderNum, orderState, payState, backNum);
    }

    /*
     * 微信退款功能
     * */
    @Override
    public String weixinBack(Order input) throws Exception {
        EntityWrapper ew = new EntityWrapper();
        List<Payfor> list = _payforRepository.selectList(ew);
        Payfor p = list.get(0);
        if (list.isEmpty() || p == null) throw new Exception("该商户支付信息不存在");
        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）
        // 账号信息
        String appid = p.getWechatpayId();  // appid
        String mch_id = p.getWechatpayAgent(); // 商业号
        String key = p.getWechatpayKey(); // key
        String currTime = PayToolUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayToolUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        // 回调接口
        String notify_url = PayConfig.Back_Url;
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("out_refund_no", input.getDeviceId() + "" + System.currentTimeMillis());
        //    packageParams.put("refund_desc", 111);  //（调整为自己的名称）
        packageParams.put("total_fee", input.getPrice().toString()); //价格的单位为分
        packageParams.put("refund_fee", input.getPrice().toString());
        packageParams.put("notify_url", notify_url);
        String sign = PayToolUtil.createSign("UTF-8", packageParams, key);
        packageParams.put("sign", sign);

        String requestXML = PayToolUtil.getRequestXml(packageParams);
        String resXml = HttpUtil.back(requestXML, p);
        return resXml;
    }

    private Map<String, Object> getDayKeysAndValues(List<SalePercentDto> list) {
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String t = "";
            if (i <= 9) {
                t = "0" + i;
            } else {
                t = "" + i;
            }
            keys.add(t);
            Integer v = 0;
            for (int j = 0; j < list.size(); j++) {
                SalePercentDto temp = list.get(j);
                if (temp.getTime().equals(t)) {
                    v = temp.getCount();
                }
            }
            values.add(v);
        }
        Map<String, Object> r = new HashMap<>();
        r.put("keys", keys);
        r.put("values", values);
        return r;
    }


    private Map<String, Object> getKeysAndValues(List<SalePercentDto> list) {
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            SalePercentDto temp = list.get(j);
            keys.add(temp.getTime());
            values.add(temp.getCount());
        }
        Map<String, Object> r = new HashMap<>();
        r.put("keys", keys);
        r.put("values", values);
        return r;
    }

    private Map<String, Object> getMonthKeysAndValues(List<SalePercentDto> list) {
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            String t = "";
            if (i <= 9) {
                t = "0" + i;
            } else {
                t = "" + i;
            }
            keys.add(t);
            Integer v = 0;
            for (int j = 0; j < list.size(); j++) {
                SalePercentDto temp = list.get(j);
                if (temp.getTime().equals(t)) {
                    v = temp.getCount();
                }
            }
            values.add(v);
        }
        Map<String, Object> r = new HashMap<>();
        r.put("keys", keys);
        r.put("values", values);
        return r;
    }
}
