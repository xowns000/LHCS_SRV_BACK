package kr.co.hkcloud.palette3.fcm.app;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import kr.co.hkcloud.palette3.fcm.message.FcmMessage;

public interface FcmService {

    ResponseEntity<String> sendMessage(
        FcmMessage message) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    ResponseEntity<String> getFirebaseToken(
        String jwtToken) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;
}
