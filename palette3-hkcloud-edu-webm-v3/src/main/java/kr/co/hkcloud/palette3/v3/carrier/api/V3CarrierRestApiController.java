package kr.co.hkcloud.palette3.v3.carrier.api;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtDetailResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtRegistRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtRegistResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtSendSmsRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.service.V3CarrierService;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import kr.co.hkcloud.palette3.v3.common.dto.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "V3AuthRestApiController", description = "방문 예약 restful api 컨트롤러")
public class V3CarrierRestApiController {
    private final V3CarrierService v3CarrierService;
    private final JwtTokenProvider jwtTokenProvider;
    
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    
    @ApiOperation(value = "방문 예약 목록", notes = "방문 예약 목록을 요청 받아 조건에 맞는 목록을 조회한다.")
    @GetMapping("/v3-api/carrier/getVstRsvtList")
    public ResponseEntity<Page<V3CarrierVstRsvtListResponseDto>> getVstRsvtList(@Validated @ModelAttribute V3CarrierVstRsvtListRequestDto requestDto, Pageable pageable, HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        requestDto.setCustcoId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "custcoId")));
        
        Page<V3CarrierVstRsvtListResponseDto> responsePage = v3CarrierService.selectVstRsvtList(requestDto, pageable);
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "방문 예약 상세", notes = "방문 예약 상세 정보를 요청 받아 조건에 맞는 상세 정보를 조회한다.")
    @GetMapping("/v3-api/carrier/getVstRsvtDetail/{vstRsvtId}")
    public ResponseEntity<V3CarrierVstRsvtDetailResponseDto> getVstRsvtDetail(@PathVariable @NotNull Long vstRsvtId, HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        V3CommonRequestDto requestDto = new V3CommonRequestDto();
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        requestDto.setCustcoId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "custcoId")));
        requestDto.setKey(vstRsvtId);
        
        V3CarrierVstRsvtDetailResponseDto responseDto = v3CarrierService.selectVstRsvtDetail(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "방문 예약 등록", notes = "방문 예약 등록을 요청 받아 신규 방문 예약을 저장한다.")
    @PostMapping(path = "/v3-api/carrier/registVstRsvt", consumes = {"multipart/form-data"})
    public ResponseEntity<V3CarrierVstRsvtRegistResponseDto> registVstRsvt(@Validated(ValidationGroups.RegistGroup.class) @ModelAttribute V3CarrierVstRsvtRegistRequestDto requestDto, HttpServletRequest req, final BindingResult bindingResult) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException, TelewebAppException, BusinessException, ParseException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        requestDto.setCustcoId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "custcoId")));
        
        String resultVstRsvtId = v3CarrierService.vstRsvtProc(requestDto, bindingResult);
        V3CarrierVstRsvtRegistResponseDto responseDto = new V3CarrierVstRsvtRegistResponseDto();
        responseDto.setVstRsvtId(Long.parseLong(resultVstRsvtId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "방문 예약 수정", notes = "방문 예약 수정을 요청 받아 방문 예약을 수정한다.")
    @PutMapping(path = "/v3-api/carrier/updateVstRsvt/{vstRsvtId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> updateVstRsvt(@PathVariable @NotNull Long vstRsvtId, @Validated(ValidationGroups.UpdateGroup.class) @ModelAttribute V3CarrierVstRsvtRegistRequestDto requestDto, HttpServletRequest req, final BindingResult bindingResult) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException, TelewebAppException, BusinessException, ParseException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        requestDto.setVstRsvtId(vstRsvtId);
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        requestDto.setCustcoId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "custcoId")));
        
        v3CarrierService.vstRsvtProc(requestDto, bindingResult);
        
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    
    
    @ApiOperation(value = "문자 발송", notes = "문자 발송을 요청 받아 문자를 발송한다.")
    @PostMapping(path = "/v3-api/carrier/sendSms/{vstRsvtId}")
    public ResponseEntity<Object> sendSms(@PathVariable @NotNull Long vstRsvtId, @Validated @RequestBody V3CarrierVstRsvtSendSmsRequestDto requestDto, HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        requestDto.setVstRsvtId(vstRsvtId);
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        requestDto.setCustcoId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "custcoId")));
        
        v3CarrierService.sendSms(requestDto);
        
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
