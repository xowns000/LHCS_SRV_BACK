package kr.co.hkcloud.palette3.v3.auth.service;

import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO.TokenInfo;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3AuthLoginRequestDto;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3UserAppUpdateTokenRequestDto;

public interface V3AuthService {
    
    public String selectTenantByLgnId(V3AuthLoginRequestDto requestDto) throws BusinessException;
    public TokenInfo changeTokenExpireTime(String currentAccessToken) throws BusinessException;
    public long updateUserToken(V3UserAppUpdateTokenRequestDto requestDto) throws BusinessException;
    
}
