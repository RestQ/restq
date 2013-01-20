/**
 * 
 */
package org.restq.cluster.pipeline;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.restq.cluster.Response;
import org.restq.cluster.nio.impl.ResponseFutureImpl;

/**
 * @author ganeshs
 *
 */
public class ResponseHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
		Response response = (Response) event.getMessage();
		if (context.getAttachment() instanceof ResponseFutureImpl) {
			ResponseFutureImpl future = (ResponseFutureImpl) context.getAttachment();
			future.set(response);
		}
	}
	
}
