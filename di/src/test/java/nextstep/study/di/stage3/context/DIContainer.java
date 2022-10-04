package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = instantiate(classes);
        for (Object bean : this.beans) {
            injectDependencies(bean);
        }
    }

    private Set<Object> instantiate(final Set<Class<?>> classes) {
        Set<Object> results = new HashSet<>();
        for (Class<?> clazz : classes) {
            try {
                final Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                results.add(constructor.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    private void injectDependencies(final Object bean) {
        for (final Object elem : this.beans) {
            final Field[] fields = elem.getClass().getDeclaredFields();
            setFields(bean, elem, fields);
        }
    }

    private void setFields(Object bean, Object elem, Field[] fields) {
        for (final Field field : fields) {
            try {
                if (!field.getType().isInstance(bean)) {
                    return;
                }
                field.setAccessible(true);
                field.set(elem, bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        for (final Object bean : this.beans) {
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            }
        }
        throw new IllegalArgumentException("!");
    }
}
