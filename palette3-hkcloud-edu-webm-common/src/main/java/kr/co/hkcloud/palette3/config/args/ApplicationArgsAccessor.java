package kr.co.hkcloud.palette3.config.args;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * 1. getSourceArgs - 입력한 args 그대로 배열로 받아 온다.
 * 
 * 2. getOptionNames - args 앞에 "--" 를 붙이면 옵션으로 인식 한다. 옵션 args 사용 형식 --NAME=VALUE - "--fruit=apple" 이렇게 args를 사용하면 - getOptionName는 fruit 처럼 option name 들의 배열을 받아 온다.
 * 
 * 3. getNonOptionArgs - "--" 가 없는 경우 NonOption으로 인식한디. - "--" 가 없는 args 들의 값들을 받아 온다
 * 
 * @author Orange
 *
 */
@Slf4j
@Component
public class ApplicationArgsAccessor
{

    @Autowired
    public ApplicationArgsAccessor(ApplicationArguments args) {
//        boolean debug = args.containsOption("debug");
//        log.debug("debug={}", debug);
//
//        List<String> files = args.getNonOptionArgs();
//        log.debug("files={}", files);
//
//        // if run with "--debug logfile.txt" debug=true, files=["logfile.txt"]

        String[] sourceArgs = args.getSourceArgs();
        List<String> nonOptionArgs = args.getNonOptionArgs();
        Set<String> optionNames = args.getOptionNames();

        log.debug("=================================");
        log.debug("# Source Arguments");
        for(String sourceArg : sourceArgs) {
            log.debug("{}", sourceArg);
        }

        log.debug("---------------------------------");
        log.debug("# Non Option Arguments");
        for(String nonOptionArg : nonOptionArgs) {
            log.debug("{}", nonOptionArg);
        }

        log.debug("---------------------------------");
        log.debug("# Option Arguments");
        for(String optionName : optionNames) {
            List<String> optionValues = args.getOptionValues(optionName);
            for(String optionValue : optionValues) {
                log.debug("{}={}", optionName, optionValue);
            }
        }
        log.debug("=================================");
    }
}
