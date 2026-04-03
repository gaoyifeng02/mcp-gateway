# Retrofit + OkHttp 使用指南

## 目录
- [简介](#简介)
- [Maven 依赖配置](#maven-依赖配置)
- [基础配置](#基础配置)
- [接口定义](#接口定义)
- [调用方法](#调用方法)
- [高级特性](#高级特性)
- [最佳实践](#最佳实践)

---

## 简介

Retrofit + OkHttp 是 Square 公司开发的类型安全的 HTTP 客户端组合：
- **OkHttp**: 高效的 HTTP 客户端，支持连接池、缓存、拦截器等
- **Retrofit**: 基于 OkHttp 的 RESTful API 封装框架，提供类型安全的接口定义

### 核心优势

✅ 类型安全 - 编译时检查，减少运行时错误
✅ 注解驱动 - 简洁的 API 定义
✅ 自动序列化 - 支持 Gson、Jackson 等多种格式
✅ 异步支持 - 原生异步调用
✅ 拦截器 - 灵活的请求/响应处理
✅ 连接池管理 - 高效的连接复用

---

## Maven 依赖配置

### 1. 父 POM 中管理版本（推荐）

在父 `pom.xml` 的 `<dependencyManagement>` 中添加：

```xml
<properties>
    <!-- 版本管理 -->
    <retrofit.version>2.9.0</retrofit.version>
    <okhttp.version>4.9.3</okhttp.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Retrofit 核心库 -->
        <dependency>
            <groupId>com.squareup.retrofit2</groupId>
            <artifactId>retrofit</artifactId>
            <version>${retrofit.version}</version>
        </dependency>

        <!-- OkHttp 客户端 -->
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>${okhttp.version}</version>
        </dependency>

        <!-- Gson 转换器 -->
        <dependency>
            <groupId>com.squareup.retrofit2</groupId>
            <artifactId>converter-gson</artifactId>
            <version>${retrofit.version}</version>
        </dependency>

        <!-- 可选：日志拦截器 -->
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>logging-interceptor</artifactId>
            <version>${okhttp.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. 子模块中引入依赖

在需要使用的模块（如 `mcp-gateway-infrastructure`）的 `pom.xml` 中添加：

```xml
<dependencies>
    <!-- Retrofit 核心库 -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>retrofit</artifactId>
    </dependency>

    <!-- OkHttp 客户端 -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
    </dependency>

    <!-- Gson 转换器（自动 JSON 序列化） -->
    <dependency>
        <groupId>com.squareup.retrofit2</groupId>
        <artifactId>converter-gson</artifactId>
    </dependency>

    <!-- 日志拦截器（开发调试用） -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>logging-interceptor</artifactId>
    </dependency>
</dependencies>
```

### 3. 可选转换器

根据项目需求选择转换器：

| 转换器 | Artifact ID | 用途 |
|--------|-------------|------|
| Gson | converter-gson | JSON 序列化（推荐） |
| Jackson | converter-jackson | JSON 序列化 |
| Moshi | converter-moshi | JSON 序列化（轻量级） |
| Protobuf | converter-protobuf | Protocol Buffers |
| SimpleXML | converter-simplexml | XML 序列化 |
| Scalars | converter-scalars | String、Integer 等基本类型 |

---

## 基础配置

### 1. Spring Boot 配置类

```java
package com.gaoyifeng.gateway.mcp.config;

import com.gaoyifeng.gateway.mcp.infrastructure.gateway.GenericHttpGateway;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class HTTPClientConfig {

    /**
     * 配置 OkHttpClient 实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 连接池配置：最多保持 10 个空闲连接，每个连接存活 5 分钟
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))

                // 连接失败时自动重试
                .retryOnConnectionFailure(true)

                // 连接超时：100 秒
                .connectTimeout(100, TimeUnit.SECONDS)

                // 读取超时：300 秒（适用于大文件下载）
                .readTimeout(300, TimeUnit.SECONDS)

                // 写入超时：300 秒（适用于大文件上传）
                .writeTimeout(300, TimeUnit.SECONDS)

                // 添加拦截器（可选）
                .addInterceptor(new LoggingInterceptor())

                .build();
    }

    /**
     * 创建 Retrofit 实例并生成 Gateway 接口实现
     */
    @Bean
    public GenericHttpGateway genericHttpGateway(OkHttpClient okHttpClient) {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                // 基础 URL（必须以 / 结尾）
                .baseUrl("http://127.0.0.1/")

                // 添加 Gson 转换器
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())

                // 设置自定义 OkHttpClient
                .client(okHttpClient)

                .build();

        return retrofit.create(GenericHttpGateway.class);
    }
}
```

### 2. 配置参数说明

```java
OkHttpClient.Builder()
    // 连接池：最大空闲连接数，保持时间，时间单位
    .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))

    // 连接超时（建立连接的时间）
    .connectTimeout(30, TimeUnit.SECONDS)

    // 读取超时（从服务器读取数据的时间）
    .readTimeout(30, TimeUnit.SECONDS)

    // 写入超时（向服务器发送数据的时间）
    .writeTimeout(30, TimeUnit.SECONDS)

    // 整个调用的超时时间（包含连接、读取、写入）
    .callTimeout(60, TimeUnit.SECONDS)

    // 连接失败重试
    .retryOnConnectionFailure(true)

    // 重定向跟随
    .followRedirects(true)

    // SSL 重定向
    .followSslRedirects(true)

    // 添加拦截器
    .addInterceptor(customInterceptor)

    // 添加网络拦截器
    .addNetworkInterceptor(networkInterceptor)
```

### 3. 日志拦截器配置

```java
import okhttp3.logging.HttpLoggingInterceptor;

HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient okHttpClient = new OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build();
```

日志级别：
- `NONE`: 无日志
- `BASIC`: 请求/响应行
- `HEADERS`: 请求/响应行 + 头
- `BODY`: 请求/响应行 + 头 + 体

---

## 接口定义

### 1. 基础接口定义

```java
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import java.util.Map;

/**
 * 通用 HTTP Gateway 接口
 * 使用 @Url 注解实现动态 URL 调用
 */
public interface GenericHttpGateway {

    /**
     * POST 请求
     * @param url 动态 URL
     * @param headers 请求头
     * @param body 请求体
     * @return 响应对象
     */
    @POST
    Call<ResponseBody> post(
            @Url String url,
            @HeaderMap Map<String, Object> headers,
            @Body RequestBody body
    );

    /**
     * GET 请求
     * @param url 动态 URL
     * @param headers 请求头
     * @param queryParams 查询参数
     * @return 响应对象
     */
    @GET
    Call<ResponseBody> get(
            @Url String url,
            @HeaderMap Map<String, Object> headers,
            @QueryMap Map<String, Object> queryParams
    );
}
```

### 2. 常用注解说明

| 注解 | 说明 | 示例 |
|------|------|------|
| `@GET` | GET 请求 | `@GET("users")` |
| `@POST` | POST 请求 | `@POST("users")` |
| `@PUT` | PUT 请求 | `@PUT("users/{id}")` |
| `@DELETE` | DELETE 请求 | `@DELETE("users/{id}")` |
| `@PATCH` | PATCH 请求 | `@PATCH("users/{id}")` |
| `@Url` | 动态 URL | `@Url String url` |
| `@Path` | 路径参数 | `@Path("id") int id` |
| `@Query` | 查询参数 | `@Query("page") int page` |
| `@QueryMap` | 多个查询参数 | `@QueryMap Map<String, String> params` |
| `@Body` | 请求体 | `@Body User user` |
| `@Header` | 请求头 | `@Header("Authorization") String token` |
| `@HeaderMap` | 多个请求头 | `@HeaderMap Map<String, String> headers` |
| `@Field` | 表单字段 | `@Field("username") String name` |
| `@FieldMap` | 多个表单字段 | `@FieldMap Map<String, String> fields` |

### 3. 特定 API 接口示例

```java
/**
 * 用户服务 API 接口定义
 */
public interface UserApiService {

    /**
     * 获取用户列表（分页）
     */
    @GET("api/users")
    Call<UserListResponse> getUserList(
        @Query("page") int page,
        @Query("size") int size
    );

    /**
     * 根据 ID 获取用户
     */
    @GET("api/users/{id}")
    Call<UserResponse> getUserById(@Path("id") Long userId);

    /**
     * 创建用户
     */
    @POST("api/users")
    Call<UserResponse> createUser(@Body CreateUserRequest request);

    /**
     * 更新用户
     */
    @PUT("api/users/{id}")
    Call<UserResponse> updateUser(
        @Path("id") Long userId,
        @Body UpdateUserRequest request
    );

    /**
     * 删除用户
     */
    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") Long userId);

    /**
     * 上传头像
     */
    @Multipart
    @POST("api/users/{id}/avatar")
    Call<UploadResponse> uploadAvatar(
        @Path("id") Long userId,
        @Part MultipartBody.Part file
    );
}
```

### 4. 自定义请求体

```java
import okhttp3.MediaType;
import okhttp3.RequestBody;

// 创建 JSON 请求体
public static RequestBody createJsonBody(Object obj) {
    String json = new Gson().toJson(obj);
    return RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"),
        json
    );
}

// 使用示例
RequestBody body = createJsonBody(userMap);
Call<ResponseBody> call = genericHttpGateway.post(url, headers, body);
```

---

## 调用方法

### 1. 同步调用

```java
import retrofit2.Call;
import retrofit2.Response;

public class HttpService {

    private final GenericHttpGateway genericHttpGateway;

    public HttpService(GenericHttpGateway genericHttpGateway) {
        this.genericHttpGateway = genericHttpGateway;
    }

    /**
     * 同步 POST 请求
     */
    public String postSync(String url, Map<String, Object> headers, Object requestBody) {
        try {
            // 1. 创建请求体
            RequestBody body = createJsonBody(requestBody);

            // 2. 创建 Call 对象
            Call<ResponseBody> call = genericHttpGateway.post(url, headers, body);

            // 3. 同步执行（会阻塞当前线程）
            Response<ResponseBody> response = call.execute();

            // 4. 处理响应
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new RuntimeException("HTTP Error: " + response.code());
            }

        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    /**
     * 同步 GET 请求
     */
    public String getSync(String url, Map<String, Object> headers, Map<String, Object> params) {
        try {
            Call<ResponseBody> call = genericHttpGateway.get(url, headers, params);
            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new RuntimeException("HTTP Error: " + response.code());
            }

        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }
}
```

### 2. 异步调用

```java
/**
 * 异步 POST 请求
 */
public void postAsync(String url, Map<String, Object> headers, Object requestBody) {
    // 1. 创建请求体
    RequestBody body = createJsonBody(requestBody);

    // 2. 创建 Call 对象
    Call<ResponseBody> call = genericHttpGateway.post(url, headers, body);

    // 3. 异步执行（不会阻塞当前线程）
    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful() && response.body() != null) {
                try {
                    String result = response.body().string();
                    // 处理成功响应
                    log.info("Request success: {}", result);
                } catch (IOException e) {
                    log.error("Parse response failed", e);
                }
            } else {
                log.error("HTTP Error: {}", response.code());
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // 处理失败
            log.error("Request failed", t);
        }
    });
}
```

### 3. 取消请求

```java
private Call<ResponseBody> currentCall;

public void sendRequest() {
    currentCall = genericHttpGateway.post(url, headers, body);
    currentCall.enqueue(callback);
}

public void cancelRequest() {
    if (currentCall != null && !currentCall.isCanceled()) {
        currentCall.cancel();
        log.info("Request canceled");
    }
}
```

### 4. 完整使用示例

```java
import lombok.extern.slf4j.Slf4j;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class McpHttpGatewayService {

    private final GenericHttpGateway genericHttpGateway;

    public McpHttpGatewayService(GenericHttpGateway genericHttpGateway) {
        this.genericHttpGateway = genericHttpGateway;
    }

    /**
     * 发送 MCP 工具调用请求
     */
    public String callMcpTool(String serverUrl, Map<String, Object> toolParams) {
        try {
            // 1. 构建请求头
            Map<String, Object> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            // 可添加认证信息
            // headers.put("Authorization", "Bearer " + token);

            // 2. 构建完整 URL
            String fullUrl = serverUrl + "/tools/call";

            // 3. 创建请求体
            RequestBody body = createJsonBody(toolParams);

            // 4. 发送请求
            Call<ResponseBody> call = genericHttpGateway.post(fullUrl, headers, body);
            Response<ResponseBody> response = call.execute();

            // 5. 处理响应
            if (response.isSuccessful()) {
                return response.body() != null ? response.body().string() : null;
            } else {
                log.error("MCP tool call failed: {} - {}", response.code(), response.message());
                throw new RuntimeException("MCP tool call failed: " + response.code());
            }

        } catch (Exception e) {
            log.error("MCP tool call exception", e);
            throw new RuntimeException("MCP tool call failed", e);
        }
    }

    /**
     * 获取 MCP 服务器信息
     */
    public String getServerInfo(String serverUrl) {
        try {
            Map<String, Object> headers = new HashMap<>();
            headers.put("Accept", "application/json");

            Call<ResponseBody> call = genericHttpGateway.get(
                serverUrl + "/info",
                headers,
                null
            );

            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful()) {
                return response.body() != null ? response.body().string() : null;
            } else {
                throw new RuntimeException("Get server info failed: " + response.code());
            }

        } catch (Exception e) {
            throw new RuntimeException("Get server info failed", e);
        }
    }

    /**
     * 创建 JSON 请求体
     */
    private RequestBody createJsonBody(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            json
        );
    }
}
```

---

## 高级特性

### 1. 拦截器使用

#### 请求日志拦截器

```java
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {

    @Bean
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}

