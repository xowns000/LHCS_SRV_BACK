package kr.co.hkcloud.palette3.config.idgen;

import egovframework.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl;
import egovframework.rte.fdl.idgnr.impl.strategy.EgovIdGnrStrategyImpl;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author jangh
 *
 */
@Configuration
public class EgovIdgenConfig {

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    //    public EgovTableIdGnrServiceImpl loginLogIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource, @Qualifier("mixPrefixLoginLogId") EgovIdGnrStrategyImpl mixPrefixLoginLogId)
    public EgovTableIdGnrServiceImpl loginLogIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        //        egovTableIdGnrServiceImpl.setStrategy(mixPrefixLoginLogId);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("USER_LOG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     * @return
     */
    @Bean
    public EgovIdGnrStrategyImpl mixPrefixLoginLogId() {
        EgovIdGnrStrategyImpl egovIdGnrStrategyImpl = new EgovIdGnrStrategyImpl();
        egovIdGnrStrategyImpl.setPrefix("LOG-");
        egovIdGnrStrategyImpl.setCipers(10);
        egovIdGnrStrategyImpl.setFillChar('0');
        return egovIdGnrStrategyImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl twbSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ01");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl readyOffSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_TALK_READY_OFF");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl prohibiteSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_PROHIBITE");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl sndrKeySeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_SNDR_KEY");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl custcoIdSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_CUSTCO_ID");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl inqryTypCdSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_INQRY_TYP_CD");
        return egovTableIdGnrServiceImpl;
    }

    //    /**
    //     * 
    //     * @param  paletteRoutingLazyDataSource
    //     * @return
    //     */
    //    @Bean(destroyMethod = "destroy")
    //    public EgovTableIdGnrServiceImpl orgFileSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource)
    //    {
    //        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    //        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    //        egovTableIdGnrServiceImpl.setBlockSize(1);
    //        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    //        egovTableIdGnrServiceImpl.setTableName("TWB_SEQ_FILE");
    //        return egovTableIdGnrServiceImpl;
    //    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl orgLongTextSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TWB_TALK_CONTACT_ORG_CONT_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl orgSmsMngSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_SMS_TMPL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl orgNtcnTalkTmplMngSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_NTCN_TALK_TMPL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl orgNtcnTalkHistSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("NTCN_TALK_SEND_HIST");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl smsSendHistSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_SMS_SND_HST");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnQAEvalShtSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_QA_EVAL_SHT_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl chtQAEvalRstSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_CHT_QA_EVAL_RST_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl chtQAEvalShtSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_CHT_QA_EVAL_SHT_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl chtQAEvalSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_CHT_QA_EVAL_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnCnslSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CNSL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnCnslDtlSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CNSL_DTL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnCnslPrtHstSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CNSL_PRT_HST");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnCnslHstChngSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHNG_HIST_SEQ");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnClbKSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CLBK");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnCnslRsbSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CNSL_RSV");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnEmailSndHstSeqIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_EML_SND_HST");
        return egovTableIdGnrServiceImpl;
    }

    /**
     *
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltCnslUnityHstService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_CNSL_UNITY_HST");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaEvlGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA_EVA");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaQsGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA_QS");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaVeGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA_VE");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaDataRstGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA_DATA_RST");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneQaDataGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_QA_DATA");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmEvaGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_EVA");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmQsGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_QS");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmVeGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_VE");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmEvaRstGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_EVA_RST");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmDataGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_DATA");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl phoneLmDataRstGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_LM_DATA_RST");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl notificationTalkService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MTS_ATALK_MSG");
        egovTableIdGnrServiceImpl.setTableName("MTS_ATALK_MSG_BAT");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhoneSmsManageService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MTS_SMS_MSG");
        egovTableIdGnrServiceImpl.setTableName("MTS_MMS_MSG");
        egovTableIdGnrServiceImpl.setTableName("MTS_SMS_MSG_BAT");
        egovTableIdGnrServiceImpl.setTableName("MTS_MMS_MSG_BAT");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl IdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("ATTR_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CuttTypeIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUTT_TYPE_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl OgnzIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("DEPT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PstIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PST_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhnIpExtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PHN_IP_EXT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl VocRcptIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("VOC_RCPT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhnCuttIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PHN_CUTT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhnCuttChgHistIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHG_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustTelNoIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUST_TELNO_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUST_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustChgHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUST_CHG_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustItgrtHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUST_ITGRT_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustAgreHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUST_AGRE_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhnCuttRsvtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUTT_RSVT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CuttItgrtHistIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("ITGRT_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PhnDsptchHistIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("DSPTCH_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl BbsIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("BBS_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyQitemGroupIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_QITEM_GROUP_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyQitemIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_QITEM_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QitemChcIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QITEM_CHC_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyTrgtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_TRGT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ObdCpiIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CPI_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ObdCpiAltmntHstryGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("ALTMNT_HSTRY");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ObdCpiCuslGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUSL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ObdCpiCustGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("OBD_CUST_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ObdCpiCustDtlGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("ATTR_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl MenuIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MENU_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl BtnIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("BTN_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QAPlanIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QA_PLAN_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QACyclIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QA_CYCL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QAQltylIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QA_QLTY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QAQltyClsfIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QLTY_CLSF_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QAEvlArtclIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("EVL_ARTCL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QATrgtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QA_TRGT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SysMsgIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SYS_MSG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl QstnTypeIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("QSTN_TYPE_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ScheduleGetIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SCHDL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SndrKeyGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SNDR_KEY");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl FbdwdIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("FBDWD_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl msgIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MSG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl msgGroupIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MSG_GROUP_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl UserIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("USER_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PstnIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PSTN_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl HldyIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("HLDY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CtcIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CTC_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtCuttIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtCuttDtlIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_DTL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PswdLogIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PSWD_LOG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl MtsHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MTS_SNDNG_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtRdyIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_RDY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtCuttHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsTmplIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_TMPL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsClsfIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_CLSF_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsClsfAuthrtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_CLSF_AUTHRT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsContsIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_CONTS_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsContsRvwHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_CONTS_RVW_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtRdyHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_RDY_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtUserHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_USER_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsGuideIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_GUIDE_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsGuideCuttIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_GUIDE_CUTT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsKeywdIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_KEYWD_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl KmsRelLnkIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("KMS_REL_LNK_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl tmplMngeTmplClsfIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TMPL_CLSF_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl tmplMngeSmsTmplIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SMS_TMPL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl pltPhnClbkIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PLT_PHN_CLBK_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl tmplMngeAtalkTmplIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("ATALK_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    /* CUSTCO SCHEMA*/
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CertCustcoIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("CUSTCO.PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CERT_CUSTCO_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    /* CUSTCO SCHEMA */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CertUserIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("CUSTCO.PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CERT_USER_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    /* CUSTCO SCHEMA */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CertCustcoDsptchNoIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("CUSTCO.PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CERT_CUSTCO_DSPTCH_NO_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CustcoDsptchNoIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUSTCO_DSPTCH_NO_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl PstQstnChnIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PST_QSTN_CHN_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl MtsSndngHstryGroupIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("MTS_SNDNG_HSTRY_GROUP_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ShortKeyIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SHORT_KEY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CertSmsHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CERT_SMS_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    /**
     * 채팅_상담_이메일 키 생성 - 채팅_상담_이메일_ID(CHT_CUTT_EML_ID)
     * @param paletteRoutingLazyDataSource
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtChttEmlIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_EML_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl EmlSndngIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("EML_SNDNG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ClctJobMngIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("JOB_MNG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyClsfIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_CLSF_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagMstIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LKAG_MST_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagCertCustcoIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CERT_CUSTCO_LKAG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagCertCustcoHeadIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("HEAD_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagMngIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LKAG_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagParamArtclIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("PARAM_ARTCL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LkagRspnsArtclIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("RSPNS_ARTCL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LAYOUT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl TabIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("TAB_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutListIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LIST_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutListDwnGroupIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LIST_DWN_GROUP_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutListDwnGroupDtlIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LIST_DWN_GROUP_DTL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutListGroupIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LIST_GROUP_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl LayoutListGroupDtlIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("LIST_GROUP_DTL_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvySttsHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_STTS_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyExlTrgtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_EXL_TRGT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl SrvyExlCondIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("SRVY_EXL_COND_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtCuttBbsIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_BBS_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl ChtCuttBbsAnswrIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CHT_CUTT_BBS_ANSWR_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl RsvtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("RSVT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl RsvtCuttGuideIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("RSVT_CUTT_GUIDE_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl VsrRsvtIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
    	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    	egovTableIdGnrServiceImpl.setBlockSize(1);
    	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    	egovTableIdGnrServiceImpl.setTableName("VST_RSVT_ID");
    	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    	return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl VsrRsvtHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
    	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    	egovTableIdGnrServiceImpl.setBlockSize(1);
    	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    	egovTableIdGnrServiceImpl.setTableName("VST_RSVT_HSTRY_ID");
    	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    	return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl TrnsfCuttIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
    	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    	egovTableIdGnrServiceImpl.setBlockSize(1);
    	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    	egovTableIdGnrServiceImpl.setTableName("TRNSF_CUTT_ID");
    	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    	return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CuttTrnsfIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
    	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    	egovTableIdGnrServiceImpl.setBlockSize(1);
    	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    	egovTableIdGnrServiceImpl.setTableName("CUTT_TRNSF_ID");
    	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    	return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CuttTrnsfHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
    	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    	egovTableIdGnrServiceImpl.setBlockSize(1);
    	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    	egovTableIdGnrServiceImpl.setTableName("CUTT_TRNSF_HSTRY_ID");
    	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    	return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CmntIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CMNT_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl CuttTrnsfAltmntHstryIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource) {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
        egovTableIdGnrServiceImpl.setTableName("CUTT_TRNSF_ALTMNT_HSTRY_ID");
        egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
        egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
        return egovTableIdGnrServiceImpl;
    }

    //    게시판 문의 관련
    //    @Bean(destroyMethod = "destroy")
    //	public EgovTableIdGnrServiceImpl PstQstnCuttIdGnrService(@Qualifier("paletteRoutingLazyDataSource") DataSource paletteRoutingLazyDataSource)
    //	{
    //	   	EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
    //	   	egovTableIdGnrServiceImpl.setDataSource(paletteRoutingLazyDataSource);
    //	   	egovTableIdGnrServiceImpl.setBlockSize(1);
    //	   	egovTableIdGnrServiceImpl.setTable("PLT_TBL_SEQ");
    //	   	egovTableIdGnrServiceImpl.setTableName("PST_QSTN_CUTT_ID");
    //	   	egovTableIdGnrServiceImpl.setTableNameFieldName("COL_ID");
    //	   	egovTableIdGnrServiceImpl.setNextIdFieldName("SEQ_VL");
    //	   	return egovTableIdGnrServiceImpl;
    //	}

}
