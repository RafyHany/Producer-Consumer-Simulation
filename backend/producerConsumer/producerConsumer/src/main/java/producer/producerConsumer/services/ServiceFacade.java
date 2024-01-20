package producer.producerConsumer.services;

import org.springframework.stereotype.Service;
import producer.producerConsumer.services.models.ElementDto;
import producer.producerConsumer.services.simulation.Graph;
import producer.producerConsumer.services.simulation.ServiceSimulation;

import java.util.List;



@Service
public class ServiceFacade {
    private ServiceSimulation sim = ServiceSimulation.getInstance();
    public ServiceFacade(){}
    public void startSimulation(List<ElementDto> dtos){
        sim.buildGraph(dtos);
        sim.makeLinksBetweenElements();
        // complete amen
    }
    public void addProduct(){
        sim.addProduct();
    }
    public Graph getGraph(){
        return sim.getGraph() ;
    }
}
