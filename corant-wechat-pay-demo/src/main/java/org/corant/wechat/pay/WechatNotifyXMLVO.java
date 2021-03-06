package org.corant.wechat.pay;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * corant-root <br>
 *
 * @auther sushuaihao 2020/11/27
 * @since
 */
@XmlRootElement(name = "xml")
public class WechatNotifyXMLVO {
  private String appid;
  private String mch_id;
  private String nonce_str;
  private String sign;
  private String result_code;
  private String openid;
  private String is_subscribe;
  private String trade_type;
  private String bank_type;
  private String total_fee;
  private String cash_fee;
  private String transaction_id;
  private String out_trade_no;
  private String time_end;

  public String getAppid() {
    return appid;
  }

  public String getBank_type() {
    return bank_type;
  }

  public String getCash_fee() {
    return cash_fee;
  }

  public String getIs_subscribe() {
    return is_subscribe;
  }

  public String getMch_id() {
    return mch_id;
  }

  public String getNonce_str() {
    return nonce_str;
  }

  public String getOpenid() {
    return openid;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public String getResult_code() {
    return result_code;
  }

  public String getSign() {
    return sign;
  }

  public String getTime_end() {
    return time_end;
  }

  public String getTotal_fee() {
    return total_fee;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public String getTransaction_id() {
    return transaction_id;
  }

  public WechatNotifyXMLVO setAppid(String appid) {
    this.appid = appid;
    return this;
  }

  public WechatNotifyXMLVO setBank_type(String bank_type) {
    this.bank_type = bank_type;
    return this;
  }

  public WechatNotifyXMLVO setCash_fee(String cash_fee) {
    this.cash_fee = cash_fee;
    return this;
  }

  public WechatNotifyXMLVO setIs_subscribe(String is_subscribe) {
    this.is_subscribe = is_subscribe;
    return this;
  }

  public WechatNotifyXMLVO setMch_id(String mch_id) {
    this.mch_id = mch_id;
    return this;
  }

  public WechatNotifyXMLVO setNonce_str(String nonce_str) {
    this.nonce_str = nonce_str;
    return this;
  }

  public WechatNotifyXMLVO setOpenid(String openid) {
    this.openid = openid;
    return this;
  }

  public WechatNotifyXMLVO setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
    return this;
  }

  public WechatNotifyXMLVO setResult_code(String result_code) {
    this.result_code = result_code;
    return this;
  }

  public WechatNotifyXMLVO setSign(String sign) {
    this.sign = sign;
    return this;
  }

  public WechatNotifyXMLVO setTime_end(String time_end) {
    this.time_end = time_end;
    return this;
  }

  public WechatNotifyXMLVO setTotal_fee(String total_fee) {
    this.total_fee = total_fee;
    return this;
  }

  public WechatNotifyXMLVO setTrade_type(String trade_type) {
    this.trade_type = trade_type;
    return this;
  }

  public WechatNotifyXMLVO setTransaction_id(String transaction_id) {
    this.transaction_id = transaction_id;
    return this;
  }
}
