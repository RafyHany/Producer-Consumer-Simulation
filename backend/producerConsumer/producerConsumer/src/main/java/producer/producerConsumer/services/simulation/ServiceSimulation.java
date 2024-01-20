package producer.producerConsumer.services.simulation;
import producer.producerConsumer.services.models.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServiceSimulation {
    private static ServiceSimulation sim = null ;
    private Graph graph = new Graph() ;
    private int productInStock ;
    private ServiceSimulation(){}

    public static synchronized ServiceSimulation getInstance(){
        if(sim == null)
            sim = new ServiceSimulation() ;
        return sim ;
    }
    public void addProduct(){
        this.productInStock ++ ;
    }
    public void removeProduct(){
        this.productInStock -- ;
    }

    public void buildGraph(List<ElementDto> elementDtos){
        for(ElementDto e : elementDtos ){
            if(e.name.equals("line")){
                this.graph.addLink((Link) ElementFactory.getType(e));
            }
            this.graph.addElement(ElementFactory.getType(e));
        }
    }
    public void makeLinksBetweenElements(){
        for(Link l : this.graph.getLinks()){
            Element from = this.graph.getElements().get(l.getFrom()) ;
            Element to = this.graph.getElements().get(l.getTo()) ;
            if(from.getName().equals("circle")){
                ((Machine)from).setOutQueue((Queues)to) ;
            }
            else{

                ((Machine)to).addToInQueues((Queues)from);
                ((Queues)from).makeMachineFree((Machine)to);
                ((Queues)from).addToOutMachines((Machine)to);
                ((Queues)from).addToId(to.getId());

            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void runSimulation() throws InterruptedException {
        int OriginalInputProducts = productInStock;
        Graph graph = new Graph();
        while (true) {
           TimeUnit.SECONDS.sleep(getRandomTime());
            if(productInStock > 0){
                ((Queues) graph.getElements().get("0")).addToProducts(new Product());
                productInStock--;
            }
        }
    }

    public int getRandomTime(){
        int minimum = 1;
        int maximum = 5;
        int randomtime = (int)Math.floor(Math.random() *(maximum - minimum + 1) + minimum);
        return randomtime;
    }

}
