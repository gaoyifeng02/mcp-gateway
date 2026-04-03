package com.gaoyifeng.gateway.mcp.cases;

import com.gaoyifeng.gateway.mcp.domain.session.model.entity.HandleMessageCommandEntity;
import org.springframework.http.ResponseEntity;


public interface IMcpMessageService {

    ResponseEntity<Void> handleMessage(HandleMessageCommandEntity commandEntity) throws Exception;

}
