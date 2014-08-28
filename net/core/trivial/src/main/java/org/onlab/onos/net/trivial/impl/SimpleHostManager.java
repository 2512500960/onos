package org.onlab.onos.net.trivial.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onlab.onos.event.AbstractListenerRegistry;
import org.onlab.onos.event.EventDeliveryService;
import org.onlab.onos.net.host.HostDescription;
import org.onlab.onos.net.host.HostEvent;
import org.onlab.onos.net.host.HostListener;
import org.onlab.onos.net.host.HostProvider;
import org.onlab.onos.net.host.HostProviderRegistry;
import org.onlab.onos.net.host.HostProviderService;
import org.onlab.onos.net.provider.AbstractProviderRegistry;
import org.onlab.onos.net.provider.AbstractProviderService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Provides basic implementation of the host SB &amp; NB APIs.
 */
@Component(immediate = true)
@Service
public class SimpleHostManager
        extends AbstractProviderRegistry<HostProvider, HostProviderService>
        implements HostProviderRegistry {

    private final Logger log = getLogger(getClass());

    private final AbstractListenerRegistry<HostEvent, HostListener>
            listenerRegistry = new AbstractListenerRegistry<>();

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    private EventDeliveryService eventDispatcher;


    @Activate
    public void activate() {
        eventDispatcher.addSink(HostEvent.class, listenerRegistry);
        log.info("Started");
    }

    @Deactivate
    public void deactivate() {
        eventDispatcher.removeSink(HostEvent.class);
        log.info("Stopped");
    }

    @Override
    protected HostProviderService createProviderService(HostProvider provider) {
        return new InternalHostProviderService(provider);
    }

    // Personalized host provider service issued to the supplied provider.
    private class InternalHostProviderService extends AbstractProviderService<HostProvider>
            implements HostProviderService {

        public InternalHostProviderService(HostProvider provider) {
            super(provider);
        }

        @Override
        public void hostDetected(HostDescription hostDescription) {
            log.info("Host {} detected", hostDescription);
        }

        @Override
        public void hostVanished(HostDescription hostDescription) {
            log.info("Host {} vanished", hostDescription);
        }
    }
}
