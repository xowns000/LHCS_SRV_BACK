package kr.co.hkcloud.palette3.editor.api;


import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.editor.util.EditorUploadUtils;
import kr.co.hkcloud.palette3.editor.util.EditorUploadValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "EditorUploadRestController", description = "에디터 업로드 REST 컨트롤러")
public class EditorUploadRestController {

    private final EditorUploadValidator editorUploadValidator;
    private final EditorUploadUtils eitorUploadUtils;

    private final HcTeletalkEnvironment env;

    @ResponseBody
    @RequestMapping(value = "/api/editor/imageUpload.do", method = RequestMethod.POST)
    public String fileUpload(HttpServletRequest req, HttpServletResponse resp, MultipartHttpServletRequest multiFile) throws Exception {
        JsonObject json = new JsonObject();
        PrintWriter printWriter = null;
        OutputStream out = null;
        MultipartFile file = multiFile.getFile("upload");
        if (file != null) {
            if (file.getSize() > 0 && StringUtils.isNotBlank(file.getName())) {
                if (file.getContentType().toLowerCase().startsWith("image/")) {
                    try {
                        String fileName = file.getName();
                        byte[] bytes = file.getBytes();
                        String uploadPath = env.getString("file.repository.root-dir", "");
                        String year = DateCmmnUtils.getCurrentTime("yyyy");
                        String month = DateCmmnUtils.getCurrentTime("MM");
                        String day = DateCmmnUtils.getCurrentTime("dd");
                        //파일 디렉터리 생성은 최종 저장 시 생성할 것
                        Path storePath = Paths.get(year, month, day);
                        log.debug("storePath :{}", storePath);
                        uploadPath =
                            uploadPath + "/images/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/editor/"
                                + storePath.toString();

                        File uploadFile = new File(uploadPath);
                        if (!uploadFile.exists()) {
                            uploadFile.mkdirs();
                        }
                        fileName = UUID.randomUUID().toString();

                        uploadPath = uploadPath + "/" + fileName;
                        out = new FileOutputStream(new File(uploadPath));
                        out.write(bytes);

                        printWriter = resp.getWriter();
                        resp.setContentType("text/html");
                        String fileUrl =
                            "/upload/images/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/editor/"
                                + year + "/" + month + "/" + day + "/" + fileName;

                        // json 데이터로 등록
                        // {"uploaded" : 1, "fileName" : "test.jpg", "url" : "/img/test.jpg"}
                        // 이런 형태로 리턴이 나가야함.
                        json.addProperty("uploaded", 1);
                        json.addProperty("fileName", fileName);
                        //                        json.addProperty("url", req.getRequestURL().toString().replace(req.getRequestURI(),"") + fileUrl);
                        json.addProperty("url", fileUrl);

                        printWriter.println(json);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                        if (printWriter != null) {
                            printWriter.close();
                        }
                    }
                }
            }
        }
        return null;
    }

}
