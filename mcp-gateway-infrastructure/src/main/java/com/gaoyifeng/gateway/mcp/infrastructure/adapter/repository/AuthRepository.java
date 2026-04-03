package com.gaoyifeng.gateway.mcp.infrastructure.adapter.repository;

import com.gaoyifeng.gateway.mcp.domain.auth.adapter.repository.IAuthRepository;
import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.LicenseCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.auth.model.valobj.McpGatewayAuthVO;
import com.gaoyifeng.gateway.mcp.domain.auth.model.valobj.enums.AuthStatusEnum;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.IMcpGatewayAuthDao;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.IMcpGatewayDao;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayAuthPO;
import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayPO;
import com.gaoyifeng.gateway.mcp.types.enums.McpErrorCodes;
import com.gaoyifeng.gateway.mcp.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class AuthRepository implements IAuthRepository {

    @Resource
    private IMcpGatewayAuthDao mcpGatewayAuthDao;

    @Resource
    private IMcpGatewayDao mcpGatewayDao;

    @Override
    public int queryEffectiveGatewayAuthCount(String gatewayId) {
        return mcpGatewayAuthDao.queryEffectiveGatewayAuthCount(gatewayId);
    }

    @Override
    public McpGatewayAuthVO queryEffectiveGatewayAuthInfo(LicenseCommandEntity commandEntity) {

        McpGatewayAuthPO poReq = new McpGatewayAuthPO();
        poReq.setGatewayId(commandEntity.getGatewayId());
        poReq.setApiKey(commandEntity.getApiKey());

        McpGatewayAuthPO mcpGatewayAuthPO = mcpGatewayAuthDao.queryMcpGatewayAuthPO(poReq);
        if (null == mcpGatewayAuthPO) return null;

        return McpGatewayAuthVO.builder()
                .gatewayId(mcpGatewayAuthPO.getGatewayId())
                .apiKey(mcpGatewayAuthPO.getApiKey())
                .rateLimit(mcpGatewayAuthPO.getRateLimit())
                .expireTime(mcpGatewayAuthPO.getExpireTime())
                .status(AuthStatusEnum.AuthConfig.get(mcpGatewayAuthPO.getStatus()))
                .build();
    }

    @Override
    public void insert(McpGatewayAuthVO mcpGatewayAuthVO) {
        McpGatewayAuthPO mcpGatewayAuthPO = McpGatewayAuthPO.builder()
                .gatewayId(mcpGatewayAuthVO.getGatewayId())
                .apiKey(mcpGatewayAuthVO.getApiKey())
                .rateLimit(mcpGatewayAuthVO.getRateLimit())
                .expireTime(mcpGatewayAuthVO.getExpireTime())
                .status(mcpGatewayAuthVO.getStatus().getCode())
                .build();
        mcpGatewayAuthDao.insert(mcpGatewayAuthPO);
    }

    @Override
    public AuthStatusEnum.GatewayConfig queryGatewayAuthStatus(String gatewayId) {
        McpGatewayPO mcpGatewayPO = mcpGatewayDao.queryMcpGatewayByGatewayId(gatewayId);
        if (null == mcpGatewayPO) {
            throw new AppException(McpErrorCodes.INVALID_PARAMS, "无效参数 gatewayId 不存在");
        }
        return AuthStatusEnum.GatewayConfig.get(mcpGatewayPO.getAuth());
    }

}
