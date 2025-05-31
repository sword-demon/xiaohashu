package top.wjstar.xiaohashu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 填写 mapper 接口所处的包的路径
@MapperScan("top.wjstar.xiaohashu.auth.domain.mapper")
public class XiaohashuAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohashuAuthApplication.class, args);
    }
}
