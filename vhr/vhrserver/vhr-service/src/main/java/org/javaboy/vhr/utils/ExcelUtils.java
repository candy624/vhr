package org.javaboy.vhr.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.javaboy.vhr.common.annotation.Excel;
import org.javaboy.vhr.common.annotation.Excels;
import org.javaboy.vhr.common.utils.Convert;
import org.javaboy.vhr.common.utils.DateUtils;
import org.javaboy.vhr.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by candy on 2020/11/5.
 */
public class ExcelUtils<T> {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    /**
     * 工作表对象
     */
    private HSSFSheet sheet;

    /**
     * 样式列表
     */
    private Map<String, HSSFCellStyle> styles;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ResponseEntity<byte[]>  exportExcel(List<T> list, String fileName) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheets = workbook.createSheet(fileName);
        //2. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        sheet = workbook.createSheet(fileName);
        // 6. 创建标题行
        HSSFRow row = sheet.createRow(0);
        int column = 0;
        this.createExcelField();
        // 写入各个字段的列头名称
        HSSFCell cell;
        for (Object[] os : fields) {
            cell = row.createCell(column ++);
            Excel excel = (Excel) os[1];
            cell.setCellValue(excel.name());
            setDataValidation(excel, row, column);
            cell.setCellStyle(headerStyle);
        }
        // 填充Excel数据
        fillExcelData(1, row, list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String((fileName + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 填充excel数据
     * @param index 序号
     * @param row 单元格行
     */
    public void fillExcelData(int index, HSSFRow row, List<T> list) {
        int startNo = 0;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = 0; i < endNo; i++) {
            row = sheet.createRow(i + 1 -startNo);
            T vo = list.get(i);
            int column = 0;
            for (Object[] os : fields) {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * 添加单元格
     */
    public HSSFCell addCell(Excel attr, HSSFRow row, T vo, Field field, int column) {
        HSSFCell cell = null;
        try {
            // 根据Excel中设置情况决定 是否导出， 有些情况需要保持为空， 希望用户填写这一列
            if (attr.isExport()) {
                // 创建 cell
                cell = row.createCell(column);
                // 用户读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value)) {
                    cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                } else if (value instanceof BigDecimal && -1 != attr.scale()) {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).toString());
                } else {
                    // 设置列类型
                    setCellVo(value, attr, cell);
                }

            }
        } catch (Exception e) {
            log.error("导出Excel失败:", e);
        }
        return cell;
    }


    /**
     * 设置单元格信息
     *
     * @param value 单元格值
     * @param attr 注解相关
     * @param cell 单元格信息
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (Excel.ColumnType.STRING == attr.cellType())
        {
            cell.setCellValue(StringUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
        }
        else if (Excel.ColumnType.NUMERIC == attr.cellType())
        {
            cell.setCellValue(StringUtils.contains(Convert.toStr(value), ".") ? Convert.toDouble(value) : Convert.toInt(value));
        }
    }

    /**
     * 获取bean 中的属性值
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws IllegalAccessException 异常
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr())) {
            String target = excel.targetAttr();
            if (target.contains(".")) {
                String[] targets = target.split("[.]]");
                for (String name : targets) {
                    o = getValue(o, name);
                }
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法获取值
     * @param o
     * @param name
     * @return
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception {
        if (StringUtils.isNotEmpty(name)) {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }


    /**
     * 创建表格样式 （宽高）
     */
    public void setDataValidation(Excel attr, HSSFRow row, int column) {
        if (attr.name().indexOf("注：") > 0) {
            sheet.setColumnWidth(column, 6000);
        } else {
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields)
        {
            // 单注解
            if (field.isAnnotationPresent(Excel.class))
            {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // 多注解
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels)
                {
                    putToField(field, excel);
                }
            }
        }
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
    }

    /**
     * 放到字段集合中
     */
    private void putToField(Field field, Excel attr)
    {
        if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
        {
            this.fields.add(new Object[] { field, attr });
        }
    }
}
