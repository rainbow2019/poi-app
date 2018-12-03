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

    private static final String firstSqlPre = "update risk_case_trade_info set trade_no = '";
    private static final String secondSqlPre = "update risk_case_payment_trade_info set trade_no = '";
    private static final String sqlWhere = "' where trade_no = '";
    private static final String TAIL = "';";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd HHmmss");
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat("MMdd");

    /**
     * 主函数
     *
     * excel文件名格式为： 交易号更改-MMdd.xlsx
     * txt文件名格式为：交易号更改-MM-dd HHmmss-处理后.txt
     * MMdd必须
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("........任务开始了......");
        //S1. 读取excel文件到sourceLists中;其中每一个元素代表一个交易号对
        ArrayList<ArrayList> sourceLists = readExcel();
        //S2. 拼接前后语句，写到txt文件中
        writeToTxt(sourceLists);
        System.out.println("........任务结束了......");
    }

    /**
     * S1：读取Excel文件，返回一个ArrayList<ArrayList>
     *
     * @return
     */
    private static ArrayList<ArrayList> readExcel() {
        String destFilePath = SOURCE_DIR + SOURCE_FILENAME_PREFIX + SDF2.format(new Date()) + SOURCE_FILENAME_SUFFIX;
        System.out.println("destFilePath = " + destFilePath);
        ArrayList<ArrayList> readLists = new ArrayList<ArrayList>();
        XSSFWorkbook xssfWorkbook;
        XSSFSheet xssfSheet;
        XSSFRow xssfRow;
        XSSFCell cell;
        InputStream is;
        try {
            File file = new File(destFilePath);
            if (!file.exists()) file.createNewFile();
            else System.out.println("文件已经存在");
            is = new FileInputStream(file);
            System.out.println("FileInputStream流创建成功");
            xssfWorkbook = new XSSFWorkbook(is);
            System.out.println("创建XSSFWorkbook正常");
            xssfSheet = xssfWorkbook.getSheetAt(0);
            System.out.println("创建XSSFSheet正常;下面开始操作该Sheet!!!!!!!!!");
            for (int start = 1; ; start++) {
                xssfRow = xssfSheet.getRow(start);
                cell = xssfRow.getCell(1);
                String value1 = cell.getStringCellValue();
                System.out.println("cell-1 = " + value1);
                cell = xssfRow.getCell(2);
                String value2 = cell.getStringCellValue();
                System.out.println("cell-2 = " + value2);
                if (null == value1 || "".equals(value1) || null == value2 || "".equals(value2)) break;
                ArrayList perList = new ArrayList();
                perList.add(value1);
                perList.add(value2);
                readLists.add(perList);
            }
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("文件没有找到异常 : " + fileNotFound.toString());
        } catch (Exception e) {
            System.out.println("读取Excel文件异常 : " + e.toString());
        }
        return readLists;
    }

    /**
     * S2: 拼接sql语句，并直接写到txt文件中
     *
     * @param lists
     */
    public static void writeToTxt(ArrayList<ArrayList> lists) {
        System.out.println("------------开始写文件了------------");
        String destFilePath = DEST_DIR + DEST_FILENAME_PREFIX + SDF.format(new Date()) + DEST_FILENAME_SUFFIX;
        System.out.println("destFilePath : " + destFilePath);
        File file = new File(destFilePath);
        ArrayList<String> list;
        BufferedWriter bw;
        String oldTradeNo;
        String newTradeNo;
        try {
            if (file.exists()) file.delete();
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(destFilePath, true));
            int total = lists.size();
            String sql_1;
            String sql_2;
            for (int i = 0; i < total; i++) {
                list = lists.get(i);
                oldTradeNo = list.get(0);
                newTradeNo = list.get(1);
                sql_1 = generateSql(SQLTYPE.FIRST, oldTradeNo, newTradeNo);
                bw.write(sql_1);
                bw.newLine();
                sql_2 = generateSql(SQLTYPE.SECOND, oldTradeNo, newTradeNo);
                bw.write(sql_2);
                bw.newLine();
                bw.newLine();
                System.out.println("sql_1 : " + sql_1);
                System.out.println("sql_2 : " + sql_2+"\n");
                bw.flush();
            }
            bw.close();
        } catch (IOException ioEx) {
            System.out.println("创建文件失败");
            System.out.println("ioEx = " + ioEx);
        }
        System.out.println("---------写文件结束---------");
    }


    /**
     * 辅助方法: 生成一个sql语句
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
}
