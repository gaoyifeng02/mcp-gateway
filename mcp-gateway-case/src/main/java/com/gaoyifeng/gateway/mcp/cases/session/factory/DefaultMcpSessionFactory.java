package com.gaoyifeng.gateway.mcp.cases.session.factory;

import com.gaoyifeng.gateway.mcp.cases.session.node.RootNode;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;
import com.gaoyifeng.wrench.design.tree.DynamicContext;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Component
public class DefaultMcpSessionFactory {


    @Resource
    private RootNode rootNode;

    public StrategyHandler<String, DynamicContext, Flux<ServerSentEvent<String>>> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext extends com.gaoyifeng.wrench.design.tree.DynamicContext {
        private SessionConfigVO sessionConfigVO;
    }

}
