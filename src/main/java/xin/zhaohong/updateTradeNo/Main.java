package xin.zhaohong.updateTradeNo;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Description 注释
 * @Author zhaohong
 * @Time 2018-11-30 20:57
 */
public class Main {
    private static final String SOURCE_DIR = "E:\\1. 2018年京东实习文件0918\\2. 京东实习功能需求开发\\案件系统更改交易号-丁一峰\\";
    private static final String SOURCE_FILENAME_PREFIX = "交易号更改-";
    private static final String SOURCE_FILENAME_SUFFIX = ".xlsx";

    private static final String DEST_DIR = "E:\\1. 2018年京东实习文件0918\\2. 京东实习功能需求开发\\案件系统更改交易号-丁一峰\\";
    private static final String DEST_FILENAME_PREFIX = "交易号更改-";
    private static final String DEST_FILENAME_SUFFIX = "-处理后.txt";

    private static final String firstSqlPre = "update risk_case_trade_info set trade_no = ";
    private static final String secondSqlPre = "update risk_case_payment_trade_info set trade_no = ";
    private static final String sqlWhere = " where trade_no = '";
    private static final String TAIL = "';";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat("MMdd");

    public static void main(String[] args) {
        System.out.println("任务开始了......");
        ArrayList<ArrayList> outLists = new ArrayList<ArrayList>();
        ArrayList<ArrayList> sourceLists = readExcel();
        ArrayList<String> list;
        String oldTradeNo;
        String newTradeNo;
        int total = sourceLists.size();
        for (int i = 0; i < total; i++) {
            list = sourceLists.get(i);
            oldTradeNo = list.get(0);
            newTradeNo = list.get(1);
            String secondSql = generateSql(SQLTYPE.SECOND, oldTradeNo, newTradeNo);
            ArrayList<String> destList = new ArrayList<String>();
            destList.add(generateSql(SQLTYPE.FIRST, oldTradeNo, newTradeNo));
            destList.add(generateSql(SQLTYPE.SECOND, oldTradeNo, newTradeNo));
            outLists.add(destList);
        }
        writeToTxt(outLists);
        System.out.println("任务结束了......");
    }

    private static ArrayList<ArrayList> readExcel(){
        String destFilePath = SOURCE_DIR + SOURCE_FILENAME_PREFIX + SDF2.format(new Date()) + SOURCE_FILENAME_SUFFIX;
        System.out.println("destFilePath = " + destFilePath);
        ArrayList<ArrayList> readLists = new ArrayList<ArrayList>();
        XSSFWorkbook xssfWorkbook;
        InputStream is;
        try {
            File file = new File(destFilePath);
            if(!file.exists()) file.exists();
            else System.out.println("文件存在");
            is = new FileInputStream(file);
            System.out.println("is流正常");
            xssfWorkbook = new XSSFWorkbook(is);
            System.out.println("创建Workbook正常");
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
//            while(true){
                XSSFRow xssfRow = xssfSheet.getRow(0);
                XSSFCell cell = xssfRow.getCell(0);
                cell.getStringCellValue();
                System.out.println("cell = " + cell);
//            }
        }catch(Exception e){
            System.out.println("读取Excel文件异常");
            System.out.println("e = " + e);
        }
        return readLists;
    }

    /**
     * 生成一个sql语句
     *
     * @param sqlType
     * @param oldTradeNo
     * @param newTradeNo
     * @return
     */
    private static String generateSql(SQLTYPE sqlType, String oldTradeNo, String newTradeNo) {
        if (sqlType == SQLTYPE.FIRST)
            return firstSqlPre + newTradeNo + sqlWhere + oldTradeNo + TAIL;
        else if (sqlType == SQLTYPE.SECOND)
            return secondSqlPre + newTradeNo + sqlWhere + oldTradeNo + TAIL;
        else
            return "";
    }

    /**
     * 写两个list到txt文件中
     *
     * @param lists
     */
    public static void writeToTxt( ArrayList<ArrayList> lists ) {
        System.out.println("开始写文件了......");
        String destFilePath = DEST_DIR + DEST_FILENAME_PREFIX + SDF.format(new Date()) + DEST_FILENAME_SUFFIX;
        System.out.println("destFilePath : " + destFilePath);
        File file = new File(destFilePath);
        ArrayList<String> list;
        BufferedWriter bw;
        try {
            if (file.exists()) file.delete();
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(destFilePath, true));
            int total = lists.size();
            for (int i = 0; i < total; i++) {
                list = lists.get(i);
                bw.write(list.get(0));
                bw.newLine();
                bw.write(list.get(1));
                bw.newLine();
                bw.flush();
            }
            bw.close();
        } catch (IOException ioEx) {
            System.out.println("创建文件失败");
        }
        System.out.println("写文件结束......");
    }

}
