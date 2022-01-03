package Jcaptcha;

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
