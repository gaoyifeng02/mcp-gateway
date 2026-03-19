package com.gaoyifeng.gateway.mcp.infrastructure.adapter.repository;

import com.gaoyifeng.gateway.mcp.domain.session.adapter.repository.ISessionRepository;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.gateway.McpGatewayConfigVO;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.IMcpGatewayDao;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.IMcpProtocolRegistryDao;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayPO;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpProtocolRegistryPO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SessionRepository implements ISessionRepository {

    @Resource
    private IMcpGatewayDao mcpGatewayDao;

    @Resource
    private IMcpProtocolRegistryDao mcpProtocolRegistryDao;

    @Override
    public McpGatewayConfigVO queryMcpGatewayConfigByGatewayId(String gatewayId) {
        // 1. 查询网关配置（这里只判空，返回null就可以）
        McpGatewayPO mcpGatewayPO = mcpGatewayDao.queryMcpGatewayByGatewayId(gatewayId);
        if (null == mcpGatewayPO) return null;

        // 2. 查询协议注册（1:1 -> gatewayId:toolId）
        McpProtocolRegistryPO mcpProtocolRegistryPO = mcpProtocolRegistryDao.queryMcpProtocolRegistryByGatewayId(gatewayId);
        if (null == mcpProtocolRegistryPO) return null;

        return McpGatewayConfigVO.builder()
                .gatewayId(mcpGatewayPO.getGatewayId())
                .gatewayName(mcpGatewayPO.getGatewayName())
                .toolId(mcpProtocolRegistryPO.getToolId())
                .toolName(mcpProtocolRegistryPO.getToolName())
                .toolDesc(mcpProtocolRegistryPO.getToolDescription())
                .toolVersion(mcpProtocolRegistryPO.getToolVersion())
                .build();
    }

}
