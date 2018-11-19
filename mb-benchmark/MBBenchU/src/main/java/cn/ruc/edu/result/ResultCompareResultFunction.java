package cn.ruc.edu.result;

import cn.ruc.edu.basecore.FileFunction;

import java.util.Map;

public class ResultCompareResultFunction {

    public static String transCharResult(String leftDBName, String rightDBName, double leftAvg, double rightAvg,
                                         float leftSumVar, float rightSumVar, float leftMax, float rightMax,
                                         float leftMin, float rightMin, String typeName){
        StringBuilder result = new StringBuilder("");
        if(leftAvg < rightAvg){
            result.append("在" + typeName + "的平均速度上，" + rightDBName +
                    "数据库的传输速度比" + leftDBName +
                    "数据库的传输速度快" + "，在" + typeName + "效率上" + rightDBName
                    + "数据库是" + leftDBName +
                    "数据库的" + FileFunction.floatToPresentString((float)(rightAvg / leftAvg))
                    + "%;");
        }else{
            result.append("在" + typeName + "的平均速度上，" + leftDBName +
                    "数据库的传输速度比" + rightDBName +
                    "数据库的传输速度快" + "，相比" + typeName + "效率" + leftDBName
                    + "数据库是" + rightDBName +
                    "数据库的" + FileFunction.floatToPresentString((float)(leftAvg / rightAvg))
                    + "%;");
        }

        if(leftSumVar < rightSumVar){
            result.append("在" + typeName + "的速度稳定性上，" + rightDBName +
                    "数据库的相较于" + leftDBName +
                    "数据库来说更不稳定" + "，其浮动比率" + rightDBName
                    + "数据库是" + leftDBName +
                    "数据库的" + FileFunction.floatToPresentString(rightSumVar / leftSumVar)
                    + "%;");
        }else{
            result.append("在" + typeName + "的速度稳定性上，" + leftDBName +
                    "数据库的相较于" + rightDBName +
                    "数据库来说更不稳定" + "，其浮动比率" + leftDBName
                    + "数据库是" + rightDBName +
                    "数据库的" + FileFunction.floatToPresentString(leftSumVar / rightSumVar)
                    + "%;");
        }

        float leftDValue = leftMax - leftMin;
        float rightDValue = rightMax - rightMin;
        if(leftDValue < rightDValue){
            result.append("在文件上传的速度峰值差上，" + rightDBName +
                    "数据库的相较于" + leftDBName +
                    "数据库来说波峰与波谷的差距更大" + "，" + rightDBName
                    + "数据库是" + leftDBName +
                    "数据库差距的" + FileFunction.floatToPresentString(rightDValue / leftDValue)
                    + "%");
        } else{
            result.append("在文件上传的速度峰值差上，" + leftDBName +
                    "数据库的相较于" + rightDBName +
                    "数据库来说波峰与波谷的差距更大" + "，" + leftDBName
                    + "数据库是" + rightDBName +
                    "数据库差距的" + FileFunction.floatToPresentString(leftDValue / rightDValue)
                    + "%");
        }
        return result.toString();
    }

    public static String queryResult(String queryType, String leftDBName, String rightDBName,
                                           String leftSpeedString, String rightSpeedString){
        StringBuilder result = new StringBuilder("");
        if(leftSpeedString == null || rightSpeedString == null){
            if(leftSpeedString == null)
                result.append(leftDBName + "数据库不支持" + queryType + "负载。");
            if(rightSpeedString == null)
                result.append(rightDBName + "数据库不支持" + queryType + "负载。");
        }
        else {
            float leftSpeed = Float.parseFloat(leftSpeedString);
            float rightSpeed = Float.parseFloat(rightSpeedString);

            if(leftSpeed < rightSpeed){
                result.append("在" + queryType + "负载上，" + rightDBName + "数据库的查询速度比" + leftDBName + "数据库要快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(rightSpeed / leftSpeed) + "%");
            } else{
                result.append("在" + queryType + "负载上，" + leftDBName + "数据库的查询速度比" + rightDBName + "数据库要快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(leftSpeed / rightSpeed) + "%");
            }
        }
        return  result.toString();
    }

    public static String copyQueryResult(String leftDBName, String rightDBName, Map left, Map right){
        StringBuilder result = new StringBuilder("");

        if(left.get("ability").equals("false") || right.get("ability").equals("false")){
            if(left.get("ability").equals("false"))
                result.append(leftDBName + "数据库不支持版本管理负载。");
            if(right.get("ability").equals("false"))
                result.append(rightDBName + "数据库不支持版本管理负载。");
        }
        else{
            float leftSpeed = Float.parseFloat(( String ) left.get("speed"));
            float rightSpeed = Float.parseFloat(( String ) right.get("speed"));
            if(leftSpeed < rightSpeed)
                result.append("在版本管理负载上，" + rightDBName + "数据库的查询速度比" + leftDBName + "数据库要快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(rightSpeed / leftSpeed) + "%");
            else
                result.append("在版本管理负载上，" + leftDBName + "数据库的查询速度比" + rightDBName + "数据库要快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(leftSpeed / rightSpeed) + "%");
        }
        return result.toString();
    }

    public static String abnormalQueryResult(String leftDBName, String rightDBName, Map left, Map right){
        StringBuilder result = new StringBuilder("");
        if(left.get("ability").equals("false") || right.get("ability").equals("false")){
            if(left.get("ability").equals("false"))
                result.append(leftDBName + "数据库不支持异常文件检测负载。");
            if(right.get("ability").equals("false"))
                result.append(rightDBName + "数据库不支持异常文件检测负载。");
        }
        else{
            float leftRate = Float.parseFloat(( String ) left.get("rate"));
            float rightRate = Float.parseFloat(( String ) right.get("rate"));
            float leftSpeed = Float.parseFloat(( String ) left.get("speed"));
            float rightSpeed = Float.parseFloat(( String ) right.get("speed"));

            if(leftRate < rightRate && leftSpeed < rightRate)
                result.append("在异常文件检测负载上，" + rightDBName + "数据库的查询正确率比" + leftDBName + "数据库要好，并且其查询速度也更快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(rightSpeed / leftSpeed) + "%");
            else if(leftRate > rightRate && leftSpeed > rightRate)
                result.append("在异常文件检测负载上，" + leftDBName + "数据库的查询正确率比" + rightDBName + "数据库要好，并且其查询速度也更快，" +
                        "是其查询速度的" + FileFunction.floatToPresentString(leftSpeed / rightSpeed) + "%");
            else if(leftRate > rightRate && leftSpeed < rightRate)
                result.append("在异常文件检测负载上，" + rightDBName + "数据库的查询正确率比" + leftDBName + "数据库要好，但是查询速度相对较慢，" +
                        leftDBName + "数据库在查询速度上是" + rightDBName + "数据库的"
                         + FileFunction.floatToPresentString(leftSpeed / rightSpeed) + "%");
            else
                result.append("在异常文件检测负载上，" + leftDBName + "数据库的查询正确率比" + rightDBName + "数据库要好，但是查询速度相对较慢，" +
                        rightDBName + "数据库在查询速度上是" + leftDBName + "数据库的"
                        + FileFunction.floatToPresentString(rightSpeed / leftSpeed) + "%");
        }

        return result.toString();
    }
}
