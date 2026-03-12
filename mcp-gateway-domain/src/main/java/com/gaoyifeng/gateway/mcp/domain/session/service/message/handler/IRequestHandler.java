package com.gaoyifeng.gateway.mcp.domain.session.service.message.handler;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;

public interface IRequestHandler {

    McpSchemaVO.JSONRPCResponse handle(McpSchemaVO.JSONRPCRequest message);

}
