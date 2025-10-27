package kr.co.hkcloud.palette3.board.app;

import static kr.co.hkcloud.palette3.constant.Constants.COMMENT_TOAST_SHOW_POSITION;
import static kr.co.hkcloud.palette3.constant.Constants.COMMENT_TOAST_SHOW_SECOND;
import static kr.co.hkcloud.palette3.constant.Constants.NEW_CHILD_COMMENT_MESSAGE;
import static kr.co.hkcloud.palette3.constant.Constants.NEW_POST_COMMENT_MESSAGE;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.BOARD_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.COMMENT_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.COMMENT_MAPPER_NAME;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.CUSTOMER_COMPANY_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.HAS_POST_PERMISSION_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.HIGHER_COMMENT_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.POST_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.POST_MAPPER_NAME;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.POST_TITLE_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.REGISTER_ID_KEY;
import static kr.co.hkcloud.palette3.constant.DatabaseConstants.USER_ID_KEY;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnServiceImpl;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage.MessageType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.common.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 게시글 댓글 서비스 구현체
 *
 * @author Jun Hyeong Jo
 * @since 2025-01-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final InnbCreatCmmnServiceImpl innbCreatCmmnService;
    private final SseService sseService;
    private final TwbComDAO dao;

    /**
     * 게시글의 댓글을 모두 조회한다.
     *
     * @param request
     * @return TelewebJSON
     */
    @Override
    public TelewebJSON getPostComments(@NonNull TelewebJSON request) {
        return dao.select(COMMENT_MAPPER_NAME, "selectPostComments", request);
    }

    /**
     * 게시글의 댓글을 등록한다.
     *
     * @param request
     * @return TelewebJSON
     */
    @Override
    public TelewebJSON createPostComment(@NonNull TelewebJSON request) {
        request.setInt(COMMENT_ID_KEY, innbCreatCmmnService.createSeqNo(COMMENT_ID_KEY));

        final TelewebJSON insertCommentResult = dao.insert(COMMENT_MAPPER_NAME, "insertPostComment", request);
        final boolean isSuccess = !insertCommentResult.getHeaderBoolean("ERROR_FLAG");

        if (isSuccess) {
            sendCommentNotificationViaSSE(request);
        }
        return insertCommentResult;
    }

    /**
     * 게시글의 댓글을 모두 업데이트한다.
     *
     * @param request
     * @return TelewebJSON
     */
    @Override
    public TelewebJSON updatePostComment(@NonNull TelewebJSON request) {
        //update comment
        return dao.update(COMMENT_MAPPER_NAME, "updatePostComment", request);
    }

    /**
     * 게시글의 댓글을 삭제한다.
     *
     * @param request
     * @return TelewebJSON
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public TelewebJSON deletePostComment(@NonNull TelewebJSON request) {
        //validate, 게시글 관리 권한이 있는지 (menu_id == 39)
        final TelewebJSON getAuthority = dao.select(COMMENT_MAPPER_NAME, "hasPostPermission", request);
        request.setBoolean(HAS_POST_PERMISSION_KEY, 0, getAuthority.getBoolean(HAS_POST_PERMISSION_KEY));

        //delete comment
        return dao.update(COMMENT_MAPPER_NAME, "deletePostComment", request);
    }

    private void sendCommentNotificationViaSSE(@NonNull final TelewebJSON request) {
        final String boardId = request.getString(BOARD_ID_KEY);
        final String customerCompanyId = request.getString(CUSTOMER_COMPANY_ID_KEY);
        final String postId = request.getString(POST_ID_KEY);
        final String higherCommentId = request.getString(HIGHER_COMMENT_ID_KEY);
        final String sender = request.getString(USER_ID_KEY);
        final boolean isPostComment = StringUtils.isEmpty(higherCommentId);

        if (isPostComment) {
            //case: new post comment
            sendNotificationToBoardAuthorViaSSE(boardId, customerCompanyId, postId, sender);
        } else {
            //case: new child comment
            final String postTitle = sendNotificationToBoardAuthorViaSSE(boardId, customerCompanyId, postId, sender);
            sendNotificationToCommentAuthorViaSSE(boardId, customerCompanyId, postId, postTitle, higherCommentId, sender);
        }
    }

    private String sendNotificationToBoardAuthorViaSSE(@NonNull final String boardId, @NonNull final String customerCompanyId, @NonNull final String postId, @NonNull final String sender) {
        //get post's author
        final TelewebJSON getPostAuthorParam = new TelewebJSON();
        getPostAuthorParam.setString(BOARD_ID_KEY, boardId);
        getPostAuthorParam.setString(CUSTOMER_COMPANY_ID_KEY, customerCompanyId);
        getPostAuthorParam.setString(POST_ID_KEY, postId);

        final TelewebJSON getPostAuthor = dao.select(POST_MAPPER_NAME, "selectPostAuthor", getPostAuthorParam);
        final String receiver = getPostAuthor.getString(REGISTER_ID_KEY);
        final String postTitle = getPostAuthor.getString(POST_TITLE_KEY);
        final String message = String.format(NEW_POST_COMMENT_MESSAGE, postTitle);
        //send message
        sseService.sendMessage(MessageType.SYSTEM_MESSAGE, sender, receiver, message, COMMENT_TOAST_SHOW_POSITION, COMMENT_TOAST_SHOW_SECOND);
        return postTitle;
    }

    private void sendNotificationToCommentAuthorViaSSE(@NonNull final String boardId, @NonNull final String customerCompanyId, @NonNull final String postId, @NonNull final String postTitle,
        @NonNull final String commentId, @NonNull final String sender) {
        final TelewebJSON getParentCommentAuthorParam = new TelewebJSON();
        getParentCommentAuthorParam.setString(BOARD_ID_KEY, boardId);
        getParentCommentAuthorParam.setString(CUSTOMER_COMPANY_ID_KEY, customerCompanyId);
        getParentCommentAuthorParam.setString(POST_ID_KEY, postId);
        getParentCommentAuthorParam.setString(COMMENT_ID_KEY, commentId);

        final TelewebJSON getParentCommentAuthor = dao.select(COMMENT_MAPPER_NAME, "selectParentCommentAuthor", getParentCommentAuthorParam);
        final String receiver = getParentCommentAuthor.getString(REGISTER_ID_KEY);
        final String message = String.format(NEW_CHILD_COMMENT_MESSAGE, postTitle);
        //send message
        sseService.sendMessage(MessageType.SYSTEM_MESSAGE, sender, receiver, message, COMMENT_TOAST_SHOW_POSITION, COMMENT_TOAST_SHOW_SECOND);
    }
}
