# Kotlin Android Util
ComposeUtils is a Kotlin compose tool library that simplifies Android development, making the code more concise and readable.

[简体中文🇨🇳](README.md)

[![Jitpack](https://jitpack.io/v/peihua8858/ComposeUtils.svg)](https://github.com/peihua8858)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen.svg)](https://github.com/peihua8858)
[![Star](https://img.shields.io/github/stars/peihua8858/ComposeUtils.svg)](https://github.com/peihua8858/ComposeUtils)


## Contents
-[Latest version](https://github.com/peihua8858/ComposeUtils/releases/latest)<br>
-[Download](#Download)<br>
-[Usage](#Usage)<br>
-[Permission](#Permission)<br>
-[Issues](https://github.com/peihua8858/ComposeUtils/wiki/%E5%A6%82%E4%BD%95%E6%8F%90Issues%3F)<br>
-[License](#License)<br>


## Download

Use Gradle

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

Or Maven:

```xml
<dependency>
  <groupId>com.github.peihua8858</groupId>
  <artifactId>ComposeUtils</artifactId>
  <version>${latestVersion}</version>
</dependency>
```
## Permission
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## Usage

A simple use case is shown below:

1、Check String is null

```kotlin
import com.peihua.isNonEmpty
val spu:String? =null 
var result:String=""
if (spu.isNonEmpty()) {
    result = spu
}
```

2、Check List or Map is nonNull
```kotlin
//List
import com.peihua.collections.isNonEmpty
//Array 
import com.peihua.array.isNonEmpty

val list:List<String>? =null 
var result:List<String> = arrayListOf()
if (list.isNonEmpty()) {
    result = list
}

//Map 
import com.peihua.map.isNonEmpty
val map:Map<String,String>? =null 
var result:Map<String,String> = hashMapOf()
if (map.isNonEmpty()) {
    result = map
}
```
3、use ContentProvider save image to sdcard
```kotlin
import com.peihua.utils.saveImageToGallery
val imageFile = File("D://images/5.jpg")
context.saveImageToGallery(imageFile, imageFile.name)
```
4、Network status
```kotlin
//kotlin  or java
import com.peihua.network.NetworkUtil
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
