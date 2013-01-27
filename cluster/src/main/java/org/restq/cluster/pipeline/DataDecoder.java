/**
 * 
 */
package org.restq.cluster.pipeline;

import java.io.DataInput;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.restq.core.Serializer;

/**
 * @author ganeshs
 *
 */
public class DataDecoder extends OneToOneDecoder {
	
	private Serializer serializer;
	
	public DataDecoder() {
		this(new Serializer());
	}
	
	public DataDecoder(Serializer serializer) {
		this.serializer = serializer;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) msg;
		DataInput dataInput = new ChannelBufferInputStream(buffer);
		return serializer.deserialize(dataInput);
	}
}
