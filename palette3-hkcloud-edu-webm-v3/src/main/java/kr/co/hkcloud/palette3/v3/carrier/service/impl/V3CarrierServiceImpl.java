package kr.co.hkcloud.palette3.v3.carrier.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileUploadUtils;
import kr.co.hkcloud.palette3.file.util.FileUploadValidator;
import kr.co.hkcloud.palette3.message.app.MessageService;
import kr.co.hkcloud.palette3.setting.cuttType.app.CuttTypeService;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtDetailResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtListResponseDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtRegistRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.api.dto.V3CarrierVstRsvtSendSmsRequestDto;
import kr.co.hkcloud.palette3.v3.carrier.service.V3CarrierService;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestDto;
import kr.co.hkcloud.palette3.v3.common.dto.V3CommonRequestListDto;
import kr.co.hkcloud.palette3.vst.app.VstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
@Service("v3CarrierService")
public class V3CarrierServiceImpl implements V3CarrierService {
    private final VstService vstService;
    private final MessageService messageService;
    private final CuttTypeService cuttTypeService;
    private final FileUploadValidator fileUploadValidator;
    private final FileUploadUtils fileUploadUtils;
    
    @Value("${stomp.allow.origin}")
    public String STOMP_ALLOW_ORIGIN;
    
    private final SqlSessionTemplate paletteRoutingSqlSessionTemplate;
    private String namespace = "kr.co.hkcloud.palette3.v3.carrier.dao.V3CarrierMapper.";
    
    @Override
    @Transactional(readOnly=true)
    public Page<V3CarrierVstRsvtListResponseDto> selectVstRsvtList(V3CarrierVstRsvtListRequestDto requestDto, Pageable pageable) throws BusinessException {
        // 빌더 패턴으로 data, pageable 파라미터에 데이터 주입
        V3CommonRequestListDto<?> requestListDto = V3CommonRequestListDto.builder()
            .data(requestDto)
            .pageable(pageable)
            .build();
         
        List<V3CarrierVstRsvtListResponseDto> list = paletteRoutingSqlSessionTemplate.selectList(namespace + "selectVstRsvtList", requestListDto);
        return new PageImpl<>(list);
    }
    
    @Override
    @Transactional(readOnly=true)
    public V3CarrierVstRsvtDetailResponseDto selectVstRsvtDetail(V3CommonRequestDto requestDto) throws BusinessException {
        V3CarrierVstRsvtDetailResponseDto responseDto = paletteRoutingSqlSessionTemplate.selectOne(namespace + "selectVstRsvtDetail", requestDto);
        
        if (responseDto == null) {
            throw new BusinessException("Entity Not Found", ErrorCode.ENTITY_NOT_FOUND);
        } else {
            responseDto.setFileDomain(STOMP_ALLOW_ORIGIN);
            
            //이미지 목록 조회
            if(StringUtils.isNotEmpty(responseDto.getImgFileGroupKey())) {
                responseDto.setImgList(paletteRoutingSqlSessionTemplate.selectList(namespace + "selectVstRsvtFileList", responseDto.getImgFileGroupKey()));
            }
            //동영상 목록 조회
            if(StringUtils.isNotEmpty(responseDto.getVdoFileGroupKey())) {
                responseDto.setVdoList(paletteRoutingSqlSessionTemplate.selectList(namespace + "selectVstRsvtFileList", responseDto.getVdoFileGroupKey()));
            }
        }
        
        return responseDto;
    }
    
