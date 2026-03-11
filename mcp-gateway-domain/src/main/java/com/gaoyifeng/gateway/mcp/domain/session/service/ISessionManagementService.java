package com.gaoyifeng.gateway.mcp.domain.session.service;


import com.gaoyifeng.gateway.mcp.domain.session.model.valobj.SessionConfigVO;

public interface ISessionManagementService {

    /**
     * 创建回话
     * @return 会话配置
     */
    SessionConfigVO createSession(String gatewayId);

    /**
     * 删除回话
     * @param sessionId 会话ID
     */
    void removeSession(String sessionId);

    /**
     * 获取会话
     * @param sessionId 会话ID
     * @return 会话配置
     */
    SessionConfigVO getSession(String sessionId);

    /**
     * 清理过期会话
     */
    void cleanupExpiredSessions();

    /**
     * 关闭服务时，清理资源使用
     */
    void shutdown();
}
