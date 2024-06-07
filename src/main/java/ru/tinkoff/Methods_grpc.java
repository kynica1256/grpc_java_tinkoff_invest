package ru.tinkoff;



// https://tinkoff.github.io/investAPI



import ru.tinkoff.piapi.contract.v1.OperationsServiceGrpc;
import ru.tinkoff.piapi.contract.v1.PortfolioResponse;
import ru.tinkoff.piapi.contract.v1.PortfolioRequest;



import ru.tinkoff.piapi.contract.v1.UsersServiceGrpc;
import ru.tinkoff.piapi.contract.v1.GetInfoResponse;
import ru.tinkoff.piapi.contract.v1.GetInfoRequest;



import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc;
import ru.tinkoff.piapi.contract.v1.GetLastPricesResponse;
import ru.tinkoff.piapi.contract.v1.GetLastPricesRequest;


import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.GetOrderBookRequest;



import ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc;
import ru.tinkoff.piapi.contract.v1.PostOrderRequest;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;




import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.MethodDescriptor;
import io.grpc.stub.MetadataUtils;

import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import javax.net.ssl.*;
import java.io.*;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;


import javax.net.ssl.SSLException;

import java.util.*;
import java.util.UUID;




public class Methods_grpc {
    public HashMap<String, long[]> price_data = new HashMap<String, long[]>();
    public Strng[] figi_;
    public int depth = 20;
    public float money_ = 0.0f;
    public float[] bids  = new float[depth];
    public float[] asks  = new float[depth];
    public float[] bids_lot = new float[depth];
    public float[] asks_lot = new float[depth];
    private String figi;
    private long quantity;
    private int type_operation;
    private String account_id;
    private int currency_data;
    public float total_grpc_money_get;
    private ManagedChannel channel;
    private boolean ManagedChannel_init = false;
    public void for_grpc(String token) {
      this.token_ = token_;
    }
    public void for_grpc_figi_get(String[] figi) {
      this.figi_ = figi_;
    }
    public void for_grpc_order_post(String figi, int quantity, int type_operation, String account_id) {
        this.figi = figi;
        this.quantity = (long)quantity;
        this.type_operation = type_operation;
        this.account_id = account_id;
    }
    public void for_grpc_money_get(String account_id, String currency_data) {
        this.account_id = account_id;
        switch (currency_data) {
            case "RUB": this.currency_data = 0;
                break;
            case "USD": this.currency_data = 1;
                break;
            case "EUR": this.currency_data = 2;
                break;
            default:
                break;
        }
    }


    public void grpc_main(String type_, String figi_) throws SSLException {
        Metadata metadata = new Metadata();
        Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(key, "Bearer "+token_);
        if (ManagedChannel_init == false) {
            channel = NettyChannelBuilder.forAddress("invest-public-api.tinkoff.ru", 443).sslContext(GrpcSslContexts.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build()).intercept(new AuthInterceptor(token_)).build();
            ManagedChannel_init = true;
        }
        if (type_ == "check") {
            grpc_figi_get();
        } else if (type_ == "get_order") {
            grpc_order_post();
        } else if (type_ == "get_orderbook") {
            grpc_check_orderbook(figi_);
        } else if (type_ == "get_money") {
            grpc_money_get();
        }

    }
    private void grpc_figi_get() {
        MarketDataServiceGrpc.MarketDataServiceBlockingStub blockingStub = MarketDataServiceGrpc.newBlockingStub(channel);
        for (String i:figi_) {
            GetLastPricesResponse blockResponse = blockingStub.getLastPrices(GetLastPricesRequest.newBuilder().addFigi(i).build());
            var data_response = blockResponse.getLastPricesList().get(0).getPrice();
            price_data.put(i, new long[] {data_response.getUnits(), data_response.getNano()});
        }
    }
    private void grpc_order_post() {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        System.out.println(uuidAsString);
        OrdersServiceGrpc.OrdersServiceBlockingStub blockingStub = OrdersServiceGrpc.newBlockingStub(channel);
        PostOrderResponse blockResponse = blockingStub.postOrder(PostOrderRequest.newBuilder().setFigi(figi).setQuantity(quantity).setDirectionValue(type_operation).setAccountId(account_id).setOrderTypeValue(2).setOrderId(uuidAsString).build());
    }
    private void grpc_money_get() {
        OperationsServiceGrpc.OperationsServiceBlockingStub blockingStub = OperationsServiceGrpc.newBlockingStub(channel);
        PortfolioResponse blockResponse = blockingStub.getPortfolio(PortfolioRequest.newBuilder().setAccountId(account_id).setCurrencyValue(currency_data).build());
        var need_data = blockResponse.getTotalAmountCurrencies();
        Methods_calc class_ = new Methods_calc();
        class_.Methods_calc_(need_data.getUnits(), need_data.getNano(), "");
        total_grpc_money_get = class_.data_;
    }
    private void grpc_check_orderbook(String figi) {
        MarketDataServiceGrpc.MarketDataServiceBlockingStub blockingStub = MarketDataServiceGrpc.newBlockingStub(channel);
        GetOrderBookResponse blockResponse = blockingStub.getOrderBook(GetOrderBookRequest.newBuilder().setFigi(figi).setDepth(depth).setInstrumentId("").build());
        Methods_calc class_;
        for(int i = 0; i < depth; i++) {
            var lot_bids = blockResponse.getBids(i).getQuantity();
            bids_lot[i] = (float)lot_bids;

            var data_price_bids = blockResponse.getBids(i).getPrice();
            long units_bids = data_price_bids.getUnits();
            long nano_bids = data_price_bids.getNano();
            class_ = new Methods_calc();
            class_.Methods_calc_(units_bids, nano_bids, figi);
            bids[i] = class_.data_;


            var lot_asks = blockResponse.getAsks(i).getQuantity();
            asks_lot[i] = (float)lot_asks;

            var data_price_asks = blockResponse.getAsks(i).getPrice();
            long units_asks = data_price_asks.getUnits();
            long nano_asks = data_price_asks.getNano();
            class_ = new Methods_calc();
            class_.Methods_calc_(units_asks, nano_asks, figi);
            asks[i] = class_.data_;
        }
    }
    public void close_channel_grpc() {
        channel.shutdown();
    }
}





class AuthTokenProvideInterceptor implements ClientInterceptor {

    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(final MethodDescriptor<ReqT, RespT> methodDescriptor, final CallOptions callOptions, final Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(final Listener<RespT> responseListener, final Metadata headers) {
                headers.put(Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer "+token_);
                super.start(responseListener, headers);
            }
        };
    }
}


class AuthInterceptor implements ClientInterceptor {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final Metadata.Key<String> AUTH_HEADER = Metadata.Key.of(AUTH_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER);

    private final String authHeaderValue;

    public AuthInterceptor(String token) {
        this.authHeaderValue = "Bearer " + token;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        var call = channel.newCall(methodDescriptor, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(AUTH_HEADER, authHeaderValue);
                super.start(responseListener, headers);
            }
        };
    }
}
