package com.gaoyifeng.gateway.mcp.trigger.http;


import com.gaoyifeng.gateway.mcp.api.IMcpGatewayService;
import com.gaoyifeng.gateway.mcp.cases.IMcpSessionService;
import com.gaoyifeng.gateway.mcp.types.enums.ResponseCode;
import com.gaoyifeng.gateway.mcp.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/")
public class McpGatewayController implements IMcpGatewayService {

    @Resource
    private IMcpSessionService mcpSessionService;

    public McpGatewayController() {
        System.out.println("xxxx");
    }

    /**
     * 建立sse连接，创建会话
     * <br/>
     * <a href="http://localhost:8777/api-gateway/test10001/mcp/sse">http://localhost:8777/api-gateway/test10001/mcp/sse</a>
     *
     * @param gatewayId 网关ID
     */
    @GetMapping(value = "{gatewayId}/mcp/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Override
    public Flux<ServerSentEvent<String>> establishSSEConnection(@PathVariable("gatewayId") String gatewayId) throws Exception {
        try {
            log.info("建立 MCP SSE 连接，gatewayId:{}", gatewayId);
            if (StringUtils.isBlank(gatewayId)) {
                log.info("非法参数，gateway is null");
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }

            return mcpSessionService.createMcpSession(gatewayId);
        } catch (Exception e) {
            log.error("建立 MCP SSE 连接失败，gatewayId: {}", gatewayId, e);
            throw e;
        }
    }

}
