package com.gaoyifeng.gateway.mcp.cases;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;


public interface IMcpSessionService {

    /**
     * 创建 MCP 会话服务
     *
     * @return 流式响应
     */
    Flux<ServerSentEvent<String>> createMcpSession(String gatewayId, String apiKey) throws Exception;

}
