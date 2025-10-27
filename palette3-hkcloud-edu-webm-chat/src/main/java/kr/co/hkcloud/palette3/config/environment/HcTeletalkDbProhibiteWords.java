package kr.co.hkcloud.palette3.config.environment;

import lombok.extern.slf4j.Slf4j;

/**
 * 텔레톡 DB 금칙어 속성
 * <p>initialization on demand holder idiom
 * <p>JVM의 class loader의 매커니즘과 class load 시점을 이용해 내부 class를 생성시켜 thread간 동기화 문제를 해결한다.
 * <p> - 5.0에서는 cache를 사용하도록 변경함
 * @author Orange
 * @version 5.0
 */
@Slf4j
public class HcTeletalkDbProhibiteWords
{
//    private static volatile JSONArray dbProhibiteWords = new JSONArray();
//    private HcTeletalkDbProhibiteWords()
//    {
//        log.info("=================================");
//        log.info("HcTeletalkDbProhibiteWords loaded :::");
//        log.info("=================================");
//    }
//
//    private static class Singleton
//    {
//        private static final HcTeletalkDbProhibiteWords INSTANCE = new HcTeletalkDbProhibiteWords();
//    }
//
//    public static HcTeletalkDbProhibiteWords getInstance()
//    {
//        return Singleton.INSTANCE;
//    }
//    
//    /**
//     * 금칙어 setter
//     * @param prohibiteWords
//     */
//    public void setDbProhibiteWords(TelewebJSON prohibiteJson)
//    {
//        JSONArray dbPrhoibiteWordsLoop = prohibiteJson.getDataObject();
//        if (dbPrhoibiteWordsLoop.size() > 0)
//        {
//            HcTeletalkDbProhibiteWords.dbProhibiteWords = dbPrhoibiteWordsLoop;
//        }
//        else
//        {
//            log.error("ProhibiteWords size 0? dbPrhoibiteWordsLoop.size()={}", dbPrhoibiteWordsLoop.size());
//        }
//    }
//    
//    public JSONArray getDbProhibiteWords()
//    {
//        return HcTeletalkDbProhibiteWords.dbProhibiteWords;
//    }
}
