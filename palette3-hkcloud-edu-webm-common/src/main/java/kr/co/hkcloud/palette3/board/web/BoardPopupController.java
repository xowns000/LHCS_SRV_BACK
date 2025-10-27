package kr.co.hkcloud.palette3.board.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.editor.domain.EditorResponse.EditorPropertiesResponse;
import kr.co.hkcloud.palette3.editor.util.EditorRulePropertiesUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadPropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "BoardPopupController",
     description = "게시판/실시간공지사항팝업-화면 컨트롤러")
public class BoardPopupController
{
    private final FileRulePropertiesUtils   fileRulePropertiesUtils;
    private final EditorRulePropertiesUtils editorRulePropertiesUtils;


    /**
     * 
     * @return
     */
    @ApiOperation(value = "게시물 팝업-상세",
                  notes = " 게시물상세팝업으로 이동한다")
    @GetMapping("/board/web/board-detail-popup")
    public String moveBoardDetailPopup(@RequestParam("BRD_ID") String brdId, Model model) throws TelewebWebException
    {
        log.debug("moveBoardDetailPopup");

        final RepositoryTaskTypeCd taskTypeCd;
        final RepositoryPathTypeCd pathTypeCd;

        //이미지관리일 경우
        if("4".equals(brdId)) {
            taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
            pathTypeCd = RepositoryPathTypeCd.images;  //이미지
        }
        else {
            taskTypeCd = RepositoryTaskTypeCd.bbs;    //게시판
            pathTypeCd = RepositoryPathTypeCd.files;  //파일
        }

        final FileDownloadPropertiesResponse fileProperties = fileRulePropertiesUtils.getDownloadProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        model.addAttribute("fileProperties", fileProperties);

        return "board/board-detail-popup";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "게시물 팝업-처리",
                  notes = "처리팝업으로 이동한다")
    @GetMapping("/board/web/board-process-popup")
    public String moveBoardProcessPopup(@RequestParam("BRD_ID") String brdId, Model model) throws TelewebWebException
    {
        log.debug("moveBoardProcessPopup");

        //이미지관리일 경우
        if("4".equals(brdId)) {
            //파일 속성
            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(RepositoryTaskTypeCd.chat, RepositoryPathTypeCd.images); //채팅 > 이미지
            log.debug("fileProperties>>>{}", fileProperties);
            model.addAttribute("fileProperties", fileProperties);
        }
        else {
            //파일 속성
            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(RepositoryTaskTypeCd.bbs, RepositoryPathTypeCd.files); //게시판 > 파일
            log.debug("fileProperties>>>{}", fileProperties);
            model.addAttribute("fileProperties", fileProperties);
        }

        //에디터 속성
        final EditorPropertiesResponse editorProperties = editorRulePropertiesUtils.getProperties(RepositoryTaskTypeCd.bbs, RepositoryPathTypeCd.images); //게시판 > 이미지
        log.debug("editorProperties>>>{}", editorProperties);

        model.addAttribute("editorProperties", editorProperties);

        model.addAttribute("timestamp", DateCmmnUtils.toEpochMilli());
        return "board/board-process-popup";
    }
}
