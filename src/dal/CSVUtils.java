package dal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Util methods to read / write CSV files
 * parseLine copy/pasted with some heavy modifications from
 * https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 * https://agiletribe.wordpress.com/2012/11/23/the-only-class-you-need-for-csv-files/
 * replace try-catch-finally with Java 7 try-with-resources
 * Java 8 to read & write to files
 *
 * @author Dimz
 * @since 14/5/17.
 */
class CSVUtils {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    // singleton instance
    private static CSVUtils instance;

    //private constructor
    private CSVUtils() {}

    // lazy instance constructor
    static CSVUtils getInstance() {
        if (instance == null) {
            instance = new CSVUtils();
        }
        return instance;
    }


     List<String> readAndSearch(String filePath, String searchValue){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> CSVLine;
                CSVLine = parseLine(line);
                if (CSVLine.contains(searchValue)) return CSVLine;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    private List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    private List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    void writeLine(List<String> values, String filename) throws IOException {
        try (Writer w = new FileWriter(filename, true)) {
            boolean firstVal = true;
            for (String val : values) {
                if (!firstVal) {
                    w.write(DEFAULT_SEPARATOR);
                }
                w.write(DEFAULT_QUOTE);
                for (int i = 0; i < val.length(); i++) {
                    char ch = val.charAt(i);
                    if (ch == DEFAULT_QUOTE) {
                        w.write(DEFAULT_QUOTE);  //extra quote
                    }
                    w.write(ch);
                }
                w.write(DEFAULT_QUOTE);
                firstVal = false;
            }
            w.write("\r\n");
        }
    }

}
