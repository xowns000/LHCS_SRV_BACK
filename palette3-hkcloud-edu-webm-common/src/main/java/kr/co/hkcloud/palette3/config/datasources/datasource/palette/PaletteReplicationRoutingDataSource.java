package kr.co.hkcloud.palette3.config.datasources.datasource.palette;


import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * @author jangh
 */
@Slf4j
public class PaletteReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private PaletteDataSourceNameList<String> paletteDataSourceNameList;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        paletteDataSourceNameList = new PaletteDataSourceNameList<>(
            targetDataSources.keySet().stream().filter(key -> key.toString().contains("slave")).map(key -> key.toString()).collect(Collectors.toList()));
    }


    /**
     * Transactional readOnly=true|false 여부에 따라 master/slave 분기
     */
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(isReadOnly) {
            return paletteDataSourceNameList.getOne();
        }
        return "master";
    }
}
