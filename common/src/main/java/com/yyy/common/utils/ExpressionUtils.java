package com.yyy.common.utils;

/**
 * @author yanqiang
 * 表达式（计算公式）格式、运算符检查工具类
 */
public class ExpressionUtils {

    private static String[] OPERATORS = {"+", "-", "*", "/","(", ")"};

    private static char LEFT_PLACEHOLDER_CHAR = '{';

    private static String LEFT_PLACEHOLDER_STR = "{";

    private static char RIGHT_PLACEHOLDER_CHAR = '}';

    private static String RIGHT_PLACEHOLDER_STR = "}";

    private static String NULL_STR = "";

    private static String COMMA = ",";


    /**
     * @author yanqiang
     * 检查表达是并返回参数code数组
     * @param expression
     * @throws Exception
     */
    public static String[] checkAndReturnCode(String expression) throws Exception{

        if (MyStringUtils.isEmpty(expression)){
            throw new NullPointerException("expression不能为空");
        }


        String operatorsFiltered = filterOperators(expression);
        if (MyStringUtils.isEmpty(operatorsFiltered)){
            throw new Exception("没有提供可用的code");
        }

        checkPlaceHolder(operatorsFiltered);

        /**
         * 提取左边占位符索引
         */
        int[] leftPlaIndice = MyStringUtils.indicesOfSprcifiedChar(operatorsFiltered, LEFT_PLACEHOLDER_CHAR);

        /**
         * 提取右边占位符索引
         */
        int[] rightPlaIndice = MyStringUtils.indicesOfSprcifiedChar(operatorsFiltered, RIGHT_PLACEHOLDER_CHAR);
        if (leftPlaIndice != null && rightPlaIndice != null && leftPlaIndice.length == rightPlaIndice.length){
            String[] codes = new String[leftPlaIndice.length];
            for (int i = 0; i <= leftPlaIndice.length-1; i++){
                codes[i] = operatorsFiltered.substring(leftPlaIndice[i]+1,rightPlaIndice[i]);
            }
            if (codes.length == 0) {
                throw new Exception("表达式中必须包含指标代码。");
            }
            return codes;
        }else {
            throw new Exception("表达式有误,请确保表达式正确。");
        }




    }


    /**
     * @author yanqiang
     * 过滤占位符
     * @param expression
     * @throws Exception
     */
    public static String filterPlaceHodlers(String expression) throws Exception{

        if (MyStringUtils.isEmpty(expression)){
            throw new NullPointerException("expression不能为空");
        }
       return expression.replace(LEFT_PLACEHOLDER_STR, NULL_STR)
                .replace(RIGHT_PLACEHOLDER_STR, NULL_STR);
    }

    /**
     * @author yanqiang
     * 过滤掉运算符
     * @param expression
     * @return
     * @throws Exception
     */
    private static String filterOperators(String expression) throws Exception{
        expression = expression.trim();
        boolean operatorFlag = false;
        for (int i = 0; i < OPERATORS.length; i++) {
            String operator = OPERATORS[i];
            if (i < OPERATORS.length - 2) {
                if (expression.contains(operator)) {
                    operatorFlag = true;
                }
            }
            expression = expression.replace(operator, NULL_STR);
        }
        //如果表达式没有运算符
        if (!operatorFlag) {
            throw new Exception("表达式中没有运算符。");
        }
        return expression.trim();
    }


    /**
     * @author yanqiang
     * 检查占位符号
     * @param operatorsFiltered
     * @throws Exception
     */
    private static void checkPlaceHolder(String operatorsFiltered) throws Exception {
        int lpIndex = 0;
        int rpIndex = 0;
        int lpCount = 0;
        int rpCount = 0;
        char[] array = operatorsFiltered.toCharArray();
        for(int i = 0; i <= array.length-1; i++){
            if (LEFT_PLACEHOLDER_CHAR == array[i]){
                lpIndex = i;
                lpCount += 1;
            }

            if (RIGHT_PLACEHOLDER_CHAR == array[i]){
                rpIndex = i;
                rpCount +=1;
            }

            if (lpCount - rpCount >= 2 || rpCount - lpCount >= 2){
                throw new Exception("表达式占位符没不正确");
            }

            if (lpCount !=0 && lpCount == rpCount && lpIndex > rpIndex){
                throw new Exception("表达式占位符没不正确");
            }
        }
    }


}
