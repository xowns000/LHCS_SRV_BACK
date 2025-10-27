package kr.co.hkcloud.palette3.core.util;


import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PaletteFilterUtils
{
    public String filter(String message)
    {

        if(message == null) { return null; }

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for(int i = 0; i < content.length; i++) {
            switch(content[i])
            {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return result.toString();
    }


    public String filter2(String message)
    {

        if(message == null) { return null; }

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for(int i = 0; i < content.length; i++) {
            switch(content[i])
            {
                case '"':
                    result.append("\\\"");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return result.toString();
    }


    public String filter3(String message)
    {

        if(message == null) { return null; }

        message = message.replaceAll("&amp;", "&");
        message = message.replaceAll("&lt;", "<");
        message = message.replaceAll("&gt;", ">");
        message = message.replaceAll("&#40;", "(");
        message = message.replaceAll("&#41;", ")");
        message = message.replaceAll("&apos;", "'");
        message = message.replaceAll("&#91;", "[");
        message = message.replaceAll("&#93;", "]");
        message = message.replaceAll("&quot;", "\"");

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for(int i = 0; i < content.length; i++) {
            switch(content[i])
            {
                case '"':
                    result.append("\\\"");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                default:
                    result.append(content[i]);
            }
        }

        return result.toString();
    }


    public String filter4(String message)
    {

        if(message == null) { return null; }

        message = message.replaceAll("&amp;", "&");
        message = message.replaceAll("&lt;", "<");
        message = message.replaceAll("&gt;", ">");
        message = message.replaceAll("&#40;", "(");
        message = message.replaceAll("&#41;", ")");
        message = message.replaceAll("&apos;", "'");
        message = message.replaceAll("&#91;", "[");
        message = message.replaceAll("&#93;", "]");
        message = message.replaceAll("&quot;", "\"");

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for(int i = 0; i < content.length; i++) {
            switch(content[i])
            {
                case '"':
                    result.append("\\\"");
                    break;
                case '\n':
                    result.append("\\\n");
                    break;
                default:
                    result.append(content[i]);
            }
        }

        return result.toString();
    }


    public String filter5(String message)
    {

        if(message == null) { return null; }

        message = message.replaceAll("&amp;", "&");
        message = message.replaceAll("&lt;", "<");
        message = message.replaceAll("&gt;", ">");
        message = message.replaceAll("&#40;", "(");
        message = message.replaceAll("&#41;", ")");
        message = message.replaceAll("&apos;", "'");
        message = message.replaceAll("&#91;", "[");
        message = message.replaceAll("&#93;", "]");
        message = message.replaceAll("&quot;", "\"");

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
//        for (int i = 0; i < content.length; i++)
//        {
//            switch (content[i])
//            {
//                case '"':
//                    result.append("\\\"");
//                    break;
//                default:
//                    result.append(content[i]);
//            }
//        }

        return message.toString();
    }

}
