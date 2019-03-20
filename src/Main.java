
import java.util.ArrayList;
import java.util.List;

/**
 * Код частично взят из первой программы.
 * Реализована простая генерация данных.
 */
public class Main {
    public static final String DELIMITER = ";";

    /**Стандарные значения, меняются на введённые*/
    public static String OutputName = "output.csv";
    public static String ENCODING = "utf-8";
    public static int ColCount = 5;
    public static int RowCount = 8;
    public static int Length = 15;

    /**  Для числовых значений выбрано данное значение длины */
    public static int Length_Digits = 8;

    /** Используется для создания или пересоздания файла вывода  */
    public static boolean oneTimeRefreshingFile=true;

    public static List<List<DataCell>> dataList = new ArrayList<>();
    public enum DataType { STRING, DATE, INTEGER, FLOAT, HEADING, WRONG_TYPE, NONE}
    public static List<DataType> dataTypeList = new ArrayList<>();


    /**
     Пример ввода:  java –jar gen_csv.jar   543      987654        300          in_file.csv
                              имя файла   колонки    строки     макс.длина     вывод в файл       */
    public static void main(String[] args) {
        action(args);
    }

    private static void action(String[] args) {
        if(args.length>0){
            if(args[0].toLowerCase().equals("help")){
                help();
            }else{
                try{
                    ColCount=Integer.parseInt(args[0]);
                    RowCount=Integer.parseInt(args[1]);
                    Length=Integer.parseInt(args[2]);
                    OutputName=args[3];
                }catch (Exception e){
                    System.out.println("Ошибка подачи параметров.");
                    help();
                }
            }
        }else{
            System.out.println("Отсутствуют параметры для запуска. Будет " +
                    "осуществлён запуск со стандартными значениями.");
            System.out.println("Данные будут записаны в файл \"output.csv\"");
        }

        List<List<DataCell>> list = CustomSupport.generateList();
        for(List<DataCell> l:list){
            CustomSupport.saveToCSV(l);
        }
        System.out.println("Данные успешно сгенерированы и записаны!");
    }

    private static void help() {
        System.out.println("Правильный порядок для построения таблицы:");
        System.out.println("1) Кол-во столбиков");
        System.out.println("2) Кол-во строчек");
        System.out.println("3) Максимальная длина каждой ячейки(кроме даты)");
        System.out.println("4) Файл для вывода");
        System.out.println("Пример полной команды запуска:java –jar gen_csv.jar 54 12 20 in_file.csv");
        System.out.println("По-возможности данные будут сгенерированы и записаны с стандартными установками.");
    }
}
