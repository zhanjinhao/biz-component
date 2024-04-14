package cn.addenda.component.dynamicdatasource;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author addenda
 * @since 2022/3/3 18:58
 */
public interface SlaveDataSourceSelector {

  DataSource select(String key, List<DataSource> dataSourceList);

}
