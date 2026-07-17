# 19 — TƯƠNG THÍCH (COMPATIBILITY)

> **Chủ sở hữu:** Technical Director
> **Mục tiêu:** UMBRA chơi tốt *một mình*, và chơi *lịch sự* trong hệ sinh thái Fabric — không ép người chơi chọn phe giữa UMBRA và bộ mod yêu thích của họ.

---

## 1. Ma trận phụ thuộc

| Loại | Mod/Thư viện | Chính sách |
|---|---|---|
| Bắt buộc | Fabric API | Duy nhất bắt buộc |
| Tùy chọn (soft) | ModMenu + Cloth Config | Có thì config GUI, không thì file config |
| Tùy chọn | REI / EMI | Hiển thị công thức Đài Rèn Hắc Ảnh |
| Tùy chọn | Jade / WAILA | Tooltip chỉ số quái/bóng |
| Tương thích kỳ vọng | Sodium, Iris, Lithium, FerriteCore, ImmediatelyFast | **Không hook, không xung đột** — kiểm chứng mỗi release |
| Không can thiệp | Carpet, Create, mod nông trại/tự động | Chơi chung bình thường; quy tắc anti-AFK của UMBRA áp dụng riêng trong hệ thống UMBRA |

## 2. Quy tắc "lịch sự" (civil-mod rules)

1. **Namespace mọi thứ** (`umbra:*`) — ID, tag, command, packet, config.
2. **Không sửa hành vi vanilla ngoài phạm vi thiết kế:** combat rework chỉ áp dụng cho entity/người chơi *trong hệ thống UMBRA* khi combat stance bật; dân làng, vật nuôi, máy farm hoạt động y như cũ.
3. **Tag hóa thay vì hardcode:** loot/mob thêm qua tag để modpack tự điều chỉnh.
4. **Mixin tối thiểu + sổ đăng ký:** mỗi mixin ghi mục tiêu, lý do, rủi ro xung đột (tài liệu 16.1).
5. **Datapack-first:** server/modpack muốn cân bằng lại → sửa JSON, không cần fork mod.
6. **Config tôn trọng:** mọi cơ chế xâm lấn (world event, gate gần nhà, adaptive difficulty) tắt được.

## 3. Tương thích save & phiên bản

- Migration chain bảo toàn dữ liệu (tài liệu 15.9); cam kết **đọc được save từ mọi bản 1.x**.
- Forward compatibility MC: lớp trừu tượng registry/render (16.1) giảm chi phí port; mục tiêu port sang bản MC mới trong ≤ 4 tuần sau khi Fabric API ổn định.
- Không cam kết backport (chính sách ghi rõ để tránh kỳ vọng sai).

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

## 7. Bổ sung v3.0 — chính sách Fabric, shader và bản phát hành

Mỗi release phát hành trên **một bản Minecraft stable hiện hành đã qua compatibility gate** với Fabric Loader/Fabric API tương ứng; “mới nhất” không được hiểu là cập nhật ngay ngày đầu khi dependency, shader và save migration chưa được test. Bản target được chốt ở M0, cập nhật qua ADR và một nhánh port riêng; không trộn port nền tảng với tính năng gameplay.

Sodium/Iris và shader pack là mục tiêu hỗ trợ chính thức theo nguyên tắc không hook render pipeline, có smoke test cho Gate, Domain, Arise VFX, dungeon nước và HUD ở quality thấp/cao. OptiFine không hỗ trợ. Mod combat khác được phát hiện/cảnh báo theo capability; không crash, không âm thầm ghi đè binding hay damage pipeline. Keybind conflict scan và fallback layout là một phần compatibility test, không chỉ UX.
