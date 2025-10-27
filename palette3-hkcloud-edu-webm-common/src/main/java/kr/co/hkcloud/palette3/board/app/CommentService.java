package kr.co.hkcloud.palette3.board.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.NonNull;

/**
 * 게시글 댓글 서비스
 *
 * @author Jun Hyeong Jo
 * @since 2025-01-09
 */
public interface CommentService {
    TelewebJSON getPostComments(@NonNull TelewebJSON request);

    TelewebJSON createPostComment(@NonNull TelewebJSON request);

    TelewebJSON updatePostComment(@NonNull TelewebJSON request);

    TelewebJSON deletePostComment(@NonNull TelewebJSON request);
}
