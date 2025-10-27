package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.hkcloud.palette3.servlet.dhtmlx.app.CSVWriter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@WebServlet(urlPatterns = {"/servlet/CSVGenerator"})
public class CSVGenerator extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = -4216821989995014116L;


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String xml = req.getParameter("grid_xml");
        xml = URLDecoder.decode(xml, StandardCharsets.UTF_8.name());
        CSVWriter writer = new CSVWriter();
        writer.generate(xml, resp);
    }
}
