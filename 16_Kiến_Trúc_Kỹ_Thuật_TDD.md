# 16 — KIẾN TRÚC KỸ THUẬT (TECHNICAL DESIGN DOCUMENT / MOD ARCHITECTURE)

> **Chủ sở hữu:** Technical Director
> **Mục tiêu:** Kiến trúc cho một mod Fabric **duy trì được nhiều năm**, data-driven, hiệu năng cao, ít phụ thuộc, dễ mở rộng. Tài liệu này mô tả *quyết định kiến trúc và lý do* — không chứa mã nguồn.

---

## 1. Quyết định nền tảng

| Quyết định | Lựa chọn | Lý do |
|---|---|---|
| Loader | **Fabric** | Yêu cầu của dự án; hệ sinh thái hiệu năng mạnh (Lithium, Sodium) |
| Phiên bản MC | Stable mới nhất tại thờI điểm phát triển; thiết kế **port-forward friendly** (lớp trừu tượng registry/render) | Forward compatibility là yêu cầu tầm nhìn |
| Ngôn ngữ | Java 21+ | Chuẩn MC hiện đại |
| Kiến trúc nội dung | **Data-driven qua datapack + Codec** | Thêm nội dung không cần code (tài liệu 15, 21) |
| Phụ thuộc bắt buộc | **Chỉ Fabric API** | Tránh ràng buộc ngườichơi; giảm vỡ khi MC cập nhật |
| Phụ thuộc mềm | Cloth Config, ModMenu, REI/EMI, Jade (tự động tắt nếu thiếu) | QoL hệ sinh thái, không bắt buộc (tài liệu 19) |
| Mixin | **Tối thiểu, có sổ đăng ký** (mixin audit log) | Mỗi mixin là nợ bảo trì — phải có lý do ghi lại |

---

## 2. Bản đồ module (modularity trong một mod)

```
umbra-core            — hạt nhân: registry, event bus nội bộ, data loader, save/migration
 ├─ umbra-system      — Hệ Thống (UI diegetic, quest, store, daily/penalty)
 ├─ umbra-progression — level/stat/rank/class/mastery/prestige
 ├─ umbra-combat      — combat stance, combo, dodge/parry, damage pipeline, posture
 ├─ umbra-shadows     — Arise, quân đoàn, AI bóng, formation, commands
 ├─ umbra-ai          — utility/BT engine, squad brain, perception, LOD
 ├─ umbra-dungeons    — gate lifecycle, procedural generator, break/field, spire
 ├─ umbra-world       — worldgen hooks, NPC, world events, integration vanilla
 ├─ umbra-items       — rarity/affix/rune/growth/reforge
 ├─ umbra-economy     — currencies, faucet/sink, shop logic
 └─ umbra-client      — HUD, VFX/SFX, màn hình Hệ Thống (client-only code tách bạch)
```

**Luật phụ thuộc:** module chỉ được phụ thuộc *xuống* (về `core`), giao tiếp ngang qua **event bus nội bộ + service interface** — ví dụ `umbra-combat` phát sự kiện `EntityKilledEvent`; `umbra-shadows` lắng nghe để mở cửa sổ Arise. Không module nào import trực tiếp implementation của module ngang.

> **Lý do:** mod sẽ sống nhiều năm, nhiều ngườI đóng góp. Ranh giới module rõ ràng = thay thế/vô hiệu một hệ thống (ví dụ tắt economy trong config) không kéo sập hệ thống khác. Đây cũng là điều kiện để test độc lập (tài liệu 18).

---

## 3. Hệ thống con then chốt

### 3.1. Data Loader & Registry nội dung

- Load JSON datapack qua **Codec**; validate toàn bộ khi reload → báo lỗi có dòng/cột, nội dung lỗi bị loại khỏi registry nhưng game không crash.
- Registry hai tầng: **definition registry** (tĩnh) và **runtime registry** (instance đang sống: gate đang mở, bóng đang triệu hồi).
- Mọi tra cứu nóng (combat, AI) dùng **index dựng sẵn** (bảng tra theo faction/role/tier) — không quét danh sách trong tick.

### 3.2. Event Bus nội bộ

