# Nova ERP Server

欢迎来到 Nova ERP 服务器端项目！此项目基于强大的开源项目**芋道（ruoyi-vue-pro）**构建，在此基础之上经过精简和聚焦，打造为高度纯粹的企业级 ERP（企业资源计划）解决方案。

当前项目去除了其他与 ERP 无关的边缘业务板块（如商城、AI、CRM 等），使代码体积更加小巧，结构更清晰，专注于满足企业内网或后台管理及 ERP 业务应用流转。

## 🎯 系统架构与技术栈

- **JDK 版本**：Java 17 （基于最新的 Spring Boot 3）
- **核心框架**：Spring Boot 3.5.x
- **持久层机制**：MyBatis Plus，支持多数据源隔离及读写分离保护
- **缓存及数据库**：支持 Redis 5.0+ 结合 Redisson 客户端，兼容 MySQL/PostgreSQL/Oracle 等主流关系型数据库
- **权限及安全机制**：基于 Spring Security + Token + Redis 的权限认证系统，支持方法级、按组织机构及租户维度的数据权限隔离

## 📁 项目模块详细说明

当前项目基于多模块 Maven 结构管理，重点保留了 ERP 系统的核心支撑模块：

### 1. 基础依赖与入口层
- **`nova-dependencies`（全局依赖管理）**：作为超级 POM，利用 `<dependencyManagement>` 统一锁定了所有的框架和第三方库版本，防止项目中出现版本冲突。
- **`nova-server`（应用服务主入口）**：存放项目启动类（`Application`）、项目级全局配置文件（`application.yml` 等），聚合其他所有业务模块最终打包为可执行的 jar 文件，并提供了容器化部署的 `Dockerfile` 配置。

### 2. 底层框架核心模块 (`nova-framework`)
该模块是对底座技术的二次封装，集成了各类 Spring Boot Starter，保障全系统各子模块拥有统一的技术底层支撑与开箱即用的特性：
- **`nova-common`**：系统通用工具类、全局异常处理、参数校验机制、通用基础配置（如 Web/Jackson 等相关的切面与配置）。
- **`starter-web`**：对 SpringMVC 进行了封装保护，包含过滤器链、XSS 防护网、Cors 全局跨域设定及 Swagger 各版本全自动的 API 接口文档集成。
- **`starter-security`**：深度封装了 Spring Security，提供 Token 的解析认证过滤、RBAC（基于角色的访问控制）基类拦截、密码加密及无状态鉴权等。
- **`starter-mybatis`**：封装 MyBatis-Plus，提供了多数据源配置支持及读写分离能力，以及公共的 BaseMapper、基础 Entity 等。
- **`starter-redis`**：针对 Redis 提供的缓存封装、操作接口及相关序列化模版。
- **`starter-mq`**：消息队列基础封装，支持通过配置文件无缝切换不同的 MQ 实现组件（如 Event / Kafka / RabbitMQ / RocketMQ / Redis）。
- **`starter-tenant`**：SaaS 多租户拦截隔离插件，能根据请求隐式对租户 ID 建立 ThreadLocal 传导并实施数据边界过滤。
- **`starter-data-permission`**：实现强大的数据级权限过滤，可细粒度地拦截并改写 SQL 来限定不同部门或人员仅能看到指定职责的数据。
- **`starter-protection`**：基于 Redisson 提供的分布式锁应用及限流、幂等性请求拦截，用于应用的安全防护。
- **`starter-job`**：为 Quartz 及异步任务调度提供集中化且简洁的声明式服务。
- **`starter-monitor`**：对接 Spring Boot Admin、各种 Actuator 端点监控以及提供 SkyWalking 链路追踪增强。
- **`starter-excel`**：集成 Alibaba EasyExcel 提供高性能的 Excel 文档导入、导出。
- **`starter-websocket`**：用于维持服务端端到端长连接、支撑前端实时通知的消息通道。

### 3. 应用层系统管理模块 (`nova-module-system`)
企业级系统的根基底座平台，管理应用权限及生态资产：
- **用户体系与组织架构**：用户、部门组织树、角色、岗位设置等基本管理体系。
- **菜单及权限中心**：按钮资源的细颗粒度控制、动态侧边栏路由解析分发。
- **租户空间管理**：SaaS 租户套餐维护及账号空间开启。
- **安全审计**：登录日志和管理所有正常操作请求的审计日志拦截存档。
- **通讯应用配置**：统筹站内信通知、参数配置、字典管理、短信模版管理并集成各主流 OAuth 云端三方登录授权等杂项服务。

### 4. 基础设施开发模块 (`nova-module-infra`)
面向系统维护人员的 DevOps 和研发强辅助工具：
- **文件与对象存储服务**：本地存储和分布式云存储（OSS、MinIO 等）统一接口封装和接入映射。
- **开放 API 管理平台**：Swagger 访问管理拦截、内部联调访问信息监控。
- **定时任务仪表盘**：管理所有的 Cron 调度任务配置更改以及作业执行日志存储检阅。
- **后端监控对接**：包含 Redis 及 MySQL 监控数据的抓取对接。

### 5. ERP 业务主线模块 (`nova-module-erp`)
在剥离了商城和其它边缘代码后，此模块是项目的核心引擎所在！业务功能具体细分为以下核心闭环：
- **商品与物料 (`product`)**：管理商品 SPUs 及 SKUs、企业基础物料信息、计量单位及分类规格参数等基础底表资源。
- **仓储及库存物流 (`stock` & `logistics`)**：盘点进出库历史，实施库存预警和调拨单审核，记录出入库流转动作及物流发货单追踪信息。
- **采购系统 (`purchase`)**：处理对上游供应商侧的采购工单、供应商名单考核等进货流向与记录。
- **销售与订单 (`sale`)**：负责管理下游客户的询盘、订单确认、销退售后明细以及客户档案等。
- **财务结算管理 (`finance`)**：围绕着销售回本、采购应付和各种其他杂费进行的凭证往来核销与台账清算。
- **报表和统计 (`statistics`)**：针对财务营收、仓库周转指标或者销售额度等进行宏观数据层面的指标抽取及大屏可视化接口供应。
- **AI 辅助 (`ai`)**：集成部分轻量的基于大语言模型的助理分析能力，辅助内部 ERP 单据分析及文本快捷回复预测等。

## 🚀 启动与部署

1. **环境准备**：JDK 17 环境、Redis 服务、以及本地数据库环境（如 MySQL 8+）。
2. 安装并编译：在根目录下执行 `mvn clean install` 完成全局依赖打包验证。
3. 初始化数据库：根据 `sql` 目录下找到对应的数据库类型的 SQL 脚本进行结构及初始化数据写入。
4. 修改配置文件：编辑 `nova-server` 下的 application 配置文件（主要验证数据库、Redis 密码）。
5. 启动服务：运行 `nova-server` 模块下的主启动类，应用启动后在控制台打印自动输出带有 API 接口文档路径的终端链接。

## 📜 协议声明

本项目架构基于并致敬 [ruoyi-vue-pro](https://github.com/YunaiV/ruoyi-vue-pro) ，该核心引擎代码原作者使用 MIT License 授权开源使用。



