package com.gaoyifeng.gateway.mcp.domain.session.service;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;

public interface ISessionMessageService {

    McpSchemaVO.JSONRPCResponse processHandlerMessage(String gatewayId,McpSchemaVO.JSONRPCMessage message);

}