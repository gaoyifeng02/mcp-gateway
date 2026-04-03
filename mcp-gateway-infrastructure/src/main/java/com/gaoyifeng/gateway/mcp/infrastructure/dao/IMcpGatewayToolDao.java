package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayToolPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMcpGatewayToolDao {

    List<McpGatewayToolPO> queryEffectiveTools(String gatewayId);

    Long queryToolProtocolIdByToolName(McpGatewayToolPO mcpGatewayToolPOReq);

}
