package com.gaoyifeng.gateway.mcp.cases.message.node;

import com.gaoyifeng.gateway.mcp.cases.message.AbstractMcpMessageServiceSupport;
import com.gaoyifeng.gateway.mcp.cases.message.factory.DefaultMcpMessageFactory;
import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.McpSchemaVO;
import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;
import com.gaoyifeng.wrench.design.tree.StrategyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;


@Slf4j
@Service("mcpMessageMessageHandlerNode")
public class MessageHandlerNode extends AbstractMcpMessageServiceSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ResponseEntity<Void> doApply(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        log.info("消息处理 mcp message MessageHandlerNode:{}", requestParameter);

        McpSchemaVO.JSONRPCResponse jsonrpcResponse =
                serviceMessageService.processHandlerMessage(requestParameter.getGatewayId(), requestParameter.getJsonrpcMessage());

        if (null != jsonrpcResponse) {
            String responseJson = objectMapper.writeValueAsString(jsonrpcResponse);

            SessionConfigVO sessionConfigVO = dynamicContext.getSessionConfigVO();
            sessionConfigVO.getSink().tryEmitNext(ServerSentEvent.<String>builder()
                    .event("message")
                    .data(responseJson)
                    .build());
        }

        return ResponseEntity.accepted().build();
    }

    @Override
    public StrategyHandler<HandleMessageCommandEntity, DefaultMcpMessageFactory.MessageDynamicContext, ResponseEntity<Void>> get(HandleMessageCommandEntity requestParameter, DefaultMcpMessageFactory.MessageDynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

}
