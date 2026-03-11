package com.gaoyifeng.gateway.mcp.cases.session;

import com.gaoyifeng.gateway.mcp.cases.IMcpSessionService;
import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Service
public class McpSessionService implements IMcpSessionService {

    @Resource
    private DefaultMcpSessionFactory defaultMcpSessionFactory;

    @Override
    public Flux<ServerSentEvent<String>> createMcpSession(String gatewayId) throws Exception {
        StrategyHandler<String, DefaultMcpSessionFactory.DynamicContext, Flux<ServerSentEvent<String>>> strategyHandler = defaultMcpSessionFactory.strategyHandler();

        return strategyHandler.apply(gatewayId, new DefaultMcpSessionFactory.DynamicContext());

    }
}
