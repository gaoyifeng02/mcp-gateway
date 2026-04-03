package com.gaoyifeng.gateway.mcp.config;

import com.gaoyifeng.gateway.mcp.infrastructure.gateway.GenericHttpGateway;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
public class HTTPClientConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public GenericHttpGateway genericHttpGateway(OkHttpClient okHttpClient) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(GenericHttpGateway.class);
    }

}
