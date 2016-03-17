package com.tinytrustframework.epos.common.utils.division;

import com.tinytrustframework.epos.common.utils.division.filter.DivisionParagraphNodeFilter;
import com.tinytrustframework.epos.common.utils.division.model.Division;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;

/**
 * 一句话功能简述
 *
 * @author owen
 * @version 2016-03-16 16:29
 */
public class DivisionUtil {
    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        parser.setURL("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201504/t20150415_712722.html");
        NodeList divisionNodeList = parser.extractAllNodesThatMatch(new DivisionParagraphNodeFilter());
        if (null == divisionNodeList || divisionNodeList.size() == 0) {
            return;
        }
        Division division = null;
        Node paragraphNode = null;
        Node idNode = null, nameNode = null;
        String areaId = null, areaName = null, parentAreaId = null;
        String provinceId = null, cityId = null;
        for (int i = 0, j = divisionNodeList.size(); i < j; i++) {
            paragraphNode = divisionNodeList.elementAt(i);
            idNode = paragraphNode.getFirstChild();
            areaId = idNode.toPlainTextString().replace("&nbsp;", "").trim();//区域编号
            nameNode = paragraphNode.getLastChild();
            areaName = nameNode.toPlainTextString();//区域名称
            int areaNameOriginLength = areaName.length();
            int areaNameUpdatedLength = removeNonChineseChar(areaName).length();
            division = Division.builder().areaId(areaId).areaName(areaName).build();
            // 一个空格
            if (areaNameOriginLength - areaNameUpdatedLength == 1) {
                parentAreaId = areaId;
                provinceId = areaId;
            } else if (areaNameOriginLength - areaNameUpdatedLength == 2) {
                //两个空格
                parentAreaId = provinceId;
                cityId = areaId;
            } else if (areaNameOriginLength - areaNameUpdatedLength == 3) {
                // 三个空格
                parentAreaId = cityId;
            }
            division.setParentAreaId(parentAreaId);//父节点
            System.out.println(division.getAreaId() + "\t" + removeNonChineseChar(division.getAreaName()) + "\t" + division.getParentAreaId());
        }

        System.exit(0);
    }

    /**
     * 删除非中文字符
     *
     * @param originStr 字符源串
     */
    private static String removeNonChineseChar(String originStr) {
        String updatedStr = originStr.replaceAll("[^\\u4e00-\\u9fa5]", "").trim();
        return updatedStr;
    }

}
