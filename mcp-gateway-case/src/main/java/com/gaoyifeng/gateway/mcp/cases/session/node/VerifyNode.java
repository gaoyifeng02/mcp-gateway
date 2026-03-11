package com.gaoyifeng.gateway.mcp.cases.session.node;

import com.gaoyifeng.gateway.mcp.cases.session.AbstractMcpSessionSupport;
import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Slf4j
@Service
public class VerifyNode extends AbstractMcpSessionSupport {

    @Resource
    private SessionNode sessionNode;

    @Override
    protected Flux<ServerSentEvent<String>> doApply(String s, DefaultMcpSessionFactory.DynamicContext dynamicContext) throws Exception {
        return router(s, dynamicContext);
    }

    @Override
    public StrategyHandler<String, DefaultMcpSessionFactory.DynamicContext, Flux<ServerSentEvent<String>>> get(String s, DefaultMcpSessionFactory.DynamicContext dynamicContext) throws Exception {
        return sessionNode;
    }
}
