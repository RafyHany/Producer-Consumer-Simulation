package producer.producerConsumer.services.models;

import java.util.ArrayList;

public class Link extends Element {
    private ArrayList<Double> points ;
    private String to ;   // id of element
    private String from ; // id of element

    public Link(){}
    public Link(ElementDto dto){
        super(dto);
        this.points = dto.points ;
        this.to = dto.to ;
        this.from = dto.from ;
    }
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public ArrayList<Double> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Double> points) {
        this.points = points;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
