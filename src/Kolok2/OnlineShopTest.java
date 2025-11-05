package Kolok2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class Product {
    //String category, String id, String name, LocalDateTime createdAt, double price
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double quantitySold;
    double price;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        quantitySold = 0;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getQuantitySold() {
        return quantitySold;
    }

    public void addQuantitySold(double quantitySold) {
        quantitySold += quantitySold;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "category='" + category + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", quantitySold=" + quantitySold +
                '}';
    }
}

class OnlineShop {

    Map<String,Product> products;

    public OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product product = new Product(category, id, name, createdAt, price);
        products.putIfAbsent(id, product);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        double total = product.getPrice() * quantity;
        product.addQuantitySold(quantity);
        return total;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> filteredProducts = products.values().stream().filter(c->c.getCategory().equals(category) || category == null).collect(Collectors.toList());
        if(filteredProducts.isEmpty()) {
            return Collections.emptyList();
        }
        Comparator<Product> comparator = Comparator.comparing(Product::getCreatedAt);
        if(comparatorType == COMPARATOR_TYPE.NEWEST_FIRST) {
            comparator = Comparator.comparing(Product::getCreatedAt).reversed();
        } else if(comparatorType == COMPARATOR_TYPE.OLDEST_FIRST) {
            comparator = Comparator.comparing(Product::getCreatedAt);
        } else if(comparatorType == COMPARATOR_TYPE.LOWEST_PRICE_FIRST) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if(comparatorType == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) {
            comparator = Comparator.comparing(Product::getPrice).reversed();
        } else if(comparatorType == COMPARATOR_TYPE.MOST_SOLD_FIRST) {
            comparator = Comparator.comparing(Product::getQuantitySold).reversed();
        } else if(comparatorType == COMPARATOR_TYPE.LEAST_SOLD_FIRST) {
            comparator = Comparator.comparing(Product::getQuantitySold);
        }
        filteredProducts.sort(comparator);
        List<List<Product>> pages = new ArrayList<>();
        for(int i=0;i<filteredProducts.size();i++) {
            pages.add(filteredProducts.subList(i, Math.min(i+pageSize,filteredProducts.size())));
        }
        return pages;
    }
}

public class OnlineShopTest {
    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);
    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}
