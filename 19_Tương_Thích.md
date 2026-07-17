# 19 — TƯƠNG THÍCH (COMPATIBILITY)

> **Chủ sở hữu:** Technical Director
> **Mục tiêu:** UMBRA chơi tốt *một mình*, và chơi *lịch sự* trong hệ sinh thái Fabric — không ép người chơi chọn phe giữa UMBRA và bộ mod yêu thích của họ.

---

## 1. Ma trận phụ thuộc — baseline 26.2.x

| Loại | Mod/Thư viện | Chính sách |
|---|---|---|
| Nền tảng bắt buộc | Minecraft Java **26.2.x** + **Fabric Loader** | Đúng build được ghi trong release; baseline đã duyệt ban đầu là Loader 0.19.3+ |
| Bắt buộc | **Fabric API** | Cùng nhánh 26.2; baseline đã duyệt ban đầu là `0.154.2+26.2`, release pin chính xác bản đã test |
| Runtime dev/server | Java **25** | Client Launcher thường tự kèm runtime; dedicated server phải dùng Java 25+ đúng build MC |
| Tùy chọn (soft) | ModMenu + Cloth Config | Có thì config GUI, không thì file config |
| Tùy chọn | REI / EMI | Hiển thị công thức Đài Rèn Hắc Ảnh |
| Tùy chọn | Jade / WAILA | Tooltip chỉ số quái/bóng |
| Tương thích kỳ vọng | Sodium, Iris, Lithium, FerriteCore, ImmediatelyFast | **Không hook pipeline của chúng** — kiểm chứng trên từng bản pin, không tự nhận hỗ trợ nếu chưa smoke-test |
| Không can thiệp | Carpet, Create, mod nông trại/tự động | Chơi chung bình thường; quy tắc anti-AFK của UMBRA áp dụng riêng trong hệ thống UMBRA |

`Fabric Loader`, `Fabric API` và UMBRA phải cùng đúng nhánh Minecraft. Không dùng Fabric API “gần giống version”, không dùng NeoForge/Forge JAR, và không trộn snapshot với release nếu bảng release không nói rõ.

### 1.1. Hướng dẫn cài cho người chơi — phải xuất hiện nguyên văn trên trang phát hành

1. Sao lưu world/save trước khi thêm hoặc cập nhật mod.
2. Cài **Fabric Loader** đúng Minecraft Java `26.2.x` bằng Fabric Installer, rồi mở profile Fabric một lần để tạo thư mục `mods`.
3. Tải **đúng hai file bắt buộc cùng nhánh**: `umbra-<version>+mc26.2.jar` và `fabric-api-<đã-pin>+26.2.jar`; đặt cả hai vào thư mục `mods`. Không giải nén JAR.
4. Mở game bằng profile Fabric. Nếu màn hình Mod Menu/console báo sai MC, Loader hoặc Fabric API thì dừng ở đó — không mở save quan trọng với tổ hợp sai.
5. Tùy chọn sau khi game chạy ổn: ModMenu + Cloth Config (cấu hình); Sodium + Iris (hiệu năng/shader); REI/EMI và Jade/WAILA (thông tin). Shader pack là **tùy chọn của Iris**, không phải dependency của UMBRA.

Trang release phải có bảng “Bắt buộc / Tùy chọn / Không tương thích / Đã smoke-test”, checksum và đường dẫn hướng dẫn backup. Không được giấu một dependency trong crash log.

## 2. Quy tắc "lịch sự" (civil-mod rules)

1. **Namespace mọi thứ** (`umbra:*`) — ID, tag, command, packet, config.
2. **Không sửa hành vi vanilla ngoài phạm vi thiết kế:** combat rework chỉ áp dụng cho entity/người chơi *trong hệ thống UMBRA* khi combat stance bật; dân làng, vật nuôi, máy farm hoạt động y như cũ.
3. **Tag hóa thay vì hardcode:** loot/mob thêm qua tag để modpack tự điều chỉnh.
4. **Mixin tối thiểu + sổ đăng ký:** mỗi mixin ghi mục tiêu, lý do, rủi ro xung đột (tài liệu 16.1).
5. **Datapack-first:** server/modpack muốn cân bằng lại → sửa JSON, không cần fork mod.
6. **Config tôn trọng:** mọi cơ chế xâm lấn (world event, gate gần nhà, adaptive difficulty) tắt được.

