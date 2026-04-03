package com.gaoyifeng.gateway.mcp.cases.message.factory;

import com.gaoyifeng.gateway.mcp.cases.message.node.RootNode;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;
import com.gaoyifeng.wrench.design.tree.DynamicContext;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


@Service
public class DefaultMcpMessageFactory {

    @Resource(name = "mcpMessageRootNode")
    private RootNode rootNode;

    public StrategyHandler<HandleMessageCommandEntity, MessageDynamicContext, ResponseEntity<Void>> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MessageDynamicContext extends DynamicContext {
        private SessionConfigVO sessionConfigVO;
    }

}
