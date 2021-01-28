package util;

import entity.Attribute;
import entity.Image;
import levels.FirstLevel;
import levels.FourthLevel;
import levels.Level;

public class AttrUtil {
    public static Attribute getAttribute(String attribute) {
        String[] attributeParts = attribute.split(",");
        Attribute imageAttribute = null;
        if (attributeParts.length == 2) {
            try {
                int level = Integer.parseInt(attributeParts[0].replaceAll("[^\\d]", ""));
                if (level == 1 || level == 4) {
                    if (attributeParts[1].replaceAll("[^\\d]", "").isEmpty()) {
                        throw new NumberFormatException();
                    }
                }
                Level attrLevel = LevelUtil.getLevel(attributeParts);
                imageAttribute = new Attribute(attrLevel);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return imageAttribute;
    }

    public static Attribute getSimilarAttribute(Attribute search, Image searchIn) {
        for (Attribute ethalonAttribute : searchIn.getAttributeList()) {
            if (ethalonAttribute.getLevel().getClass().getCanonicalName().equals(search.getLevel().getClass().getCanonicalName())) {
                if (ethalonAttribute.getLevel().equals(search.getLevel())) {
                    return ethalonAttribute;
                }
            }
        }
        return null;
    }

    public static void correctImageAttributes(Image unknown, Image ethalon) {
        Attribute similarFirstAttribute;
        for (Attribute attribute : unknown.getAttributeList()) {
            similarFirstAttribute = getSimilarAttribute(attribute, ethalon);
            if (similarFirstAttribute != null) {
                switch (attribute.getLevel().getClass().getSimpleName()) {
                    case "FirstLevel":
                        ((FirstLevel) similarFirstAttribute.getLevel()).correctStat((FirstLevel) attribute.getLevel());
                        ((FirstLevel) similarFirstAttribute.getLevel()).incrementN();
                        break;
                    case "FourthLevel":
                        ((FourthLevel) similarFirstAttribute.getLevel()).correctRange((FourthLevel) attribute.getLevel());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
