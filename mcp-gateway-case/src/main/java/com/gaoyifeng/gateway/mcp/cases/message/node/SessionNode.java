package com.gaoyifeng.gateway.mcp.cases.message.node;

import com.gaoyifeng.gateway.mcp.cases.message.AbstractMcpMessageServiceSupport;
import com.gaoyifeng.gateway.mcp.cases.message.factory.DefaultMcpMessageFactory;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


@Slf4j
@Service("mcpMessageSessionNode")
public class SessionNode extends AbstractMcpMessageServiceSupport {

    @Resource(name = "mcpMessageMessageHandlerNode")
    private MessageHandlerNode messageHandlerNode;

    @Override
    protected ResponseEntity<Void> doApply(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        log.info("消息处理 mcp message SessionNode:{}", requestParameter);

        SessionConfigVO sessionConfigVO = sessionManagementService.getSession(requestParameter.getSessionId());
        if (null == sessionConfigVO) {
            log.warn("会话不存在或已过期，gatewayId:{} sessionId:{}", requestParameter.getGatewayId(), requestParameter.getSessionId());
            return ResponseEntity.notFound().build();
        }

        dynamicContext.setSessionConfigVO(sessionConfigVO);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<HandleMessageCommandEntity, DefaultMcpMessageFactory.MessageDynamicContext, ResponseEntity<Void>> get(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        return messageHandlerNode;
    }

}
