package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpProtocolMappingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MCP映射配置表 DAO接口
 *
 * @author Claude
 * @date 2026-03-16
 */
@Mapper
public interface IMcpProtocolMappingDao {

    /**
     * 插入映射配置
     *
     * @param mcpProtocolMappingPo 映射配置对象
     * @return 影响行数
     */
    int insert(McpProtocolMappingPO mcpProtocolMappingPo);

    /**
     * 根据ID更新映射配置
     *
     * @param mcpProtocolMappingPo 映射配置对象
     * @return 影响行数
     */
    int updateById(McpProtocolMappingPO mcpProtocolMappingPo);

    /**
     * 根据ID删除映射配置
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID查询映射配置
     *
     * @param id 主键ID
     * @return 映射配置对象
     */
    McpProtocolMappingPO queryById(@Param("id") Long id);

    /**
     * 根据工具ID查询映射配置列表
     *
     * @param toolId 工具ID
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryByToolId(@Param("toolId") Long toolId);

    /**
     * 根据工具ID和映射类型查询映射配置列表
     *
     * @param toolId 工具ID
     * @param mappingType 映射类型
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryByToolIdAndMappingType(@Param("toolId") Long toolId, @Param("mappingType") String mappingType);

    /**
     * 根据网关ID查询映射配置列表
     *
     * @param gatewayId 网关ID
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryByGatewayId(@Param("gatewayId") String gatewayId);

    /**
     * 根据父级路径查询映射配置列表
     *
     * @param parentPath 父级路径
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryByParentPath(@Param("parentPath") String parentPath);

    /**
     * 根据MCP路径查询映射配置
     *
     * @param mcpPath MCP路径
     * @return 映射配置对象
     */
    McpProtocolMappingPO queryByMcpPath(@Param("mcpPath") String mcpPath);

    /**
     * 查询所有映射配置
     *
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryAll();

    /**
     * 根据映射类型查询映射配置列表
     *
     * @param mappingType 映射类型
     * @return 映射配置列表
     */
    List<McpProtocolMappingPO> queryByMappingType(@Param("mappingType") String mappingType);
}
