import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

interface IReport {
    String generate();
}

class SalesReport implements IReport {
    private List<String> data;

    public SalesReport() {
        data = new ArrayList<>();
        data.add("Продажа: ID=1, Сумма=100, Дата=2023-10-01");
        data.add("Продажа: ID=2, Сумма=150, Дата=2023-10-05");
        data.add("Продажа: ID=3, Сумма=200, Дата=2023-10-10");
    }

    public String generate() {
        return "Отчет по продажам:\n" + String.join("\n", data);
    }
}

class UserReport implements IReport {
    private List<String> data;

    public UserReport() {
        data = new ArrayList<>();
        data.add("Пользователь: ID=1, Имя=Мая, Дата регистрации=2023-08-01");
        data.add("Пользователь: ID=2, Имя=Анна, Дата регистрации=2023-09-05");
        data.add("Пользователь: ID=3, Имя=Самат, Дата регистрации=2023-10-15");
    }

    public String generate() {
        return "Отчет по пользователям:\n" + String.join("\n", data);
    }
}

abstract class ReportDecorator implements IReport {
    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    public String generate() {
        return report.generate();
    }
}

class DateFilterDecorator extends ReportDecorator {
    private Date startDate;
    private Date endDate;

    public DateFilterDecorator(IReport report, Date startDate, Date endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String generate() {
        String result = super.generate();
        return result + "\nОтчет с фильтрацией по дате с " + startDate + " по " + endDate;
    }
}

class SortingDecorator extends ReportDecorator {
    private Comparator<String> comparator;

    public SortingDecorator(IReport report, Comparator<String> comparator) {
        super(report);
        this.comparator = comparator;
    }

    public String generate() {
        String result = super.generate();
        List<String> sortedData = result.lines().sorted(comparator).collect(Collectors.toList());
        return "Отчет с сортировкой:\n" + String.join("\n", sortedData);
    }
}

class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        String result = super.generate();
        return "Отчет в формате CSV:\n" + result.replaceAll("\n", ",") + "\n";
    }
}

class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        String result = super.generate();
        return "Отчет в формате PDF:\n" + result + "\n";
    }
}

public class ReportSystem {
    public static void main(String[] args) {
        IReport salesReport = new SalesReport();

        salesReport = new DateFilterDecorator(salesReport, new Date(1234567890L), new Date());
        salesReport = new SortingDecorator(salesReport, Comparator.naturalOrder());
        salesReport = new CsvExportDecorator(salesReport);

        System.out.println(salesReport.generate());

        IReport userReport = new UserReport();
        userReport = new PdfExportDecorator(userReport);
        System.out.println(userReport.generate());
    }
}
