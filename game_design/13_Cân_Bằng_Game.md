# 13 — CÂN BẰNG GAME (GAME BALANCING)

> **Chủ sở hữu:** Balance Designer
> **Mục tiêu:** Không bao giờ quá dễ, không bao giờ bất công, không power creep mất kiểm soát, không một build tối ưu duy nhất. Mọi con số cụ thể: tài liệu 14. File này là *triết lý và cơ chế*.

---

## 1. Mô hình cân bằng: Power Budget

Sức mạnh tổng thể của người chơi ở một level bất kỳ được định nghĩa là **Power Budget (PB)** — tổng hợp từ: chỉ số + kỹ năng + trang bị + quân đoàn (có trọng số). Công thức: 14.9.

- Mọi nội dung (gate, boss, event) có **PB yêu cầu** thiết kế sẵn.
- Người chơi vượt PB yêu cầu 20% → nội dung "thoải mái"; vượt 50% → "dễ"; thấp hơn 20% → "khó"; thấp hơn 40% → "tử thần".
- Balance designer không cân từng món lẻ — cân **đóng góp vào PB**. Một keystone mạnh phải kèm trade-off giữ PB tổng không đổi.

> **Lý do:** PB biến cân bằng từ nghệ thuật cảm tính thành kỹ thuật đo lường được, và cho phép simulation tự động phát hiện outlier (tài liệu 18).

---

## 2. Đường cong độ khó — "chữ V ngược có kiểm soát"

Học từ phân tích nguyên tác, UMBRA chủ động điều khiển *nguồn hứng thú* theo giai đoạn:

| Giai đoạn | Nguồn hứng thú chính | Độ khó cảm giác |
|---|---|---|
| Thức Tỉnh → D | **Suspense** (sống sót) | Cao, chết là bình thường |
| C–B | **Growth** (mở khóa) | Vừa, thử thách công bằng |
| A–S | **Tactics** (đội hình, build) | Cao trở lại, nhưng vì cơ chế không vì số |
| Quốc Gia | **Spectacle** (phô diễn sức mạnh) | Trung bình — người chơi được "ngầu" |
| Vương Giả / Tháp Vô Định | **Mastery** (đỉnh cao tự chọn) | Leo thang vô hạn theo ý người chơi |

> **Điểm tinh vi:** UMBRA *cho phép* giai đoạn power trip (Quốc Gia) tồn tại — đó là phần thưởng cảm xúc cần thiết — rồI mở Tháp Vô Định để ai muốn thử thách lại có chỗ. Không ép mọi người "khó mãi", không để ai "dễ mãi".

---

## 3. Build diversity — vòng khắc chế mềm

```
        SÁT ẢNH (đơn mục tiêu)
       ╱        khắc mềm      ╲
      ╱    THỐNG SOÁI          ╲
     ╱    (quân số)  khắc mềm   ╲
     ╲                          ╱
      ╲   HẮC PHÁP (AoE/CC)   ╱
       ╲_____________________╱
```

- **Khắc chế mềm = +25–40% hiệu quả**, không phải ×2/×0.5. Người chơi giỏi vẫn thắng bằng build "bị khắc" — khắc chế là *gợi ý chiến thuật*, không phải bản án.
- Nội dung luân phiên ưu ái: gate bầy đông (Hắc Pháp sáng), boss đơn (Sát Ảnh sáng), phòng thủ nhiều hướng (Thống Soái sáng) → mỗi build có "sân nhà".
- **Không nerf theo cảm xúc:** thay đổi cân bằng chỉ khi có dữ liệu simulation + playtest; ưu tiên *buff cái yếu* hơn nerf cái mạnh (giữ power fantasy).

---

## 4. Kiểm soát power creep

1. **Diminishing returns ở mọi nơi tăng trưởng tuyến tính:** chỉ số sau soft cap, crit có trần, kháng có trần hiệu dụng.
2. **Prestige log-scale:** sau 100, mỗi cấp Vượt Ngôi tăng theo log → tiến bộ *cảm giác* vẫn có, tiến bộ *số* chững lại (14.10).
3. **Đổi trục xung đột:** khi chiến đấu không còn khó, nội dung chuyển sang chỉ huy (nhiều mặt trận), cơ chế (boss đổi luật), khám phá (Vương mới) — không giả vờ người chơi còn yếu.
4. **Không tăng HP quái theo cấp số nhân vô hạn:** Tháp Vô Định tăng *mutator* (cơ chế) nhanh hơn tăng số.

---

## 5. TTK & nhịp combat (mục tiêu định lượng — chi tiết 14.7)

| Đối tượng | TTK mục tiêu (người chơi đúng PB) | Dung sai |
|---|---|---|
| Quái thường | 3–8 giây | ±2s |
| Elite | 20–40 giây | ±10s |
| Mini-boss | 2–4 phút | ±1 phút |
| Boss | 5–12 phút | theo ma trận boss |

TTK quá thấp → combat mất đọc/đếm nhịp; quá cao → grind. Hai chỉ số này được simulation theo dõi tự động mỗi bản build (tài liệu 18.4).

