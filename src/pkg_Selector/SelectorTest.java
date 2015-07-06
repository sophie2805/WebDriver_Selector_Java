package pkg_Selector;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.*;

/**
 * Created by Sophie on 15/7/6.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SelectorTest {
    private static Selector s;
    private static WebDriver afterLogin;
    private static WebDriver afterSwitch;
    private static WebDriver aftermouseMove;
    private static WebDriver afterSearch;
    private static WebDriver afterSelect;

    @BeforeClass
    public static void setUp() throws Exception {
        s = new Selector();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        s.tearDown();
    }

    @Test
    public void test1_login() throws Exception {
        afterLogin = s.login();
        assertEquals("挖财社区，国内最大的理财社区", afterLogin.getTitle());
    }
     @Test
    public void test2_switchWindow() throws Exception {
         afterSwitch = s.switchWindow();
         assertEquals("理财规划_理财学院_挖财社区，国内最大的理财社区，爱理财的人都在这里",afterSwitch.getTitle());
     }
    @Test
    public void test3_mouseMove() throws Exception {
        aftermouseMove = s.mouseMove();
        assertEquals("用户",aftermouseMove.findElement(By.cssSelector("a.showmenu.xg1")).getText());
    }
    @Test
    public void test4_search() throws Exception {
        afterSearch = s.search();
        assertEquals("查找好友 - 挖财社区",afterSearch.getTitle());
    }
    @Test
    public void test5_select() throws InterruptedException {
        afterSelect = s.select();
        assertEquals(true,afterSelect.getPageSource().contains("以下是查找到的用户列表(最多显示 100 个)"));
    }

}
