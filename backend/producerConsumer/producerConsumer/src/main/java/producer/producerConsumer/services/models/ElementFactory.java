package producer.producerConsumer.services.models;

public class ElementFactory {
    public static Element getObject(ElementDto dto){
        switch (dto.name){
            case "circle":
                int min = 3;
                int max = 8;
                int randomTime = (int)Math.floor(Math.random() *(max - min + 1) + min);
                return new Machine(dto,randomTime);
            case "queue":
                return new Queues(dto);
            case "line":
                return new Link(dto);
            default: return null;
        }
    }
}
