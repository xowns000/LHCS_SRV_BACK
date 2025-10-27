package kr.co.hkcloud.palette3.config.environment;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Document;
import org.jdom.JDOMException;

import lombok.extern.slf4j.Slf4j;


/**
 * EAI XML Instance
 * 
 * @author leeiy
 */
@Slf4j
public class HcTelewebXmlEai
{

    private static volatile ConcurrentHashMap<String, Document> EAI_XML_DOCUMENT = new ConcurrentHashMap<>();


    /**
     * 
     * @throws JDOMException
     * @throws IOException
     */
    private HcTelewebXmlEai() {
        log.info("=================================");
        log.info("HcTelewebXmlEai Instance loaded :::");
        log.info("=================================");
    }


    private static class Singleton
    {
        private static final HcTelewebXmlEai INSTANCE = new HcTelewebXmlEai();
    }


    public static HcTelewebXmlEai getInstance()
    {
        return Singleton.INSTANCE;
    }


    /**
     * set
     * 
     * @param fileBaseName
     * @param document
     */
    public void setDocument(String fileBaseName, Document document)
    {
        EAI_XML_DOCUMENT.put(fileBaseName, document);
    }


    /**
     * get
     * 
     * @param  interfaceId
     * @return
     */
    public Document getDocument(String interfaceId)
    {
        return EAI_XML_DOCUMENT.get(interfaceId);
    }
}
