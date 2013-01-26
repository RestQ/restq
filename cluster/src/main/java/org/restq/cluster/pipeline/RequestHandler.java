/**
 * 
 */
package org.restq.cluster.pipeline;

import java.io.DataOutput;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.restq.cluster.RequestMapper;
import org.restq.core.Request;
import org.restq.core.Response;
import org.restq.core.Serializer;
import org.restq.core.Response.Status;

/**
 * @author ganeshs
 *
 */
public class RequestHandler extends SimpleChannelUpstreamHandler {
	
	private Serializer serializer;
	
	private RequestMapper requestMapper;
	
	private List<MessageObserver> messageObservers;
	
	private List<PreProcessor> preProcessors;
	
	private List<PostProcessor> postProcessors;
	
	private static Logger logger = Logger.getLogger(RequestHandler.class);
	
	public RequestHandler() {
	}
	
	/**
	 * @param serializer
	 * @param mapper
	 * @param messageObservers
	 * @param preProcessors
	 * @param postProcessors
	 */
	public RequestHandler(Serializer serializer, RequestMapper mapper, List<MessageObserver> messageObservers, List<PreProcessor> preProcessors, List<PostProcessor> postProcessors) {
		this.serializer = serializer;
		this.requestMapper = mapper;
		this.messageObservers = messageObservers;
		this.preProcessors = preProcessors;
		this.postProcessors = postProcessors;
	}

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
		logger.info("Received a message");
		MessageContext messageContext = createMessageContext(context, (Request)event.getMessage());
		try {
			notifyReceived(messageContext);
			invokePreProcessors(messageContext);
			Response response = handleRequest(messageContext.getRequest());
			messageContext.setResponse(response);
			invokePostProcessors(messageContext);
			
			DynamicChannelBuffer buffer = new DynamicChannelBuffer(10);
			DataOutput output = new ChannelBufferOutputStream(buffer);
			serializer.serialize(output, response);
			context.getChannel().write(buffer);
			notifySuccess(messageContext);
		} catch (Throwable throwable) {
			notifyFailure(messageContext, throwable);
		} finally {
			notifyCompleted(messageContext);
		}
	}
	
	/**
	 * @param context
	 * @param request
	 * @return
	 */
	protected MessageContext createMessageContext(ChannelHandlerContext context, Request request) {
		MessageContext messageContext = new MessageContext();
		messageContext.setRequest(request);
		context.setAttachment(messageContext);
		return messageContext;
	}
	
	public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) throws Exception {
		MessageContext messageContext = (MessageContext) context.getAttachment();
		Response response = new Response(Status.failed, event.getCause().getMessage());
		messageContext.setResponse(response);
		notifyFailure(messageContext, event.getCause());
	}
	
	protected void notifyReceived(MessageContext messageContext) {
		for (MessageObserver observer : messageObservers) {
			observer.onReceived(messageContext.getRequest());
		}
	}
	
	protected void notifySuccess(MessageContext messageContext) {
		for (MessageObserver observer : messageObservers) {
			observer.onSuccess(messageContext.getRequest(), messageContext.getResponse());
		}
	}
	
	protected void notifyFailure(MessageContext messageContext, Throwable throwable) {
		for (MessageObserver observer : messageObservers) {
			observer.onFailure(messageContext.getRequest(), messageContext.getResponse(), throwable);
		}
	}
	
	protected void notifyCompleted(MessageContext messageContext) {
		for (MessageObserver observer : messageObservers) {
			observer.onCompleted(messageContext.getRequest(), messageContext.getResponse());
		}
	}
	
	protected void invokePreProcessors(MessageContext messageContext) {
		for (PreProcessor processor : preProcessors) {
			processor.process(messageContext.getRequest());
		}
	}
	
	protected void invokePostProcessors(MessageContext messageContext) {
		for (PostProcessor processor : postProcessors) {
			processor.process(messageContext.getRequest(), messageContext.getResponse());
		}
	}
	
	protected Response handleRequest(Request request) {
		return requestMapper.mappingFor(request.getClass()).invoke(request);
	}

	/**
	 * @return the serializer
	 */
	public Serializer getSerializer() {
		return serializer;
	}

	/**
	 * @param serializer the serializer to set
	 */
	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	/**
	 * @return the requestMapper
	 */
	public RequestMapper getRequestMapper() {
		return requestMapper;
	}

	/**
	 * @param requestMapper the requestMapper to set
	 */
	public void setRequestMapper(RequestMapper requestMapper) {
		this.requestMapper = requestMapper;
	}

	/**
	 * @return the messageObservers
	 */
	public List<MessageObserver> getMessageObservers() {
		return messageObservers;
	}

	/**
	 * @param messageObservers the messageObservers to set
	 */
	public void setMessageObservers(List<MessageObserver> messageObservers) {
		this.messageObservers = messageObservers;
	}

	/**
	 * @return the preProcessors
	 */
	public List<PreProcessor> getPreProcessors() {
		return preProcessors;
	}

	/**
	 * @param preProcessors the preProcessors to set
	 */
	public void setPreProcessors(List<PreProcessor> preProcessors) {
		this.preProcessors = preProcessors;
	}

	/**
	 * @return the postProcessors
	 */
	public List<PostProcessor> getPostProcessors() {
		return postProcessors;
	}

	/**
	 * @param postProcessors the postProcessors to set
	 */
	public void setPostProcessors(List<PostProcessor> postProcessors) {
		this.postProcessors = postProcessors;
	}
}
