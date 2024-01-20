package producer.producerConsumer.services.simulation;


// Caretaker class responsible for managing the Memento
class Caretaker {
    private GraphMemento memento;

    public GraphMemento getMemento() {
        return memento;
    }

    public void setMemento(GraphMemento memento) {
        this.memento = memento;
    }
}
