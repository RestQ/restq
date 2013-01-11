/**
 * 
 */
package org.restq.core.cluster.impl;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.restq.core.cluster.ClusterManager;
import org.restq.core.cluster.Event;
import org.restq.core.cluster.JoinRequest;
import org.restq.core.cluster.MembershipEvent;
import org.restq.core.cluster.Request;
import org.restq.core.cluster.Response;
import org.restq.core.cluster.nio.DataSerializable;

/**
 * @author ganeshs
 *
 */
public class RequestHandler extends SimpleChannelUpstreamHandler {
	
	private ClusterManager clusterManager;
	
	public RequestHandler(ClusterManager clusterManager) {
		this.clusterManager = clusterManager;
	}

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
		DataSerializable data = (DataSerializable) event.getMessage();
		if (data instanceof Request) {
			handleRequest((Request) data);
		} else if (data instanceof Event) {
			handleEvent((Event) data);
		}
		super.messageReceived(context, event);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) throws Exception {
		super.exceptionCaught(context, event);
	}
	
	protected Response handleRequest(Request request) {
		if (request instanceof JoinRequest) {
			return clusterManager.join((JoinRequest) request);
		}
		return null;
	}
	
	protected void handleEvent(Event event) {
		if (event instanceof MembershipEvent) {
			
		}
	}
}
