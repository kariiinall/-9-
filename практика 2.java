interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
}

class InternalDeliveryService implements IInternalDeliveryService {
    public void deliverOrder(String orderId) {
        System.out.println("Внутренняя доставка заказа " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return "Статус заказа " + orderId + ": Доставляется";
    }
}

class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Служба A отправила товар " + itemId);
    }

    public String trackShipment(int shipmentId) {
        return "Служба A: Статус отправки для " + shipmentId;
    }
}

class ExternalLogisticsServiceB {
    public void sendPackage(String packageInfo) {
        System.out.println("Служба B отправила посылку " + packageInfo);
    }

    public String checkPackageStatus(String trackingCode) {
        return "Служба B: Статус для " + trackingCode;
    }
}

class ExternalLogisticsServiceC {
    public void startDelivery(String productCode) {
        System.out.println("Служба C начала доставку " + productCode);
    }

    public String deliveryStatus(String productCode) {
        return "Служба C: Статус для " + productCode;
    }
}

class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA serviceA;

    public LogisticsAdapterA(ExternalLogisticsServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public void deliverOrder(String orderId) {
        int itemId = Integer.parseInt(orderId);
        serviceA.shipItem(itemId);
    }

    public String getDeliveryStatus(String orderId) {
        int shipmentId = Integer.parseInt(orderId);
        return serviceA.trackShipment(shipmentId);
    }
}

class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB serviceB;

    public LogisticsAdapterB(ExternalLogisticsServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public void deliverOrder(String orderId) {
        serviceB.sendPackage(orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return serviceB.checkPackageStatus(orderId);
    }
}

class LogisticsAdapterC implements IInternalDeliveryService {
    private ExternalLogisticsServiceC serviceC;

    public LogisticsAdapterC(ExternalLogisticsServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public void deliverOrder(String orderId) {
        serviceC.startDelivery(orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return serviceC.deliveryStatus(orderId);
    }
}

class DeliveryServiceFactory {
    public static IInternalDeliveryService getDeliveryService(String serviceType) {
        switch (serviceType) {
            case "Internal":
                return new InternalDeliveryService();
            case "ExternalA":
                return new LogisticsAdapterA(new ExternalLogisticsServiceA());
            case "ExternalB":
                return new LogisticsAdapterB(new ExternalLogisticsServiceB());
            case "ExternalC":
                return new LogisticsAdapterC(new ExternalLogisticsServiceC());
            default:
                throw new IllegalArgumentException("Неизвестный тип службы доставки: " + serviceType);
        }
    }
}

public class LogisticsSystem {
    public static void main(String[] args) {
        IInternalDeliveryService deliveryService = DeliveryServiceFactory.getDeliveryService("ExternalA");
        deliveryService.deliverOrder("101");
        System.out.println(deliveryService.getDeliveryStatus("101"));

        deliveryService = DeliveryServiceFactory.getDeliveryService("ExternalB");
        deliveryService.deliverOrder("Pack-202");
        System.out.println(deliveryService.getDeliveryStatus("Pack-202"));

        deliveryService = DeliveryServiceFactory.getDeliveryService("ExternalC");
        deliveryService.deliverOrder("Prod-303");
        System.out.println(deliveryService.getDeliveryStatus("Prod-303"));
    }
}
