package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpGatewayPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MCP网关配置表 DAO接口
 *
 * @author Claude
 * @date 2026-03-16
 */
@Mapper
public interface IMcpGatewayDao {

    /**
     * 插入网关配置
     *
     * @param mcpGatewayPo 网关配置对象
     * @return 影响行数
     */
    int insert(McpGatewayPO mcpGatewayPo);

    /**
     * 根据ID更新网关配置
     *
     * @param mcpGatewayPo 网关配置对象
     * @return 影响行数
     */
    int updateById(McpGatewayPO mcpGatewayPo);

    /**
     * 根据ID删除网关配置
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询网关配置
     *
     * @param id 主键ID
     * @return 网关配置对象
     */
    McpGatewayPO queryById(@Param("id") Long id);

    /**
     * 根据网关ID查询网关配置
     *
     * @param gatewayId 网关ID
     * @return 网关配置对象
     */
    McpGatewayPO queryByGatewayId(@Param("gatewayId") String gatewayId);

    /**
     * 查询所有网关配置
     *
     * @return 网关配置列表
     */
    List<McpGatewayPO> queryAll();

    /**
     * 根据状态查询网关配置列表
     *
     * @param status 状态
     * @return 网关配置列表
     */
    List<McpGatewayPO> queryByStatus(@Param("status") Integer status);


    McpGatewayPO queryMcpGatewayByGatewayId(String gatewayId);


}
