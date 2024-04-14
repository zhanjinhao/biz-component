package cn.addenda.component.bean.pojo;

import lombok.*;

/**
 * 四元
 *
 * @author addenda
 * @since 2023/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Quaternary<T1, T2, T3, T4> {

  private T1 f1;
  private T2 f2;
  private T3 f3;
  private T4 f4;

}
