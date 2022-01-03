package Jcaptcha;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;

/**
 * 各类与方法说明:
 *
 * Code:验证码实体类,可以对已生成的验证码进行操作;
 *   Code:构造函数,供生成方法调用,无需理会;
 *   getImage:获取该验证码对象的图片内容;
 *   getText:获取该验证码对象的文字内容;
 *   equals(String input):将该验证码与给定字符串作匹配(无视大小写),返回匹配结果;
 *   saveImg(String outfile):以给定文件名与路径输出验证码图片,便于其他程序的接入;
 *
 * NegativeLengthException:异常类:当验证码长度为负时触发;
 *
 * JCaptcha:验证码生成类,可通过参数生成验证码实体对象;
 *   createCode():创建验证码:重载形式1:创建一个4位的,可包含大小写字母与数字的验证码;
 *   createCode(int length):创建验证码:重载形式2:创建一个指定位数,可包含大小写字母与数字的验证码;
 *   createCode//创建验证码:重载形式3:可自定义验证码的生成长度与字符范围
 *     (int length,//长度
 *      boolean containsUpper,//包含大写
 *      boolean containsLower,//包含小写
 *      boolean containsNumber)//包含数字
 *   randomText(<参数同上>):生成一段符合要求的随机字符串
 *   randomBColor():生成随机背景颜色,以便利用随机颜色覆盖背景的每个像素点;
 *   randomFColor():生成随机文字颜色与干扰线颜色;
 *   drawPicture(String text):将字符串绘制为验证码图片,返回 BufferedImage 类型;
 *   main方法:无实际含义,供单元测试使用
 *
 * Test:测试类,提供各模块的测试方法,通过终端输入,并将结果输出至终端;
 *
 * ShowImage:借助图形化界面展示验证码图片,继承自JFrame;
 *
 * 用法:
 * 生成验证码:Code code = Jcaptcha.createCode(length, containsUpper, containsLower, containsNumber);
 *   length:int,验证码总长度;
 *   containsUpper:boolean,是否允许包含大写字母,true:允许,false:不允许,下同;
 *   containsLower:boolean,是否允许包含小写字母;
 *   containsNumber:是否允许包含数字;
 * 保存验证码图片:code.saveImg(outfile);
 *   code:Code类型对象;
 *   outfile:String,文件名(包含文件路径),请带上扩展名,否则一律按png格式处理;
 * 验证码校验:code.equals(input);
 *   code:Code类型对象;
 *   input:String,待校验的字符串,由用户输入;
 *   函数将在校验完成后返回boolean类型,true代表校验通过,false代表校验不通过
 *   按照当前的通行做法,验证码校验采取不区分大小写的方法
 */

class NegativeLengthException extends IllegalArgumentException {
    public NegativeLengthException() {
        super("length must be positive");
    }
}

public class Jcaptcha {//验证码控制类

    public static Code createCode() {//创建验证码,默认四位字符串
        return createCode(4);
    }

    public static Code createCode(int length) {//创建验证码,默认包含三种类型
        return createCode(length,
                true,
                true,
                true);
    }

    public static Code createCode//创建验证码
    (int length,//长度
     boolean containsUpper,//包含大写
     boolean containsLower,//包含小写
     boolean containsNumber)//包含数字
    {
        String text = randomText(length, containsUpper, containsLower, containsNumber);
        BufferedImage image = drawPicture(text);
        return new Code(image,text);
    }

    public static String randomText//创建随机字串
    (int length,//长度
     boolean containsUpper,//包含大写
     boolean containsLower,//包含小写
     boolean containsNumber)//包含数字
    {
        if (length <= 0) {
            throw new NegativeLengthException();//长度取值不为负
        }
        Random random = new Random();//随机数生成器
        StringBuilder text = new StringBuilder();//字串构造器
        for (int i = 0; i < length; ) {
            int type = random.nextInt(3);//类型选取
            if (type == 0 && containsUpper) {//大写
                text.append((char) (random.nextInt(26) + 65));
                i++;
            }
            if (type == 1 && containsLower) {//小写
                text.append((char) (random.nextInt(26) + 97));
                i++;
            }
            if (type == 2 && containsNumber) {//数字
                text.append(random.nextInt(10));
                i++;
            }
        }
        return text.toString();
    }

    public static Color randomBColor(){//生成随机背景色
        final int from = 32;//随机数最小值:代表背景底色亮度,数值越小颜色越杂,混淆程度越高
        final int range = 256-from;//随机数范围
        Random random = new Random();//随机数生成器
        return new Color(from + random.nextInt(range),from + random.nextInt(range),from + random.nextInt(range));
    }

    public static Color randomFColor(){//生成随机前景色
        final int to = 96;//随机数最大值:代表文字亮度,数值越大文字越浅,混淆程度越高
        Random random = new Random();//随机数生成器
        return new Color(random.nextInt(to),random.nextInt(to),random.nextInt(to));
    }

