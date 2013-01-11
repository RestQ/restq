/**
 * 
 */
package org.restq.core.cluster.nio;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @author ganeshs
 *
 */
public class ClusterChannelHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
		super.messageReceived(context, event);
	}
	
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		super.writeRequested(ctx, e);
	}
}
