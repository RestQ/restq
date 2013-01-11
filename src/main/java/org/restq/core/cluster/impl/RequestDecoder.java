/**
 * 
 */
package org.restq.core.cluster.impl;

import java.io.DataInput;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.restq.core.cluster.nio.Serializer;

/**
 * @author ganeshs
 *
 */
public class RequestDecoder extends FrameDecoder {
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		DataInput dataInput = new ChannelBufferInputStream(buffer);
		return Serializer.instance().deserialize(dataInput);
	}
}
