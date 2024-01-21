package producer.producerConsumer.services.models;
import java.util.ArrayList;
import java.util.List;

public class Machine extends Element implements Runnable{
    private int operatingTime;
    private List<Queues> inQueues = new ArrayList<>();
    private Queues outQueue;
    private Product product;

    public Machine(){}
    public Machine(ElementDto dto, int operatingTime) {
        super(dto);
        this.operatingTime = operatingTime;
    }
    public int getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(int operatingTime) {
        this.operatingTime = operatingTime;
    }

    public List<Queues> getInQueues() {
        return inQueues;
    }

    public void setInQueues(List<Queues> inQueues) {
        this.inQueues = inQueues;
    }

    public Queues getOutQueue() {
        return outQueue;
    }

    public void setOutQueue(Queues outQueue) {
        this.outQueue = outQueue;
    }

    public Product getProduct() {
        return product;
    }


    public void addToInQueues(Queues queue){
        inQueues.add(queue);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(operatingTime * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        outQueue.addToProducts(product);
        product = null;
        machineNotifyFree();
    }

        public void machineNotifyFree(){
        this.setColor("#ddd");
        for(Queues q: inQueues) {
            q.makeMachineFree(this.getId());
        }
    }

    public synchronized void setProduct(Product product) {
        this.product = product;
        machineNotifyBusy();
        Thread thread = new Thread(this::run);
        thread.start();
        this.setColor(product.getColor());
    }

    public void machineNotifyBusy(){
        for(Queues q: inQueues) {
            q.makeMachineBusy(this.getId());
        }
    }

}
