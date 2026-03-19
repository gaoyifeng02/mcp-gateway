package com.gaoyifeng.gateway.mcp.domain.session.adapter.repository;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.gateway.McpGatewayConfigVO;

public interface ISessionRepository {

    McpGatewayConfigVO queryMcpGatewayConfigByGatewayId(String gatewayId);

}