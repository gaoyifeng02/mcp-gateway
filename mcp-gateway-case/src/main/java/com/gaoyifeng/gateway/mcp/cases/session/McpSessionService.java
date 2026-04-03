package com.gaoyifeng.gateway.mcp.cases.session;

import com.gaoyifeng.gateway.mcp.cases.IMcpSessionService;
import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;

@Service
public class McpSessionService implements IMcpSessionService {

    @Resource
    private DefaultMcpSessionFactory defaultMcpSessionFactory;

    @Override
    public Flux<ServerSentEvent<String>> createMcpSession(String gatewayId, String apiKey) throws Exception {

        StrategyHandler<String, DefaultMcpSessionFactory.SessionDynamicContext, Flux<ServerSentEvent<String>>> strategyHandler =
                defaultMcpSessionFactory.strategyHandler();

        DefaultMcpSessionFactory.SessionDynamicContext dynamicContext = DefaultMcpSessionFactory.SessionDynamicContext.builder()
                .apiKey(apiKey)
                .build();

        return strategyHandler.apply(gatewayId, dynamicContext);
    }

}
