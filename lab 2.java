interface IPaymentProcessor {
    void processPayment(double amount);
    void refundPayment(double amount);
}

class InternalPaymentProcessor implements IPaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of " + amount + " via internal system.");
    }

    @Override
    public void refundPayment(double amount) {
        System.out.println("Refunding payment of " + amount + " via internal system.");
    }
}

class ExternalPaymentSystemA {
    public void makePayment(double amount) {
        System.out.println("Making payment of " + amount + " via External Payment System A.");
    }

    public void makeRefund(double amount) {
        System.out.println("Making refund of " + amount + " via External Payment System A.");
    }
}

class ExternalPaymentSystemB {
    public void sendPayment(double amount) {
        System.out.println("Sending payment of " + amount + " via External Payment System B.");
    }

    public void processRefund(double amount) {
        System.out.println("Processing refund of " + amount + " via External Payment System B.");
    }
}

class PaymentAdapterA implements IPaymentProcessor {
    private ExternalPaymentSystemA externalSystemA;

    public PaymentAdapterA(ExternalPaymentSystemA externalSystemA) {
        this.externalSystemA = externalSystemA;
    }

    @Override
    public void processPayment(double amount) {
        externalSystemA.makePayment(amount);
    }

    @Override
    public void refundPayment(double amount) {
        externalSystemA.makeRefund(amount);
    }
}

class PaymentAdapterB implements IPaymentProcessor {
    private ExternalPaymentSystemB externalSystemB;

    public PaymentAdapterB(ExternalPaymentSystemB externalSystemB) {
        this.externalSystemB = externalSystemB;
    }

    @Override
    public void processPayment(double amount) {
        externalSystemB.sendPayment(amount);
    }

    @Override
    public void refundPayment(double amount) {
        externalSystemB.processRefund(amount);
    }
}

class PaymentProcessorFactory {
    public static IPaymentProcessor getPaymentProcessor(String region) {
        switch (region) {
            case "US":
                return new InternalPaymentProcessor();
            case "EU":
                return new PaymentAdapterA(new ExternalPaymentSystemA());
            case "ASIA":
                return new PaymentAdapterB(new ExternalPaymentSystemB());
            default:
                throw new IllegalArgumentException("Invalid region specified.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        IPaymentProcessor processorUS = PaymentProcessorFactory.getPaymentProcessor("US");
        processorUS.processPayment(100.0);
        processorUS.refundPayment(50.0);

        IPaymentProcessor processorEU = PaymentProcessorFactory.getPaymentProcessor("EU");
        processorEU.processPayment(200.0);
        processorEU.refundPayment(100.0);

        IPaymentProcessor processorAsia = PaymentProcessorFactory.getPaymentProcessor("ASIA");
        processorAsia.processPayment(300.0);
        processorAsia.refundPayment(150.0);
    }
}
