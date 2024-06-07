package ru.tinkoff;
import ru.tinkoff.Dev_methods;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.SSLException;

class Users_Methods {
  private String[] figi;
  private String token;
  private void give_data() {
    Dev_data class_ = new Dev_data();
    figi = class_.figi;
    token = class_.token;
  }
  public float get_money(String accountid, String ticker_money) {
    give_data();
    Methods_grpc class_ = new Methods_grpc();
    class_.for_grpc(token);
    class_.for_grpc_money_get(accountid, ticker_money);
    class_.grpc_main("get_money", "");
    float money = class_.total_grpc_money_get;
    return money;
  }
  public void buy_method(String accountid, String ticker_money, String figi) {
    give_data();
    try {
      Methods_grpc class_ = new Methods_grpc();
      class_.for_grpc(token);
      class_.for_grpc_money_get(accountid, ticker_money);
      class_.grpc_main("get_money", "");
      float money = class_.total_grpc_money_get;
      float qu_money_ = money/buy_price;
      qu_money = (int)qu_money_;
      class_.for_grpc_order_post(figi,qu_money,1,accountid);
      class_.grpc_main("get_order", "");
      class_.close_channel_grpc();
    } catch (SSLException e) {
      System.out.println("false");
    }
  }
  public void sale_method(String accountid, String ticker_money, String figi) {
    give_data();
    try {
        Methods_grpc class_ = new Methods_grpc();
        class_.for_grpc(token);
        class_.for_grpc_order_post(figi,qu_money,2,accountid);
        class_.grpc_main("get_order", "");
        class_.close_channel_grpc();
      } catch (SSLException e) {
        System.out.println("false");
      }
    }
}
