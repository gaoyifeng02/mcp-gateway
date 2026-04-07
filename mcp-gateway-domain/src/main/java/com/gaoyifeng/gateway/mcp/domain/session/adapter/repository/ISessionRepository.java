package com.gaoyifeng.gateway.mcp.domain.session.adapter.repository;

import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.gateway.McpGatewayConfigVO;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.gateway.McpToolConfigVO;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.gateway.McpToolProtocolConfigVO;

import java.util.List;

public interface ISessionRepository {

    List<McpToolConfigVO> queryMcpGatewayToolConfigListByGatewayId(String gatewayId);

    McpToolProtocolConfigVO queryMcpGatewayProtocolConfig(String gatewayId, String toolName);


}