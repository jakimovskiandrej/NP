package Kolok2;

import java.util.stream.*;
import java.util.*;
import java.io.*;

class Payment implements Comparable<Payment> {

    String index;
    String itemName;
    double price;

    public Payment(String index, String itemName, double price) {
        this.index = index;
        this.itemName = itemName;
        this.price = price;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int compareTo(Payment o) {
        return Double.compare(this.price, o.price);
    }
}

class OnlinePayments {

    List<Payment> payments;

    public OnlinePayments() {
        payments = new ArrayList<>();
    }

    void readItems (InputStream is) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String index = parts[0];
                String itemName = parts[1];
                double price = Double.parseDouble(parts[2]);
                payments.add(new Payment(index, itemName, price));
            }
        }
    }

    void printStudentReport (String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        List<Payment> result = new ArrayList<>();
        int flag = 0;
        for (Payment payment : payments) {
            if(payment.getIndex().equals(index)) {
                result.add(payment);
            }
        }
        if(result.isEmpty()) {
            System.out.printf("Student %s not found!\n", index);
            flag = 1;
        }
        if(flag == 0) {
            double amount = result.stream().mapToDouble(Payment::getPrice).sum();
            double fee = Math.round(amount * 0.0114);
            fee = Math.max(3,Math.min(fee,300));
            double total = amount + fee;
            pw.println(String.format("Student: %s Net: %d Fee: %d Total: %d", index, (int)amount, (int)fee, (int)total));
            pw.println("Items");
            Collections.sort(result);
            int rank = 1;
            for (Payment payment : result) {
                pw.println(String.format("%d. %s %d", rank++, payment.getItemName(), (int)payment.getPrice()));
            }
            pw.flush();
        }
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();
        try {
            onlinePayments.readItems(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
