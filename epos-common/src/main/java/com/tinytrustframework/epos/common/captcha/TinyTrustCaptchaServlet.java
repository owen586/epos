/*
 * 文 件 名:  Example1.java
 * 版    权:  www.astcard.com. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  owen
 * 修改时间:  2013-6-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.tinytrustframework.epos.common.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.Captcha.Builder;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.SimpleCaptchaServlet;
import nl.captcha.text.renderer.DefaultWordRenderer;

/**
 * 验证码
 *
 * @author Owen
 * @version [版本号, 2013-6-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TinyTrustCaptchaServlet extends SimpleCaptchaServlet {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2648330702611405621L;

    /**
     * 高度,默认为50
     */
    private static final String PARAM_HEIGHT = "height";

    /**
     * 宽度,默认为100
     */
    private static final String PARAM_WIDTH = "width";

    /**
     * 干扰线条 默认是没有干扰线条
     */
    private static final String PARAM_NOISE = "noise";

    /**
     * 宽度
     */
    protected int _width = 100;

    /**
     * 高度
     */
    protected int _height = 50;

    /**
     * 干扰线条
     */
    protected boolean _noise = false;

    /**
     * 初始化过滤器.将配置文件的参数文件赋值
     *
     * @throws ServletException
     * @see [类、类#方法、类#成员]
     */

    public void init()
            throws ServletException {
        if (getInitParameter(PARAM_HEIGHT) != null) {
            _height = Integer.valueOf(getInitParameter(PARAM_HEIGHT));
        }

        if (getInitParameter(PARAM_WIDTH) != null) {
            _width = Integer.valueOf(getInitParameter(PARAM_WIDTH));
        }

        if (getInitParameter(PARAM_NOISE) != null) {
            _noise = Boolean.valueOf(getInitParameter(PARAM_NOISE));
        }

    }

    /**
     * 一句话功能简述
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Builder builder = new Builder(_width, _height);
        builder.addBorder(); //增加边框
        if (_noise == true)//是否增加干扰线条
        {
            //            builder.addNoise(new StraightLineNoiseProducer());//直线干扰
            int curvedLineRandomNum = new Random().nextInt(2) + 5;//随机生成N+1条干扰线
            for (int i = 0, j = curvedLineRandomNum; i < j; i++) {
                builder.addNoise(new CurvedLineNoiseProducer(new Color(0, 168, 143), 1));//弧线干扰
            }
        }

        //----------------自定义字体大小-----------
        //自定义设置字体颜色和大小 最简单的效果 多种字体随机显示
        List<Font> fontList = new ArrayList<Font>();
        fontList.add(new Font("Arial", Font.BOLD, 40));//可以设置斜体之类的

        //加入多种颜色后会随机显示 字体空心
        List<Color> colorList = new ArrayList<Color>();
        colorList.add(new Color(0, 168, 143));//深绿色
        DefaultWordRenderer defaultWordRender = new DefaultWordRenderer(colorList, fontList);

        //增加文本，默认为5个随机数字  
        builder.addText(defaultWordRender).addBackground();
        //builder.addText(new NumbersAnswerProducer(new Random().nextInt(2) + 4), coloredEdgesWordRender);//Number Text
        //            builder.addText(new ArabicTextProducer(5));//Arabic Text
        //            builder.addText(new FiveLetterFirstNameTextProducer());//Letter Text
        //            builder.addText(new ChineseTextProducer(5),cwr);

        //--------------添加背景-------------  
        //设置背景渐进效果 以及颜色 form为开始颜色，to为结束颜色  
        GradiatedBackgroundProducer gbp = new GradiatedBackgroundProducer();
        //        gbp.setFromColor(Color.BLACK);
        //        gbp.setFromColor(Color.DARK_GRAY);//深灰
        gbp.setFromColor(Color.LIGHT_GRAY);//浅灰 good
        //        gbp.setFromColor(Color.GRAY);
        gbp.setToColor(Color.WHITE);
        builder.addBackground(gbp);

        //加入阴影效果 默认3，75   
        //        builder.gimp(new FishEyeGimpyRenderer(Color.white, Color.white));
        //加入阴影效果 默认3，75   
        builder.gimp(new DropShadowGimpyRenderer(3, 50));

        //创建对象  
        Captcha captcha = builder.build();
        BufferedImage image = captcha.getImage();
        CaptchaServletUtil.writeImage(resp, image);
        req.getSession().setAttribute(Captcha.NAME, captcha);
    }

}
