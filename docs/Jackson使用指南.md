# Jackson 使用指南

## 1. 概述

Jackson 是 Java 生态中最主流的 JSON 处理库，Spring Boot 默认内置。核心模块：

| 模块 | artifactId | 作用 |
|------|-----------|------|
| Streaming | `jackson-core` | 底层流式 API（JsonParser / JsonGenerator） |
| Databind | `jackson-databind` | 高层 API（ObjectMapper），依赖 jackson-core |
| Annotations | `jackson-annotations` | `@JsonProperty`、`@JsonIgnore` 等注解 |

日常开发主要使用 **jackson-databind** 的 `ObjectMapper`。

---

## 2. Maven 引入

### 2.1 Spring Boot 项目（推荐）

Spring Boot 通过 `spring-boot-starter-web` 已自动引入 Jackson，**无需手动添加依赖**：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

Spring Boot 还自动配置了 `ObjectMapper` Bean，可直接 `@Autowired` 注入使用。

### 2.2 非 Spring Boot 项目

手动引入 jackson-databind（会自动传递依赖 jackson-core 和 jackson-annotations）：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

### 2.3 额外模块（按需引入）

```xml
<!-- Java 8 日期时间支持（Spring Boot 已自动引入） -->
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>

<!-- XML 支持 -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>

<!-- YAML 支持 -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
</dependency>
```

---

## 3. 基本使用

### 3.1 ObjectMapper 核心操作

```java
ObjectMapper mapper = new ObjectMapper();

// ---- 序列化：Java 对象 → JSON 字符串 ----
String json = mapper.writeValueAsString(user);

// 序列化到文件
mapper.writeValue(new File("user.json"), user);

// ---- 反序列化：JSON 字符串 → Java 对象 ----
User user = mapper.readValue(json, User.class);

// 从文件读取
User user = mapper.readValue(new File("user.json"), User.class);
```

### 3.2 与 Map 互转

```java
// JSON → Map
Map<String, Object> map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});

// Map → JSON
String json = mapper.writeValueAsString(map);

// Map → Java 对象（两步转换）
User user = mapper.convertValue(map, User.class);
```

### 3.3 与 List 互转

```java
// JSON → List<POJO>
List<User> users = mapper.readValue(json, new TypeReference<List<User>>() {});

// JSON → List<Map>
List<Map<String, Object>> list = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
```

### 3.4 convertValue（对象间转换）

不经过 JSON 字符串，直接在 Java 对象之间转换：

```java
// Map → POJO
User user = mapper.convertValue(map, User.class);

// POJO → Map
Map<String, Object> map = mapper.convertValue(user, new TypeReference<HashMap<String, Object>>() {});

// POJO → 另一个 POJO（字段名相同时很有用）
UserDTO dto = mapper.convertValue(user, UserDTO.class);
```

---

## 4. 常用注解

### 4.1 字段映射

```java
public class User {

    // JSON 中的 key 名与 Java 字段名不同时使用
    @JsonProperty("user_name")
    private String userName;

    // 序列化/反序列化时忽略该字段
    @JsonIgnore
    private String password;

    // 序列化时如果值为 null 则不输出该字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    // 忽略 JSON 中存在但 Java 类中没有的字段（反序列化不报错）
    // 通常放在类上
}
```

### 4.2 类级别注解

```java
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
    @JsonProperty("user_name") String userName,
    @JsonProperty("age") Integer age
) {}
```

| 注解 | 作用 |
|------|------|
| `@JsonIgnoreProperties(ignoreUnknown = true)` | 忽略未知字段，反序列化时不报错 |
| `@JsonInclude(Include.NON_NULL)` | 值为 null 的字段不序列化 |
| `@JsonInclude(Include.NON_ABSENT)` | null + Optional.empty + 空集合都不序列化 |
| `@JsonInclude(Include.NON_EMPTY)` | null + 空字符串 + 空集合都不序列化 |

### 4.3 枚举处理

```java
// 枚举按名称序列化（默认行为）
public enum Status { ACTIVE, INACTIVE }

// 按序号序列化（不推荐，可读性差）
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum Status { ACTIVE, INACTIVE }
```

---

## 5. 泛型擦除与 TypeReference

### 5.1 问题

Java 泛型在运行时会被擦除（Type Erasure），导致无法直接传递带泛型参数的类型给 Jackson：

```java
// 编译错误 —— .class 不支持泛型参数
mapper.readValue(json, HashMap<String, Object>.class);

// 类型不安全 —— 丢失 <String, Object> 信息
mapper.readValue(json, HashMap.class);
```

### 5.2 解决方案：TypeReference

Jackson 的 `TypeReference` 利用匿名子类保留父类泛型参数信息的特性，在运行时通过反射获取完整类型：

```java
import com.fasterxml.jackson.core.type.TypeReference;

// 创建 TypeReference 的匿名子类，泛型信息被保留在字节码中
private static final TypeReference<HashMap<String, Object>> MAP_TYPE_REF =
        new TypeReference<>() {};
//                                              ^^ 钻石操作符，Java 7+ 可省略泛型参数
//                                                 ^^ 花括号不能省略！它创建匿名子类

// 使用
Map<String, Object> map = mapper.readValue(json, MAP_TYPE_REF);
```

**注意**：`new TypeReference<>() {}` 中的 `{}` 不能省略，它创建匿名子类，这是保留泛型信息的关键。

### 5.3 更多泛型场景

