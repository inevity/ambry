package com.github.ambry.rest;

import com.codahale.metrics.MetricRegistry;
import com.github.ambry.config.VerifiableProperties;
import java.util.Properties;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * Tests functionality of {@link NettyServerFactory}.
 */
public class NettyServerFactoryTest {

  /**
   * Checks to see that getting the default {@link NioServer} (currently {@link NettyServer}) works.
   */
  @Test
  public void getNettyServerTest() {
    // dud properties. server should pick up defaults
    Properties properties = new Properties();
    VerifiableProperties verifiableProperties = new VerifiableProperties(properties);
    RestRequestHandler restRequestHandler = new MockRestRequestResponseHandler();
    PublicAccessLogger publicAccessLogger = new PublicAccessLogger(new String[]{}, new String[]{});
    VIPHealthCheckService vipHealthCheckService = new VIPHealthCheckService("/ambry-frontend/admin");

    NettyServerFactory nettyServerFactory =
        new NettyServerFactory(verifiableProperties, new MetricRegistry(), restRequestHandler, publicAccessLogger,
            vipHealthCheckService);
    NioServer nioServer = nettyServerFactory.getNioServer();
    assertNotNull("No NioServer returned", nioServer);
    assertEquals("Did not receive a NettyServer instance", NettyServer.class.getCanonicalName(),
        nioServer.getClass().getCanonicalName());
  }

  /**
   * Tests instantiation of {@link NettyServerFactory} with bad input.
   */
  @Test
  public void getNettyServerFactoryWithBadInputTest() {
    // dud properties. server should pick up defaults
    Properties properties = new Properties();
    VerifiableProperties verifiableProperties = new VerifiableProperties(properties);
    MetricRegistry metricRegistry = new MetricRegistry();
    RestRequestHandler restRequestHandler = new MockRestRequestResponseHandler();
    PublicAccessLogger publicAccessLogger = new PublicAccessLogger(new String[]{}, new String[]{});
    VIPHealthCheckService vipHealthCheckService = new VIPHealthCheckService("/ambry-frontend/admin");

    // VerifiableProperties null.
    try {
      new NettyServerFactory(null, metricRegistry, restRequestHandler, publicAccessLogger, vipHealthCheckService);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // MetricRegistry null.
    try {
      new NettyServerFactory(verifiableProperties, null, restRequestHandler, publicAccessLogger, vipHealthCheckService);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // RestRequestHandler null.
    try {
      new NettyServerFactory(verifiableProperties, metricRegistry, null, publicAccessLogger, vipHealthCheckService);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // PublicAccessLogger null.
    try {
      new NettyServerFactory(verifiableProperties, metricRegistry, restRequestHandler, null, vipHealthCheckService);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }

    // VIPHealthCheckService null.
    try {
      new NettyServerFactory(verifiableProperties, metricRegistry, restRequestHandler, publicAccessLogger, null);
      fail("Instantiation should have failed because one of the arguments was null");
    } catch (IllegalArgumentException e) {
      // expected. Nothing to do.
    }
  }
}
