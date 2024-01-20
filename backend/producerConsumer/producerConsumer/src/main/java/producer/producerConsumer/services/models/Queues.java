package producer.producerConsumer.services.models;

import java.util.HashMap;
import java.util.Map;

public class Queues extends Element {
    private Map<String, Machine> outMachines = new HashMap<>();
    private Map<String, Machine> freeMachines = new HashMap<>();

    public Queues(){}
    public Queues(ElementDto dto) {
        super(dto);
    }

    public void makeMachineFree(String id){
        freeMachines.put(id, outMachines.get(id)) ;
        return ;
    }
    public void makeMachineFree(Machine machine){
        freeMachines.put(machine.getId(), machine) ;
        return ;
    }
    public void makeMachineBusy(String id){
        freeMachines.remove(id) ;
    }
    public void makeMachineBusy(Machine machine){
        freeMachines.remove(machine.getId()) ;
    }


}
