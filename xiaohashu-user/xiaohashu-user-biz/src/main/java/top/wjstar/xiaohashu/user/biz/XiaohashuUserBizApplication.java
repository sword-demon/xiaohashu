package top.wjstar.xiaohashu.user.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.wjstar.xiaohashu.user.biz.domain.mapper")
public class XiaohashuUserBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohashuUserBizApplication.class, args);
    }
}
