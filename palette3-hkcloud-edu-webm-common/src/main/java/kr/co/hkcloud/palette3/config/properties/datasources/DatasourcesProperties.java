package kr.co.hkcloud.palette3.config.properties.datasources;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.datasources.enumer.DatasourceDbVendor;
import kr.co.hkcloud.palette3.config.datasources.enumer.DatasourcePoolName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 데이터소스 팔레트 속성
 * 
 * @author RND
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "datasources")
public class DatasourcesProperties
{
    private final Datasource datasource;


    //데이터소스 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Datasource
    {
        @NotNull
        private final DatasourcePoolName defaultPoolName;

        //팔레트 데이터소스
        private final Palette palette;


        @Getter
        @RequiredArgsConstructor
        public static final class Palette
        {
            @NotNull
            private final DatasourceDbVendor dbVendor;

            private final Master master;
            private final Slave  slave;


            //Master 속성
            @Getter
            @RequiredArgsConstructor
            public static final class Master
            {
                @NotBlank
                private final String jndiName;
            }


            //Slave 속성
            @Getter
            @RequiredArgsConstructor
            public static final class Slave
            {
                @NotBlank
                private final String jndiName;
            }
        }
    }
}
