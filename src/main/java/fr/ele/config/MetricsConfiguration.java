package fr.ele.config;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration extends MetricsConfigurerAdapter implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(MetricsConfiguration.class);

    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

    private static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = new HealthCheckRegistry();

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment, "metrics.");
    }

    @Override
    @Bean
    public MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }

    @Override
    @Bean
    public HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }

    @PostConstruct
    public void init() {
        log.debug("Registring JVM gauges");
        METRIC_REGISTRY.register("jvm.memory", new MemoryUsageGaugeSet());
        METRIC_REGISTRY.register("jvm.garbage", new GarbageCollectorMetricSet());
        METRIC_REGISTRY.register("jvm.threads", new ThreadStatesGaugeSet());
        METRIC_REGISTRY.register("jvm.files", new FileDescriptorRatioGauge());
        METRIC_REGISTRY.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        ConsoleReporter.forRegistry(metricRegistry).build().start(30, TimeUnit.SECONDS);
        if (propertyResolver.getProperty("jmx.enabled", Boolean.class, false)) {
            log.info("Initializing Metrics JMX reporting");
            final JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
            jmxReporter.start();
        }
    }

    @Configuration
    @ConditionalOnClass(Graphite.class)
    public static class GraphiteRegistry implements EnvironmentAware {
        @Inject
        private MetricRegistry metricRegistry;

        private RelaxedPropertyResolver propertyResolver;

        @Override
        public void setEnvironment(Environment environment) {
            propertyResolver = new RelaxedPropertyResolver(environment, "metrics.graphite");
        }

        @PostConstruct
        private void init() {
            Boolean graphiteEnabled = propertyResolver.getProperty("enabled", Boolean.class, false);
            if (graphiteEnabled) {
                log.info("Initializing Metrics Graphite reporting");
                String graphiteHost = propertyResolver.getRequiredProperty("host");
                Integer graphitePort = propertyResolver.getRequiredProperty("port", Integer.class);
                Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
                GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
                        .convertDurationsTo(TimeUnit.MILLISECONDS).build(graphite);
                graphiteReporter.start(1, TimeUnit.MINUTES);
            } else {
                log.warn("Graphite server is not configured, unable to send any data to Graphite");
            }
        }
    }
}
