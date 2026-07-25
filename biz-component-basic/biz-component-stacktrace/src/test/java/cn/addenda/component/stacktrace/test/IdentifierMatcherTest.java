package cn.addenda.component.stacktrace.test;

import cn.addenda.component.stacktrace.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentifierMatcherTest {

  @Test
  void testIdentifier() {
    IdentifierMatcher identifierMatcher1 = IdentifierMatcherFactory.getIdentifierMatcher(
            IdentifierMatcherFactory.withHash(IdentifierMatcherTest.class, "testIdentifier"));
    System.out.println(identifierMatcher1);
    Assertions.assertEquals(ClassNameAndMethodNameMatcher.class, identifierMatcher1.getClass());
    IdentifierMatcher identifierMatcher2 = IdentifierMatcherFactory.getIdentifierMatcher(IdentifierMatcherTest.class);
    System.out.println(identifierMatcher2);
    Assertions.assertEquals(ClassNameMatcher.class, identifierMatcher2.getClass());
    IdentifierMatcher identifierMatcher3 = IdentifierMatcherFactory.getIdentifierMatcher(IdentifierMatcherTest.class, true);
    System.out.println(identifierMatcher3);
    Assertions.assertEquals(ClassNamePrefixMatcher.class, identifierMatcher3.getClass());
  }

}
