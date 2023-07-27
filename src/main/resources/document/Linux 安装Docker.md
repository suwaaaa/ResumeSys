## Linux 安装Docker

安装Docker在Linux系统上通常是非常简单的。以下是在常见Linux发行版（例如Ubuntu、Debian和CentOS）上安装Docker的基本步骤：

1. 卸载旧版本（如果适用）：
如果您之前安装过旧版本的Docker，建议先卸载它们。可以使用以下命令卸载旧版本的Docker：
```
sudo apt-get remove docker docker-engine docker.io containerd runc   # Ubuntu/Debian
sudo yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-engine   # CentOS
```

2. 更新包管理器（可选）：
虽然不是必需的，但通常推荐在安装Docker之前更新您的包管理器的软件包列表。可以使用以下命令执行此操作：
```
sudo apt-get update   # Ubuntu/Debian
sudo yum update   # CentOS
```

3. 安装Docker：
接下来，使用包管理器来安装Docker。请根据您的Linux发行版选择相应的命令：

对于Ubuntu和Debian：
```
sudo apt-get install docker.io
```

对于CentOS：
```
sudo yum install docker
```

4. 启动Docker并设置开机启动：
安装完成后，启动Docker服务并将其设置为开机自启动。使用以下命令完成这些操作：
```
sudo systemctl start docker
sudo systemctl enable docker
```

5. 验证Docker安装是否成功：
输入以下命令，如果安装成功，您将看到Docker的版本信息：
```
docker --version
```

6. 授予非特权用户访问Docker（可选）：
默认情况下，只有root用户和具有sudo权限的用户才能访问Docker。如果您希望其他普通用户也能够访问Docker，可以将用户添加到`docker`用户组：
```
sudo usermod -aG docker your_username
```
在上面的命令中，将`your_username`替换为您要添加到docker用户组的实际用户名。

重启您的终端会话，或注销并重新登录，以便新的用户组设置生效。

至此，您应该已经成功地在Linux系统上安装了Docker。现在您可以开始使用Docker运行容器了。记得查阅Docker文档以了解更多命令和用法。



### 修改yum 清华大学的镜像地址

1. 备份现有的YUM源配置文件（可选）： 在修改YUM源之前，建议先备份现有的配置文件，以防意外情况发生。可以使用以下命令备份：

```bash
sudo cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
```

1. 使用编辑器打开YUM源配置文件： 使用文本编辑器（如`vi`或`nano`）打开YUM源配置文件。以下是使用`nano`编辑器打开的示例命令：

```bash
sudo nano /etc/yum.repos.d/CentOS-Base.repo
```

1. 修改镜像地址： 在打开的文件中，找到以`baseurl`开头的行和以`gpgcheck`开头的行。将它们替换为清华大学的镜像地址。请注意，下面的示例命令是为CentOS 7和清华大学的镜像地址而设计的。如果您的Linux发行版或版本不同，镜像地址可能会有所不同。

对于CentOS 7：

```bash
[base]
name=CentOS-$releasever - Base - Tuna
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos/$releasever/os/$basearch/
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7


//已修改全部：
[base]
name=CentOS-$releasever - Base
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos/$releasever/os/$basearch/
#mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=os
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
 
#released updates
[updates]
name=CentOS-$releasever - Updates
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos/$releasever/updates/$basearch/
#mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=updates
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
 
#additional packages that may be useful
[extras]
name=CentOS-$releasever - Extras
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos/$releasever/extras/$basearch/
#mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=extras
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
 
#additional packages that extend functionality of existing packages
[centosplus]
name=CentOS-$releasever - Plus
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos/$releasever/centosplus/$basearch/
#mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=centosplus
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
```

1. 保存并退出编辑器： 在`nano`编辑器中，按`Ctrl + X`，然后按`Y`以确认保存所做的更改。
2. 清除YUM缓存： 运行以下命令以清除YUM缓存，以便下次使用新的镜像地址：

```bash
sudo yum clean all
```

1. 生成YUM缓存： 更新YUM缓存以使用新的清华大学镜像地址：

```bash
sudo yum makecache
```