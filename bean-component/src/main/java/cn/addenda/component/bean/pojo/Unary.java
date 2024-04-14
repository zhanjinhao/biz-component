package cn.addenda.component.bean.pojo;

import lombok.*;

/**
 * 一元
 *
 * @author addenda
 * @since 2023/1/22 13:47
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Unary<T1> {

  private T1 f1;

}
