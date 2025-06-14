# 仿小红书微服务项目学习

## 服务注册

> 服务注册是一种机制,用于将服务实例的信息(如地址,端口,健康状态等)注册到服务注册中心.
> 服务实例启动时,会向注册中心登记自己的信息,停止时则注销.

- 提供服务元数据:注册中心保存了所有服务实例的元数据,供其他服务或负载均衡器查询
- 健康检查:注册中心通常会定期检查注册的服务实例的健康状况,以确保它们可用并将不可用的实例从注册表中移除

## Docker 本地安装 Minio 对象存储

```bash
docker pull minio/minio:RELEASE.2023-09-30T07-02-29Z
```

在本地新建挂载目录

运行 Docker Minio 容器

```bash
docker run -d \
   -p 9000:9000 \
   -p 9090:9090 \
   --name minio \
   -v E:\docker\minio\data:/data \
   -e "MINIO_ROOT_USER=wujiedeyouxi" \
   -e "MINIO_ROOT_PASSWORD=wujiedeyouxi" \
   minio/minio:RELEASE.2023-09-30T07-02-29Z server /data --console-address ":9090"

```

密码一定要至少 8 位

浏览器进入地址: http://localhost:9090/browser

新建`Bucket`并设置`Change Access Policy`为`public`

