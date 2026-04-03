package com.gaoyifeng.gateway.mcp.domain.auth.service;

import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.RateLimitCommandEntity;


public interface IAuthRateLimitService {

    /**
     * 限流操作
     * true - 限流
     * false - 未限流
     */
    boolean rateLimit(RateLimitCommandEntity commandEntity);

}
