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
import com.monkey.core.mapper.ChargeorderRepository;
import com.monkey.core.mapper.OrderRepository;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.monkey.core.mapper.ProductRepository;
import com.monkey.web.aspect.WebSocketServer;
import com.monkey.web.controller.dtos.ChargeOrderInput;
import com.monkey.web.controller.dtos.OrderInput;
import com.monkey.web.controller.dtos.StaticalInput;
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
    ISerialService _serialService;
    protected static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);



    /*
     * 创建订单*/
    @Override
    public Order insertOrder(OrderInput input) throws Exception {
        Order o = new Order();
        o.setPayType(input.payType);
        o.setOrderState(0);
        o.setPayState(0);
        o.setDryType(input.dryType);
        o.setDeviceNum(input.deviceNum);
        o.setPrice(input.payType == 1 ? input.price : input.price / 100);
        _orderRepository.insert(o);
        return o;
    }

    /*微信支付*/
    @Override
    public String weixinPayQrCode(Order input) throws Exception {

        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）
        // 账号信息
        String appid = PayConfig.WX_APPID ;// appid
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
        String mch_id = PayConfig.WX_SHOPID; // 商业号
        String key = PayConfig.WX_SECRIT; // key

        String currTime = PayToolUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayToolUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;

        // 获取发起电脑 ip
        String spbill_create_ip = PayConfig.Create_Ip;
        // 回调接口
        String notify_url = PayConfig.WX_ORDER_NOTIFY_URL;
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

        String resXml = HttpUtil.postData(PayConfig.WX_PAYURL, requestXML);

        Map map = XMLUtil4jdom.doXMLParse(resXml);
        logger.warn(JSON.toJSONString(map));
        String urlCode = (String) map.get("code_url");

        return urlCode;
    }

    /*
     * 阿里云支付*/
    @Override
    public String aliPay(Order product) throws Exception {
        String out_trade_no = product.getId(); //订单号 （调整为自己的生产逻辑）
    //todo 配置阿里支付的参数
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", PayConfig.AL_APPID, PayConfig.AL_KEY,
                "json", "UTF-8", PayConfig.AL_AGENT, "RSA2"); //获得初始化的AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        Map<String, String> r = new HashMap<>();
        r.put("out_trade_no", product.getId());
        r.put("total_amount", product.getPrice() + "");
        r.put("store_id", "");
        r.put("timeout_express", "120m");
        String w = JSON.toJSONString(r);
        request.setNotifyUrl(PayConfig.AL_ORDER_NOTIFY_URL);
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

        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", PayConfig.AL_APPID, PayConfig.AL_KEY,
                "json", "UTF-8", PayConfig.AL_AGENT, "RSA2");
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
        s.setDeviceNum(order.getDeviceNum());
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
    public void updateOrderState(String orderNum, Integer orderState, Integer payState, String backNum) {
        _orderRepository.updateOrderState(orderNum, orderState, payState, backNum);
    }

    /*
     * 微信退款功能
     * */
    @Override
    public String weixinBack(Order input) throws Exception {
        String out_trade_no = input.getId(); //订单号 （调整为自己的生产逻辑）
        // 账号信息
        String appid =PayConfig.WX_APPID;  // appid
        String mch_id = PayConfig.WX_SHOPID; // 商业号
        String key = PayConfig.WX_PAYFOR; // key
        String currTime = PayToolUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayToolUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        // 回调接口
        String notify_url = PayConfig.WX_BACK_NOTIFY_URL;
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("out_refund_no", input.getDeviceNum() + "" + System.currentTimeMillis());
        //    packageParams.put("refund_desc", 111);  //（调整为自己的名称）
        packageParams.put("total_fee", input.getPrice().toString()); //价格的单位为分
        packageParams.put("refund_fee", input.getPrice().toString());
        packageParams.put("notify_url", notify_url);
        String sign = PayToolUtil.createSign("UTF-8", packageParams, key);
        packageParams.put("sign", sign);

        String requestXML = PayToolUtil.getRequestXml(packageParams);
        String resXml = HttpUtil.back(requestXML);
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
