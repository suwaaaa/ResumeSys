在CentOS 7上安装Docker并运行MySQL容器可以按以下步骤进行操作：

1. 安装Docker：
如果您尚未在CentOS 7上安装Docker，请按照以下步骤进行安装：

首先，更新包管理器的软件包列表：
```
sudo yum update
```

安装Docker所需的依赖：
```
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

添加Docker的官方仓库：
```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

安装Docker引擎：
```
sudo yum install docker-ce
```

启动Docker并设置开机自启动：
```
sudo systemctl start docker
sudo systemctl enable docker
```

2. 拉取并运行MySQL容器：
接下来，使用Docker拉取并运行MySQL容器。您可以使用Docker Hub上的官方MySQL镜像来运行MySQL容器。在这里，我们将MySQL的root密码设置为"your_mysql_root_password"，您可以根据需要将其替换为自己的密码。

```
sudo docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=your_mysql_root_password -p 3306:3306 mysql:latest
```

这将拉取最新版本的MySQL镜像并在容器中运行MySQL服务。MySQL将通过主机的3306端口进行访问。

3. 验证MySQL容器是否正常运行：
使用以下命令可以验证MySQL容器是否正在运行：
```
sudo docker ps
```

如果MySQL容器正在运行，您应该能够在输出中看到该容器的信息。

4. 连接到MySQL容器：
使用MySQL客户端连接到MySQL容器。您可以在CentOS上安装MySQL客户端，并使用以下命令连接到MySQL容器（将"your_mysql_root_password"替换为您设置的实际密码）：
```
sudo yum install mysql   # 安装MySQL客户端
mysql -h 127.0.0.1 -P 3306 -u root -p
```

输入密码后，您应该能够成功连接到MySQL容器，并可以开始使用MySQL数据库。

请注意，在生产环境中，应该考虑对MySQL容器进行适当的配置和数据持久化，以确保数据的安全性和可靠性。以上步骤仅供参考，并假设您正在使用一个测试环境或学习目的。