package com.gaoyifeng.gateway.mcp.infrastructure.dao.po;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * MCP网关配置表 PO对象
 *
 * @author Claude
 * @date 2026-03-16
 */
@Data
@Builder
public class McpGatewayPO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 网关唯一标识
     */
    private String gatewayId;

    /**
     * 网关名称
     */
    private String gatewayName;

    /**
     * 网关描述
     */
    private String gatewayDesc;

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
