package kr.co.hkcloud.palette3.file.enumer;


/**
 * 저장소 대상 타입
 * 
 * @author RND
 *
 */
public enum RepositoryTrgtTypeCd
{

    // 파일에 저장
    FILE,

    // DB에 저장
    DB,

    //에디터에 저장
    //ㄴ에디터의 경우 외부에 이미지를 읽어들일 수 있는 URI를 제공함
    EDITOR
}
