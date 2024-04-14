package cn.addenda.component.bean.pojo;

import lombok.*;

/**
 * 三元
 *
 * @author addenda
 * @since 2023/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ternary<T1, T2, T3> {

  private T1 f1;
  private T2 f2;
  private T3 f3;

}
