package cn.addenda.component.dynamicdatasource;

import lombok.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author addenda
 * @since 2022/3/3 17:26
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultiDataSourceEntry {

  // todo 不区分MASTER和SLAVE。提供抽象方法给子类实现，进而实现一主多从。
  // todo 更名为multi-datasource-component

  private DataSource master;

  private List<DataSource> slaves = new ArrayList<>();

}
