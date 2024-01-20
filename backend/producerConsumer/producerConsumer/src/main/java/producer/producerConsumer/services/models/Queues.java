package producer.producerConsumer.services.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queues extends Element {
    private Map<String, Machine> outMachines = new HashMap<>();
    private Map<String, Machine> freeMachines = new HashMap<>();
    private List<Product> products = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private Thread thread;


    public Queues(){}
    public Queues(ElementDto dto) {
        super(dto);
    }
    public void addToOutMachines(Machine machine){
        outMachines.put(machine.getId(), machine);
    }
    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<String> getIds() {
        return ids;
    }
    public void addToId(String id){
        ids.add(id) ;
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
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
// we have some bug here
//    public Map<String, Machine> getOutMachines() {
//        return outMachines;
//    }
//
//    public Map<String, Machine> getFreeMachines() {
//        return freeMachines;
//    }
//
//    public void setFreeMachines(Map<String, Machine> freeMachines) {
//        this.freeMachines = freeMachines;
//    }
//
//    public void setOutMachines(Map<String, Machine> outMachines) {
//        this.outMachines = outMachines;
//    }

    public void addToProducts(Product product){
        getProducts().add(product);
        //make it amen
//        if(getProducts().size()==1){
//            this.thread = new Thread(this::run);
//            thread.start();
//        }

    }
}
