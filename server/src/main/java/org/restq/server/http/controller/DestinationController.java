/**
 * 
 */
package org.restq.server.http.controller;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import org.restq.messaging.Destination;
import org.restq.messaging.Queue;
import org.restq.messaging.Topic;
import org.restq.messaging.impl.QueueImpl;
import org.restq.messaging.impl.TopicImpl;
import org.restq.messaging.repository.DestinationRepository;
import org.restq.messaging.service.DestinationService;
import org.restq.server.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.reflect.TypeToken;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

/**
 * @author ganeshs
 *
 */
@Controller("httpDestinationController")
public class DestinationController {
	
	@Autowired
	private DestinationService destinationService;
	
	@Autowired
	private DestinationRepository destinationRepository;

	public void createQueues(Request request, Response response) {
		Type type = new TypeToken<List<QueueImpl>>() {}.getType();
		List<Queue> queues = GsonUtil.createGson().fromJson(request.getBody().toString(Charset.defaultCharset()), type);
		for (Queue queue : queues) {
			destinationService.createQueue(queue);
		}
	}
	
	public void createTopics(Request request, Response response) {
		Type type = new TypeToken<List<TopicImpl>>() {}.getType();
		List<Topic> topics = GsonUtil.createGson().fromJson(request.getBody().toString(Charset.defaultCharset()), type);
		for (Topic topic : topics) {
			destinationService.createTopic(topic);
		}
	}
	
	public void getQueues(Request request, Response response) {
		List<Destination> destinations = destinationRepository.findAll();
		response.setBody(destinations);
	}
}
