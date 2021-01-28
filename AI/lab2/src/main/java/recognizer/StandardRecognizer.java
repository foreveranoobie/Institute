package recognizer;

import entity.Attribute;
import entity.Image;
import util.AttrUtil;
import util.LevelUtil;

import java.util.List;
import java.util.Scanner;

public class StandardRecognizer extends Recognizer {
    public StandardRecognizer(Scanner scanner) {
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
        if (firstPolinomial > secondPolinomial) {
            System.out.println("По моим расчётам, это " + ethalons.get(0).getName());
        } else {
            System.out.println("По моим расчётам, это " + ethalons.get(1).getName());
        }
    }
}
