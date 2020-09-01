package com.tingxuelou.www.provider.init;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化引导类
 * <p>
 * Date: 2020/9/1 下午7:09
 * Copyright (C), 2015-2020
 */
public class Initiator implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(Initiator.class);

    /**
     * 在XML里配置的初始化对象列表
     */
    private volatile List<String> initList;

    /**
     * key: bean id, value: bean id对应的AbstractInit实例类
     */
    private static volatile ConcurrentHashMap<String, AbstractInit> initMap = new ConcurrentHashMap<>();

    /**
     * 自动注入:ApplicationContext
     * 便于子类继承后可以直接使用
     */
    protected ApplicationContext applicationContext;

    // 启动标记：0-未初始化；1-进行中；2-初始化结束
    private volatile InitStage hasInited = InitStage.INIT;

    public List<String> getInitList() {
        return initList;
    }

    public void setInitList(List<String> initList) {
        this.initList = initList;
    }

    /**
     * 定义公共处理流程
     *
     * @param context spring-mvc 上下文
     */
    private synchronized void init(ApplicationContext context) {

        // 初始化阶段串行执行
        if (hasInited != InitStage.INIT) {
            log.info("init-operation can't be reduplicated, the stage now is {}", hasInited);
            return;
        }

        // 设置标记位
        hasInited = InitStage.ING;

        // 开始初始化过程
        startup();

        hasInited = InitStage.DONE;
    }

    /**
     * 按照application-context.xml里的bean配置属性启动顺序依次执行
     */
    private void startup() {
        if (CollectionUtils.isEmpty(initList)) {
            log.info("no initiator list to start-up");
            return;
        }

        // key: initSequence, value: abstractInitList的index
        ConcurrentHashMap<String, AbstractInit> sequenceMap = new ConcurrentHashMap<>();
        for (String beanId : initList) {
            AbstractInit initiator = null;
            Exception exception = null;
            try {
                initiator = (AbstractInit)applicationContext.getBean(beanId);
            } catch (NoSuchBeanDefinitionException ex) {
                exception = ex;
            }

            // 通过bean id获取不到bean的情况下继续执行
            if (initiator == null) {
                log.warn("get_spring_bean_by_id_error_than_escape_initiator_process, beanId:" + beanId, exception);
                continue;
            }

            // 不能有重复的beanId
            if (sequenceMap.containsKey(beanId)) {
                throw new InvalidPropertyException(initiator.getClass(), "initSequence", "启动顺序和其他启动类的重复了");
            }

            // 不做try-catch处理，子类初始化异常就支持抛出终止启动
            log.debug("init-method start, class:{}", initiator.getClass().getName());
            initiator.init(applicationContext);
            log.debug("init-method done, class:{}", initiator.getClass().getName());
            sequenceMap.put(beanId, initiator);
        }

        initMap = sequenceMap;
    }

    /**
     * Spring容器停止时执行
     */
    private void destroy() {
        // 执行子类自定义释放资源操作
        // 及时子类的操作出现异常，也不一样阻断父类的释放资源操作
        try {
            for(int i = initList.size() - 1; i >= 0; i--){
                String beanId = initList.get(i);
                if (StringUtils.isEmpty(beanId)) {
                    log.warn("spring_get_bean_by_id_error_than_escape_destroy_process, beanId:" + beanId);
                    continue;
                }
                AbstractInit item = initMap.get(beanId);
                log.debug("destroy-method start, class:{}", item.getClass().getName());
                item.destroy(applicationContext);
                log.debug("destroy-method done, class:{}", item.getClass().getName());
            }
        } catch (Exception e) {
            log.warn("异常:", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        /*
         Spring初始化完成:
         在web项目中(springMVC), 系统会存在两个容器，一个是root application context, 另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
         这种情况下，就会造成onApplicationEvent方法被执行两次。为了避免上面提到的问题，可以在root application context初始化完成后调用逻辑代码
         */
        if (event instanceof ContextRefreshedEvent) {
            init(applicationContext);

            // Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件
        } else if (event instanceof ContextStoppedEvent) {
            destroy();
        }
    }


    /**
     * 初始化标记常量定义
     */
    private enum InitStage {
        INIT, ING, DONE
    }
}