// 在 OkHttpClient 中添加
OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor())
    .build();
```

#### 自定义认证拦截器

```java
public class AuthInterceptor implements Interceptor {

    private final String tokenProvider;

    public AuthInterceptor(String tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 添加认证头
        Request authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer " + getAccessToken())
            .header("Accept", "application/json")
            .build();

        return chain.proceed(authenticatedRequest);
    }

    private String getAccessToken() {
        // 从配置或缓存中获取 token
        return tokenProvider;
    }
}

// 使用
OkHttpClient.Builder()
    .addInterceptor(new AuthInterceptor("your-token"))
    .build();
```

#### 重试拦截器

```java
public class RetryInterceptor implements Interceptor {

    private final int maxRetry;

    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException exception = null;

        int retryCount = 0;
        while (retryCount < maxRetry) {
            try {
                response = chain.proceed(request);
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (IOException e) {
                exception = e;
            }
            retryCount++;
        }

        if (exception != null) {
            throw exception;
        }
        return response;
    }
}
```

### 2. 错误处理

```java
public class ErrorHandler {

    public static <T> T handleResponse(Call<T> call) {
        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else {
                switch (response.code()) {
                    case 400:
                        throw new BadRequestException("Bad request");
                    case 401:
                        throw new UnauthorizedException("Unauthorized");
                    case 403:
                        throw new ForbiddenException("Forbidden");
                    case 404:
                        throw new NotFoundException("Resource not found");
                    case 500:
                        throw new InternalServerException("Internal server error");
                    default:
                        throw new RuntimeException("HTTP error: " + response.code());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Network error", e);
        }
    }
}
```

### 3. 响应解析

```java
// 定义响应对象
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // getters and setters
}

