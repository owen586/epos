package com.tinytrustframework.epos.common.utils.region;

import com.tinytrustframework.epos.common.utils.region.filter.RegionParagraphNodeFilter;
import com.tinytrustframework.epos.common.utils.region.model.Region;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;

/**
 * 获取国家统计局公布的行政区划代码(http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201504/t20150415_712722.html)
 * <p/>
 * 最新县及县以上行政区划代码（截止2014年10月31日）
 * <p/>
 * 抓取结果展示如下（以北京为例）
 * 110000 北京市 110000
 * 110100 市辖区 110000
 * 110101 东城区 110100
 * 110102 西城区 110100
 * 110105 朝阳区 110100
 * 110106 丰台区 110100
 * 110107 石景山区 110100
 * 110108 海淀区 110100
 * 110109 门头沟区 110100
 * 110111 房山区 110100
 * 110112 通州区 110100
 * 110113 顺义区 110100
 * 110114 昌平区 110100
 * 110115 大兴区 110100
 * 110116 怀柔区 110100
 * 110117 平谷区 110100
 * 110200 县 110000
 * 110228 密云县 110200
 * 110229 延庆县 110200
 *
 * @author owen
 * @version 2016-03-16 16:29
 */
public class RegionUtil {

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        parser.setURL("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201504/t20150415_712722.html");
        NodeList divisionNodeList = parser.extractAllNodesThatMatch(new RegionParagraphNodeFilter());
        if (null == divisionNodeList || divisionNodeList.size() == 0) {
            return;
        }
        Region division = null;
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
            division = Region.builder().areaId(areaId).areaName(areaName).build();
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
