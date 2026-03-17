package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayAuthPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户网关权限表 DAO接口
 *
 * @author Claude
 * @date 2026-03-16
 */@Mapper
public interface IMcpGatewayAuthDao {

    /**
     * 插入网关权限配置
     *
     * @param mcpGatewayAuthPo 网关权限对象
     * @return 影响行数
     */
    int insert(McpGatewayAuthPO mcpGatewayAuthPo);

    /**
     * 根据ID更新网关权限配置
     *
     * @param mcpGatewayAuthPo 网关权限对象
     * @return 影响行数
     */
    int updateById(McpGatewayAuthPO mcpGatewayAuthPo);

    /**
     * 根据ID删除网关权限配置
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询网关权限配置
     *
     * @param id 主键ID
     * @return 网关权限对象
     */
    McpGatewayAuthPO queryById(@Param("id") Long id);

    /**
     * 根据网关ID查询网关权限配置
     *
     * @param gatewayId 网关ID
     * @return 网关权限对象
     */
    McpGatewayAuthPO queryByGatewayId(@Param("gatewayId") String gatewayId);

    /**
     * 根据API密钥查询网关权限配置
     *
     * @param apiKey API密钥
     * @return 网关权限对象
     */
    McpGatewayAuthPO queryByApiKey(@Param("apiKey") String apiKey);

    /**
     * 查询所有网关权限配置
     *
     * @return 网关权限配置列表
     */
    List<McpGatewayAuthPO> queryAll();

    /**
     * 根据状态查询网关权限配置列表
     *
     * @param status 状态
     * @return 网关权限配置列表
     */
    List<McpGatewayAuthPO> queryByStatus(@Param("status") Integer status);
}