// 自定义转换器
public class ApiResponseConverter<T> implements Converter<ResponseBody, ApiResponse<T>> {

    private final Gson gson;
    private final Type type;

    public ApiResponseConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public ApiResponse<T> convert(ResponseBody value) throws IOException {
        String json = value.string();
        return gson.fromJson(json, type);
    }
}
```

---

## 最佳实践

### 1. 连接池配置

```java
// 生产环境推荐配置
@Bean
public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .connectionPool(new ConnectionPool(
            20,              // 最大空闲连接数
            5,               // 保持时间
            TimeUnit.MINUTES
        ))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build();
}
```

### 2. 异常处理

```java
// 统一异常处理
public class HttpClientException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public HttpClientException(int statusCode, String responseBody) {
        super("HTTP request failed: " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

// 使用
try {
    String result = httpService.post(url, headers, body);
} catch (HttpClientException e) {
    log.error("HTTP error: {}, response: {}",
        e.getStatusCode(),
        e.getResponseBody()
    );
}
```

### 3. 超时设置

```java
// 不同场景的超时配置
public class TimeoutConfig {

    // 普通请求
    public static final int NORMAL_TIMEOUT = 30;

    // 文件上传
    public static final int UPLOAD_TIMEOUT = 300;

    // 文件下载
    public static final int DOWNLOAD_TIMEOUT = 600;
}

// 使用
OkHttpClient.Builder()
    .connectTimeout(TimeoutConfig.NORMAL_TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(TimeoutConfig.DOWNLOAD_TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(TimeoutConfig.UPLOAD_TIMEOUT, TimeUnit.SECONDS)
    .build();
```

### 4. 线程池配置

```java
// 为异步请求配置专用线程池
ExecutorService executorService = new ThreadPoolExecutor(
    10,                     // 核心线程数
    20,                     // 最大线程数
    60L,                    // 空闲线程存活时间
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100),
    new ThreadFactoryBuilder()
        .setNameFormat("http-client-%d")
        .build()
);

OkHttpClient.Builder()
    .dispatcher(new Dispatcher(executorService))
    .build();
```

### 5. 监控和指标

```java
public class MetricsInterceptor implements Interceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public Response intercept(Chain chain) throws IOException {
        long start = System.nanoTime();
        String url = chain.request().url().toString();

        try {
            Response response = chain.proceed(chain.request());

            // 记录请求耗时
            long duration = System.nanoTime() - start;
            meterRegistry.timer("http.client.requests",
                "url", url,
                "status", String.valueOf(response.code())
            ).record(duration, TimeUnit.NANOSECONDS);

            return response;
        } catch (Exception e) {
            // 记录失败
            meterRegistry.counter("http.client.errors",
                "url", url,
                "exception", e.getClass().getSimpleName()
            ).increment();
            throw e;
        }
    }
}
```

---

## 总结

Retrofit + OkHttp 是一个功能强大且类型安全的 HTTP 客户端解决方案，特别适合：

- 需要类型安全的 RESTful API 调用
- 需要高性能的连接池管理
- 需要灵活的拦截器机制
- 多个相似 API 端点的场景

通过合理的配置和使用，可以大大提升 HTTP 通信的可靠性和可维护性。

## 参考资料

- [OkHttp 官方文档](https://square.github.io/okhttp/)
- [Retrofit 官方文档](https://square.github.io/retrofit/)
- [Retrofit GitHub](https://github.com/square/retrofit)
- [OkHttp GitHub](https://github.com/square/okhttp)