## 3. Tương thích save & phiên bản

- Migration chain bảo toàn dữ liệu (tài liệu 15.9); cam kết **đọc được save từ mọi bản 1.x**.
- **Tương thích ngược của save:** trong cùng dòng 1.x, migration chain + test fixture là cam kết. Trước update và trước downgrade luôn backup; downgrade chỉ được phép khi release note có đường migration quay lui, nếu không phải restore backup.
- **Tương thích phiên bản Minecraft:** không có JAR Fabric an toàn nào tự chạy đồng thời trên API 26.2 và các API cũ/mới không tương thích. `compat-26.2` là baseline; mỗi backport hoặc port-forward là **build riêng**, ma trận dependency riêng và suite save/visual/regression riêng. Sau 1.0, ưu tiên backport các bản LTS/được cộng đồng dùng nhiều nếu chi phí không làm chậm bản chính; không hứa ngày hay nhánh chưa test.
- Forward compatibility MC: lớp adapter registry/render/network (16.1) giảm chi phí port. Mục tiêu nội bộ là đánh giá port trong 1 tuần sau Fabric ổn định và chỉ công bố lịch sau compatibility gate; không lấy mốc “≤4 tuần” làm cam kết marketing.

## 4. Multiplayer (khung, không cam kết 1.0)

- Thiết kế đã server-authoritative từ đầu (16.3.4) → dedicated server chạy được; nhưng cân bằng co-op (scale gate theo số người) là nội dung tương lai (23).
- Trên server: mỗi người chơi một Hệ Thống riêng; gate là tài nguyên chung — quy tắc "ai mở người đó hưởng, hỗ trợ chia theo đóng góp" (chi tiết ở 23).

## 5. Các xung đột đã biết & chính sách xử lý

| Xung đột tiềm ẩn | Chính sách |
|---|---|
| Mod combat khác (Better Combat...) | Không cài chung; UMBRA phát hiện và cảnh báo rõ, không crash |
| Mod tăng spawn rate | Ngân sách tick của UMBRA tự co (17.5); khuyến nghị config |
| Optifine | Không hỗ trợ (thay bằng Sodium + Iris) — ghi rõ trên trang mod |
| Mod thêm mob | Mob ngoài không có definition UMBRA → dùng AI profile "vanilla+" nhẹ, có thể Arise ở cấp thấp (theo phe gán qua tag) |

## 6. Rủi ro & Câu hỏi mở

1. **Bản MC mới đổi combat/render lớn?** → Lớp trừu tượng + ít mixin là bảo hiểm; chấp nhận có bản port chậm hơn nếu cần.
2. **Người chơi đòi Forge/NeoForge?** → Kiến trúc module tách logic khỏi nền tảng giúp port khả thi về sau; 1.0 chỉ Fabric (tập trung chất lượng).
3. **Modpack muốn nhúng UMBRA?** → Cho phép theo giấy phép ghi rõ; datapack-first giúp modpack tự cân bằng cho pack của họ.

---

## 7. Bổ sung v4.0 — chính sách Fabric, shader và bản phát hành

Mỗi release phát hành trên **một bản Minecraft stable hiện hành đã qua compatibility gate** với Fabric Loader/Fabric API tương ứng; “mới nhất” không được hiểu là cập nhật ngay ngày đầu khi dependency, shader và save migration chưa được test. `26.2.x` là baseline v1.0; bản target được chốt ở M0, cập nhật qua ADR và một nhánh port riêng; không trộn port nền tảng với tính năng gameplay. Toolchain 26.2 dùng Java 25, Loom 1.17+ và Gradle 9.5.1; kể từ 26.1 dùng Mojang mappings/plugin Fabric Loom mới thay vì giả định Yarn/remap cũ.

Sodium/Iris và shader pack là mục tiêu hỗ trợ chính thức theo nguyên tắc không hook render pipeline và **render qua Blaze3D, không raw OpenGL**. Có smoke test cho Gate, Domain, Arise VFX, dungeon nước và HUD ở vanilla, Sodium và Sodium+Iris, quality thấp/cao; lỗi shader được phân loại “UMBRA / Iris / pack” trước khi hứa sửa. OptiFine không hỗ trợ. Mod combat khác được phát hiện/cảnh báo theo capability; không crash, không âm thầm ghi đè binding hay damage pipeline. Keybind conflict scan và fallback layout là một phần compatibility test, không chỉ UX.
