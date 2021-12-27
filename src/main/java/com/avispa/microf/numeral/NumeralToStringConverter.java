package com.avispa.microf.numeral;

import joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
public final class NumeralToStringConverter {
    private static final Logger log = LoggerFactory.getLogger(NumeralToStringConverter.class);

    private static final String ZERO_TRIPLET = "000";
    private static final String ZERO = "0";

    private static final int NOMINATIVE_SINGULAR = 0;
    private static final int NOMINATIVE_PLURAL = 1;
    private static final int GENITIVE_PLURAL = 2;

    private static final String[] UNITS = new String[] {
            "", "jeden", "dwa", "trzy", "cztery", "pięć", "sześć", "siedem", "osiem", "dziewięć"
    };
    private static final String[] TEENS = new String[] {
            "dziesięć", "jedenaście", "dwanaście", "trzynaście", "czternaście", "piętnaście", "szesnaście", "siedemnaście", "osiemnaście", "dziewiętnaście"
    };
    private static final String[] TENS = new String[] {
            "", "dziesięć", "dwadzieścia", "trzydzieści", "czterdzieści", "pięćdziesiąt", "sześćdziesiąt", "siedemdziesiąt", "osiemdziesiąt", "dziewięćdziesiąt"
    };
    private static final String[] HUNDREDS = new String[] {
            "", "sto", "dwieście", "trzysta", "czterysta", "pięćset", "sześćset", "siedemset", "osiemset", "dziewięćset"
    };
    private static final String[][] POWERS = new String[][]{
            {
                "", "", ""
            },
            {
                "tysiąc", "tysiące", "tysięcy"
            },
            {
                "milion", "miliony", "milionów"
            },
            {
                "miliard", "miliardy", "miliardów"
            },
            {
                "bilion", "biliony", "bilionów"
            }
    };
    private static final String[] CURRENCY = new String[]{
            "złoty", "złote", "złotych"
    };

    private NumeralToStringConverter() {

    }

    /**
     * Convert number provided as string to words form
     * @param number
     * @return
     */
    public static String convert(String number, char decimalSeparator) {
        int decimalPoint = number.indexOf(decimalSeparator);
        String integer;
        String decimal;
        if(decimalPoint != -1) {
            integer = number.substring(0, decimalPoint);
            decimal = number.substring(decimalPoint + 1);
        } else {
            integer = number;
            decimal = "";
        }

        if(integer.length() > 9) {
            throw new IllegalArgumentException("Only numbers up to 9 digits are supported");
        }

        List<String> triplets = getTriplets(integer);
        StringBuilder sb = new StringBuilder();

        if(integer.equals(ZERO)) {
            sb.append("zero");
            sb.append(" ");
            sb.append(CURRENCY[GENITIVE_PLURAL]);
        } else {
            int size = triplets.size();
            for(int i = 0; i < size; i++) {
                String triplet = triplets.get(i);
                convertTriplet(sb, triplet, size - i - 1);
            }

            appendCurrency(sb, integer);
        }

        if(!StringUtils.isEmpty(decimal)) {
            sb.append(" ").append(decimal).append("/100");
        }

        return sb.toString();
    }

    /**
     * Split integer part to triplets containing max 3 digits
     */
    private static List<String> getTriplets(String integer) {
        List<String> triplets = new ArrayList<>();
        for(int i = integer.length() - 1; i >=0; i-=3) {
            int begin = Math.max(0, i - 2);
            triplets.add(integer.substring(begin, i + 1));
        }
        Collections.reverse(triplets);
        return triplets;
    }

