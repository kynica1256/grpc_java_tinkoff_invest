
package ru.tinkoff;

import javax.net.ssl.SSLException;

import java.util.*;

import java.util.Map.*;

import java.util.concurrent.TimeUnit;

import ru.tinkoff.Dev_methods;


public class App {
    private String figi;
    public static long[] run_method() {
        try {
            Methods_grpc method_ = new Methods_grpc();
            Dev_data class_ = new Dev_data();
            String[] figi_ = class_.figi;
            figi = class_.mini_figi;
            String token = class_.token;
            method_.for_grpc(token);
            method_.for_grpc_figi_get(figi_);
            method_.grpc_main("check", "0");
            method_.close_channel_grpc();
            var map_obj = method_.price_data;
            var data_ = method_.price_data.get(figi);
            return data_;
        } catch (SSLException e) {
           System.out.println("false");
           long[] data_ = new long[2];
           return data_;
        }
    }
    public static float[][] check_order(String figi) {
        try {
            Methods_grpc method_ = new Methods_grpc();
            method_.grpc_main("get_orderbook", figi);
            method_.close_channel_grpc();
            float[][] res = {method_.bids, method_.bids_lot, method_.asks, method_.asks_lot };
            return res;
        } catch (SSLException e) {
            System.out.println("false");
            return new float[2][5];
        }
    }
    public static void parse_data_figi() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        Methods_algorithm obj_ = new Methods_algorithm();
        var price = run_method(figi);
        TimeUnit.SECONDS.sleep(2);
        var order_data = check_order(figi);
        Methods_calc obj_calc = new Methods_calc();
        obj_calc.Methods_calc_(price[0], price[1], figi);
        float price_ = obj_calc.data_;
        var bids_ = order_data[0];
        var bids_lot = order_data[1];
        var asks_ = order_data[2];
        var asks_lot = order_data[3];
        new Dev_logic().run_method(new float[][]{bids_, bids_lot, asks_, asks_lot}, price_);

    }
    public static void main(String[] args) {
        do {
            try {
                parse_data_figi();
            } catch (InterruptedException e) {
                System.out.println("false");
            }
        } while (true);
    }
}
