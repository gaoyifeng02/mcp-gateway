# McpSchemaVO 类结构分析

> 文件路径：`mcp-gateway-domain/src/main/java/com/gaoyifeng/gateway/mcp/domain/session/model/valobj/McpSchemaVO.java`

## 一、设计动机

该类参考了 Spring AI 的 `McpSchema` 源码，将 MCP 协议规范中涉及的所有数据结构集中定义在一个类中。

选择单文件集中定义的原因：
1. **sealed interface 约束**：Java 的 `sealed` + `permits` 要求子类必须在同一个包内，放在同一文件更简洁
2. **协议类型聚合**：MCP 协议本身就是一组嵌套的 JSON Schema，直接翻译成 Java record 后放在一起，便于查阅
3. **SDK 风格**：原始项目（Spring AI）作为协议 SDK，将类型集中管理是常见做法

## 二、核心判断：这是贫血模型，不是充血模型

- 该类全部由 `record`（纯数据载体）组成，没有任何业务行为
- 仅有的两个 `static` 方法（`deserializeJsonRpcMessage`、`unmarshalFrom`）是序列化工具方法
- **本质是 MCP 协议的数据字典**，不是领域模型

## 三、类结构全景

### 第 1 层：协议基础（常量 & 工具）

| 成员 | 说明 |
|---|---|
| `LATEST_PROTOCOL_VERSION` | MCP 协议版本 "2024-11-05" |
| `JSONRPC_VERSION` | JSON-RPC 版本 "2.0" |
| `MAP_TYPE_REF` | Jackson 反序列化用的类型引用 |
| `objectMapper` | 全局共享的 JSON 序列化器 |

工具方法：
- `deserializeJsonRpcMessage()` — JSON 字符串 → JSONRPCMessage 子类
- `unmarshalFrom()` — 通用反序列化，把 Object 转为指定类型

### 第 2 层：JSON-RPC 传输层

所有消息的"信封"，负责包装通信内容：

```
JSONRPCMessage (sealed interface)
├── JSONRPCRequest          有 method + id（请求）
│   └── jsonrpc, method, id, params
├── JSONRPCNotification     有 method，无 id（通知）
│   └── jsonrpc, method, params
└── JSONRPCResponse         有 result 或 error（响应）
    ├── jsonrpc, id, result, error
    └── JSONRPCError        code, message, data
```

### 第 3 层：MCP 业务模型

按 MCP 协议生命周期分为 3 个阶段：

#### 阶段 A：初始化握手（Initialize）

```
InitializeRequest（客户端发起）
├── protocolVersion
├── clientInfo → Implementation(name, version)
└── capabilities → ClientCapabilities
    ├── experimental: Map<String, Object>
    ├── roots → RootCapabilities(listChanged)
    └── sampling → Sampling

InitializeResult（服务端返回）
├── protocolVersion
├── serverInfo → Implementation(name, version)
├── capabilities → ServerCapabilities
│   ├── completions → CompletionCapabilities
│   ├── experimental: Map<String, Object>
│   ├── logging → LoggingCapabilities
│   ├── prompts → PromptCapabilities(listChanged)
│   ├── resources → ResourceCapabilities(subscribe, listChanged)
│   └── tools → ToolCapabilities(listChanged)
└── instructions
```

#### 阶段 B：工具发现（Tool Discovery）

```
ListToolsResult
├── tools: List<Tool>
│   └── Tool
│       ├── name
│       ├── description
│       └── inputSchema → JsonSchema
│           ├── type, properties, required
│           ├── additionalProperties
│           └── $defs, definitions
└── nextCursor（分页游标）
```

#### 阶段 C：工具调用（Tool Call）

```
CallToolRequest
├── name: String              工具名
└── arguments: Map<String, Object>   调用参数
```

## 四、一个 MCP 消息的完整生命周期

```
JSON 字符串
  → deserializeJsonRpcMessage() 解析成 JSONRPCRequest
    → params 被反序列化为 InitializeRequest / CallToolRequest
      → 网关处理后返回 InitializeResult / ListToolsResult
        → 包装成 JSONRPCResponse 发回客户端
```

## 五、当前问题与改进建议

### 当前问题

1. **职责不单一**：一个类承担了传输层定义、协议层定义、序列化工具三重职责
2. **文件过大**：350 行、20+ 内部类，阅读困难
3. **定位模糊**：作为 VO 却包含 static 工具方法，不像值对象也不像工厂

### 建议的拆分方案（供后续重构参考）

```
mcp/model/
├── transport/                    ← 传输层（JSON-RPC）
│   ├── JSONRPCMessage.java       (sealed interface)
│   ├── JSONRPCRequest.java
│   ├── JSONRPCNotification.java
│   ├── JSONRPCResponse.java
│   └── JSONRPCError.java
│
├── protocol/                     ← MCP 协议层
│   ├── initialize/
│   │   ├── InitializeRequest.java
│   │   ├── InitializeResult.java
│   │   ├── ClientCapabilities.java
│   │   └── ServerCapabilities.java
│   ├── tool/
│   │   ├── Tool.java
│   │   ├── JsonSchema.java
│   │   ├── ListToolsResult.java
│   │   └── CallToolRequest.java
│   └── Implementation.java
│
└── McpSchemaCodec.java           ← 序列化/反序列化工具
```

拆分后每个文件职责单一，不超过 50 行，一看文件名就知道是什么。
