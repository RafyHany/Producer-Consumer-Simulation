package producer.producerConsumer.services.simulation;

import producer.producerConsumer.services.models.Element;
import producer.producerConsumer.services.models.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<String , Element> elements = new HashMap<>();
    private List<Link> links = new ArrayList<>() ;

    public Graph(){}
    public void addElement(Element element){
        elements.put(element.getId() , element) ;
        return;
    }
    public void addLink(Link link){
        links.add(link) ;
        return;
    }

    public Map<String, Element> getElements() {
        return elements;
    }

    public void setElements(Map<String, Element> elements) {
        this.elements = elements;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
