package kr.co.hkcloud.palette3.phone.transfer.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.phone.transfer.enumer.TransferAltmntType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kr.co.hkcloud.palette3.constant.Constants.*;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service("phoneTransferManageService")
public class PhoneTransferManageServiceImpl implements PhoneTransferManageService {
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;

    /**
     * 이관 배분 현황 count 조회
     * ㄴ target opd cd가 USER인 경우는 plt_cutt_trnsf 테이블의 taret_user_id 그대로 사용하기 때문에 쿼리 분기 처리
     * @param jsonParams : 조회기간 (ex: 20250113000000 ~ 20250120235959)
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTransferDstbCount(TelewebJSON jsonParams) throws TelewebAppException {
        //최종결과값 반환
        return mobjDao.select(TRANSFER_ALLOCATION_MAPPER_NAME, "selectTransferDstbCount", jsonParams);
    }

    /**
     * 상담사 별 이관 배분 현황 COUNT 조회
     * ㄴ target opd cd가 USER인 경우는 plt_cutt_trnsf 테이블의 taret_user_id 그대로 사용하기 때문에 쿼리 분기 처리
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTransferAgentDstbCount(TelewebJSON jsonParams) throws TelewebAppException {
        //최종결과값 반환
        return mobjDao.select(TRANSFER_ALLOCATION_MAPPER_NAME, "selectTransferAgentDstbCount", jsonParams);
    }

    /**
     * 이관 상담사들에게 배분
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertTransferManageDstb(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        String altMntInfo = jsonParams.getString(ASSIGNMENT_INFO_BY_COUNSELOR); // 상담사들에게 몇 건 배분할지에 대한 정보 ex) 'user_id':'cnt'/'user_id':'cnt'/...
        String[] splitAltMntInfo = altMntInfo.split("/");

        // 배분할 대상이 되는 이관 데이터만 조회하기 위해 list set
        List<String> cuttTrnsfIds = new ArrayList<>();
        String CUTT_TRNSF_ID_LIST = jsonParams.getString(NOT_ASSIGNMENT_LIST);
        CUTT_TRNSF_ID_LIST = CUTT_TRNSF_ID_LIST.replace(ESCAPE_LEFT_SQUARE_BRACKETS, LEFT_SQUARE_BRACKETS)
                                               .replace(ESCAPE_RIGHT_SQUARE_BRACKETS, RIGHT_SQUARE_BRACKETS);
        JSONArray arryCuttTrnsfIdList = JSONArray.fromObject(CUTT_TRNSF_ID_LIST);
        for(int i=0;i<arryCuttTrnsfIdList.size();i++) {
            cuttTrnsfIds.add(arryCuttTrnsfIdList.getJSONObject(i).getString(CONSULTATION_TRANSFER_ID_KEY));
        }

        for(int i=0;i<splitAltMntInfo.length;i++) {
            String[] splitDstbInfo = splitAltMntInfo[i].split(":");
            String trgtUserId = splitDstbInfo[0]; // 배분할 상담사
            String dstbCnt = splitDstbInfo[1]; // 배분 건수

            // 해당 상담사에게 배분할 건수만큼 조회한다.
            TelewebJSON selectDstbParams = new TelewebJSON();
            selectDstbParams.setString(CUSTOMER_COMPANY_ID_KEY, jsonParams.getString(CUSTOMER_COMPANY_ID_KEY));
            selectDstbParams.setString(DISTRIBUTED_COUNT, dstbCnt);
            selectDstbParams.setObject(CONSULTATION_TRANSFER_ID_KEY_LIST, 0, cuttTrnsfIds);
            TelewebJSON selectDstbInfo = new TelewebJSON();
            selectDstbInfo = mobjDao.select(TRANSFER_ALLOCATION_MAPPER_NAME, "selectTransferDstbInfo", selectDstbParams);

            // 배분할 이관 데이터가 조회됐다면
            if(selectDstbInfo.getSize() > 0) {
                for(int j=0;j<selectDstbInfo.getSize();j++) {
                    String cuttTrnsfId = selectDstbInfo.getString(CONSULTATION_TRANSFER_ID_KEY, j); // 해당 이관 데이터의 id
                    String targetOptCd = selectDstbInfo.getString(TARGET_OPTION_CODE_KEY, j); // 해당 이관 데이터의 이관 담당자 옵션(USER, DEPT, CODE)
                    TelewebJSON transferDstrParams = new TelewebJSON();
                    TelewebJSON historyParams = new TelewebJSON();

                    // 이관 담당자 옵션에 따라 분기 처리
                    switch (targetOptCd) {
                        case "USER": // USER인 경우 PLT_CUTT_TRNSF 테이블의 TRGT_USER_ID 컬럼을 update
                            transferDstrParams.setString(CONSULTATION_TRANSFER_ID_KEY, cuttTrnsfId);
                            transferDstrParams.setString(TARGET_USER_ID_KEY, trgtUserId);
                            objRetParams = mobjDao.update(TRANSFER_ALLOCATION_MAPPER_NAME, "updateTransferDstr", transferDstrParams);
                            break;
                        case "DEPT": // DEPT와 CODE인 경우 상태를 REQ(대기)로 update하고
                        case "CODE": // PLT_CUTT_TRNSF_ALTMNT 테이블에 insert
                            transferDstrParams.setString(CONSULTATION_TRANSFER_ID_KEY, cuttTrnsfId);
                            transferDstrParams.setString(CONSULTANT_ID_KEY, trgtUserId);
                            transferDstrParams.setString(USER_ID_KEY, jsonParams.getString(USER_ID_KEY));
                            objRetParams = mobjDao.update(TRANSFER_ALLOCATION_MAPPER_NAME, "updateTransferDstr", transferDstrParams);
                            objRetParams = mobjDao.insert(TRANSFER_ALLOCATION_MAPPER_NAME, "insertTransferDstr", transferDstrParams);
                            break;
                        default:
                            throw new PaletteAppException("Palette App Exception :::::: 정의되지 않은 이관 담당자 옵션 코드 입니다.");
                    }

                    // 배분 이력 HISTORY
                    historyParams.setString(CONSULTATION_TRANSFER_ID_KEY, cuttTrnsfId);
                    historyParams.setString(CONSULTANT_ID_KEY, trgtUserId);
                    historyParams.setString(USER_ID_KEY, jsonParams.getString(USER_ID_KEY));
                    addTransferHistory(historyParams, TransferAltmntType.ASSIGNED);
                }
            }
        }

        return objRetParams;
    }

    /**
     * 이관 상담사 회수
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteTransferManageDstb(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        int cnt = 0;

        JSONArray jsonArray = jsonParams.getDataObject("SEL_CUTT_TRNSF_LIST");

        for(int i=0;i<jsonArray.size();i++) {
            TelewebJSON jsonDelDstbParams = new TelewebJSON();
            TelewebJSON historyParams = new TelewebJSON();
            String cuttTrnsfId = jsonArray.getJSONObject(i).getString("CUTT_TRNSF_ID");
            String trgtUserId = jsonArray.getJSONObject(i).getString("TRGT_USER_ID");
            jsonDelDstbParams.setString("CUTT_TRNSF_ID", cuttTrnsfId);
            jsonDelDstbParams.setString("STTS_CD", "REQ");
            switch (jsonArray.getJSONObject(i).getString("TARGET_OPT_CD")) {
                case "DEPT":
                case "CODE":
                    // 담당자 옵션 코드가 DEPT, CODE 일 떄
                    // 이관 배분 데이터 삭제
                    objRetParams = mobjDao.delete(TRANSFER_ALLOCATION_MAPPER_NAME, "deleteTransferDstr", jsonDelDstbParams);
                    break;
                default:
                    // 담당자 옵션 코드가 USER(NULL)일 때
                    // 이관 테이블에 담당자를 0으로 업데이트
                    jsonDelDstbParams.setString("TRGT_USER_ID", "0");
                    objRetParams = mobjDao.update(TRANSFER_ALLOCATION_MAPPER_NAME, "updateTransferDstr", jsonDelDstbParams);
                    break;
            }

            // 배분 이력 HISTORY
            historyParams.setString("CUTT_TRNSF_ID", cuttTrnsfId);
            historyParams.setString("CUSL_ID", trgtUserId);
            historyParams.setString("USER_ID", jsonParams.getString("USER_ID"));
            addTransferHistory(historyParams, TransferAltmntType.REVOKED);
            cnt += objRetParams.getHeaderInt("COUNT");
        }

        objRetParams.setInt("DEL_COUNT", cnt);

        return objRetParams;
    }

    private void addTransferHistory(@NonNull final TelewebJSON transfer, @NonNull final TransferAltmntType type) {
        final TelewebJSON insertHistoryParams = new TelewebJSON();
        insertHistoryParams.setInt(TRANSFER_ALLOCATION_SEQ_KEY, innbCreatCmmnService.createSeqNo(TRANSFER_ALLOCATION_SEQ_KEY));
        insertHistoryParams.setString(CONSULTATION_TRANSFER_ID_KEY, transfer.getString(CONSULTATION_TRANSFER_ID_KEY));
        insertHistoryParams.setString(CONSULTANT_ID_KEY, transfer.getString(CONSULTANT_ID_KEY));
        insertHistoryParams.setString(TYPE_KEY, type.getState());
        insertHistoryParams.setString(USER_ID_KEY, transfer.getString(USER_ID_KEY));

        mobjDao.insert(TRANSFER_ALLOCATION_MAPPER_NAME, "insertTransferHistory", insertHistoryParams);
    }
}
