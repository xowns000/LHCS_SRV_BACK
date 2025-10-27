package kr.co.hkcloud.palette3.stt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// WebSocket의 호스트 주소 설정
@Slf4j
@Service
@ServerEndpoint(value = "/stt", configurator = ServerEndpointConfigurator.class)
public class SttWebsocket implements OnResult {

    FileOutputStream lFileOutputStream;
    private boolean isconnected = false;
    private List<byte[]> mAudioBufferList = null;
    private static final int mAudioBufferMaxN = 100;
    private Thread mSaveThread = null;
    private Thread mIntelloidSttclient = null;
    private IntelloidSttClient stt = null;
    private String resultText = null;


    @Value("${stt.server.test-mode}")
    public boolean isOnlyStringFileMode;        //파일 저장 테스트 모드 or STT 모드

    @Value("${stt.server.test-dir}")
    public String STT_TEST_DIR;

    @Value("${stt.server.ws-uri}")
    public String STT_SERVER_URI; //Speech-to-text server URL :: "ws://nlu-00.intelloia.com:38480/intelloid-STT-stream-web/websocket"

    Session sttSession = null;

    @Override
    public void onResult(String jsonStr) {
        log.info(jsonStr);

	    /*
	    JsonObject jsonRoot = JsonParser.parseString(jsonStr).getAsJsonObject();
	    JsonArray results = jsonRoot.getAsJsonArray("results");
	    JsonObject result = results.get(0).getAsJsonObject();
	    log.info("sentence: " + result.get("sentence").getAsString());
	    log.info("sampFreq: " + jsonRoot.get("sampFreq").getAsString());
	    log.info("sentIndex: " + jsonRoot.get("sentIndex").getAsString());
	    log.info("status: " + jsonRoot.get("status").getAsString());
	    log.info("sentStart: " + jsonRoot.get("sentStart").getAsString());
	    log.info("sentEnd: " + jsonRoot.get("sentEnd").getAsString());
	    log.info("sendConfidence: " + jsonRoot.get("sentConfidence").getAsString());

	    resultText = result.get("sentence").getAsString();*/

        resultText = jsonStr;
    }

    // WebSocket으로 client가 접속하면 요청되는 함수

    @OnOpen
    public void handleOpen(Session userSession) {

        log.info("client is now connected...");
        log.info("userSession.getQueryString() : " + userSession.getQueryString());

        isconnected = true;

        mAudioBufferList = Collections.synchronizedList(new LinkedList<byte[]>());

        if (isOnlyStringFileMode) {
            //store(save) to file mode
            mSaveThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        save(userSession);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (mSaveThread != null) {
                try {
                    mSaveThread.start();
                } catch (Exception e) {
                    log.info("mSaveThread not started");
                    return;
                }
            }
        } else {
            //STT mode
            mIntelloidSttclient = new Thread(new Runnable() {

                public void run() {
                    try {
                        connect_stt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (mIntelloidSttclient != null) {
                try {
                    mIntelloidSttclient.start();
                } catch (Exception e) {
                    log.info("mIntelloidSttclient not started");
                    return;
                } // try {
            } // if (mIntelloidSttclient != null) {
        }
    }

    // WebSocket으로 binary messages가 오면 요청되는 함수
    @OnMessage(maxMessageSize = 1024000)
    public byte[] handleBinaryMessage(byte[] buffer) {
        log.info("New Binary Message Received, buffer.length : " + buffer.length);

        if (mAudioBufferList.size() < mAudioBufferMaxN) {
            mAudioBufferList.add(buffer);
        } else {
            log.info("audio buffer full");
            mAudioBufferList.clear();
        }
        return buffer;
    }

    private void connect_stt() throws InterruptedException, IOException {

        byte[] audio;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            log.info("WebSocketContainer connectToServer sttUri : " + STT_SERVER_URI);
            stt = new IntelloidSttClient();
            stt.regOnResult(this::onResult);
            sttSession = container.connectToServer(stt /* was IntelloidSttClient.class */, URI.create(STT_SERVER_URI));

            // After the connection is closed, you can do other things here if needed
        } catch (DeploymentException e) {
            e.printStackTrace();
        }

        while (isconnected == true) {
            audio = null;
            if (mAudioBufferList.size() <= 0) {
                Thread.sleep(20);
                continue;
            } // if (mAudioBufferList.size() <= 0) {

            audio = mAudioBufferList.remove(0);
            if (audio == null) {
                continue;
            } // if (audio.length == 0) {

            if (stt != null && sttSession != null) {
                String base64data = Base64.getEncoder().encodeToString(audio);
                //log.info("base64data : " + base64data);

                sttSession.getBasicRemote().sendText(base64data);
            }

        } // while (isconnected == true) {

    }

    private void save(Session userSession) throws InterruptedException {
        byte[] audio;
        File lOutFile;

        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss-SSS");
        String to = transFormat.format(from);
        String replaceto = to.replaceAll(" ", "-");
        String filename = replaceto.replaceAll(":", "-") + "-" + userSession.getId() + ".pcm";

        lOutFile = new File(STT_TEST_DIR + File.separator + filename);
        lOutFile.getParentFile().mkdirs();

        try {
            lFileOutputStream = new FileOutputStream(lOutFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("File open error !");
            return;
        }

        while (isconnected == true) {
            audio = null;
            if (mAudioBufferList.size() <= 0) {
                Thread.sleep(20);
                continue;
            } // if (mAudioBufferList.size() <= 0) {

            audio = mAudioBufferList.remove(0);
            if (audio == null || audio.length == 0) {
                log.info("audio == null || audio.length == 0");
                continue;
            }

            try {
                lFileOutputStream.write(audio, 0, audio.length);
                lFileOutputStream.flush();
            } catch (IOException e) { // try {
                log.info("save(): error");
            }
        }

        try {
            lFileOutputStream.close();
        } catch (IOException e) {
            log.info("lFileOutputStream.close(): error");
        }
    } // private void save() {

    // WebSocket과 client가 접속이 끊기면 요청되는 함수

    @OnClose
    public void handleClose() {

        isconnected = false;

        try {
            if (sttSession != null) {
                sttSession.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mIntelloidSttclient != null) {
                mIntelloidSttclient.join();
                mIntelloidSttclient = null;
            } // if (mRecogThread != null) {
        } catch (InterruptedException e) { // try {
        } // try {} catch InterruptedException e) {

        try {
            if (lFileOutputStream != null) {
                lFileOutputStream.close();
            }
        } catch (IOException e) {
            log.info("File close error");
        }

        try {
            if (mSaveThread != null) {
                mSaveThread.join();
                mSaveThread = null;
            }
        } catch (InterruptedException e) { // try {
        } // try {} catch InterruptedException e) {

        if (mAudioBufferList != null) {
            while (mAudioBufferList.isEmpty() != true) {
                mAudioBufferList.remove(0);
            } // while (mAudioBufferList.isEmpty() != true) {
        } // if (mAudioBufferList != null) {

        log.info("client is now disconnected...");

    }

    // WebSocket과 client 간에 통신 에러가 발생하면 요청되는 함수.

    @OnError

    public void handleError(Throwable t) {
        // 콘솔에 에러를 표시한다.
        t.printStackTrace();
    }

}