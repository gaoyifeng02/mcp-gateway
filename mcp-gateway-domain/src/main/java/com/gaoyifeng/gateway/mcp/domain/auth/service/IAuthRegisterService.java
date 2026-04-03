package com.gaoyifeng.gateway.mcp.domain.auth.service;

import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.RegisterCommandEntity;


public interface IAuthRegisterService {

    String register(RegisterCommandEntity commandEntity);

}
