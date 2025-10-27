package kr.co.hkcloud.palette3.config.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 전화 Application 의 준비가 완료되었을 때 발생하는 이벤트 리스너
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PhoneApplicationReadyEventListener
{
    @Value("classpath:scpdb_agent-${spring.profiles.active}.ini")
    private Resource resource;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
    	//EAI XML Loading START////////////////////////////////////////////////////////////////////////////
        /*
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        String fileBaseName = null;
        File eaiXmlFile = null;
        
        //설정된 경로 하위 모든 xml 파일객체 목록을 가져온다.
        ApplicationContext content = PaletteSpringContextSupport.getApplicationContext();
        
        try
        {
        	//신협 EAI 로딩 로직	
        	//Resource[] resources = content.getResources("classpath*:kr/co/hkcloud/palette/infra/cuics/eai/dao/xml/*.xml");
        	Resource[] resources = content.getResources("classpath*:kr/co/hkcloud/palette/infra/tibco/eai/dao/xml/*.xml");
        	
            for (Resource resource : resources)
            {
            	InputStream inputStream = resource.getInputStream();
            	eaiXmlFile = File.createTempFile(FilenameUtils.getBaseName(resource.getFilename()), ".xml");
                
            	try {
                    FileUtils.copyInputStreamToFile(inputStream, eaiXmlFile);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                //eaiXmlFile = resource.getFile();
                
                //객체가 파일이면서 읽을 수 있는 상태 파일만 처리한다.
                if (eaiXmlFile.isFile() )
                {
                    document = builder.build(eaiXmlFile);
                    
                    fileBaseName = FilenameUtils.getBaseName(resource.getFilename());
                    HcTelewebXmlEai.getInstance().setDocument(fileBaseName, document);
                    if(log.isTraceEnabled())
                    {
                        log.trace("EAI XML File Loaded...[{}]\n{}", fileBaseName, document);
                    }
                    else
                    {
                        log.debug("EAI XML File Loaded...[{}]", fileBaseName);    
                    }
                }
                else
                {
                	fileBaseName = FilenameUtils.getBaseName(eaiXmlFile.getName());
                	log.error("EAI XML File Load Failed..."+ fileBaseName);
                }
            }
        }
        catch (Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
        
        log.info("=================================");
        log.info("Phone Application ready: {}", event);
        log.info("=================================");
        */
    }
}
