package kr.co.hkcloud.palette3.stt;

import javax.websocket.*;

interface OnResult
{
    void onResult(String resultJson);
}

@ClientEndpoint
public class IntelloidSttClient
{

    private OnResult resultCallback;

    public void regOnResult(OnResult r)
    {
        resultCallback = r;
    }

    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println("Connected to Stt server!");
        try
        {
            // Send a message to the server after connecting
            //session.getBasicRemote().sendText("Hello, WebSocket Server!");
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message)
    {
        //System.out.println("Received message from server: " + message);
        if(message != null && !"".equals(message) && resultCallback != null)
        {
            resultCallback.onResult(message);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        System.out.println("stt connection closed: " + closeReason.getReasonPhrase());
    }
}