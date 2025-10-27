package kr.co.hkcloud.palette3.core.profile.util;


import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.core.profile.enumer.PaletteModules;
import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PaletteProfileUtils {

    @NotBlank
    @Value("${spring.profiles.active}")
    private String[] profiles;


    /**
     * 활성화된 프로필 주!) 수정하지 마시오
     *
     * @return
     */
    public PaletteProfiles getActiveProfile() {
        PaletteProfiles result = null; //기본값
        for (String profile : profiles) {
            //enum에 -를 사용할 수 없으므로 "" 치환 후 체크
            PaletteProfiles checkProfile = PaletteProfiles.valueOf(profile.replaceAll("-", ""));
            switch (checkProfile) {
                case local:
                case localphone:
                case localchat:
                case localadmin:
                case localkm: {
                    result = PaletteProfiles.local;
                    break;
                }
                case dev:
                case devphone:
                case devchat:
                case devadmin:
                case devkm: {
                    result = PaletteProfiles.dev;
                    break;
                }
                case uat:
                case uatphone:
                case uatchat:
                case uatadmin:
                case uatkm: {
                    result = PaletteProfiles.uat;
                    break;
                }
                case production:
                case productionlhcs:
                case productionphone:
                case productionchat:
                case productionadmin:
                case productionkm: {
                    result = PaletteProfiles.production;
                    break;
                }

                case productioncloud:
                case productioncloudgwm:
                case productioncloudphone:
                case productioncloudchat:
                case productioncloudadmin:
                case productioncloudkm: {
                    result = PaletteProfiles.productioncloud;
                    break;
                }
                case localtest: {
                    result = PaletteProfiles.localtest;
                    break;
                }

                default: {
                    break;
                }
            }
        }
        return result;
    }


    /**
     * 활성화된 모듈 주!) 수정하지 마시오
     *
     * @return
     */
    public List<PaletteModules> getActiveModule() {
        List<PaletteModules> resultList = new ArrayList<>();
        for (String profile : profiles) {
            //enum에 -를 사용할 수 없으므로 "" 치환 후 체크
            PaletteProfiles checkProfile = PaletteProfiles.valueOf(profile.replaceAll("-", ""));
            switch (checkProfile) {
                case localphone:
                case devphone:
                case uatphone:
                case productioncloudphone:
                case productionphone: {
                    resultList.add(PaletteModules.phone);
                    break;
                }
                case localchat:
                case devchat:
                case uatchat:
                case productioncloudchat:
                case productionchat: {
                    resultList.add(PaletteModules.chat);
                    break;
                }
                case localkm:
                case devkm:
                case uatkm:
                case productioncloudkm:
                case productionkm: {
                    resultList.add(PaletteModules.km);
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return (resultList.size() > 0 ? resultList : null);
    }
}