    public static BufferedImage drawPicture(String text){//绘制验证码
        int width = text.length()*25;//宽,根据字串长度自动放缩
        int height = 50;//高

        //建立图片对象
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //画笔初始化
        Graphics g = image.getGraphics();
        Font font = new Font("Cascadia Code",Font.BOLD,22);//设置字体
        //g.getFontMetrics(font).getHeight();自动根据字体放缩图片(暂未实现)
        //g.getFontMetrics(font).stringWidth(text);
        g.setFont(font);//设置字体

        //填充背景色
        //g.setColor(Color.WHITE);//纯色方案(已弃用)
        //g.fillRect(0,0,width-1,height-1);
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                g.setColor(randomBColor());//随机杂色绘制,调整随机函数参数可调节画面效果
                g.drawLine(x,y,x,y);
            }
        }

        //绘制边框
        g.setColor(Color.BLACK);
        g.drawRect(0,0,width-1,height-1);

        //绘制文本
        Random random = new Random();
        for(int i=0;i<text.length();i++){
            g.setColor(randomFColor());//随机前景色,调整参数可调节画面效果
            int x = width/text.length()*i//均分图片宽度
                    +random.nextInt(11);//水平间距随机
                    //+ g.getFontMetrics(font).getWidths()[0]/2;//加一半的字宽
            //int y = height/4*3;//四分之三的图片高度
            int y = height/3
                    +random.nextInt(33);//垂直位置随机
            g.drawString(String.valueOf(text.charAt(i)),x,y);
        }

        //添加干扰线
        g.setColor(randomFColor());
        g.drawLine(0,random.nextInt(height/2),width,height/2+random.nextInt(height/2));//左上到右下,确保干扰线覆盖
        g.drawLine(0,height/2+random.nextInt(height/2),width,random.nextInt(height/2));//右上到左下,确保干扰线覆盖

        return image;
    }

    public static void main(String[] args) {
        //Test.testRandomText();
        //Test.testDrawPicture();
        //Test.testCreateCode();
        //Test.testEquals();
        Test.testEqualsQuick();
        //Test.testType();
        //Test.testJcaptcha();
    }
}

class Test {//测试类

    public static void testRandomText() {//测试随机字串生成器
        System.out.println("input length[int], containsUpper[boolean], containsLower[boolean] and containsNumber[boolean]");
        System.out.println("e.g. 4 true true true");
        Scanner scn = new Scanner(System.in);
        System.out.println(Jcaptcha.randomText(scn.nextInt(), scn.nextBoolean(), scn.nextBoolean(), scn.nextBoolean()));
    }

    public static void testDrawPicture(){//测试验证码图绘制
        System.out.println("input test String");
        System.out.println("e.g. o0O6");
        Scanner scn = new Scanner(System.in);
        String testtext = scn.nextLine();
        int width = testtext.length()*25;//宽,根据字串长度自动放缩
        int height = 50;//高
        Image img = Jcaptcha.drawPicture(testtext);
        new ShowImage(width,height,img);
    }

    public static void outFontList(){//输出系统字体列表
        java.awt.GraphicsEnvironment eq = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = eq.getAvailableFontFamilyNames();
        for(String font:fontNames){
            System.out.println(font);
        }
    }

    public static void testCreateCode(){//测试验证码生成器整体
        System.out.println("input length[int], containsUpper[boolean], containsLower[boolean] and containsNumber[boolean]");
        System.out.println("e.g. 4 true true true");
        Scanner scn = new Scanner(System.in);
        int len = scn.nextInt();
        Code code = Jcaptcha.createCode(len, scn.nextBoolean(), scn.nextBoolean(), scn.nextBoolean());
        System.out.println(code.getText());//展示文字
        int width = len*25;//宽,根据字串长度自动放缩
        int height = 50;//高
        new ShowImage(width,height,code.getImage());//展示图片
    }

    public static void testEquals(){//测试验证码生成与校验
        System.out.println("input length[int], containsUpper[boolean], containsLower[boolean] and containsNumber[boolean]");
        System.out.println("e.g. 4 true true true");
        Scanner scn = new Scanner(System.in);
        int len = scn.nextInt();
        Code code = Jcaptcha.createCode(len, scn.nextBoolean(), scn.nextBoolean(), scn.nextBoolean());
        int width = len*25;//宽,根据字串长度自动放缩
        int height = 50;//高
        new ShowImage(width,height,code.getImage());//展示图片
        System.out.println("input the Code in the Pic:");
        String input = scn.next();
        System.out.println(code.equals(input));
    }

    public static void testEqualsQuick(){//测试验证码生成与校验:简化版,无需输入生成参数
        int len = 4;
        Code code = Jcaptcha.createCode(len, true, true, true);
        int width = len*25;//宽,根据字串长度自动放缩
        int height = 50;//高
        new ShowImage(width,height,code.getImage());//展示图片
        System.out.println("input the Code in the Pic:");
        Scanner scn = new Scanner(System.in);
        String input = scn.next();
        System.out.println(code.equals(input));
        System.out.println(code.getText());
    }

    public static void testType(){//测试文件保存正则式
        System.out.println("test filepath:");
        Scanner scn = new Scanner(System.in);
        String outfile = scn.next();
        if(!outfile.matches(".*\\..*")){
            outfile=outfile+".png";
        }
        String type = outfile.replaceAll(".*\\.","");
        System.out.println("type is:\""+type+"\"");
        System.out.println("path is:\""+outfile+"\"");
    }

    public static void testJcaptcha(){
        System.out.println("input length[int], containsUpper[boolean], containsLower[boolean] and containsNumber[boolean]");
        System.out.println("e.g. 4 true true true");
        Scanner scn = new Scanner(System.in);
        int len = scn.nextInt();
        Code code = Jcaptcha.createCode(len, scn.nextBoolean(), scn.nextBoolean(), scn.nextBoolean());
        int width = len*25;//宽,根据字串长度自动放缩
        int height = 50;//高
        System.out.println("input savename:");
        String outfile = scn.next();
        code.saveImg(outfile);
        new ShowImage(width,height,code.getImage());//展示图片
        System.out.println("input the Code in the Pic:");
        String input = scn.next();
        System.out.println(code.equals(input));
    }
}

class ShowImage extends JFrame{//图片预览类
    public ShowImage(int width,int height,Image img){
        super("预览");
        setSize(width*6/5,height*2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel view = new JLabel(new ImageIcon(img));
        getContentPane().add(view);
        setVisible(true);
    }
}