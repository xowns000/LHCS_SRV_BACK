package kr.co.hkcloud.palette3.board.api;

import kr.co.hkcloud.palette3.board.app.CommentService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 댓글 컨트롤러
 *
 * @author Jun Hyeong Jo
 * @since 2025-01-09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentRestController {
    private final CommentService commentService;

    /**
     * 게시글의 댓글을 모두 가져온다.
     *
     * @param request 요청 파라미터:<br>
     *                - BBS_ID    게시판ID<br>
     *                - PST_ID    게시글ID<br>
     *                - CUSTCO_ID 고객사ID
     * @return TelewebJSON
     */
    @PostMapping(value = "/api/board/post/comments/get")
    public Object getPostComments(@TelewebJsonParam TelewebJSON request) {
        return commentService.getPostComments(request);
    }

    /**
     * 게시글의 댓글을 등록한다.
     *
     * @param request 요청 파라미터:<br>
     *                - BBS_ID       게시판ID<br>
     *                - PST_ID       게시글ID<br>
     *                - CUSTCO_ID    고객사ID<br>
     *                - HIGR_CMNT_ID 상위댓글ID<br>
     *                - USER_ID      현재 유저ID<br>
     *                - CMNT         댓글
     * @return TelewebJSON
     */
    @PostMapping(value = "/api/board/post/comment/create")
    public Object createPostComment(@TelewebJsonParam TelewebJSON request) {
        return commentService.createPostComment(request);
    }

    /**
     * 게시글의 댓글을 업데이트한다.
     *
     * @param request 요청 파라미터:<br>
     *                - BBS_ID    게시판ID<br>
     *                - PST_ID    게시글ID<br>
     *                - CUSTCO_ID 고객사ID<br>
     *                - CMNT_ID   댓글ID<br>
     *                - CMNT      댓글
     * @return TelewebJSON
     */
    @PostMapping(value = "/api/board/post/comment/update")
    public Object updatePostComment(@TelewebJsonParam TelewebJSON request) {
        return commentService.updatePostComment(request);
    }

    /**
     * 게시글의 댓글을 삭제한다.
     *
     * @param request 요청 파라미터:<br>
     *                - BBS_ID    게시판ID<br>
     *                - PST_ID    게시글ID<br>
     *                - CUSTCO_ID 고객사ID<br>
     *                - CMNT_ID   댓글ID
     * @return TelewebJSON
     */
    @PostMapping(value = "/api/board/post/comment/delete")
    public Object deletePostComment(@TelewebJsonParam TelewebJSON request) {
        return commentService.deletePostComment(request);
    }
}
