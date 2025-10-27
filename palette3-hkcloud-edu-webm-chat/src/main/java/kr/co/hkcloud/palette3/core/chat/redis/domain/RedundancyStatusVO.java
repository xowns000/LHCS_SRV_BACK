package kr.co.hkcloud.palette3.core.chat.redis.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedundancyStatusVO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -700765871406709290L;

    @NotEmpty private RedundancyStatus redundancyStatus;  //이중화상태 (ACTIVE/STANBY)
    @NotEmpty private Long             lastRunTime;       //최종수행시간
}
