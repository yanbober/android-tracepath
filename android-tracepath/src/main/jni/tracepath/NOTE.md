# 移植来源

https://android.googlesource.com/platform/external/iputils/+/refs/heads/master/tracepath6.c

# 移植变更

1. 去掉其 main 函数，改为普通函数 tracepath 供外部调用，提供头文件。
2. 宏替换 printf 函数为 callbackOnUpdate 函数。
3. `sa_family_t family = AF_INET6;` 替换为 `sa_family_t family = AF_UNSPEC;` 支持 ipv4 和 ipv6 自动识别。
4. 去除针对 family 变量的强制判断逻辑为自适应。
