# Spring Data R2DBC Repository Demo ⚡

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![R2DBC](https://img.shields.io/badge/R2DBC-1.0.0-blue.svg)](https://r2dbc.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 專案介紹

這是一個 **Spring Data R2DBC Repository** 的實作範例專案，示範如何使用反應式程式設計（Reactive Programming）來處理資料庫操作。專案以咖啡店的點餐系統為背景，展示 R2DBC（Reactive Relational Database Connectivity）的核心功能。

- **核心功能**：展示 R2DBC Repository 模式的實作與應用
- **解決問題**：傳統 JDBC 阻塞式 I/O 的效能瓶頸，提供非阻塞式資料庫存取
- **主要特色**：完全非阻塞的資料庫操作、反應式資料流處理、自訂型別轉換器
- **目標使用者**：想要學習 Spring Data R2DBC 和反應式程式設計的開發者

> 💡 **為什麼選擇 R2DBC？**
> - **高效能**：非阻塞式 I/O 操作，適合高併發場景
> - **反應式支援**：原生支援 Reactor 響應式程式設計框架
> - **Spring 生態整合**：與 Spring Boot 和 Spring Data 完美整合

### 🎯 專案特色

- ✅ **完整的 R2DBC Repository 實作**：展示基本 CRUD 和自訂查詢
- ✅ **自訂型別轉換器**：示範 Money 類型的讀寫轉換
- ✅ **反應式程式設計**：使用 Flux 和 Mono 處理資料流
- ✅ **記憶體資料庫**：使用 H2 資料庫，方便測試和學習

## 技術棧

### 核心框架
- **Spring Boot 3.4.5** - 主要應用程式框架
- **Spring Data R2DBC 3.4.5** - 反應式資料庫存取框架
- **Project Reactor** - 反應式程式設計核心函式庫
- **R2DBC H2 1.0.0** - H2 資料庫的反應式驅動程式

### 開發工具與輔助
- **Java 21** - 開發語言
- **Maven** - 專案建置工具
- **Lombok** - 減少樣板程式碼
- **Joda Money** - 金錢數值處理函式庫
- **H2 Database** - 記憶體關聯式資料庫

## 專案結構

```
r2dbc-repository-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── tw/fengqing/spring/data/reactive/r2dbc/
│   │   │       ├── config/
│   │   │       │   └── R2dbcConfiguration.java          # R2DBC 設定與轉換器註冊
│   │   │       ├── converter/
│   │   │       │   ├── MoneyReadConverter.java          # Money 讀取轉換器
│   │   │       │   ├── MoneyWriteConverter.java         # Money 寫入轉換器
│   │   │       │   ├── LocalDateTimeToDateConverter.java
│   │   │       │   └── DateToLocalDateTimeConverter.java
│   │   │       ├── model/
│   │   │       │   └── Coffee.java                      # 咖啡實體類別
│   │   │       ├── repository/
│   │   │       │   └── CoffeeRepository.java            # 咖啡資料存取介面
│   │   │       └── R2dbcRepositoryDemoApplication.java  # 主程式入口點
│   │   └── resources/
│   │       ├── application.properties                   # 應用程式設定
│   │       ├── schema.sql                              # 資料庫結構定義
│   │       └── data.sql                                # 初始資料
│   └── test/
├── pom.xml                                             # Maven 專案設定
└── README.md
```

## 快速開始

### 前置需求
- **Java 21** 或更高版本
- **Maven 3.6** 或更高版本
- 支援 R2DBC 的 IDE（建議使用 IntelliJ IDEA 或 Visual Studio Code）

### 安裝與執行

1. **克隆此倉庫：**
```bash
git clone https://github.com/SpringMicroservicesCourse/r2dbc-repository-demo.git
```

2. **進入專案目錄：**
```bash
cd r2dbc-repository-demo
```

3. **編譯專案：**
```bash
# 清理並編譯專案
mvn clean compile
```

4. **執行應用程式：**
```bash
# 啟動 Spring Boot 應用程式
mvn spring-boot:run
```

執行成功後，您會看到類似以下的輸出：
```
Find espresso-TWD 100.00
Find latte-TWD 125.00
Find Coffee(id=4, name=mocha, price=TWD 150.00, 
createTime=2025-07-03T13:49:25.879591, updateTime=2025-07-03T13:49:25.879591)
```

## 程式碼重點說明

### 🔧 R2DBC 設定檔

```java
@Configuration
public class R2dbcConfiguration {
    
    /**
     * 註冊自訂轉換器，處理 Money 型別與資料庫欄位的轉換
     * 這是 R2DBC 自訂型別轉換的關鍵設定
     */
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.NONE,
                Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
    }
}
```

### 💰 Money 轉換器實作

```java
/**
 * 將資料庫的 Long 數值轉換為 Money 物件
 * 使用台幣（TWD）作為預設貨幣單位
 */
public class MoneyReadConverter implements Converter<Long, Money> {
    @Override
    public Money convert(Long aLong) {
        return Money.ofMinor(CurrencyUnit.of("TWD"), aLong);
    }
}

/**
 * 將 Money 物件轉換為資料庫儲存的 Long 數值
 * 以最小貨幣單位（分）儲存
 */
public class MoneyWriteConverter implements Converter<Money, Long> {
    @Override
    public Long convert(Money money) {
        return money.getAmountMinorLong();
    }
}
```

### 📊 Repository 介面

```java
/**
 * 咖啡資料存取介面
 * 繼承 ReactiveCrudRepository 以獲得基本的 CRUD 操作
 */
@Repository
public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, Long> {
    
    /**
     * 根據咖啡名稱查詢，展示自訂查詢的使用
     * @param name 咖啡名稱
     * @return 符合條件的咖啡清單
     */
    @Query("SELECT * FROM t_coffee WHERE name = :name")
    Flux<Coffee> findByName(String name);
}
```

## 進階說明

### 環境變數
```properties
# R2DBC 連線設定（選用，預設使用 H2 記憶體資料庫）
R2DBC_URL=r2dbc:h2:mem:///testdb
R2DBC_USERNAME=sa
R2DBC_PASSWORD=

# 日誌等級設定
LOGGING_LEVEL_ROOT=INFO
```

### 設定檔說明
```properties
# application.properties 主要設定
# R2DBC 資料源設定
spring.r2dbc.url=r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;DB_CLOSE_ON_EXIT=FALSE
spring.r2dbc.username=sa
spring.r2dbc.password=

# SQL 初始化設定
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql

# R2DBC SQL 初始化
spring.r2dbc.init.mode=always
spring.r2dbc.init.schema-locations=classpath:schema.sql
spring.r2dbc.init.data-locations=classpath:data.sql

# 允許循環引用（解決特定版本相容性問題）
spring.main.allow-circular-references=true

# 除錯設定
logging.level.org.springframework.r2dbc=DEBUG
```

## 參考資源

- [Spring Data R2DBC 官方文件](https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/)
- [R2DBC 官方網站](https://r2dbc.io/)
- [Project Reactor 參考指南](https://projectreactor.io/docs/core/release/reference/)
- [Joda Money 使用指南](https://www.joda.org/joda-money/)

## 注意事項與最佳實踐

### ⚠️ 重要提醒

| 項目 | 說明 | 建議做法 |
|------|------|----------|
| 版本相容性 | Spring Data R2DBC 版本需與 Spring Data Commons 匹配 | 使用相同的版本號 3.4.5 |
| 型別轉換 | 實體類別的日期時間欄位型別 | 使用 LocalDateTime 而非 Date |
| 反應式程式設計 | 避免在反應式鏈中使用阻塞式操作 | 使用 Mono/Flux 的非阻塞式方法 |
| 連線池管理 | R2DBC 連線池設定 | 根據應用需求調整連線池參數 |

### 🔒 最佳實踐指南

- **型別安全**：使用強型別的 Money 類別處理金錢數值，避免精度問題
- **反應式設計**：善用 Flux 和 Mono 的操作符，如 map、filter、flatMap 等
- **錯誤處理**：使用 onErrorResume、onErrorReturn 等方法處理例外狀況
- **測試驗證**：編寫整合測試驗證 R2DBC Repository 的正確性
- **效能監控**：監控反應式操作的執行時間和資源消耗

## 故障排除

### 常見問題

**Q: 出現 LocalDateTime 轉換錯誤？**
A: 確保實體類別使用 `LocalDateTime` 而非 `java.util.Date`

**Q: 循環依賴錯誤？**
A: 在 application.properties 中設定 `spring.main.allow-circular-references=true`

**Q: Money 轉換不正確？**
A: 檢查 R2dbcConfiguration 是否正確註冊了 MoneyReadConverter 和 MoneyWriteConverter

## 授權說明

本專案採用 MIT 授權條款，詳見 LICENSE 檔案。

## 關於我們

我們主要專注在敏捷專案管理、物聯網（IoT）應用開發和領域驅動設計（DDD）。喜歡把先進技術和實務經驗結合，打造好用又靈活的軟體解決方案。

## 聯繫我們

- **FB 粉絲頁**：[風清雲談 | Facebook](https://www.facebook.com/profile.php?id=61576838896062)
- **LinkedIn**：[linkedin.com/in/chu-kuo-lung](https://www.linkedin.com/in/chu-kuo-lung)
- **YouTube 頻道**：[雲談風清 - YouTube](https://www.youtube.com/channel/UCXDqLTdCMiCJ1j8xGRfwEig)
- **風清雲談 部落格**：[風清雲談](https://blog.fengqing.tw/)
- **電子郵件**：[fengqing.tw@gmail.com](mailto:fengqing.tw@gmail.com)

---

**📅 最後更新：2025年7月3日**  
**👨‍💻 維護者：風清雲談團隊** 