```java
// 嵌套泛型
private static final TypeReference<List<Map<String, Object>>> LIST_MAP =
        new TypeReference<>() {};

// 复杂泛型
private static final TypeReference<Map<String, List<User>>> COMPLEX =
        new TypeReference<>() {};
```

### 5.4 三种方式对比

| 方式 | 泛型支持 | 示例 |
|------|:---:|------|
| `.class` 字面量 | 不支持 | `HashMap.class` |
| `TypeReference` | 支持 | `new TypeReference<HashMap<String, Object>>() {}` |
| `JavaType`（手动构造） | 支持 | `mapper.getTypeFactory().constructMapType(...)` |

`TypeReference` 是最简洁的方式，推荐优先使用。

---

## 6. Spring MVC 中替换默认 JSON 库

### 6.1 默认行为

Spring Boot 引入 `spring-boot-starter-web` 后，默认使用 **Jackson** 作为 JSON 序列化库。
Controller 返回 `@ResponseBody` 或 `@RestController` 标注的对象时，自动通过 Jackson 序列化为 JSON。

### 6.2 替换为 Gson

```xml
<!-- 排除 Jackson，引入 Gson -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

Spring Boot 检测到 classpath 中有 Gson 且没有 Jackson 时，自动切换为 Gson。

### 6.3 替换为 Fastjson2

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2-extension-spring6</artifactId>
    <version>2.0.47</version>
</dependency>
```

同时需要注册 `FastJsonHttpMessageConverter`：

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converters.add(0, converter);
    }
}
```

### 6.4 为什么推荐使用 Jackson

- **Spring Boot 官方默认**，兼容性最好，零配置即可使用
- 社区活跃，生态完善，支持 XML / YAML / CSV 等多种格式扩展
- 性能优秀，`blackbird` 模块在 Java 21+ 上利用动态字节码生成进一步提升性能
- 与 Spring Security、Spring Cloud 等无缝集成

> **本项目中同时引入了 fastjson 和 Jackson。** `mcp-gateway-domain` 模块中 `McpSchemaVO` 使用的是 Jackson（`ObjectMapper` + `TypeReference`）。建议统一使用 Jackson，避免多库混用导致序列化行为不一致。

---

## 7. ObjectMapper 配置

### 7.1 常用配置

```java
ObjectMapper mapper = new ObjectMapper();

// 反序列化时忽略 JSON 中存在但 Java 类中没有的字段
mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// 序列化时忽略 null 值
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

// 格式化输出（调试用，生产不建议开启）
mapper.enable(SerializationFeature.INDENT_OUTPUT);

// 日期格式
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

// 支持 Java 8 日期时间类型
mapper.registerModule(new JavaTimeModule());
// 默认将日期序列化为时间戳，关闭此行为改为 ISO-8601 格式
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
```

### 7.2 Spring Boot 中自定义 ObjectMapper

Spring Boot 自动配置的 ObjectMapper 可以通过 `application.yml` 全局配置：

```yaml
spring:
  jackson:
    # 日期格式
    date-format: yyyy-MM-dd HH:mm:ss
    # 时区
    time-zone: GMT+8
    # 序列化包含策略
    default-property-inclusion: non_null
    # 反序列化
    deserialization:
      fail-on-unknown-properties: false
    serialization:
      # 格式化输出
      indent-output: false
      # 日期写为时间戳
      write-dates-as-timestamps: false
```

也可以通过 Bean 覆盖：

```java
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

---

## 8. 项目中的实际应用

在 `McpSchemaVO.java` 中，Jackson 用于 MCP 协议的 JSON-RPC 消息处理：

```java
@Slf4j
public final class McpSchemaVO {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<HashMap<String, Object>> MAP_TYPE_REF =
            new TypeReference<>() {};

    // 先解析为通用 Map，再根据字段判断消息类型
    public static JSONRPCMessage deserializeJsonRpcMessage(String jsonText) throws IOException {
        var map = objectMapper.readValue(jsonText, MAP_TYPE_REF);

        if (map.containsKey("method") && map.containsKey("id")) {
            return objectMapper.convertValue(map, JSONRPCRequest.class);
        } else if (map.containsKey("method") && !map.containsKey("id")) {
            return objectMapper.convertValue(map, JSONRPCNotification.class);
        } else if (map.containsKey("result") || map.containsKey("error")) {
            return objectMapper.convertValue(map, JSONRPCResponse.class);
        }
        throw new IllegalArgumentException("Cannot deserialize: " + jsonText);
    }
}
```

核心模式：`readValue`（JSON 字符串 → Map） → 判断类型 → `convertValue`（Map → 具体 record）。

---

## 9. 常见问题速查

| 问题 | 解决方案 |
|------|---------|
| 反序列化报 `UnrecognizedPropertyException` | 类上加 `@JsonIgnoreProperties(ignoreUnknown = true)` |
| 字段名为驼峰，JSON 为下划线 | 使用 `@JsonProperty("user_name")` 或全局配置命名策略 `mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)` |
| 日期序列化为时间戳 | 关闭 `WRITE_DATES_AS_TIMESTAMPS`，注册 `JavaTimeModule` |
| 枚举反序列化失败 | 使用 `@JsonCreator` 标注工厂方法 |
| 循环引用导致栈溢出 | 使用 `@JsonManagedReference` / `@JsonBackReference` |
| 泛型类型丢失 | 使用 `TypeReference`（见第 5 节） |
