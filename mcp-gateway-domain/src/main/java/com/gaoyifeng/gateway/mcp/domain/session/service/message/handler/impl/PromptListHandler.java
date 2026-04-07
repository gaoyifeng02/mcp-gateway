package com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.impl;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;
import com.gaoyifeng.gateway.mcp.domain.session.service.message.handler.IRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service("promptListHandler")
public class PromptListHandler implements IRequestHandler {
    @Override
    public McpSchemaVO.JSONRPCResponse handle(String gatewayId, McpSchemaVO.JSONRPCRequest message) {
        return null;
    }
}
