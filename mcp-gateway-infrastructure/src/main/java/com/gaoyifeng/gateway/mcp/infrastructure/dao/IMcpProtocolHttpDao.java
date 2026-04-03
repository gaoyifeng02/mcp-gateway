package com.gaoyifeng.gateway.mcp.infrastructure.dao;

import com.gaoyifeng.gateway.mcp.infrastructure.dao.po.McpProtocolHttpPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMcpProtocolHttpDao {

    int insert(McpProtocolHttpPO po);

    int deleteById(Long id);

    int updateById(McpProtocolHttpPO po);

    McpProtocolHttpPO queryById(Long id);

    List<McpProtocolHttpPO> queryAll();

    McpProtocolHttpPO queryMcpProtocolHttpByProtocolId(Long protocolId);

}

