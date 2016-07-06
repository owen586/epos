package com.tinytrust.epos.common.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Random.*;

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
import nl.captcha.text.producer.ChineseTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;

/**
 * 验证码
 *
 * @author Owen
 * @version [版本号, 2013-6-25]
 */
public class TinyTrustCaptchaServlet extends SimpleCaptchaServlet {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2648330702611405621L;

    // 高度,默认为50
    private static final String PARAM_HEIGHT = "height";

    // 宽度,默认为100
    private static final String PARAM_WIDTH = "width";

    // 干扰线条 默认是没有干扰线条
    private static final String PARAM_NOISE = "noise";

    // 宽度
    protected int _width = 100;

    // 高度
    protected int _height = 50;

    // 干扰线条
    protected boolean _noise = false;

    /**
     * 初始化过滤器.将配置文件的参数文件赋值
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

    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
            throws ServletException, IOException {
        Builder builder = new Builder(_width, _height);
        builder.addBorder(); //增加边框
        if (_noise == true)//是否增加干扰线条
        {
            //            builder.addNoise(new StraightLineNoiseProducer());//直线干扰
            int curvedLineRandomNum = new Random().nextInt(3)+1;//随机生成N+1条干扰线
            for (int i = 0, j = curvedLineRandomNum; i < j; i++) {
                builder.addNoise(new CurvedLineNoiseProducer(new Color(0, 0,0), 1));//弧线干扰
            }
        }

        //----------------自定义字体大小-----------
        //自定义设置字体颜色和大小 最简单的效果 多种字体随机显示
        List<Font> fontList = new ArrayList<Font>();
        //获得系统中可用的字体名称
//        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        String[] fontName = e.getAvailableFontFamilyNames();
//        for (int i = 0, j = fontName.length; i < j; i++) {
//            fontList.add(new Font(fontName[i],Font.BOLD,40));
//        }


        fontList.add(new Font("Arial", fontStyleRandom(), 38));
        fontList.add(new Font("Georgia", fontStyleRandom(), 38));
        fontList.add(new Font("Comic Sans MS", fontStyleRandom(), 38));
        fontList.add(new Font("Verdana", fontStyleRandom(), 38));
        fontList.add(new Font("Times New Roman", fontStyleRandom(), 38));
        fontList.add(new Font("Trebuchet MS", fontStyleRandom(), 38));
        fontList.add(new Font("Impact", fontStyleRandom(), 38));
        fontList.add(new Font("Courier New", fontStyleRandom(), 38));

        //加入多种颜色后会随机显示 字体空心
        List<Color> colorList = new ArrayList<Color>();
//        colorList.add(new Color(0, 170, 85));
        colorList.add(new Color(0, 0, 0));
//        colorList.add(new Color(0, 119, 221));
//        colorList.add(new Color(0, 136, 85));
//        colorList.add(new Color(136, 153, 17));


        DefaultWordRenderer defaultWordRender = new DefaultWordRenderer(colorList, fontList);
        //增加文本，默认为5个随机数字
        builder.addText(defaultWordRender).addBackground();
        //            builder.addText(new NumbersAnswerProducer(new Random().nextInt(2) + 4), coloredEdgesWordRender);//Number Text
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
        builder.gimp(new DropShadowGimpyRenderer(2,50));

        //创建对象  
        Captcha captcha = builder.build();
        BufferedImage image = captcha.getImage();
        CaptchaServletUtil.writeImage(rsp, image);
        req.getSession().setAttribute(Captcha.NAME, captcha);
    }


    /**
     * 随机生成字体样式
     * <p/>
     * Blod:粗体样式
     * <p/>
     * Italic:斜体样式
     * <p/>
     * Plain:普通样式
     */
    private int fontStyleRandom() {
        int[] fontStyleArray = {Font.PLAIN, Font.BOLD, Font.ITALIC};
        return fontStyleArray[new Random().nextInt(fontStyleArray.length)];
    }

}
