package kr.co.hkcloud.palette3.config.datasources.datasource.palette;


import java.util.List;

import lombok.extern.slf4j.Slf4j;


/**
 * Slave DataSource Name List
 * 
 * @author     Orange
 *
 * @param  <T>
 */
@Slf4j
public class PaletteDataSourceNameList<T>
{
    private List<T> list;
    private Integer counter = 0;


    public PaletteDataSourceNameList(List<T> list) {
        this.list = list;
        log.debug("list={}", list);
    }


    public T getOne()
    {
        return list.get((counter++) % list.size());
    }
}
