package pkg_Selector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Sophie on 15/7/6.
 */
public class Selector {
    private WebDriver dr;
    private String url;
    private String account;
    private String pwd;

    public Selector() {
        this.url = "https://www.wacai.com/user/user.action?url=http%3A%2F%2Fbbs.wacai.com%2Fportal.php";
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the account");
        this.account = input.next();
        System.out.println("Please input the password");
        this.pwd = input.next();
        this.dr = new FirefoxDriver();
    }


    public void login( ) throws Exception {
        //登录
        dr.get(url);
        dr.findElement(By.id("account")).clear();
        dr.findElement(By.id("account")).sendKeys(account);
        dr.findElement(By.id("pwd")).clear();
        dr.findElement(By.id("pwd")).sendKeys(pwd);
        dr.findElement(By.id("login-btn")).click();
    }

    public void switchWindow() throws Exception {
        //打开理财规划子版块
        String a = dr.findElement(By.linkText("理财规划")).getAttribute("href");
        //直接用WebDriver.get()方式打开页面，浏览器不会新开页面，省得切换窗口
        dr.get(a);
        dr.manage().window().maximize();
        //然后关闭左侧导航栏
        dr.findElement(By.cssSelector("a.comiis_left_closes")).click();
    }

    public void mouseMove() throws Exception {

        //点开搜索类型下拉框，将鼠标移动到用户上并选择
        Actions act = new Actions(dr);
        WebElement dropDown = dr.findElement(By.cssSelector("a.showmenu.xg1"));
        WebElement user = dr.findElement(By.cssSelector("ul#comiis_twtsc_type_menu>li>a[rel='user']"));
        act.click(dropDown).perform();
        act.moveToElement(user).click().perform();
        act.moveByOffset(20, 30).click().perform();
    }

    public void search() throws Exception {
        //找到搜索输入框，并输入关键字，然后点击搜索按钮
        WebElement input = dr.findElement(By.id("comiis_twtsc_txt"));
        input.clear();
        input.sendKeys("周杰伦");
        dr.findElement(By.id("comiis_twtsc_btn")).click();//Click之后FireFox新开了一个页面

        //用WindowHandle+页面title来切换dr至我们想要的窗口
        String currentWindow = dr.getWindowHandle();
        Set<String> handles = dr.getWindowHandles();
        //System.out.println(handles.size());
        Iterator h = handles.iterator();
        while (h.hasNext()) {
            dr = dr.switchTo().window((String) h.next());
            //System.out.println(dr.getTitle());
            if (dr.getTitle().equals("查找好友 - 挖财社区"))
                break;
        }
    }

    public WebDriver select() throws InterruptedException {
        dr.findElement(By.cssSelector("html>body#nv_home.pg_spacecp.sour>div#wp.wp.cl>div#ct.ct2_a.wp.cl>div.mn>div.bm.bw0>ul.tb.cl>li:nth-child(2)>a")).click();
        /*用普通定位+click方式处理下拉框
        dr.findElement(By.xpath("//select[@id='resideprovince']/option[@value='浙江省']")).click();
        Thread.sleep(2000);
        dr.findElement(By.xpath("//select[@id='residecity']/option[@value='杭州市']")).click();
        */
        //用Select方式
        Select sProvince = new Select(dr.findElement(By.xpath("//select[@id='resideprovince']")));
        sProvince.selectByValue("浙江省");
        Thread.sleep(2000);
        Select sCity = new Select(dr.findElement(By.xpath("//select[@id='residecity']")));
        sCity.selectByValue("杭州市");
        dr.findElement(By.cssSelector("button.pn")).click();
        return dr;
    }

    public void tearDown(WebDriver dr) throws Exception {
        dr.quit();
    }
    public static void main(String[] args) throws Exception {
        Selector s = new Selector();
        s.login();
        s.switchWindow();
        s.mouseMove();
        s.search();
        WebDriver selectPage = s.select();
        //xpath模糊查找
        List<WebElement> searchResult = selectPage.findElements(By.cssSelector("li.bbda.cl"));
        System.out.println("搜索到" + searchResult.size() + "个同城用户，他们是：");
        System.out.printf("%-20s %-20s %-20s\n", "用户名", "用户等级", "用户积分");
        String userName;
        String userLevel;
        String userMark = "";
        String[] sArray;
        for (int i = 0; i < searchResult.size(); i++) {
            sArray = searchResult.get(i).findElement(By.cssSelector("li>p")).getText().split(" ");
            if (sArray.length < 2)
                userMark = "0";
            else
                userMark = sArray[3];
            userLevel = sArray[0];
            userName = searchResult.get(i).findElement(By.cssSelector("h4>a")).getText();
            System.out.printf("%-20s %-20s %-20s\n",userName,userLevel,userMark);
        }
    }
}
