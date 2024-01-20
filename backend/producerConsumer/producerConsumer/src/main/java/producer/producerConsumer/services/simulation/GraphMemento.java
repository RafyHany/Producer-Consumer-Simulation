package producer.producerConsumer.services.simulation;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import producer.producerConsumer.services.models.Element;
import producer.producerConsumer.services.models.Link;

class GraphMemento {
    private final Map<String, Element> elements;
    private final List<Link> links;

    public GraphMemento(Map<String, Element> elements, List<Link> links) {
        this.elements = new HashMap<>(elements);
        this.links = List.copyOf(links);
    }

    public Map<String, Element> getElements() {
        return elements;
    }

    public List<Link> getLinks() {
        return links;
    }
}
