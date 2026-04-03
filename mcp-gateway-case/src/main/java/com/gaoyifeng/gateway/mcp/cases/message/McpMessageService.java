package com.gaoyifeng.gateway.mcp.cases.message;

import com.gaoyifeng.gateway.mcp.cases.IMcpMessageService;
import com.gaoyifeng.gateway.mcp.cases.message.factory.DefaultMcpMessageFactory;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


@Slf4j
@Service
public class McpMessageService implements IMcpMessageService {

    @Resource
    private DefaultMcpMessageFactory defaultMcpMessageFactory;

    @Override
    public ResponseEntity<Void> handleMessage(HandleMessageCommandEntity commandEntity) throws Exception {
        StrategyHandler<HandleMessageCommandEntity, DefaultMcpMessageFactory.MessageDynamicContext, ResponseEntity<Void>> strategyHandler
                = defaultMcpMessageFactory.strategyHandler();

        return strategyHandler.apply(commandEntity, DefaultMcpMessageFactory.MessageDynamicContext.builder().build());
    }

}
