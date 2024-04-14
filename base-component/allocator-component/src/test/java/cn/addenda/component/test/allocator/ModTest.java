package cn.addenda.component.test.allocator;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author addenda
 * @since 2023/6/2 18:23
 */
public class ModTest {

    @Test
    public void test() {
        int segment = 2 << 5;
        for (int i = 0; i < 100; i++) {
            int v1 = i % (segment);
            int v2 = (i & ((segment) - 1));
            System.out.println(i + " : " + v1 + " " + v2);
            Assert.assertEquals((i % (segment)), (i & ((segment) - 1)));
        }
    }

}
