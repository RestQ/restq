/**
 * 
 */
package org.restq.cluster.pipeline;

import java.io.DataInput;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.restq.cluster.nio.Serializer;

/**
 * @author ganeshs
 *
 */
public class DataDecoder extends FrameDecoder {
	
	private Serializer serializer;
	
	public DataDecoder() {
		this(new Serializer());
	}
	
	public DataDecoder(Serializer serializer) {
		this.serializer = serializer;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		DataInput dataInput = new ChannelBufferInputStream(buffer);
		return serializer.deserialize(dataInput);
	}
}
