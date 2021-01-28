package recognizer;

import entity.Attribute;
import entity.Image;
import util.AttrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Recognizer {
    private Scanner scanner;

    public Recognizer(Scanner scanner) {
        this.scanner = scanner;
    }

    abstract void recognizeImage(Image unknown, List<Image> ethalons);

    public void enterUnknownImage(Scanner scan, List<Image> ethalons) {
        System.out.println("Далее необходимо ввести аттрибуты.\n" +
                "Вводить их следует следующим образом: i='номер индекса',значение.\n" +
                "Чтобы остановить ввод аттрибутов, необходимо нажать Enter в пустой строке.");
        int attrIndex = 1;
        String enteredAttribute;
        List<Attribute> attributes = new ArrayList<>();
        Image unknownImage;
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
        unknownImage = new Image(attributes);
        recognizeImage(unknownImage, ethalons);
    }
}
