import entity.Attribute;
import entity.Image;
import recognizer.SelfTeachingRecognizer;
import recognizer.StandardRecognizer;
import recognizer.WithTeacherRecognizer;
import util.AttrUtil;

import java.util.*;
import java.util.function.Consumer;

public class Application {
    private List<Image> ethalons;
    private Map<Integer, Consumer<Scanner>> menuFunctions;
    private StandardRecognizer standardRecognizer;
    private SelfTeachingRecognizer selfTeachingRecognizer;
    private WithTeacherRecognizer withTeacherRecognizer;
    private Scanner scan;

    public Application() {
        scan = new Scanner(System.in);
        standardRecognizer = new StandardRecognizer(scan);
        selfTeachingRecognizer = new SelfTeachingRecognizer(scan);
        withTeacherRecognizer = new WithTeacherRecognizer(scan);
        ethalons = new ArrayList<>();
        menuFunctions = new HashMap<>();
        menuFunctions.put(1, scan -> writeObjects(scan));
        menuFunctions.put(2, scan -> withTeacherRecognizer.enterUnknownImage(scan, ethalons));
        menuFunctions.put(3, scan -> standardRecognizer.enterUnknownImage(scan, ethalons));
        menuFunctions.put(4, scan -> selfTeachingRecognizer.enterUnknownImage(scan, ethalons));
        menuFunctions.put(5, scan -> showEthalons());
        initDefaultEthalons();
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.menu();
    }

    private void menu() {
        int answer;
        for (; ; ) {
            System.out.println("1 - Запись априорной информации\n" +
                    "2 - Режим обучения с учителем\n" +
                    "3 - Режим распознавания\n" +
                    "4 - Режим распознавания с самообучением\n" +
                    "5 - Показать эталонные образы");
            while (!scan.hasNextLine()){}
            answer = Integer.parseInt(scan.nextLine());
            Consumer executeFunction = menuFunctions.get(answer);
            if (executeFunction == null) {
                System.out.println("Неправильный вариант!");
                continue;
            }
            executeFunction.accept(scan);
        }
    }

    private void writeObjects(Scanner scan) {
        String currentName;
        List<Attribute> attributes;
        String enteredAttribute;
        for (int i = 0; i < 2; i++) {
            System.out.println("Введите имя для " + (i + 1) + "-го объекта");
            currentName = scan.nextLine();
            attributes = new ArrayList<>();
            System.out.println("Далее необходимо ввести аттрибуты.\n" +
                    "Вводить их следует следующим образом: i='номер индекса',значение.\n" +
                    "Чтобы остановить ввод аттрибутов, необходимо нажать Enter в пустой строке.");
            int attrIndex = 1;
            do {
                System.out.print("Введите " + attrIndex + "-й аттрибут: ");
                enteredAttribute = scan.nextLine();
                Attribute currentAttribute = AttrUtil.getAttribute(enteredAttribute);
                if (currentAttribute == null) {
                    continue;
                }
                attributes.add(currentAttribute);
                attrIndex++;
            } while (!enteredAttribute.isEmpty());
            ethalons.add(new Image(currentName, attributes));
        }
    }

    private void showEthalons() {
        for (Image image : ethalons) {
            System.out.println(image.getName() + "\n----------------");
            for (Attribute attribute : image.getAttributeList()) {
                System.out.println(attribute.getLevel().getClass().getSimpleName());
                System.out.println(attribute.getLevel().getName());
                System.out.println("--------------------");
            }
            System.out.println("\n\n");
        }
    }

    private void initDefaultEthalons() {
        Attribute firstAttribute = AttrUtil.getAttribute("i=1, ram 4gb");
        Attribute secondAttribute = AttrUtil.getAttribute("i=4, price 400$");
        ethalons.add(new Image("PC1", Arrays.asList(firstAttribute, secondAttribute)));
        firstAttribute = AttrUtil.getAttribute("i=1, ram 64gb");
        secondAttribute = AttrUtil.getAttribute("i=4, price 5000$");
        ethalons.add(new Image("PC2", Arrays.asList(firstAttribute, secondAttribute)));
    }
}
