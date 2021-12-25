package com.avispa.microf.numeral;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
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

            //sb.setLength(sb.length() - 1);
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

        int length = triplet.length();
        int begin = 3 - length;
        for(int j = begin;j < 3; j++) {
            int index = j - begin;
            int digit = Character.getNumericValue(triplet.charAt(index));
            if (digit == 0) {
                continue;
            }
            switch (j) {
                case 0:
                    sb.append(HUNDREDS[digit]).append(" ");
                    break;
                case 1:
                    if (digit == 1) {
                        j++;
                        index++;
                        digit = Character.getNumericValue(triplet.charAt(index));
                        sb.append(TEENS[digit]).append(" ");
                        appendPower(sb, tripletNumber, digit, true);
                    } else {
                        sb.append(TENS[digit]).append(" ");
                    }
                    break;
                case 2:
                    sb.append(UNITS[digit]).append(" ");
                    appendPower(sb, tripletNumber, digit, triplet.length() > 1);
                    break;
                default:
                    log.error("Unknown digit position");
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

    private static void appendPower(StringBuilder sb, int tripletNumber, int digit, boolean teens) {
        if (tripletNumber != 0) {
            if(teens) {
                sb.append(POWERS[tripletNumber][GENITIVE_PLURAL]);
                /*if (digit > 1 && digit < 5) {
                    sb.append(POWERS[tripletNumber][NOMINATIVE_PLURAL]);
                } else {
                    sb.append(POWERS[tripletNumber][GENITIVE_PLURAL]);
                }*/
            } else {
                if (digit == 1) {
                    sb.append(POWERS[tripletNumber][NOMINATIVE_SINGULAR]);
                } else if (digit > 3) {
                    sb.append(POWERS[tripletNumber][GENITIVE_PLURAL]);
                } else {
                    sb.append(POWERS[tripletNumber][NOMINATIVE_PLURAL]);
                }
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
        StringBuilder sb = new StringBuilder();
        int lastDigit;
        int power = 0;
        int j = 0;

        if (number == 0) {
            return "zero";
        }

        Deque<String> arrayDeque = new ArrayDeque<>();

        while (number > 0) {
            lastDigit = (number % 10);
            number /= 10;
            if(j == 0) {
                if ((number % 100 != 0 || lastDigit != 0) && power != 0) {
                    arrayDeque.add(" ");

                    if (number % 10 != 1) { // add only when we don't deal with teens
                        if (number != 0 || lastDigit > 3) {
                            arrayDeque.add(POWERS[power][GENITIVE_PLURAL]);
                        } else if (lastDigit == 1) {
                            arrayDeque.add(POWERS[power][NOMINATIVE_SINGULAR]);
                        } else {
                            arrayDeque.add(POWERS[power][NOMINATIVE_PLURAL]);
                        }
                    }
                }
                if (number % 10 != 1) {
                    if(lastDigit != 0) {
                        arrayDeque.add(" ");
                    }
                    arrayDeque.add(UNITS[lastDigit]);
                } else { // == 1
                    /*if (lastDigit > 1 && lastDigit < 5) {
                        arrayDeque.add(POWERS[power][NOMINATIVE_PLURAL]);
                    } else {
                        arrayDeque.add(POWERS[power][GENITIVE_PLURAL]);
                    }*/
                    arrayDeque.add(POWERS[power][GENITIVE_PLURAL]); // always add plural version of genitive
                    arrayDeque.add(" ");
                    arrayDeque.add(TEENS[lastDigit]);
                    number /= 10;
                    j += 2;
                    continue;
                }
            }
            if (j == 1) {
                if(lastDigit != 0) {
                    arrayDeque.add(" ");
                }
                arrayDeque.add(TENS[lastDigit]);
            }
            if (j == 2) {
                if(lastDigit != 0) {
                    arrayDeque.add(" ");
                }
                arrayDeque.add(HUNDREDS[lastDigit]);
                j = -1;
                power++;
            }
            j++;
        }

        for(Iterator dItr = arrayDeque.descendingIterator(); dItr.hasNext();) {
            sb.append(dItr.next());
        }

        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    public static String convert(BigDecimal number) {
        return convert(number.setScale(2, RoundingMode.HALF_UP).toPlainString(), '.');
    }
}
