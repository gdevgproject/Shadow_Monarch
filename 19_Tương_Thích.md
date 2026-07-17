# 19 — TƯƠNG THÍCH (COMPATIBILITY)

> **Chủ sở hữu:** Technical Director
> **Mục tiêu:** UMBRA chơi tốt *một mình*, và chơi *lịch sự* trong hệ sinh thái Fabric — không ép ngườichơi chọn phe giữa UMBRA và bộ mod yêu thích của họ.

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
2. **Không sửa hành vi vanilla ngoài phạm vi thiết kế:** combat rework chỉ áp dụng cho entity/ngườichơi *trong hệ thống UMBRA* khi combat stance bật; dân làng, vật nuôi, máy farm hoạt động y như cũ.
3. **Tag hóa thay vì hardcode:** loot/mob thêm qua tag để modpack tự điều chỉnh.
4. **Mixin tối thiểu + sổ đăng ký:** mỗi mixin ghi mục tiêu, lý do, rủi ro xung đột (tài liệu 16.1).
5. **Datapack-first:** server/modpack muốn cân bằng lại → sửa JSON, không cần fork mod.
6. **Config tôn trọng:** mọi cơ chế xâm lấn (world event, gate gần nhà, adaptive difficulty) tắt được.

## 3. Tương thích save & phiên bản

- Migration chain bảo toàn dữ liệu (tài liệu 15.9); cam kết **đọc được save từ mọi bản 1.x**.
- Forward compatibility MC: lớp trừu tượng registry/render (16.1) giảm chi phí port; mục tiêu port sang bản MC mới trong ≤ 4 tuần sau khi Fabric API ổn định.
- Không cam kết backport (chính sách ghi rõ để tránh kỳ vọng sai).

## 4. Multiplayer (khung, không cam kết 1.0)

- Thiết kế đã server-authoritative từ đầu (16.3.4) → dedicated server chạy được; nhưng cân bằng co-op (scale gate theo số ngườI) là nội dung tương lai (23).
- Trên server: mỗi ngườichơi một Hệ Thống riêng; gate là tài nguyên chung — quy tắc "ai mở ngườI đó hưởng, hỗ trợ chia theo đóng góp" (chi tiết ở 23).

## 5. Các xung đột đã biết & chính sách xử lý

| Xung đột tiềm ẩn | Chính sách |
|---|---|
| Mod combat khác (Better Combat...) | Không cài chung; UMBRA phát hiện và cảnh báo rõ, không crash |
| Mod tăng spawn rate | Ngân sách tick của UMBRA tự co (17.5); khuyến nghị config |
| Optifine | Không hỗ trợ (thay bằng Sodium + Iris) — ghi rõ trên trang mod |
| Mod thêm mob | Mob ngoài không có definition UMBRA → dùng AI profile "vanilla+" nhẹ, có thể Arise ở cấp thấp (theo phe gán qua tag) |

## 6. Rủi ro & Câu hỏi mở

1. **Bản MC mới đổi combat/render lớn?** → Lớp trừu tượng + ít mixin là bảo hiểm; chấp nhận có bản port chậm hơn nếu cần.
2. **Ngườichơi đòi Forge/NeoForge?** → Kiến trúc module tách logic khỏi nền tảng giúp port khả thi về sau; 1.0 chỉ Fabric (tập trung chất lượng).
3. **Modpack muốn nhúng UMBRA?** → Cho phép theo giấy phép ghi rõ; datapack-first giúp modpack tự cân bằng cho pack của họ.
