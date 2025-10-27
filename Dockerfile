FROM openjdk:8-jre-alpine

#font
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

WORKDIR /orange/app/palette3/webapps
ADD cloud_start.sh .
ADD scouter/ /scouter/
ADD palette3-hkcloud-edu-webm-run/build/libs/*.jar palette3-api.jar

RUN dos2unix /scouter/agent.host/host.sh
RUN dos2unix ./cloud_start.sh

#타임존 Asia/Seoul 설정.
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN chmod +x /orange/app/palette3/webapps/cloud_start.sh

# APM툴(scouter) 과 합치기 위함.
# /agent.host => 서버 자원모니터링용.
# /agent.java => java 모니터링용.
ENTRYPOINT ["/orange/app/palette3/webapps/cloud_start.sh"]