    private static void convertTriplet(StringBuilder sb, String triplet, int tripletNumber) {
        // skip triplets containing all zeros
        if(triplet.equals(ZERO_TRIPLET)) {
            return;
        }

        boolean hasTeens = triplet.length() > 1 && Character.getNumericValue(triplet.charAt(triplet.length() - 2)) == 1;

        int length = triplet.length();
        int begin = 3 - length;
        for(int j = begin; j < 3; j++) {
            int index = j - begin;
            int digit = Character.getNumericValue(triplet.charAt(index));

            if(digit != 0) {
                switch (j) {
                    case 0:
                        sb.append(HUNDREDS[digit]).append(" ");
                        break;
                    case 1:
                        if (!hasTeens) {
                            sb.append(TENS[digit]).append(" ");
                        }
                        break;
                    case 2:
                        if (hasTeens) {
                            sb.append(TEENS[digit]);
                        } else {
                            sb.append(UNITS[digit]);
                        }
                        sb.append(" ");
                        break;
                    default:
                        log.error("Unknown digit position");
                }
            }

            if(j == 2) {
                appendPower(sb, tripletNumber, length, digit, hasTeens);
            }
        }
    }

    private static void appendCurrency(StringBuilder sb, String number) {
        int lastDigit = Character.getNumericValue(number.charAt(number.length() - 1));
        boolean isTeen = number.length() > 1 && Character.getNumericValue(number.charAt(number.length() - 2)) == 1;

        if(number.length() == 1) {
            if(lastDigit == 1) {
                sb.append(CURRENCY[NOMINATIVE_SINGULAR]);
            } else if (lastDigit > 4 || lastDigit == 0) {
                sb.append(CURRENCY[GENITIVE_PLURAL]);
            } else {
                sb.append(CURRENCY[NOMINATIVE_PLURAL]);
            }
        } else if(isTeen) {
            sb.append(CURRENCY[GENITIVE_PLURAL]);
        } else if (lastDigit > 1 && lastDigit < 5) {
            sb.append(CURRENCY[NOMINATIVE_PLURAL]);
        } else {
            sb.append(CURRENCY[GENITIVE_PLURAL]);
        }
    }

    private static void appendPower(StringBuilder sb, int tripletNumber, int tripletLength, int digit, boolean teens) {
        if (tripletNumber != 0) {
            if (digit > 3 || digit == 0 || teens) {
                sb.append(POWERS[tripletNumber][GENITIVE_PLURAL]);
            } else if (digit == 1) {
                if(tripletLength == 1) {
                    sb.append(POWERS[tripletNumber][NOMINATIVE_SINGULAR]);
                } else {
                    sb.append(POWERS[tripletNumber][GENITIVE_PLURAL]);
                }
            } else {
                sb.append(POWERS[tripletNumber][NOMINATIVE_PLURAL]);
            }
            sb.append(" ");
        }
    }

    /**
     * Does not support fractional parts
     * @param number
     * @return
     */
    public static String convert(int number) {
        int lastDigit;
        int power = 0;
        int j = 0;

        if (number == 0) {
            return "zero";
        }

        List<String> result = new ArrayList<>();

        while (number > 0) {
            lastDigit = (number % 10);
            number /= 10;
            if(j == 0) {
                if (number % 100 != 0 || lastDigit != 0) {
                    addPower(number, lastDigit, power, result);
                }
                if (number % 10 != 1) {
                    result.add(UNITS[lastDigit]);
                } else { // == 1
                    result.add(TEENS[lastDigit]);
                    number /= 10;
                    j += 2;
                    continue;
                }
            }
            if (j == 1) {
                result.add(TENS[lastDigit]);
            }
            if (j == 2) {
                result.add(HUNDREDS[lastDigit]);
                j = -1;
                power++;
            }
            j++;
        }

        result.removeIf(String::isEmpty);
        Collections.reverse(result); // results must be reversed

        return Strings.join(result, " ");
    }

    private static void addPower(int number, int lastDigit, int power, List<String> result) {
        if (lastDigit == 0 || lastDigit > 3 || number % 10 == 1) {
            result.add(POWERS[power][GENITIVE_PLURAL]);
        } else if (lastDigit == 1) {
            if(number == 0) { // there is no next digit
                result.add(POWERS[power][NOMINATIVE_SINGULAR]);
            } else {
                result.add(POWERS[power][GENITIVE_PLURAL]);
            }
        } else {
            result.add(POWERS[power][NOMINATIVE_PLURAL]);
        }
    }

    public static String convert(BigDecimal number) {
        return convert(number.setScale(2, RoundingMode.HALF_UP).toPlainString(), '.');
    }
}
