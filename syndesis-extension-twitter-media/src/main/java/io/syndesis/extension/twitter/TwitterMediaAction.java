package io.syndesis.extension.twitter;

import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import twitter4j.MediaEntity;
import twitter4j.Status;

@Action(id = "twitterMedia", name = "TwitterMedia", description = "Retrieve the URL of the Tweets media (if any)", tags = {
		"twitter", "media", "extension" })
public class TwitterMediaAction implements Step {

	@Override
	public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route,
			Map<String, Object> parameters) {
		ObjectHelper.notNull(route, "route");

		try {
			route.process(this::process);
		} catch (NullPointerException e) {
			route.log(e.getMessage());
		} catch (ClassCastException e) {
			route.log(e.getMessage());
		}
		return Optional.of(route);
	}

	private void process(Exchange exchange) {
		// validate input
		if (ObjectHelper.isEmpty(exchange)) {
			throw new NullPointerException("Exchange is empty. Should be impossible.");
		}

		Message message = exchange.getMessage();
		if (ObjectHelper.isEmpty(message)) {
			throw new NullPointerException("Message is empty. Should be impossible.");
		}

		Object body = message.getBody();

		if (body instanceof Status) {
			MediaEntity[] entities = ((Status) body).getMediaEntities();
			if (entities != null) {
				String urls = "";

				for (int i = 0; i < entities.length; i++) {
					MediaEntity entity = entities[i];
					urls += entity.getMediaURL();

					if (entities.length > (1 + i)) {
						urls += ", ";
					}
				}

				if (!ObjectHelper.isEmpty(urls)) {
					message.setBody(urls);
					message.getExchange().setOut(message);
				} else {
					message.getExchange().setOut(null); // "no Media Objects in Tweet";
				}
			}
		} else {
			throw new ClassCastException("Body isn't Status, why are you using this component!?"
					+ (body != null ? body.getClass() : " empty"));
		}
	}
}