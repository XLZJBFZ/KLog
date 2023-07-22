# KLog
使用Kotlin编写的更方便的Log

## 主要功能
enable - 开关Log  
logtype - log类型  
tag - log的tag  
divider - log上下的分割线 ，可为空，空则没有分割线
connectionSymbol - log中description和content的连接符号  
converter - content转化为String的方法  
globalConfig - 全局配置以上参数  
with - 添加description和content  

## 使用示例
```kotlin
KLog.globalConfig {
    enable = true
    logType = KLog.LogType.DEBUG
    tag = "debug"
    divider = null
    connectionSymbol = ":"
    converter = { any ->
        any.toString()
    }
}
KLog.log {
    enable = true
    logType = KLog.LogType.DEBUG
    tag = "debug"
    divider = "-".repeat(50)
    connectionSymbol = "->"
    converter = { any ->
        any.toString()
    }
    "当前时间" with Date(System.currentTimeMillis())
}
```

## 在Logcat的输出结果
```
D/debug: --------------------------------------------------
D/debug: 当前时间 -> Sat Jul 22 10:05:08 GMT+08:00 2023
D/debug: --------------------------------------------------
```

## 添加依赖
```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```
```
dependencies {
	implementation 'com.github.XLZJBFZ:KLog:1.0.4'
}
```
