package com.gaoyifeng.gateway.mcp.cases.session.node;

import com.gaoyifeng.gateway.mcp.cases.session.AbstractMcpSessionSupport;
import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;


@Slf4j
@Service("mcpSessionRootNode")
public class RootNode extends AbstractMcpSessionSupport {

    @Resource(name = "mcpSessionVerifyNode")
    private VerifyNode verifyNode;

    @Override
    protected Flux<ServerSentEvent<String>> doApply(String requestParameter, DefaultMcpSessionFactory.SessionDynamicContext dynamicContext) throws Exception {
        try {
            log.info("创建会话 mcp session RootNode:{}", requestParameter);

            return router(requestParameter, dynamicContext);
        } catch (Exception e) {
            log.error("创建会话 mcp session RootNode 异常:{}", requestParameter, e);
            throw e;
        }
    }

    @Override
    public StrategyHandler<String, DefaultMcpSessionFactory.SessionDynamicContext, Flux<ServerSentEvent<String>>> get(String requestParameter, DefaultMcpSessionFactory.SessionDynamicContext dynamicContext) throws Exception {
        return verifyNode;
    }

}
