interface Beverage {
    String getDescription();
    double cost();
}

class Espresso implements Beverage {
    public String getDescription() {
        return "Эспрессо";
    }

    public double cost() {
        return 1.50;
    }
}

class Tea implements Beverage {
    public String getDescription() {
        return "Чай";
    }

    public double cost() {
        return 1.00;
    }
}

class Latte implements Beverage {
    public String getDescription() {
        return "Латте";
    }

    public double cost() {
        return 2.00;
    }
}

abstract class BeverageDecorator implements Beverage {
    protected Beverage beverage;

    public BeverageDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription();
    }

    public double cost() {
        return beverage.cost();
    }
}

class Milk extends BeverageDecorator {
    public Milk(Beverage beverage) {
        super(beverage);
    }

    public String getDescription() {
        return beverage.getDescription() + ", Молоко";
    }

    public double cost() {
        return beverage.cost() + 0.30;
    }
}

class Sugar extends BeverageDecorator {
    public Sugar(Beverage beverage) {
        super(beverage);
    }

    public String getDescription() {
        return beverage.getDescription() + ", Сахар";
    }

    public double cost() {
        return beverage.cost() + 0.10;
    }
}

class WhippedCream extends BeverageDecorator {
    public WhippedCream(Beverage beverage) {
        super(beverage);
    }

    public String getDescription() {
        return beverage.getDescription() + ", Взбитые сливки";
    }

    public double cost() {
        return beverage.cost() + 0.50;
    }
}

interface IPaymentProcessor {
    void processPayment(double amount);
}

class PayPalPaymentProcessor implements IPaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Оплата через PayPal на сумму: $" + amount);
    }
}

class StripePaymentService {
    public void makeTransaction(double totalAmount) {
        System.out.println("Оплата через Stripe на сумму: $" + totalAmount);
    }
}

class StripePaymentAdapter implements IPaymentProcessor {
    private StripePaymentService stripeService;

    public StripePaymentAdapter(StripePaymentService stripeService) {
        this.stripeService = stripeService;
    }

    public void processPayment(double amount) {
        stripeService.makeTransaction(amount);
    }
}

class AnotherPaymentService {
    public void executePayment(double value) {
        System.out.println("Оплата через AnotherService на сумму: $" + value);
    }
}

class AnotherPaymentAdapter implements IPaymentProcessor {
    private AnotherPaymentService anotherService;

    public AnotherPaymentAdapter(AnotherPaymentService anotherService) {
        this.anotherService = anotherService;
    }

    public void processPayment(double amount) {
        anotherService.executePayment(amount);
    }
}

public class CafeOrderAndPaymentSystem {
    public static void main(String[] args) {
        Beverage order = new Latte();
        order = new Milk(order);
        order = new Sugar(order);
        order = new WhippedCream(order);

        System.out.println("Заказ: " + order.getDescription());
        System.out.println("Стоимость: $" + order.cost());

        IPaymentProcessor paypalProcessor = new PayPalPaymentProcessor();
        paypalProcessor.processPayment(order.cost());

        IPaymentProcessor stripeProcessor = new StripePaymentAdapter(new StripePaymentService());
        stripeProcessor.processPayment(order.cost());

        IPaymentProcessor anotherProcessor = new AnotherPaymentAdapter(new AnotherPaymentService());
        anotherProcessor.processPayment(order.cost());
    }
}
