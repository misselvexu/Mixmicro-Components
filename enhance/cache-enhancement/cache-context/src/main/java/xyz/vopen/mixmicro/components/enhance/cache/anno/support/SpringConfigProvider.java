package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import xyz.vopen.mixmicro.components.enhance.cache.anno.method.SpringCacheContext;
import xyz.vopen.mixmicro.components.enhance.cache.support.CacheMessagePublisher;
import xyz.vopen.mixmicro.components.enhance.cache.support.StatInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class SpringConfigProvider extends ConfigProvider implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringConfigProvider() {
        super();
        encoderParser = new DefaultSpringEncoderParser();
        keyConvertorParser = new DefaultSpringKeyConvertorParser();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void doInit() {
        if (encoderParser instanceof ApplicationContextAware) {
            ((ApplicationContextAware) encoderParser).setApplicationContext(applicationContext);
        }
        if (keyConvertorParser instanceof ApplicationContextAware) {
            ((ApplicationContextAware) keyConvertorParser).setApplicationContext(applicationContext);
        }
        super.doInit();
    }

    @Override
    protected CacheContext newContext() {
        return new SpringCacheContext(this, globalCacheConfig, applicationContext);
    }

    @Autowired(required = false)
    @Override
    public void setCacheManager(SimpleCacheManager cacheManager) {
        super.setCacheManager(cacheManager);
    }

    @Autowired(required = false)
    @Override
    public void setEncoderParser(EncoderParser encoderParser) {
        super.setEncoderParser(encoderParser);
    }

    @Autowired(required = false)
    @Override
    public void setKeyConvertorParser(KeyConvertorParser keyConvertorParser) {
        super.setKeyConvertorParser(keyConvertorParser);
    }

    @Autowired(required = false)
    @Override
    public void setCacheMonitorManager(CacheMonitorManager cacheMonitorManager) {
        super.setCacheMonitorManager(cacheMonitorManager);
    }

    @Autowired(required = false)
    @Override
    public void setMetricsCallback(Consumer<StatInfo> metricsCallback) {
        super.setMetricsCallback(metricsCallback);
    }

    @Autowired(required = false)
    @Override
    public void setCacheMessagePublisher(CacheMessagePublisher cacheMessagePublisher) {
        super.setCacheMessagePublisher(cacheMessagePublisher);
    }

}
