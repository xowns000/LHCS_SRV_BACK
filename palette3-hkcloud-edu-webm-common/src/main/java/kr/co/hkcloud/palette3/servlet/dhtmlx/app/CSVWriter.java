package kr.co.hkcloud.palette3.servlet.dhtmlx.app;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;


public class CSVWriter extends BaseWriter
{
    int    rows      = 0;
    int    cols      = 0;
    String watermark = null;


    @Override
    public void generate(String xml, HttpServletResponse resp) throws IOException
    {
        CSVxml data = new CSVxml(xml);

        resp.setContentType("application/vnd.ms-excel");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader("Content-Disposition", "attachment;filename=grid.csv");
        resp.setHeader("Cache-Control", "max-age=0");

        String[] csv;
        PrintWriter writer = resp.getWriter();

        csv = data.getHeader();
        while(csv != null) {
            writer.append(dataAsString(csv));
            csv = data.getHeader();
        }

        csv = data.getRow();
        if(csv != null) {
            cols = csv.length;
        }
        while(csv != null) {
            writer.append(dataAsString(csv));
            csv = data.getRow();
            rows += 1;
        }

        csv = data.getFooter();
        while(csv != null) {
            writer.append(dataAsString(csv));
            writer.flush();
            csv = data.getFooter();
        }

        drawWatermark(writer);

        writer.flush();
        writer.close();
    }


    private String dataAsString(String[] csv)
    {
        if(csv.length == 0) { return ""; }

        StringBuffer buff = new StringBuffer();
        for(int i = 0; i < csv.length; i++) {
            if(i > 0) {
                buff.append(",");
            }
            if(!csv[i].equals("")) {
                buff.append("\"");
                buff.append(csv[i].replace("\"", "\"\""));
                buff.append("\"");
            }
        }
        buff.append("\n");
        return buff.toString();
    }


    private void drawWatermark(PrintWriter writer)
    {
        if(watermark != null) {
            writer.append(watermark);
        }
    }


    @Override
    public int getColsStat()
    {
        return cols;
    }


    @Override
    public int getRowsStat()
    {
        return rows;
    }


    @Override
    public void setWatermark(String watermark)
    {
        this.watermark = watermark;
    }


    @Override
    public void setFontSize(int fontsize)
    {
        // do nothing
    }
}
