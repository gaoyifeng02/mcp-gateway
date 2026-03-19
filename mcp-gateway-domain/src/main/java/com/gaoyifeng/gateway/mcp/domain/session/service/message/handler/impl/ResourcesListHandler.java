package com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.impl;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;
import com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.IRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("resourcesListHandler")
public class ResourcesListHandler implements IRequestHandler {

    @Override
    public McpSchemaVO.JSONRPCResponse handle(String gatewayId, McpSchemaVO.JSONRPCRequest message) {
        return new McpSchemaVO.JSONRPCResponse("2.0", message.id(), Map.of(
                "resources", Map.of(
                        "resources", new Object[]{}
                )
        ), null);
    }

}