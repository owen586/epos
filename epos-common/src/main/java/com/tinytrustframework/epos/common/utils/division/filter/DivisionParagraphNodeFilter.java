package com.tinytrustframework.epos.common.utils.division.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.ParagraphTag;

/**
 * 一句话功能简述
 *
 * @author owen
 * @version 2016-03-16 16:54
 */
public class DivisionParagraphNodeFilter implements NodeFilter {

    /**
     * 过滤筛选p节点
     *
     * @param node P节点
     */
    public boolean accept(Node node) {
        if (node instanceof ParagraphTag) {
            ParagraphTag pTag = (ParagraphTag) node;
            if ("MsoNormal".equals(pTag.getAttribute("class"))) {
                return true;
            }
            return false;
        }
        return false;
    }


}
