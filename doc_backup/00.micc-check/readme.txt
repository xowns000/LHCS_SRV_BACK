micc 운영의 카카오톡 네이버톡 정상유무 확인용도로 활용예정.
방식 ::
 - 해당 타겟 서버에 직접적인 사용자채팅 메시지를 호출 할 수 없음(로그인 인증정보로 인해)
 - scouter에서 수집하는 패킷중 중 아래와 같은 패턴의 패킷을 확인 할 수 있음.
    네이버 : service=*/navertalktalk/* , userAgent=navertalk/v1
    카카오 : service=/message<POST>, userAgent=AHC/20
 - 위 두개의 패킷을 수집하여 NCLOUD <hkc-redis-svr01> 서버의 /root/sample/micc-healthcheck 에 데이타로 저장
 - node로 서비스 10초 리프레시
 - 추후 업그레이드 필요함.

호출 주소 :: https://micc-check.hkpalette.com:4443/

