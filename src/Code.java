import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

public class Code {//验证码实体类
    private final BufferedImage image;//验证码图
    private final String text;//验证码文本备选列表,用于大小写模糊匹配

    Code(BufferedImage image, String text) {
        this.image = image;
        this.text = text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public boolean equals(String input) {//验证码校验
        return text.equalsIgnoreCase(input);
    }

    public void saveImg(String outfile) {//保存图片
        File file = new File(outfile);
        if (!outfile.matches(".*\\..*")) {//缺失文件扩展名
            outfile = outfile + ".png";
        }
        String type = outfile.replaceAll(".*\\.", "");
        try {
            ImageIO.write(image, type, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
