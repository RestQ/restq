/**
 * 
 */
package org.restq.server.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.ConflictException;
import com.strategicgains.restexpress.exception.NotFoundException;
import com.strategicgains.restexpress.pipeline.MessageObserver;
import com.strategicgains.restexpress.plugin.AbstractPlugin;

/**
 * @author ganeshs
 *
 */
@Component
public class LoggerPlugin extends AbstractPlugin {
	
	@Override
	public void bind(RestExpress server) {
		
	}
	
	public LoggerPlugin register(RestExpress server) {
		server.addMessageObserver(new LogMessageObserver());
		return this;
	}
	
	private static class LogMessageObserver extends MessageObserver {
		
		private static Logger logger = Logger.getLogger("");
		
		private Map<String, Timer> timers = new ConcurrentHashMap<String, Timer>();
		
		public LogMessageObserver() {
		}
		
		
		
		@Override
		protected void onReceived(Request request, Response response) {
			timers.put(request.getCorrelationId(), new Timer());
		}
		
		@Override
		protected void onSuccess(Request request, Response response) {
			super.onSuccess(request, response);
		}
		
		@Override
		protected void onComplete(Request request, Response response) {
			Timer timer = timers.remove(request.getCorrelationId());
			if (timer != null) {
				timer.stop();
				logger.info(request.getEffectiveHttpMethod() + " " + request.getUrl() + " responded with " + response.getResponseStatus() + " in " + timer);
			} else {
				logger.info(request.getEffectiveHttpMethod() + " " + request.getUrl() + " responded with " + response.getResponseStatus());
			}
		}
		
		@Override
		protected void onException(Throwable e, Request request, Response response) {
			if (e instanceof NotFoundException || e instanceof ConflictException) {
				return;
			}
			logger.error(request.getEffectiveHttpMethod().toString() + " " + request.getUrl() + " threw exception: " + e.getClass().getSimpleName(), e);
			super.onException(e, request, response);
		}
		
	}
	
	private static class Timer {
		
		private long startMillis = 0;
		
		private long stopMillis = 0;
		
		private long latency = 0;

		public Timer() {
			this.startMillis = System.currentTimeMillis();
		}

		public void stop() {
			latency = System.currentTimeMillis() - startMillis;
		}
		
		public long getLatency() {
			return latency;
		}

		public String toString() {
			return String.valueOf(latency) + "ms";
		}
	}

}
