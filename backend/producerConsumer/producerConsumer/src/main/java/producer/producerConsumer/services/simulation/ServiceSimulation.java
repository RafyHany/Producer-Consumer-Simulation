package producer.producerConsumer.services.simulation;
import producer.producerConsumer.services.models.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServiceSimulation {
    private static ServiceSimulation sim = null ;
    private Graph graph = new Graph() ;
    private int productInStock ;

    private Boolean end = false;
    
    private Caretaker caretaker = new Caretaker(); 

    public void saveState() {
        // Save the current state of the graph to the caretaker
        caretaker.setMemento(graph.createMemento());
    }

    public void restoreState() {
        // Restore the state from the caretaker
        graph.restoreMemento(caretaker.getMemento());
    }

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
        while (!end) {
           TimeUnit.SECONDS.sleep(getRandomTime());
            if(productInStock > 0){
                ((Queues) this.graph.getElements().get("0")).addToProducts(new Product());
                sim.saveState();
                productInStock--;
            }
        }
        end = false;
    }

    public void endSimulation() {
        this.productInStock = 0;
        this.end = true;
    }

    public int getRandomTime(){
        int minimum = 1;
        int maximum = 5;
        int randomtime = (int)Math.floor(Math.random() *(maximum - minimum + 1) + minimum);
        return randomtime;
    }

    public void start(){
        this.end = false;
    }

}
