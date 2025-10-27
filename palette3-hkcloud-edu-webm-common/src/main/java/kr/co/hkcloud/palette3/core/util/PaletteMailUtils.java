package kr.co.hkcloud.palette3.core.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Component
public class PaletteMailUtils {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private HcTeletalkEnvironment env;

    public void sendMimeMessage(String to, String subject, String text, String from,
        TelewebJSON mjsonParams) throws MessagingException, UnsupportedEncodingException {

        String pathfile = "";					// 파일경로 : 파일이름을 포함한 경로 ex) D:/~~/파일이름
        String originalFileNm = "";			// 파일이름
        FileDataSource fds = null;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        helper.setText(HtmlUtils.htmlUnescape(text), true); //***HTML 적용
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);
        /*
         * 첨부파일 로직 추가 2021.05.25 이동욱 (FILE인 경우만)
         */
        if (mjsonParams.getDataObject("ATTACH") != null) {
            for (int i = 0; i < mjsonParams.getDataObject("ATTACH").size(); i++) {
                JSONObject objJson = mjsonParams.getDataObject("ATTACH").getJSONObject(i);
                pathfile = objJson.getString("PATH_DIR") + "/" + objJson.getString("FILENAME");
                fds = new FileDataSource(pathfile);
                originalFileNm = objJson.getString("FILENAME");

                //B는 Base64, Q는 quoted-printable
                helper.addAttachment(MimeUtility.encodeText(originalFileNm, "UTF-8", "B"), fds);
            }
        }

        mailSender.send(mimeMessage);
    }
}
