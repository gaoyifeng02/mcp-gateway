package com.gaoyifeng.gateway.mcp.cases.message.node;

import com.gaoyifeng.gateway.mcp.cases.message.AbstractMcpMessageServiceSupport;
import com.gaoyifeng.gateway.mcp.cases.message.factory.DefaultMcpMessageFactory;
import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.RateLimitCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.auth.service.IAuthRateLimitService;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.enums.SessionMessageHandlerMethodEnum;
import com.gaoyifeng.gateway.mcp.types.enums.McpErrorCodes;
import com.gaoyifeng.gateway.mcp.types.exception.AppException;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


@Slf4j
@Service("mcpMessageRootNode")
public class RootNode extends AbstractMcpMessageServiceSupport {

    @Resource(name = "mcpMessageSessionNode")
    private SessionNode sessionNode;

    @Resource
    private IAuthRateLimitService authRateLimitService;

    @Override
    protected ResponseEntity<Void> doApply(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        try {
            log.info("消息处理 mcp message RootNode:{}", requestParameter);

            // 判断命中工具调用做限流处理
            if (requestParameter.getJsonrpcMessage() instanceof McpSchemaVO.JSONRPCRequest request) {
                String method = request.method();

                SessionMessageHandlerMethodEnum sessionMessageHandlerMethodEnum = SessionMessageHandlerMethodEnum.getByMethod(method);
                if (SessionMessageHandlerMethodEnum.TOOLS_CALL.equals(sessionMessageHandlerMethodEnum)){
                    // 是（true）否（false）命中限流
                    boolean isHit = authRateLimitService.rateLimit(new RateLimitCommandEntity(requestParameter.getGatewayId(), requestParameter.getApiKey()));
                    if (isHit) {
                        log.warn("消息处理 mcp message RootNode - 命中限流{} {}", requestParameter.getGatewayId(), requestParameter.getApiKey());
                        throw new AppException(McpErrorCodes.INSUFFICIENT_PERMISSIONS, "fail to auth apikey rateLimiter");
                    }
                }
            }

            return router(requestParameter, dynamicContext);
        } catch (Exception e) {
            log.error("消息处理 mcp message RootNode:{}", requestParameter, e);
            throw e;
        }
    }

    @Override
    public StrategyHandler<HandleMessageCommandEntity, DefaultMcpMessageFactory.MessageDynamicContext, ResponseEntity<Void>> get(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        return sessionNode;
    }

}
