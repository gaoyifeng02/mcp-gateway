package com.gaoyifeng.gateway.mcp.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * MCP映射配置表 PO对象
 *
 * @author Claude
 * @date 2026-03-16
 */
@Data
public class McpProtocolMappingPO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属网关ID
     */
    private String gatewayId;

    /**
     * 所属工具ID
     */
    private Long toolId;

    /**
     * 映射类型：request-请求参数映射，response-响应数据映射
     */
    private String mappingType;

    /**
     * 父级路径
     */
    private String parentPath;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * MCP完整路径
     */
    private String mcpPath;

    /**
     * MCP数据类型
     */
    private String mcpType;

    /**
     * MCP字段描述
     */
    private String mcpDesc;

    /**
     * 是否必填：0-否，1-是
     */
    private Integer isRequired;

    /**
     * HTTP路径
     */
    private String httpPath;

    /**
     * HTTP位置
     */
    private String httpLocation;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
