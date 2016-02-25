package com.github.ambry.rest;

import com.codahale.metrics.MetricRegistry;
import com.github.ambry.config.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Netty specific implementation of {@link NioServerFactory}.
 * <p/>
 * Sets up all the supporting cast required for the operation of {@link NettyServer} and returns a new instance on
 * {@link #getNioServer()}.
 */
public class NettyServerFactory implements NioServerFactory {

  private final NettyConfig nettyConfig;
  private final NettyMetrics nettyMetrics;
  private final RestRequestHandler requestHandler;
  private final PublicAccessLogger publicAccessLogger;
  private final VIPHealthCheckService vipHealthCheckService;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Creates a new instance of NettyServerFactory.
   * @param verifiableProperties the in-memory {@link VerifiableProperties} to use.
   * @param metricRegistry the {@link MetricRegistry} to use.
   * @param requestHandler the {@link RestRequestHandler} that can be used to submit requests that need to be handled.
   * @param publicAccessLogger the {@link PublicAccessLogger} that can be used for public access logging
   * @param vipHealthCheckService the {@link VIPHealthCheckService} that can be used to check the health of the system
   *                              to respond to VIP health check requests
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public NettyServerFactory(VerifiableProperties verifiableProperties, MetricRegistry metricRegistry,
      RestRequestHandler requestHandler, PublicAccessLogger publicAccessLogger, VIPHealthCheckService vipHealthCheckService) {
    if (verifiableProperties != null && metricRegistry != null && requestHandler != null && publicAccessLogger != null &&
        vipHealthCheckService != null) {
      this.nettyConfig = new NettyConfig(verifiableProperties);
      this.nettyMetrics = new NettyMetrics(metricRegistry);
      this.requestHandler = requestHandler;
      this.publicAccessLogger = publicAccessLogger;
      this.vipHealthCheckService = vipHealthCheckService;
    } else {
      StringBuilder errorMessage =
          new StringBuilder("Null arg(s) received during instantiation of NettyServerFactory -");
      if (verifiableProperties == null) {
        errorMessage.append(" [VerifiableProperties] ");
      }
      if (metricRegistry == null) {
        errorMessage.append(" [MetricRegistry] ");
      }
      if (requestHandler == null) {
        errorMessage.append(" [RestRequestHandler] ");
      }
      if (publicAccessLogger == null) {
        errorMessage.append(" [PublicAccessLogger] ");
      }
      if (vipHealthCheckService == null) {
        errorMessage.append(" [VIPHealthCheckService] ");
      }
      throw new IllegalArgumentException(errorMessage.toString());
    }
    logger.trace("Instantiated NettyServerFactory");
  }

  /**
   * Returns a new instance of {@link NettyServer}.
   * @return a new instance of {@link NettyServer}.
   */
  @Override
  public NioServer getNioServer() {
    return new NettyServer(nettyConfig, nettyMetrics, requestHandler, publicAccessLogger, vipHealthCheckService);
  }
}
