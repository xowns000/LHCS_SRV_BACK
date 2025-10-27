package kr.co.hkcloud.palette3.sse.app;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.NonNull;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : kr.co.hkcloud.palette3.sse.app
 * fileName       : SseService
 * author         : KJD
 * date           : 2023-11-10
 * description    : << 여기 설명 >>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-10        KJD       최초 생성
 */
public interface SseService {
    void sendMessage(@NonNull final SseMessage.MessageType type, @NonNull final String sender, @NonNull final String receiver, @NonNull final String message, @NonNull final String toastShowPosition,
        final int toastShowSecond);

    ResponseEntity<String> sendMessage(SseMessage message) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;
}
