/**
 * 
 */
package org.restq.cluster.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;

/**
 * @author ganeshs
 *
 */
public class ClusterPipelineFactory implements ChannelPipelineFactory {
	
	private ChannelHandler decoder;
	
	private ExecutionHandler executionHandler;
	
	private List<RequestHandler> requestHandlers = new ArrayList<RequestHandler>();
	
	public ClusterPipelineFactory() {
	}

	/**
	 * @param decoder
	 * @param executionHandler
	 * @param requestHandlers
	 */
	public ClusterPipelineFactory(ChannelHandler decoder, ExecutionHandler executionHandler, List<RequestHandler> requestHandlers) {
		this.decoder = decoder;
		this.executionHandler = executionHandler;
		this.requestHandlers = requestHandlers;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		if (decoder != null) {
			pipeline.addLast("requestDecoder", decoder);
		}
		if(executionHandler != null) {
			pipeline.addLast("executionHandler", executionHandler);
		}
		for (RequestHandler handler : requestHandlers) {
			pipeline.addLast(handler.getClass().getSimpleName(), handler);
		}
		return pipeline;
	}

	/**
	 * @return the decoder
	 */
	public ChannelHandler getDecoder() {
		return decoder;
	}

	/**
	 * @param decoder the decoder to set
	 */
	public void setDecoder(ChannelHandler decoder) {
		this.decoder = decoder;
	}

	/**
	 * @return the executionHandler
	 */
	public ExecutionHandler getExecutionHandler() {
		return executionHandler;
	}

	/**
	 * @param executionHandler the executionHandler to set
	 */
	public void setExecutionHandler(ExecutionHandler executionHandler) {
		this.executionHandler = executionHandler;
	}

	/**
	 * @return the requestHandlers
	 */
	public List<RequestHandler> getRequestHandlers() {
		return requestHandlers;
	}

	/**
	 * @param requestHandlers the requestHandlers to set
	 */
	public void setRequestHandlers(List<RequestHandler> requestHandlers) {
		this.requestHandlers = requestHandlers;
	}
	
	/**
	 * @param handler
	 */
	public void addRequestHandler(RequestHandler handler) {
		this.requestHandlers.add(handler);
	}
}
