# 17 — CHIẾN LƯỢC HIỆU NĂNG (PERFORMANCE STRATEGY)

> **Chủ sở hữu:** Performance Engineer
> **Tuyên ngôn:** Hiệu năng là *tính năng*, không phải giai đoạn tối ưu cuối dự án. Mục tiêu: **200 bóng sở hữu dưới dạng dữ liệu, tối đa 40 bóng và 80 địch gần người chơi có AI đầy đủ**, phần còn lại dùng LOD/ngủ; TPS 20 ổn định trên server tầm trung, không giật client.

---

## 1. Ngân sách (budget) — con số cam kết

| Hạng mục | Ngân sách mỗi tick (server) |
|---|---|
| Toàn bộ AI (địch + bóng) | ≤ 8 ms |
| Hệ thống Bóng (quản lý, hồi sinh) | ≤ 2 ms |
| Dungeon/gate logic | ≤ 2 ms |
| Hệ Thống (quest, economy tick) | ≤ 1 ms |
| Dự phòng cho vanilla + mod khác | phần còn lại của 50 ms |

Ngân sách được **giám sát runtime**: vượt ngân sách → scheduler trì hoãn tác vụ không quan trọng (không bao giờ vượt bằng được).

## 2. Kỹ thuật trụ cột

### 2.1. AI theo mức chi tiết (AI LOD)

| Tầm với người chơi | Chế độ AI |
|---|---|
| < 32 block | Đầy đủ (BT + utility + squad) |
| 32–64 block | Giản lược (utility mỗi 10 tick, không squad re-eval) |
| 64–128 block | Chỉ di chuyển/tuần tra thô, nghĩ 1 lần/giây |
| > 128 block | **Ngủ** — chỉ lưu vị trí, thức dậy khi có người chơi gần |

### 2.2. Chia nhịp "suy nghĩ" (staggering)

- Entity chia làm 4 nhóm; mỗi tick chỉ một nhóm chạy utility scoring → AI "nghĩ" 5 lần/giây thay vì 20, người chơi không nhận ra.
- Perception scan tối đa 1 lần/10 tick, dùng **spatial hash** thay vì quét entity.
- Pathfinding: cache đường, chia sẻ trong squad, re-path ≤ 1 lần/20 tick; đường ngắn dùng steering thuần (không A*).

### 2.3. Quân đoàn không triệu hồi = dữ liệu thuần

- Bóng trong storage **không tồn tại entity** (tài liệu 15) — 200 bóng sở hữu chi phí ≈ 0 tick.
- Trần triệu hồi đồng thời (thiết kế 04.6.1, tối đa 40) chính là đòn bẩy hiệu năng — thiết kế game và hiệu năng ở đây là một.
- Xác trong Gate chỉ là entity/VFX trong 120 giây; sau đó chuyển thành **Soul Echo** serialized, không tick/pathfind/render như mob. Trần 128 Echo/Gate của 14.21, tối đa 2 Gate/Stratum, là ngân sách cứng; boss/elite/unique được bảo lưu trước, common target gộp thành Mảnh Bóng bảo lưu có thông báo.

### 2.4. Client-side

- Particle/VFX có **ngân sách theo vùng** (tối đa N particle/16 block); VFX phụ tự tắt khi FPS thấp (adaptive quality).
- Damage number, HUD pool đối tượng (không cấp phát mỗi hit).
- Hoàn toàn tương thích Sodium/Iris ở mức không-xung-đột (không hook render của chúng — tài liệu 19).

### 2.5. Worldgen & dungeon

- Gen dungeon **async + pre-gen** trước khi mở cổng (tài liệu 16.3.5); không gen trong tick người chơi bước vào.
- Template room là dữ liệu nén; đặt khối theo chunk-batch, không setblock lẻ.

## 3. Đo lường & quy trình

- **Benchmark scene chuẩn** (có sẵn trong dev tools): "40 bóng + 80 địch giao tranh trong gate hạng A" — mọi bản build chạy scene này, so mspt với baseline.
- Profiling định kỳ bằng Spark; báo cáo flame graph lưu kèm milestone.
- **Ngân sách regression:** PR làm scene chuẩn chậm hơn >5% phải có giải trình + quyết định chấp nhận/tối ưu.
- Test trên cấu hình mục tiêu: PC tầm trung (không phải máy dev cao cấp).

## 4. Danh sách cấm (performance don'ts)

1. Không quét entity theo vùng trong tick — dùng index/spatial hash.
2. Không cấp phát đối tượng mới trong vòng lặp nóng — pool.
3. Không đọc/ghi NBT trong tick — cache.
4. Không hook vào render pipeline của mod khác.
5. Không tính lại thứ có thể cache theo "dirty flag".

## 5. Rủi ro & Quyết định vận hành

1. **Server yếu (host rẻ tiền)?** → Config hạ trần entity/tần suất gate; AI LOD có thêm tầng "siết".
2. **Mod khác cũng nặng AI?** → Ngân sách của ta tự co lại khi mspt tổng cao (nhượng bộ lịch sự) — nhưng log cảnh báo để người chơi biết.
3. **VFX "khoảnh khắc aura" (quân đoàn trỗi dậy hàng loạt) quá tải?** → Gom thành một VFX tổng thay vì 40 VFX nhỏ — vừa đẹp hơn vừa rẻ hơn.

---

## 6. Bổ sung v4.0 — ngân sách vùng, đô thị và nước

Ngân sách entity là **theo vùng combat**, không phải lời hứa “càng nhiều càng hay”: mặc định tối đa 40 bóng vật thể hóa, 80 địch full AI; người chơi có thể sở hữu nhiều hơn mà không mất tick. Khi boss có hộ vệ hoặc phòng thủ đô thị, Director phân đợt để không đẩy quá ngân sách; giảm số entity phải đổi bằng AI/role/telegraph thú vị hơn, không chỉ xóa quân.

Đô thị dùng NPC proxy ngoài tầm, schedule theo event và animation LOD; hàng người xếp giám định là presentation cục bộ, không simulation pathfinding cho toàn bộ cư dân. **Từ P7+**, water dungeon dùng mesh/particle giới hạn theo phòng, không quét fluid mỗi tick; mount có collision/pathing profile riêng và fallback an toàn nếu chunk/không khí chưa sẵn sàng.
