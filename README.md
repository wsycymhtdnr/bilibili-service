# Bilibili Service

## 配置说明

### 数据库配置

1. 复制 `src/main/resources/application-example.properties` 为 `src/main/resources/application.properties`
2. 修改 `application.properties` 中的数据库配置：
   - `spring.datasource.url`: 数据库连接地址
   - `spring.datasource.username`: 数据库用户名
   - `spring.datasource.password`: 数据库密码

**注意：** `application.properties` 文件已被添加到 `.gitignore`，不会被提交到 Git 仓库，请妥善保管你的配置文件。

### 环境变量配置（推荐）

你也可以通过环境变量 `DB_PASSWORD` 来设置数据库密码，这样更安全：

```bash
export DB_PASSWORD=your_actual_password
```

Windows PowerShell:
```powershell
$env:DB_PASSWORD="your_actual_password"
```

