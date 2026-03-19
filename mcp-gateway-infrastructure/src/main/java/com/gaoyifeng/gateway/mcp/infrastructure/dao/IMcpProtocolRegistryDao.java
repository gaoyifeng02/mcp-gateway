package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpProtocolRegistryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MCP工具注册表 DAO接口
 *
 * @author Claude
 * @date 2026-03-16
 */
@Mapper
public interface IMcpProtocolRegistryDao {

    /**
     * 插入工具注册信息
     *
     * @param mcpProtocolRegistryPo 工具注册对象
     * @return 影响行数
     */
    int insert(McpProtocolRegistryPO mcpProtocolRegistryPo);

    /**
     * 根据ID更新工具注册信息
     *
     * @param mcpProtocolRegistryPo 工具注册对象
     * @return 影响行数
     */
    int updateById(McpProtocolRegistryPO mcpProtocolRegistryPo);

    /**
     * 根据ID删除工具注册信息
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询工具注册信息
     *
     * @param id 主键ID
     * @return 工具注册对象
     */
    McpProtocolRegistryPO queryById(@Param("id") Long id);

    /**
     * 根据网关ID查询工具注册列表
     *
     * @param gatewayId 网关ID
     * @return 工具注册列表
     */
    List<McpProtocolRegistryPO> queryByGatewayId(@Param("gatewayId") String gatewayId);

    /**
     * 根据工具ID查询工具注册信息
     *
     * @param toolId 工具ID
     * @return 工具注册对象
     */
    McpProtocolRegistryPO queryByToolId(@Param("toolId") Long toolId);

    /**
     * 根据网关ID和工具名称查询工具注册信息
     *
     * @param gatewayId 网关ID
     * @param toolName 工具名称
     * @return 工具注册对象
     */
    McpProtocolRegistryPO queryByGatewayIdAndToolName(@Param("gatewayId") String gatewayId, @Param("toolName") String toolName);

    /**
     * 查询所有工具注册信息
     *
     * @return 工具注册列表
     */
    List<McpProtocolRegistryPO> queryAll();

    /**
     * 根据状态查询工具注册列表
     *
     * @param status 状态
     * @return 工具注册列表
     */
    List<McpProtocolRegistryPO> queryByStatus(@Param("status") Integer status);


    McpProtocolRegistryPO queryMcpProtocolRegistryByGatewayId(String gatewayId);


}