---

## 6. Độ khó thích ứng (Adaptive Difficulty — tinh tế và minh bạch)

- Hệ thống đo *hiệu suất gần đây* (tỷ lệ chết, thời gian clear, % máu mất trung bình) và điều chỉnh **nhẹ ±10%**: tốc độ telegraph, tần suất quái phụ — **không bao giờ** đụng vào HP/sát thương nền (tránh cảm giác "game tự thắng hộ").
- **Minh bạch:** có config hiển thị/tắt hoàn toàn. Người chơi hardcore chọn chế độ **Vương Giả Thuần Túy** (khóa adaptive, tăng thưởng tương xứng).

---

## 7. Cân bằng quân đoàn (riêng và quan trọng)

- **Trần triệu hồi** theo hạng (04.6.1) là đòn bẩy cân bằng chính: tăng *chất* (bóng mạnh hơn) thay vì *lượng* vô hạn.
- Boss có cơ chế khắc quân số (09.4) → quân đoàn không phải auto-win.
- Bóng không vượt quá **60% PB tổng** của người chơi ở mọi điểm thiết kế — bản thân người chơi luôn là nhân vật chính của câu chuyện.

---

## 8. Quy trình cân bằng một tính năng mới

1. Ước lượng đóng góp PB → simulation 10.000 trận (18.4).
2. Playtest nội bộ 3 kịch bản: người yếu nhất có thể tới nội dung / đúng PB / mạnh nhất có thể.
3. Telemetry đợt test đóng: tỷ lệ chọn, tỷ lệ thắng, thời gian.
4. Ra quyết định: buff / nerf / redesign / giữ nguyên — **ghi lại lý do** vào changelog thiết kế.

---

## 9. Rủi ro & Quyết định vận hành

1. **PB đo được "skill người chơi"?** → Không — PB đo số; skill được bù bằng adaptive difficulty và thiết kế encounter cho phép outplay.
2. **Người chơi tối ưu hóa PB lệch (full quân đoàn)?** → Trần 60% ở mục 7 + boss khắc quân số.
3. **Adaptive bị lạm dụng (cố chết để game dễ)?** → Đo trên cửa sổ dài, thay đổi chậm, ±10% tối đa — lạm dụng không đáng công.

---

## 10. Bổ sung v4.0 — cân bằng Thăng Giới và quân đoàn

**Thăng Giới là lựa chọn độ khó, không là stat check toàn cục.** Mỗi Thế Giới Song Song có PB target, tỷ lệ modifier và bảng loot riêng; Thế Giới Gốc giữ PB ban đầu. Tầng mới tăng thứ tự ưu tiên: encounter/AI/bố cục → mutator → phần thưởng collection → số liệu. Không được dùng “mọi quái +X% HP” làm nội dung vô hạn.

Quân đoàn được cân theo ba ngân sách độc lập: tổng PB đóng góp (tối đa 60%), số entity/đội hình, và mana dùng lúc triệu hồi/hồi sinh. Boss được phép phản ứng với đội đông nhưng phải đánh vào vị trí/lệnh/role chứ không vô hiệu hóa toàn bộ summon. Trong simulation, kiểm tra ba profile: solo-self, commander, hybrid; không profile nào được chênh tỷ lệ thắng >10 điểm phần trăm ở nội dung đúng vai trò.

Arise có pity boss là phần thưởng, nên cân bằng nằm ở điều kiện hạ boss, sức chứa, thời điểm mở slot và moveset bóng thu nhỏ — không nằm ở việc bắt người chơi lặp boss vì RNG. Một boss shadow mạnh phải có điểm yếu, chi phí triệu hồi/revive hoặc cạnh tranh vị trí đội hình rõ ràng.

---

## 11. Bổ sung v4.0 — ngân sách encounter, escort và nội dung mới

Boss có quân hộ vệ phải được cân như **một encounter**, không cộng boss đầy đủ với một đội quân đầy đủ rồi gọi đó là “khó”. Khi PB encounter đúng hạng = `E`, phân bổ khởi điểm để simulation là:

```text
PB_boss       = 55–75% E
PB_escort     = 15–30% E
PB_arena/obj  = 10–20% E
Tổng          = 100% E (trước modifier được công bố)
```

`PB_arena/obj` là áp lực có counterplay (hazard telegraph, vị trí, objective phụ), không phải sát thương vô hình. Escort giảm phần PB boss hoặc arena tương ứng; thêm healer/shield/caller phải lấy ngân sách từ đâu đó. Điều này giữ fantasy “boss được bảo vệ” nhưng vẫn để người chơi giải đội hình thay vì gặp tường số.

Mọi faction/boss/skill/set/mount mới phải chứng minh **độ khác biệt quyết định** theo 26: playtest phải chỉ ra động từ thay đổi, counter tactical + strategic, failure feedback và reward không gây snowball bắt buộc. Nếu chỉ khác số/texture, cân nó như variant/modifier nhỏ; không tăng PB target, không chiếm slot content lớn và không dùng để tuyên bố game có thêm chiều sâu.
