package com.cmms.demo.utils;

import com.cmms.demo.domain.DriverPOJO;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateAccountUtils {
    public String normalName(String input) {
        input = input.trim();
        input = input.replaceAll("\\s+", " ");
        input = input.toLowerCase();
        String tmp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        tmp = pattern.matcher(tmp).replaceAll("");
        tmp = tmp.replaceAll("đ", "d");
        return tmp;
    }

    public String navigateToUserName(String input) {
        input = normalName(input);
        String resultStr = "";
        String[] elementOfName = input.split(" ");
        resultStr += elementOfName[elementOfName.length - 1];
        for (int i = 0; i < elementOfName.length - 1; i++) {
            resultStr += elementOfName[i].charAt(0);
        }
        return resultStr;
    }

    public String convertToUsernameOutput(String input,
                                          List<DriverPOJO> driverLst) {
        int count = 0;
        String output = navigateToUserName(input);
        List<String> ls = new ArrayList<>();
        for (int i = 0; i < driverLst.size(); i++) {
            String temp = navigateToUserName(driverLst.get(i).getName());
            if (output.equals(temp)) {
                ls.add(driverLst.get(i).getUser().getAccount());
            }
        }
        if(ls.size() > 0) {
            String lastStr = ls.get(ls.size() - 1);
            int index = findIntegerIndex(lastStr);
            int number = 0;
            if (index == 0){
                number = 1;
            }else{
                number = Integer.parseInt(lastStr.substring(index)) +1;
            }
            return output + number;
        }else{
            return output;
        }
    }

    public int findIntegerIndex(String str){
        Pattern pattern = Pattern.compile("^\\D*(\\d)");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.start(1);
        }else{
            return 0;
        }
    }
}
