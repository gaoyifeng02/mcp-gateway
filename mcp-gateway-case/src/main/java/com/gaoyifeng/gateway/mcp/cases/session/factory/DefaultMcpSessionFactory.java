package com.gaoyifeng.gateway.mcp.cases.session.factory;

import com.gaoyifeng.gateway.mcp.cases.session.node.RootNode;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;
import com.gaoyifeng.wrench.design.tree.DynamicContext;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;

/**
 * MCP 会话服务工厂
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/12/13 09:09
 */
@Service
public class DefaultMcpSessionFactory {

    @Resource(name = "mcpSessionRootNode")
    private RootNode rootNode;

    public StrategyHandler<String, SessionDynamicContext, Flux<ServerSentEvent<String>>> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class SessionDynamicContext extends DynamicContext {

        private String apiKey;

        private SessionConfigVO sessionConfigVO;
    }

}
