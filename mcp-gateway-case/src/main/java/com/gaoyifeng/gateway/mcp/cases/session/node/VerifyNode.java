package com.gaoyifeng.gateway.mcp.cases.session.node;

import com.gaoyifeng.gateway.mcp.cases.session.AbstractMcpSessionSupport;
import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.LicenseCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.auth.service.IAuthLicenseService;
import com.gaoyifeng.gateway.mcp.types.enums.McpErrorCodes;
import com.gaoyifeng.gateway.mcp.types.exception.AppException;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;


@Slf4j
@Service("mcpSessionVerifyNode")
public class VerifyNode extends AbstractMcpSessionSupport {

    @Resource(name = "mcpSessionSessionNode")
    private SessionNode sessionNode;

    @Resource
    private IAuthLicenseService authLicenseService;

    @Override
    protected Flux<ServerSentEvent<String>> doApply(String requestParameter, DefaultMcpSessionFactory.SessionDynamicContext dynamicContext) throws Exception {
        log.info("创建会话-VerifyNode:{}", requestParameter);

        boolean isCheckSuccess
                = authLicenseService.checkLicense(new LicenseCommandEntity(requestParameter, dynamicContext.getApiKey()));

        if (!isCheckSuccess) {
            throw new AppException(McpErrorCodes.INSUFFICIENT_PERMISSIONS, "fail to auth apikey");
        }

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<String, DefaultMcpSessionFactory.SessionDynamicContext, Flux<ServerSentEvent<String>>> get(String requestParameter, DefaultMcpSessionFactory.SessionDynamicContext dynamicContext) throws Exception {
        return sessionNode;
    }

}
