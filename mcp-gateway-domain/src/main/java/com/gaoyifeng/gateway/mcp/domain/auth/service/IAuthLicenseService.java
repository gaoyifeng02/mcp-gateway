package com.gaoyifeng.gateway.mcp.domain.auth.service;

import com.gaoyifeng.gateway.mcp.domain.auth.model.entity.LicenseCommandEntity;


public interface IAuthLicenseService {

    boolean checkLicense(LicenseCommandEntity commandEntity);

}
