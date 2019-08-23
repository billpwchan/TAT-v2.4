/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.JSchException;
import controller.util.CommonFunctions;
import engine.Result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class IEC104CheckTimestamp {

    String inputTimestamp;
    String eventTimestamp;

    /**
     * Constructor of IdentifyLine.
     */
    public void IdentifyLine() {
    }

    /**
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException {
        Result result = new Result();
        result.setResult("NOK");

        this.inputTimestamp = parameters.get(1).getValue().trim();
        if (this.inputTimestamp.contains("@&Buffer_")) {
            this.inputTimestamp = (String) hashMap.get(this.inputTimestamp);
        }
        this.eventTimestamp = parameters.get(1).getValue().trim();
        if (this.eventTimestamp.contains("@&Buffer_")) {
            this.eventTimestamp = (String) hashMap.get(this.eventTimestamp);
        }

        LocalTime timestamp = LocalTime.parse(inputTimestamp, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        int hour = timestamp.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int minute = timestamp.get(ChronoField.MINUTE_OF_HOUR);
        int second = timestamp.get(ChronoField.SECOND_OF_MINUTE);
        int millisecond = timestamp.get(ChronoField.MILLI_OF_SECOND);
        CommonFunctions.debugLog.info(String.format("hour: %d, minute: %d, second: %d, millisecond: %d", hour, minute, second, millisecond));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime inputUnix = LocalDate.now().atTime(hour, minute, second, millisecond);
        long inputUnixLong = inputUnix.atZone(ZoneId.of("Asia/Shanghai")).toEpochSecond();
        System.out.println(inputUnixLong);

        final String regex = "\"\\s[0-9]+\\s[0-9]+\\s.*?(\"\\s[0-9]+\\s[0-9]+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(eventTimestamp);

        try {
            matcher.find();
            CommonFunctions.debugLog.error("Extracted Timestamp: " + matcher.group(1));
            String parsedEventTimeStamp =
                    matcher.group(1).substring(matcher.group(1).lastIndexOf(' ') + 1);

            if (Long.toString(inputUnixLong).equals(parsedEventTimeStamp)) {
                result.setResult("OK");
            } else {
                result.setComment("Mismatch \n Searched: " + this.inputTimestamp + "\n Found: " + parsedEventTimeStamp);
            }
        } catch (Exception e) {
            result.setComment("Not Matched IEC Time Stamp");
        }
        return result;
    }

    /**
     *
     */
    public void close() {

    }
}
