package recognizer;

import entity.Attribute;
import entity.Image;
import util.AttrUtil;
import util.LevelUtil;

import java.util.List;
import java.util.Scanner;

public class WithTeacherRecognizer extends Recognizer {
    public WithTeacherRecognizer(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void recognizeImage(Image unknown, List<Image> ethalons) {
        int firstPolinomial = 0;
        int secondPolinomial = 0;
        Attribute similarFirstAttribute;
        Attribute similarSecondAttribute;
        for (Attribute attribute : unknown.getAttributeList()) {
            similarFirstAttribute = AttrUtil.getSimilarAttribute(attribute, ethalons.get(0));
            similarSecondAttribute = AttrUtil.getSimilarAttribute(attribute, ethalons.get(1));
            if (similarFirstAttribute == null && similarSecondAttribute != null) {
                secondPolinomial++;
            } else if (similarFirstAttribute != null && similarSecondAttribute == null) {
                firstPolinomial++;
            } else if (similarFirstAttribute != null) {
                switch (attribute.getLevel().getClass().getSimpleName()) {
                    case "DefaultLevel":
                        boolean isFirstEquals = attribute.getLevel().equals(similarFirstAttribute.getLevel());
                        boolean isSecondEquals = attribute.getLevel().equals(similarSecondAttribute.getLevel());
                        if (isFirstEquals && !isSecondEquals) {
                            firstPolinomial++;
                        } else if (!isFirstEquals && isSecondEquals) {
                            secondPolinomial++;
                        } else {
                            if (isFirstEquals) {
                                firstPolinomial++;
                                secondPolinomial++;
                            }
                        }
                        break;
                    case "FirstLevel":
                        if (LevelUtil.compareAttributes(attribute.getLevel(), similarFirstAttribute.getLevel(), similarSecondAttribute.getLevel())) {
                            firstPolinomial++;
                        } else {
                            secondPolinomial++;
                        }
                        break;
                    case "FourthLevel":
                        boolean isInFirstRange = LevelUtil.numValueIsInRange(attribute.getLevel(), similarFirstAttribute.getLevel());
                        boolean isInSecondRange = LevelUtil.numValueIsInRange(attribute.getLevel(), similarFirstAttribute.getLevel());
                        if (isInFirstRange) {
                            firstPolinomial++;
                        }
                        if (isInSecondRange) {
                            secondPolinomial++;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        Scanner scan = new Scanner(System.in);
        String answer = "";
        if (firstPolinomial > secondPolinomial) {
            System.out.println("По моим расчётам, это " + ethalons.get(0).getName());
            System.out.println("Введите \"да\", если ответ верный и я проведу коррекцию. Если ответ неверный, введите \"нет\".");
            answer = scan.nextLine();
            if (answer.toLowerCase().equals("да")) {
                AttrUtil.correctImageAttributes(unknown, ethalons.get(0));
            } else {
                AttrUtil.correctImageAttributes(unknown, ethalons.get(1));
            }
        } else {
            System.out.println("По моим расчётам, это " + ethalons.get(1).getName());
            System.out.println("Введите \"да\", если ответ верный и я проведу коррекцию. Если ответ неверный, введите \"нет\".");
            answer = scan.nextLine();
            if (answer.toLowerCase().equals("да")) {
                AttrUtil.correctImageAttributes(unknown, ethalons.get(1));
            } else {
                AttrUtil.correctImageAttributes(unknown, ethalons.get(0));
            }
        }
    }
}
