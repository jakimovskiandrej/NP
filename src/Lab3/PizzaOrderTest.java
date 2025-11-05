package Lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Item {
    int getPrice();
    String getType();
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException(String message) {
        super(message);
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException(String message) {
        super(message);
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super((Throwable) item);
    }
}

class EmptyOrderException extends Exception {
    public EmptyOrderException(String message) {
        super(message);
    }
}

class ArrayIndexOutOfBоundsException extends Exception {
    int a;
    public ArrayIndexOutOfBоundsException(int idx) {
        a = idx;
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException(String message) {
        super(message);
    }
}

class ExtraItem implements Item {

    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(type.equals("Ketchup") || type.equals("Coke")) {
            this.type = type;
        } else {
            throw new InvalidExtraTypeException("InvalidExtraTypeException");
        }
    }

    @Override
    public int getPrice() {
        if(type.equals("Ketchup")) {
            return 3;
        }
        if (type.equals("Coke")) {
            return 5;
        }
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }
}

class PizzaItem implements Item {

    private String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian")) {
            this.type = type;
        } else {
            throw new InvalidPizzaTypeException("InvalidPizzaTypeException");
        }
    }
    //{ Standard , Pepperoni , Vegetarian }
    @Override
    public int getPrice() {
        if(type.equals("Standard")) {
            return 10;
        }
        if(type.equals("Pepperoni")) {
            return 12;
        }
        if(type.equals("Vegetarian")) {
            return 8;
        }
        return 0;
    }

    @Override
    public String getType() {
        return type;
    }
}

class Product {
    private final Item item;
    private int count;

    public Product(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class Order {

    private List<Product> products;
    private boolean locked;

    public Order() {
        products = new ArrayList<>();
    }
    //addItem(Item item, int count)
    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(count > 10) {
            throw new ItemOutOfStockException(item);
        }
        if(locked) {
            throw new OrderLockedException("OrderLockedException");
        }
        for(Product p : products) {
            if(p.getItem().equals(item)) {
                p.setCount(count);
                return;
            }
        }
        products.add(new Product(item, count));
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        products.forEach(p -> sb.append(String.format("%3d.%-15sx%2d%5d$%n", products.indexOf(p)+1, p.getItem().getType(),p.getCount(), p.getItem().getPrice() * p.getCount())));
        sb.append(String.format("%-22s%5d$", "Total:", getPrice()));
        System.out.println(sb.toString());
    }
    //removeItem(int idx) - се отстранува нарачката со даден индекс (сите нарачки со поголеми индекси се поместуваат во лево). Доколку не постои нарачка со таков индекс треба да се фрли исклучок ArrayIndexOutOfBоundsException(idx)
    public void removeItem(int idx) throws ArrayIndexOutOfBоundsException, OrderLockedException {
        if(idx > products.size()) {
            throw new ArrayIndexOutOfBоundsException(idx);
        }
        if(locked) {
            throw new OrderLockedException("OrderLockedException");
        }
        products.remove(idx);
    }
    //ја заклучува нарачката. За да може нарачката да се заклучи треба истата да има барем една ставка, во спротивно фрлете исклучок EmptyOrderException.
    public void lock() throws EmptyOrderException {
        if(products.isEmpty()) {
            throw new EmptyOrderException("EmptyOrderException");
        }
        locked = true;
    }

    public int getPrice() {
        return products.stream().mapToInt(p -> p.getItem().getPrice() * p.getCount()).sum();
    }
}

public class PizzaOrderTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}
