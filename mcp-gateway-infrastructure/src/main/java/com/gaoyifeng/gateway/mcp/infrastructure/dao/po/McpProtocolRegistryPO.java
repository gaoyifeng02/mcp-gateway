package com.gaoyifeng.gateway.mcp.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * MCP工具注册表 PO对象
 *
 * @author Claude
 * @date 2026-03-16
 */
@Data
public class McpProtocolRegistryPO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属网关ID
     */
    private String gatewayId;

    /**
     * 工具ID
     */
    private Long toolId;

    /**
     * MCP工具名称
     */
    private String toolName;

    /**
     * 工具类型：function/resource
     */
    private String toolType;

    /**
     * 工具描述
     */
    private String toolDescription;

    /**
     * HTTP接口地址
     */
    private String httpUrl;

    /**
     * HTTP请求方法
     */
    private String httpMethod;

    /**
     * HTTP请求头（JSON格式）
     */
    private String httpHeaders;

    /**
     * 超时时间（毫秒）
     */
    private Integer timeout;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
