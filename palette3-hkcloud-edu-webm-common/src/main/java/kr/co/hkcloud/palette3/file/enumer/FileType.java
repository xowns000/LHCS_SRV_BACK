package kr.co.hkcloud.palette3.file.enumer;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum FileType
{
    text("텍스트 파일 형식"), //text,txt,log,csv,rtf
    archive("압축 파일 형식"), //zip, rar, 7z
    office("오피스 문서 형식"), //hwp,doc,dot,xlsx,pptx,docx,dotx,xltx,potx,ppsx,xls
    image("이미지 파일 형식"), //png,jpg,jpeg,gif,bmp
    video("비디오 파일 형식"), //avi,mpg,mpeg
    wildcard("지정되지 않은 형식") //*
    ;


    private final String description; //설명
}