- Sự kiện gameplay chuẩn hóa: `CombatEvent`, `AriseEvent`, `GateEvent`, `QuestEvent`, `EconomyEvent`.
- Có **thứ tự ưu tiên + hủy được**; mọi sự kiện có thể bị module khác chặn (ví dụ world event chặn spawn gate khi đang Xâm Lược).
- Tách biệt event server (logic) và client (presentation) — client không bao giờ là nguồn quyết định.

### 3.3. Lưu trữ & Migration

- **World state** (gate, event, field dungeon): persistent state theo world, schema_version (tài liệu 15.9).
- **Player state** (level, chỉ số, quân đoàn): component gắn player; serialize gọn, nén; **không lưu thừa** (tính được thì không lưu).
- Autosave: tận dụng chu kỳ save vanilla; state của mod đánh dấu dirty và flush theo nhịp — **không tự ý ghi đĩa** gây giật.
- Migration chain + test mở save cũ (15.9, 18).

### 3.4. Networking (client–server)

- Single-player vẫn chạy mô hình client–server nội bộ của MC → thiết kế *đúng ngay từ đầu*: mọi quyết định logic ở server thread; client nhận **gói trạng thái gọn** (delta, không full snapshot mỗi tick).
- HUD Hệ Thống render hoàn toàn client-side từ state đã đồng bộ; packet custom giới hạn: stats, quest, legion roster (tóm tắt), combat feedback (số sát thương, combo).
- Sẵn sàng multiplayer sau này: không đọc trực tiếp dữ liệu client khác (tài liệu 23).

### 3.5. Dimension & Dungeon Runtime

- Dungeon dùng **pool dimension riêng** theo loại (không tạo dimension mới mỗi gate — tránh phình save): layout đặt theo "ô lưới cách xa nhau" trong pool, dọn dẹp khi gate đóng.
- Pre-gen chunk async trước khi cho phép vào; fallback: phòng chờ trong gate nếu gen chưa xong (không bao giờ để ngườichơi rơi vào chunk lỗi).
- Penalty Zone: pocket dimension đơn giản, tái sử dụng.

### 3.6. Scheduler & Tick Budget

- **Central Scheduler** chia công việc AI/AI-perception/pathing thành hàng đờI có ngân sách (tài liệu 17): mỗi tick chỉ tiêu tối đa X ms cho AI — vượt ngân sách thì trì hoãn, không vượt.
- Mọi tác vụ nặng (gen dungeon, scan vùng) chạy async, kết quả hợp nhất vào main thread an toàn.

---

## 4. Kiến trúc AI dùng chung (nhắc lại ràng buộc)

Một engine AI duy nhất (`umbra-ai`) phục vụ cả địch lẫn bóng: cùng behavior tree, cùng utility scoring, khác **profile** (bảng trọng số + danh sách lệnh chấp nhận). Hệ quả: bóng boss dùng lại được AI profile của boss (moveset thu nhỏ) mà không viết lại.

---

## 5. Cấu hình & vận hành

- Config 3 tầng: **Ngườichơi** (độ khó, adaptive, hiệu ứng), **Server/World** (tần suất gate, giới hạn entity), **Dev** (debug overlay, profiling).
- Mọi giới hạn hiệu năng (trần triệu hồi, ngân sách tick) đều config được — server yếu tự hạ, không sửa code.

---

## 6. Tiêu chí chấp nhận một quyết định kiến trúc mới (ADR)

Mọi quyết định kiến trúc lớn phải có **Architecture Decision Record** ngắn: bối cảnh → lựa chọn → hệ quả → cách đảo ngược. Lưu cùng repo tài liệu. Không có ADR, không merge.

---

## 7. Rủi ro & Câu hỏi mở

1. **Port-forward khi MC đổi lớn (registry, render)?** → Lớp trừu tượng + ít mixin giảm đau; nhưng phải chấp nhận chi phí duy trì lớp trừu tượng đó.
2. **Pool dimension dọn dẹp sót dữ liệu?** → Quy trình dọn có checklist + test; vùng "mồ côi" được quét định kỳ.
3. **Datapack reload giữa chừng khi gate đang mở?** → Definition bất biến trong runtime của instance: gate đang mở giữ snapshot definition tại lúc tạo.
4. **Câu hỏi mở:** có tách `umbra-client` thành mod riêng để server thuần chạy không cần client code? — quyết định ở M2 (tài liệu 20).