    @Override
    @Transactional(readOnly=false)
    public String vstRsvtProc(V3CarrierVstRsvtRegistRequestDto requestDto, BindingResult bindingResult) throws BusinessException, TelewebAppException, ParseException {
        
        //첨부 파일 먼저 생성.
        String imgFileGroupKey = checkUploadFiles(requestDto, bindingResult, RepositoryPathTypeCd.images);
        String vdoFileGroupKey = checkUploadFiles(requestDto, bindingResult, RepositoryPathTypeCd.videos);
        
        
        TelewebJSON jsonParams = new TelewebJSON();
        if(requestDto.getVstRsvtId() != null) {
            jsonParams.setString("VST_RSVT_ID", String.valueOf(requestDto.getVstRsvtId()));
        }
        jsonParams.setInt("CUSTCO_ID", (int)requestDto.getCustcoId());
        jsonParams.setInt("VSTR_ID", (int)requestDto.getUserId());
        jsonParams.setInt("USER_ID", (int)requestDto.getUserId());
        
        jsonParams.setString("CUST_NM", requestDto.getCustNm());
        jsonParams.setString("CUST_TELNO", requestDto.getCustTelno());
        jsonParams.setString("ZIP", requestDto.getZip());
        jsonParams.setString("ADDR", requestDto.getAddr());
        jsonParams.setString("ADDR_DTL", requestDto.getAddrDtl());
        jsonParams.setString("STTS_CD", requestDto.getSttsCd());
        jsonParams.setString("STTS_DTL_CD", requestDto.getSttsDtlCd());
        jsonParams.setString("CN", requestDto.getCn());
        jsonParams.setString("SRVC_TYPE_CD", requestDto.getSrvcTypeCd());
        jsonParams.setString("PRDCT_TYPE_ID", requestDto.getPrdctTypeId());
        jsonParams.setString("RSVT_BGNG_DT", requestDto.getRsvtBgngDt());
        jsonParams.setString("RSVT_END_DT", requestDto.getRsvtEndDt());
        jsonParams.setString("DMND_MTTR", requestDto.getDmndMttr());
        jsonParams.setString("EXCPTN_MTTR", requestDto.getExcptnMttr());
        
        jsonParams.setString("IMG_FILE_GROUP_KEY", imgFileGroupKey);
        jsonParams.setString("VDO_FILE_GROUP_KEY", vdoFileGroupKey);
        
        
        TelewebJSON objRetParams = vstService.vstRsvtProc(jsonParams);
        return objRetParams.getString("VST_RSVT_ID");
    }
    
    
    @Transactional(readOnly=false)
    public void sendSms(V3CarrierVstRsvtSendSmsRequestDto requestDto) throws BusinessException {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("CUSTCO_ID", String.valueOf(requestDto.getCustcoId()));
        jsonParams.setString("USER_ID", String.valueOf(requestDto.getUserId()));
        try {
            //발신 전화번호 조회 및 세팅
            TelewebJSON retJsonParams = cuttTypeService.dsptchNoList(jsonParams);
            JSONArray dsptchNoList = retJsonParams.getDataObject();
            if(dsptchNoList.size() > 0) {
                JSONObject dataObject = dsptchNoList.getJSONObject(0);
                jsonParams.setString("callback_number", dataObject.getString("DSPTCH_NO"));// 발신전화번호
            } else {
                throw new BusinessException("발신전화번호가 없습니다.", ErrorCode.INVALID_TYPE_VALUE);
            }
            
            String sndngSeCd = "SMS";
            if(requestDto.getByteSize() > 90) {
                sndngSeCd = "LMS";
            }
            
            jsonParams.setString("auth_code", "");
            jsonParams.setString("phone_number", requestDto.getCustTelno());// 사용자 전화번호
            jsonParams.setString("message", requestDto.getSndngCn());// 사용자에게 전달될 메시지
            jsonParams.setString("send_date", "");// 발송예정일
            jsonParams.setString("SNDNG_SE_CD", sndngSeCd);// 발송구분코드 - SMS/LMS/MMS/ATALK 구분
            jsonParams.setString("img_url", "");// 이미지 파일 정보
            jsonParams.setString("tenantId", TenantContext.getCurrentTenant());// 발송구분코드 - SMS/LMS/MMS/ATALK 구분
            
            messageService.sendInfo(jsonParams);
        } catch(Exception e) {
            throw new BusinessException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @Transactional(readOnly=false)
    private String checkUploadFiles(V3CarrierVstRsvtRegistRequestDto requestDto, BindingResult bindingResult, RepositoryPathTypeCd pathTypeCd) throws BusinessException {
        String fileGroupKey = null;
        
        MultipartFile[] files = null;
        switch (pathTypeCd) {
            case images:
                fileGroupKey = requestDto.getImgFileGroupKey();
                files = requestDto.getImgFiles();
                break;
            case videos:
                fileGroupKey = requestDto.getVdoFileGroupKey();
                files = requestDto.getVdoFiles();
                break;
            default:
                break;
        }
        
        
        if(files != null && files.length > 0) {
            FilePropertiesResponse filePropertiesResponse = new FilePropertiesResponse();
            filePropertiesResponse.setTaskTypeCd(RepositoryTaskTypeCd.vst);
            filePropertiesResponse.setPathTypeCd(pathTypeCd);
            if (filePropertiesResponse.getTrgtTypeCd() == null) {
                filePropertiesResponse.setTrgtTypeCd(RepositoryTrgtTypeCd.FILE);
            }
            filePropertiesResponse.setCustcoId( String.valueOf(requestDto.getCustcoId()));
            filePropertiesResponse.setUserId( (int)requestDto.getUserId());
            
            
            FileUploadRequests fileUploadRequests = FileUploadRequests.builder()
                .userfiles(files)
                .fileGroupKey(fileGroupKey)
                .custcoId(String.valueOf(requestDto.getCustcoId()))
                .userId((int)requestDto.getUserId())
                .build();
            
            //Validation 체크
            Map<String, Object> validateMap = new HashMap<String, Object>();
            validateMap.put("filePropertiesResponse", filePropertiesResponse);
            validateMap.put("fileUploadRequests", fileUploadRequests);

            fileUploadValidator.validate(validateMap, bindingResult);

            List<FileUploadResponse> uploadResponseList = fileUploadUtils.store(filePropertiesResponse, fileUploadRequests);
            fileGroupKey = uploadResponseList.get(0).getFileGroupKey();
        }
        return fileGroupKey;
    }
}
