package ch.frostnova.mimic.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Bean reporting CPU metrics to Actuator.
 * <p> Output: <a href="https://localhost:9000/actuator/metrics/process.cpu.load">CPU Load</a>
 *
 * @author pwalser
 * @since 03.09.2018
 */
@Component
public class CpuMetrics {

    private final static String METRICS_NAME = "process.cpu.load";

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Gauge.builder(METRICS_NAME, this, CpuMetrics::getProcessCpuLoad).baseUnit("%").description("CPU Load").register(meterRegistry);
    }

    public Double getProcessCpuLoad() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

            return Optional.ofNullable(list)
                    .map(l -> l.isEmpty() ? null : l)
                    .map(List::iterator)
                    .map(Iterator::next)
                    .map(Attribute.class::cast)
                    .map(Attribute::getValue)
                    .map(Double.class::cast)
                    .orElse(null);

        } catch (Exception ex) {
            return null;
        }
    }
}
