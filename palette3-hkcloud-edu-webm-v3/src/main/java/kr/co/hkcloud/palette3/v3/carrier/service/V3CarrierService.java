package kr.co.hkcloud.palette3.v3.carrier.service;

import java.text.ParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtDetailResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtRegistRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtSendSmsRequestDto;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;

public interface V3CarrierService {
    public Page<V3CarrierVstRsvtListResponseDto> selectVstRsvtList(V3CarrierVstRsvtListRequestDto requestDto, Pageable pageable) throws BusinessException;
    
    public V3CarrierVstRsvtDetailResponseDto selectVstRsvtDetail(V3CommonRequestDto requestDto) throws BusinessException;
    
    public String vstRsvtProc(V3CarrierVstRsvtRegistRequestDto requestDto, BindingResult bindingResult) throws BusinessException, TelewebAppException, ParseException;
    
    public void sendSms(V3CarrierVstRsvtSendSmsRequestDto requestDto) throws BusinessException;
}
