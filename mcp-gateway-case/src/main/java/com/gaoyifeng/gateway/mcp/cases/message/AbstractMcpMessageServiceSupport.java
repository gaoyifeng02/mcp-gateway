package com.gaoyifeng.gateway.mcp.cases.message;

import com.gaoyifeng.gateway.mcp.cases.message.factory.DefaultMcpMessageFactory;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.session.service.ISessionManagementService;
import com.gaoyifeng.gateway.mcp.domain.session.service.ISessionMessageService;
import com.gaoyifeng.wrench.design.tree.AbstractMultiThreadStrategyRouter;
import org.springframework.http.ResponseEntity;

import jakarta.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMcpMessageServiceSupport extends AbstractMultiThreadStrategyRouter<HandleMessageCommandEntity, DefaultMcpMessageFactory.MessageDynamicContext, ResponseEntity<Void>> {

    @Resource
    protected ISessionMessageService serviceMessageService;

    @Resource
    protected ISessionManagementService sessionManagementService;

    @Override
    protected void multiThread(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }

}
