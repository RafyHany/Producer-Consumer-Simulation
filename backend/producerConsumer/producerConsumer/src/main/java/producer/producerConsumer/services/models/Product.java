package producer.producerConsumer.services.models;

public class Product {
    private String color;
    private static String[] colorss = {"#F7B5B5","#98F86C","#ECE800","#FFBEE9","#ABE2F0","#ffba00", "#69EBBD", "#B5B0DF", "#709CD1" ,"#F7BF77"};
    private static int index = 0;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Product() {
        int min = 0;
        int max = colorss.length-1;
        int randomInd = (index++) % 10;
        this.color = colorss[randomInd];
    }
}
