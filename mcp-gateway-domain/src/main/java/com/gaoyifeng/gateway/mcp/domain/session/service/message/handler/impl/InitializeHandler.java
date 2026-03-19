package com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.impl;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;
import com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.IRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("initializeHandler")
public class InitializeHandler implements IRequestHandler {

    @Override
    public McpSchemaVO.JSONRPCResponse handle(String gatewayId, McpSchemaVO.JSONRPCRequest message) {

        log.info("模拟处理初始化请求");

        return new McpSchemaVO.JSONRPCResponse("2.0", message.id(), Map.of(
                "protocolVersion", "2024-11-05",
                "capabilities", Map.of(
                        "tools", Map.of(),
                        "resources", Map.of()
                ),
                "serverInfo", Map.of(
                        "name", "MCP Word Util Proxy Server",
                        "version", "1.0.0"
                )
        ), null);

    }

}

