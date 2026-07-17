# UMBRA — Development Environment Setup & Verification

Tài liệu này dành cho máy phát triển. Không cần Gradle toàn cục và không cần Python để build Fabric mod.

## Công cụ bắt buộc

| Công cụ | Phiên bản/chính sách | Cách xác minh |
|---|---|---|
| JDK | Java **25**, có cả `java` và `javac` | `java -version`, `javac -version` |
| Git | bất kỳ bản hiện đại | `git --version` |
| Minecraft dev target | Java Edition 26.2 / Fabric 26.2 | `runClient` sau M0-01 |
| Fabric Loader | 0.19.3 baseline | pin trong Gradle/release manifest |
| Fabric API | `0.154.2+26.2` baseline | Gradle dependency, không chép JAR tùy tiện vào source |

JDK phải là **JDK**, không phải JRE. Nếu `java -version` vẫn báo Java 8 dù JDK 25 đã cài, cấu hình biến môi trường người dùng:

```powershell
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-25.0.3', 'User')
$newPath = [Environment]::GetEnvironmentVariable('Path', 'User')
[Environment]::SetEnvironmentVariable('Path', "C:\Program Files\Java\jdk-25.0.3\bin;$newPath", 'User')
```

Đóng rồi mở lại PowerShell/Codex, sau đó hai lệnh `java -version` và `javac -version` đều phải trả Java 25. Không sửa `PATH` hệ thống nếu chỉ cần môi trường user.

## Những gì M0-01 sẽ tạo

- Gradle Wrapper 9.5.1; dùng `./gradlew`, không phụ thuộc Gradle cài toàn máy.
- Fabric Loom 1.17+ và Mojang mappings đúng nhánh 26.2.
- `runClient`, `build`, test/validation và output JAR.
- Dependency được Gradle resolve/pin. Internet cần thiết cho lần resolve đầu tiên.

## Công cụ tùy chọn nhưng nên có

| Công cụ | Mục đích | Có chặn M0? |
|---|---|---|
| IntelliJ IDEA Community | IDE Java/Gradle/Fabric tốt nhất | Không; VS Code vẫn dùng được |
| GitHub Actions hoặc CI tương đương | build/test mỗi PR | Không chặn bootstrap; cần trước M1 merge nhiều nhánh |
| Sodium + Iris | compatibility smoke test | Cần trước M0 gate, không cần ngay lúc tạo project |
| Spark | profile TPS/flamegraph | Cần trước M0 gate |
| Python | script phân tích/simulation tùy chọn | Không; chỉ thêm khi M6 cần simulation riêng |

## Không trộn hai cách chạy

- **Dev:** dùng `./gradlew runClient`; Gradle cung cấp dependency/runtime dev.
- **Chơi test bằng launcher:** copy JAR build và đúng Fabric API đã pin vào thư mục `mods`; backup save trước mọi build có migration.
- Không dùng OptiFine trong ma trận hỗ trợ. Sodium/Iris là ma trận compatibility chính thức.

## Checklist trước M0-01

- [ ] PowerShell mới trả `java` và `javac` phiên bản 25.
- [ ] `git status` sạch hoặc thay đổi hiện có đã được hiểu rõ.
- [ ] Không có instance Minecraft/TLauncher nào đang chạy khi test file `mods`.
- [ ] Có mạng cho lần đầu Gradle tải wrapper/dependency.
- [ ] Đồng ý để M0-01 tạo project build files trong repo.

