import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomSupport {

    public static void saveToCSV(List<DataCell> list){
        if(Main.oneTimeRefreshingFile){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.OutputName), //Здесь просто создаю или обновляю файл если уже создан
                    Main.ENCODING))) { writer.write(""); } catch (Exception e) {
                System.out.println("Ошибка с созданием/пересозданием файла. Прекращение работы программы");
                System.exit(1);
            }
            Main.oneTimeRefreshingFile=false;
        }
        try{
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(Main.OutputName, true)));
            StringBuilder sb = new StringBuilder("");
            for(DataCell cell:list){ sb.append(cell.data.toString()+Main.DELIMITER); }
            sb.deleteCharAt(sb.lastIndexOf(";"));
            printWriter.print(sb.toString());
            printWriter.println();
            printWriter.flush();
            printWriter.close();
        }catch(Exception e){
            System.out.println("Ошибка с записью в файл. Прекращение работы программы");
            System.exit(1);
        }
    }

    /**
     * Метод возвращает список с сгенерированными данными
     */
    public static List<List<DataCell>> generateList (){
        List<List<DataCell>> list = new ArrayList<>();
        List<Main.DataType> dataTypeList = new ArrayList<>();

        List<DataCell>sublist = new ArrayList<>();

        Main.DataType dataType = Main.DataType.NONE;
        Object object = new Object();
        for(int i=0; i<Main.ColCount;i++){
            String forHeader = " ";
            switch (new Random().nextInt(4)){
                case 0:
                    dataType=Main.DataType.INTEGER;
                    forHeader+="Integer";
                    break;
                case 1:
                    dataType=Main.DataType.STRING;
                    forHeader+="String";
                    break;
                case 2:
                    dataType=Main.DataType.FLOAT;
                    forHeader+="Float";
                    break;
                case 3:
                    dataType=Main.DataType.DATE;
                    forHeader+="Date";
                    break;
            }
            DataCell cell = new DataCell(generate(dataType)+forHeader,dataType);
            dataTypeList.add(dataType);
            sublist.add(cell);
        }
        list.add(sublist);

        for(int i=1; i<Main.RowCount;i++){
            sublist=new ArrayList<>();
            for(int j=0; j<Main.ColCount; j++){
                switch (dataTypeList.get(j)){
                    case STRING:
                        sublist.add(new DataCell(generate(Main.DataType.STRING),Main.DataType.STRING));
                        break;
                    case DATE:
                        sublist.add(new DataCell(generate(Main.DataType.DATE),Main.DataType.DATE));
                        break;
                    case INTEGER:
                        sublist.add(new DataCell(generate(Main.DataType.INTEGER),Main.DataType.INTEGER));
                        break;
                    case FLOAT:
                        sublist.add(new DataCell(generate(Main.DataType.FLOAT),Main.DataType.FLOAT));
                        break;
                }
            }
            list.add(sublist);
        }
        return list;
    }

    /**
     * Генерирует и возвращает данные по передаваемому типу данных
     */
    public static String generate(Main.DataType dataType){
        String data = "";
        switch (dataType){
            case INTEGER:
                data = generateIntOrStr(48,57, 0,0);
                break;
            case STRING:
                data = generateIntOrStr(65,90,97,122);
                break;
            case FLOAT:
                data = generateFloatStr();
                break;
            case DATE:
                data = generateDate();
                break;
        }
        return data;
    }

    /**
     * Создаёт int или String, возвращает в виде String.
     * Работа зависит от подаваемых данных
     */
    public static String generateIntOrStr(int min, int max, int min2, int max2){
        char[] array;
        int chosenLength=0;
        if(min2>0&max2>0){
            array = new char[Main.Length];
            chosenLength=Main.Length;
        }else{
            array = new char[Main.Length_Digits];
            chosenLength=Main.Length_Digits;
        }

        int rand, rand2;
        Random r = new Random();
        for (int i = 0; i< chosenLength; i++) {
            rand = r.nextInt(max-min+ 1)+min ; // (см ASCII)
            if(min2>0&max2>0){
                rand2 = r.nextInt(max2-min2+ 1)+min2 ;
                if(new Random().nextInt(2)>0){
                    rand=rand2;
                }
            }else if((char)rand=='0'&i==0){
                do{
                    rand = r.nextInt(max-min+ 1)+min ;
                }while ((char)rand=='0');
            }
            array[i] = (char)rand;
        }
        return new String(array);
    }

    /**
     * Создаёт Float значение, возвращает в виде String
     */
    public static String generateFloatStr(){
        char[] array = new char[Main.Length_Digits];
        int rand, rand2;
        Random r = new Random();
        int pointPlacement = r.nextInt(Main.Length_Digits);
        for (int i = 0; i< Main.Length_Digits; i++) {
            rand = r.nextInt(57-48+ 1)+48 ; // (см ASCII)
            if(i==pointPlacement){
                if(i==0){
                    array[i]='0';
                    array[i+1]='.';
                    i++;
                }else if(i!=array.length-1){
                    array[i] = '.';
                }
            }else if(i==0&(char)rand=='0'){
                do{
                    rand = r.nextInt(57-48+ 1)+48 ;
                }while ((char)rand=='0');
                array[i] = (char)rand;
            } else{
                array[i] = (char)rand;
            }

        }
        return new String(array);
    }
    /**
     * Создаёт случайную дату в формате дд.мм.гггг, возвращает в виде String
     */
    public static String generateDate(){
        int a,b,c,d,e,f,g,h;
        a=b=c=d=e=f=g=h=10;
        char[] array = new char[10];
        array[2]='.';
        array[5]='.';

        Random r = new Random();
        do{
            a = r.nextInt(10);        //ограничение - 31
            b = r.nextInt(10);
        }while (a*10+b>31||a+b==0);
        array[0]=(char)(a+48);
        array[1]=(char)(b+48);

        do{
            c = r.nextInt(2);         //ограничение - 12
            d = r.nextInt(10);
        }while (c*10+d>12||c+d==0);
        array[3]=(char)(c+48);
        array[4]=(char)(d+48);

        do{
            e = r.nextInt(10);
            f = r.nextInt(10);        //ограничение - 2100
            g = r.nextInt(10);
            h = r.nextInt(10);
        }while (e*1000+f*100+g*10+h>2100);
        array[6]=(char)(e+48);
        array[7]=(char)(f+48);
        array[8]=(char)(g+48);
        array[9]=(char)(h+48);

        return new String(array);
    }
}
