package com.gaoyifeng.gateway.mcp.cases.session;

import com.gaoyifeng.gateway.mcp.cases.session.factory.DefaultMcpSessionFactory;
import com.gaoyifeng.gateway.mcp.domain.session.service.ISessionManagementService;
import com.gaoyifeng.wrench.design.tree.AbstractMultiThreadStrategyRouter;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMcpSessionSupport extends AbstractMultiThreadStrategyRouter<String, DefaultMcpSessionFactory.DynamicContext, Flux<ServerSentEvent<String>>> {

    @Resource
    protected ISessionManagementService sessionManagementService;

    @Override
    protected void multiThread(String requestParameter, DefaultMcpSessionFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }

}
