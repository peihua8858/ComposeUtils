# Kotlin Android Util
ComposeUtils是一个Kotlin compose工具库，可以简化Android开发，使代码更加简洁和可读。

[English](README_EN.md)

[![Jitpack](https://jitpack.io/v/peihua8858/ComposeUtils.svg)](https://github.com/peihua8858)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen.svg)](https://github.com/peihua8858)
[![Star](https://img.shields.io/github/stars/peihua8858/ComposeUtils.svg)](https://github.com/peihua8858/ComposeUtils)


## 目录
-[最新版本](https://github.com/peihua8858/ComposeUtils/releases/latest)<br>
-[如何引用](#如何引用)<br>
-[进阶使用](#进阶使用)<br>
-[权限](#权限)<br>
-[如何提Issues](https://github.com/peihua8858/ComposeUtils/wiki/%E5%A6%82%E4%BD%95%E6%8F%90Issues%3F)<br>
-[License](#License)<br>


## 如何引用
* 把 `maven { url 'https://jitpack.io' }` 加入到 repositories 中
* 添加如下依赖，末尾的「latestVersion」指的是kotlinCommonUtils [![Download](https://jitpack.io/v/peihua8858/ComposeUtils.svg)](https://jitpack.io/#peihua8858/ComposeUtils) 里的版本名称，请自行替换。
使用 Gradle

```sh
repositories {
  google()
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  // ComposeUtils
  implementation 'com.github.peihua8858:ComposeUtils:${latestVersion}'
}
```

或者 Maven:

```xml
<dependency>
  <groupId>com.github.peihua8858</groupId>
  <artifactId>ComposeUtils</artifactId>
  <version>${latestVersion}</version>
</dependency>
```
## 权限
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## 进阶使用

一个简单的用例如下所示：

1、判断字符串是否为空

```kotlin
import com.peihua8858.utils.isNonEmpty
val spu:String? =null 
var result:String=""
if (spu.isNonEmpty()) {
    result = spu
}
```

2、判断List、Map或者Array 是否为空
```kotlin
//List
import com.peihua8858.collections.isNonEmpty
//Array 
import com.peihua8858.array.isNonEmpty

val list:List<String>? =null 
var result:List<String> = arrayListOf()
if (list.isNonEmpty()) {
    result = list
}

//Map 
import com.peihua8858.map.isNonEmpty
val map:Map<String,String>? =null 
var result:Map<String,String> = hashMapOf()
if (map.isNonEmpty()) {
    result = map
}
```
3、使用ContentProvider保存图片文件到sd卡
```kotlin
import com.peihua8858.utils.saveImageToGallery
val imageFile = File("D://images/5.jpg")
context.saveImageToGallery(imageFile, imageFile.name)
```
4、网络状态
```kotlin
//kotlin  or java
import com.peihua8858.network.NetworkUtil
if (NetworkUtil.isConnected(context, true)) {
     showToast("Internet connection.")
}else{
    showToast("Disconnected from the network. ")
}
```
## License

```sh
Copyright 2025 peihua

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
