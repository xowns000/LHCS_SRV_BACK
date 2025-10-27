package kr.co.hkcloud.palette3.core.chat.transfer.app;


import java.net.URI;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface SendToKakaoService
{
    JSONObject sendTextToKakao(final URI uri, final JSONObject writeData) throws TelewebAppException;
    JSONObject sendLinkTextToKakao(final URI writeUri, final URI imageUri, final JSONObject writeData) throws TelewebAppException;
    JSONObject sendImageToKakao(final URI writeUri, final URI imageUri, final JSONObject writeData) throws TelewebAppException;
    JSONObject endWithBot(final URI uri, final JSONObject writeData) throws TelewebAppException;
    JSONObject uploadImageToKakao(final URI imageUri, final JSONObject writeData, final String imgType) throws TelewebAppException;
}
