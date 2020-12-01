import org.json.JSONException;

import java.io.IOException;

public class WorkWithData {
    private static FinalData finalData = new FinalData();
    private static int i = 0;

    public static FinalData getFinalData() {
        return finalData;
    }

    public static void structureData(String[] data) throws JSONException, IOException {
        int i=0;
        while (data[i]!=null){
            if (data[i].startsWith("[")) {
                jsonToTable(data[i]);
            }
            else if(data[i].startsWith("-")){
                mimeToTable(data[i]);
            }
            else if (data[i].startsWith("<?xml")){
                xmlToTable(data[i]);
            }
            else {
                tableToTable(data[i]);
//                System.out.println(data[i]);
            }
            i++;

        }
        finalData.print();
    }

    private static void jsonToTable(String str) {
        while (str.length()>4) {
            int k = 0;
            while (!str.startsWith("\"")) {
                if(str.charAt(0) == '}') {
                    i++;
                }
                str = str.substring(1);
            }
            str = str.substring(1);
            String temp = "";
            while (!str.startsWith("\"")) {
                temp += str.charAt(0);
                str = str.substring(1);
            }
            while (finalData.getHeader()[k] != null) {
                if (finalData.getHeader()[k].equals(temp))
                    break;
                k++;
            }
            finalData.addHeader(temp,k);
            while (!str.startsWith(":"))
                str = str.substring(1);
            str = str.substring(1);
            if (str.startsWith("\""))
                str = str.substring(1);
            temp = "";
            while (!str.startsWith("\"")) {
                if(str.charAt(0)!=','){
                temp += str.charAt(0);}
                str = str.substring(1);
            }
            finalData.addToTable(temp, i, k);
            if(k!=0){
                str = str.substring(1);
            }
        }
        i++;
    }

    private static void tableToTable(String str){
        int[] columns = new int[10];
        int j =0;

        while (!str.startsWith("\n")){
            String temp = "";
            int k=0;
            while (!str.startsWith(",") && !str.startsWith("\n")){
                temp+=str.charAt(0);
                str = str.substring(1);
            }
            while (finalData.getHeader()[k] != null) {
                if (finalData.getHeader()[k].equals(temp))
                    break;
                k++;
            }
            columns[j] = k;
            j++;
            finalData.addHeader(temp,k);
            if(!str.startsWith("\n")) {
                str = str.substring(1);
            }
        }

        while (str.length()>15) {
            str = str.substring(1);
            j=0;
            while (!str.startsWith("\n")) {
                String temp = "";
                while (!str.startsWith(",") && !str.startsWith("\n")) {
                    temp += str.charAt(0);
                    str = str.substring(1);
                }
                finalData.addToTable(temp, i, j);
                j++;
                if (!str.startsWith("\n")) {
                    str = str.substring(1);
                }
            }
            i++;
        }
    }

    private static void xmlToTable(String str){
        while (!str.startsWith("<id>")) {
            str = str.substring(1);
        }
        while (str.length()>4){
            while (!str.startsWith("<")) {
                str = str.substring(1);
            }
            String tmp = "";
            str = str.substring(1);
            while (!str.startsWith(">")){
                tmp += str.charAt(0);
                str = str.substring(1);
            }
            str = str.substring(1);
            if(tmp.equals("/record")){
                i++;
                str = str.substring(8);
            }
            else {
                int k = 0;
                while (finalData.getHeader()[k] != null) {
                    if (finalData.getHeader()[k].equals(tmp))
                        break;
                    k++;
                }
                finalData.addHeader(tmp, k);
                tmp = "";
                while (!str.startsWith("</")) {
                    tmp += str.charAt(0);
                    str = str.substring(1);
                }
                str = str.substring(1);
                finalData.addToTable(tmp, i, k);
            }
        }
    }

    private static void mimeToTable(String str) {
        str = str.substring(5);
        while (str.length()>2){
            while (!Character.isLetter(str.charAt(0))) {
                if (str.startsWith("-")) {
                    i++;
                }
                str = str.substring(1);
            }
            String tmp = "";
            while (!str.startsWith(":")) {
                tmp += str.charAt(0);
                str = str.substring(1);
            }
            int k = 0;
            while (finalData.getHeader()[k] != null) {
                if (finalData.getHeader()[k].equals(tmp)) {
                    break;
                }
                k++;
            }
            finalData.addHeader(tmp, k);
            str = str.substring(2);
            tmp = "";
            while (!str.startsWith("\n")) {
                tmp += str.charAt(0);
                str = str.substring(1);
            }
            finalData.addToTable(tmp, i, k);
        }
    }
}
