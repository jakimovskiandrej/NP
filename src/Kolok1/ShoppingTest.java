package Kolok1;

import java.util.*;
import java.io.*;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

enum Format {
    WS,PS
}

class Item {
    String productID;
    String productName;
    double productPrice;
    double quantity;

    public Item(String productID, String productName, double productPrice, double quantity) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", productID, calculatePrice());
    }

    public double calculatePrice() {
        return productPrice * quantity;
    }
}

class ShoppingCart {
    List<Item> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    void addItem(String itemData) throws InvalidOperationException {
        String[] parts = itemData.split(";");
        String type = parts[0];
        String id = parts[1];
        String productName = parts[2];
        double productPrice = Double.parseDouble(parts[3]);
        double quantity = Double.parseDouble(parts[4]);
        if(quantity <= 0) {
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.",id));
        }
        if(type.equals("WS")) {
            Item item = new Item(id, productName, productPrice, quantity);
            items.add(item);
        } else if(type.equals("PS")) {
            double pricePerKG = quantity / 1000;
            Item item = new Item(id, productName, productPrice, pricePerKG);
            items.add(item);
        }
    }

    void printShoppingCart(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        items.sort(Comparator.comparing(Item::calculatePrice).reversed());
        for(Item item : items) {
            pw.println(item);
        }
        pw.flush();
        pw.close();
    }

    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.isEmpty()) {
            throw new InvalidOperationException("There are no products with discount.");
        }
        PrintWriter pw = new PrintWriter(os);
        items.sort(Comparator.comparingDouble(Item::calculatePrice).reversed());
        for (Item item : items) {
            if(discountItems.contains(item.getProductID())) {
                double offerPrice = item.calculatePrice() * 0.9;
                pw.println(String.format("%s - %.2f", item.getProductID(), offerPrice));
            } else {
                pw.println(String.format("%s - %.2f", item.getProductID(), item.calculatePrice()));
            }
        }
        pw.flush();
        pw.close();
    }
}

public class ShoppingTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
