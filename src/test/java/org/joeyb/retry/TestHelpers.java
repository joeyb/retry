package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.lang.reflect.Constructor;

class TestHelpers {

    static void assertClassOnlyHasPrivateConstructor(Class<?> klass) {
        Constructor<?>[] constructors = klass.getDeclaredConstructors();

        assertThat(constructors)
                .isNotNull()
                .hasSize(1);

        Constructor<?> constructor = constructors[0];

        assertThat(constructor)
                .isNotNull()
                .matches(c -> c.getParameterCount() == 0)
                .matches(c -> !c.isAccessible());

        // Execute the constructor for code coverage.
        constructor.setAccessible(true);

        try {
            constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TestHelpers() { }
}
