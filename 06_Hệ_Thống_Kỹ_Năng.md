# 06 — HỆ THỐNG KỸ NĂNG (SKILL SYSTEM)

> **Chủ sở hữu:** Systems Designer
> **Mục đích:** Toàn bộ kỹ năng của người chơi: passive, active, class skill, skill tree, rune, synergy. Nguyên tắc số 1: **mỗi kỹ năng phải thay đổi cách chơi, không chỉ thay đổi con số.**

---

## 1. Ba tầng kỹ năng

| Tầng | Nguồn | Vai trò |
|---|---|---|
| **Passive (Thụ động)** | Skill tree, mastery, trang bị | Định hình build, luôn bật |
| **Active (Chủ động)** | Skill tree + **Rune** | 4 ô kỹ năng + 1 ô class — quyết định trong combat |
| **Class Skill (Kỹ năng Vương Giả)** | Class & hạng | Trỗi Dậy, Shadow Storage, Shadow Exchange, Monarch's Domain, quyền năng Vương Giả |

---

## 2. Skill Tree — cấu trúc "Sông Ba Ngả"

Mỗi chuyên hóa (Sát Ảnh / Thống Soái / Hắc Pháp) có một cây, nhưng **ba cây chung gốc** — người chơi rẽ ngả dần, không bị khóa ngay:

```
        [GỐC CHUNG — mọi Chúa Tể đều học]
        Trỗi Dậy · Stealth cơ bản · Mắt Bóng (xem qua bóng)
        ┌───────────┼───────────┐
   SÁT ẢNH      THỐNG SOÁI      HẮC PHÁP
   burst/stealth  army/aura      AoE/CC/mana
        └───────────┼───────────┘
        [NGÕ CỤT CAO CẤP — keystone]
```

- **Điểm kỹ năng:** 1 điểm/level + thưởng quest → ở level 100 có ~120 điểm; cây có ~180 nút → **không bao giờ max hết** → lựa chọn thật.
- **Keystone (ngõ cụt):** mỗi cây 3 keystone biến đổi cách chơi (ví dụ Sát Ảnh: *"Một Đòn"* — đòn đầu sau stealth luôn crit nhưng stealth tốn gấp đôi mana; Thống Soái: *"Đạo Quân Bất Tử"* — bóng chết hồi sinh nhanh gấp 3 nhưng sát thương bóng −15%).
- **Nguyên tắc keystone:** luôn là *đánh đổi*, không bao giờ là "+X% thuần túy". Trade-off sinh build; buff thuần sinh meta duy nhất.

---

## 3. Rune — kỹ năng là loot

**Rune Kỹ Năng** là vật phẩm rơi từ quái/boss/rương: dùng để học một active skill cụ thể (ngoài cây) hoặc nâng cấp skill đã có (tối đa +3 cấp, mỗi cấp thêm hiệu ứng *về chất* ở cấp cuối).

- Tại sao rune thay vì mọi thứ trong cây? → **Build là thứ bạn *tìm thấy*, không chỉ thứ bạn *lên kế hoạch*.** Rune biến mỗi lần farm thành xúc xắc build — variable reward đúng nghĩa.
- Rune **không hủy được vô ích**: rune trùng → nghiền thành Bột Rune → rèn rune ngẫu nhiên theo loại mong muốn (sink + giảm frustration RNG).
- Một số rune **chỉ rơi từ một phe quái** → khuyến khích đi đa dạng gate (chống một-meta-farm).

Ví dụ rune định danh đầu game (tri ân nhưng gốc):

| Rune | Hiệu ứng | Phe rơi |
|---|---|---|
| Vọt Tàn Ảnh | Dash xuyên kẻ địch, để lại bóng mồi | Undead |
| Gầm Kinh Hồn | AoE fear, giảm chỉ số đám yếu hơn bạn | Dragonkin |
| Mưa Dao | Ném vòng dao quanh người, sát thương theo combo | Beast |
| Trói Hư Không | Kéo một mục tiêu về phía bạn (hoặc kéo bạn tới nó) | Demon |
| Nhịp Máu | Đòn tiếp theo hút HP bằng 50% sát thương | Insect |

---

## 4. Synergy — kỹ năng nói chuyện với nhau

Thiết kế theo cặp/bộ ba có *tên gọi* (dễ nhớ, dễ lan truyền meta):

- **"Điệu Nhảy Đôi":** đổi vũ khí trong 1s sau dodge → đòn đầu vũ khí mới +40% (thưởng kỹ năng tay).
- **"Mồi Nhử":** bóng mồi của Vọt Tàn Ảnh kích nổ khi bị phá nếu có Mưa Dao đang quay.
- **"Bữa Tiệc Quân Vương":** Monarch's Domain đang bật + bóng hạ mục tiêu → kéo dài Domain 0.5s (chuỗi snowball có trần).
- **"Kẻ Thức Tỉnh Giả":** stealth ngay sau khi một bóng chết → không tiêu mana (risk/reward cảm tính).

> **Nguyên tắc:** synergy phải *phát hiện được* khi chơi (có dòng gợi ý mờ trong mô tả kỹ năng) — khám phá synergy là niềm vui của theorycrafter, nhưng casual không bị trừng phạt vì không biết.

---

## 5. Kỹ năng Class theo hạng

| Kỹ năng | Mở khóa | Thiết kế |
|---|---|---|
| **Trỗi Dậy** | Job Change (level 20, hạng D) | Xem tài liệu 04.2 |
| **Cất Giữ Bóng** | Job Change | Vô hình, không slot giới hạn trữ (giới hạn triệu hồi) |
| **Mắt Bóng** | hạng B+ | Nhìn/nghe qua bóng đang Ẩn Mình — do thám xa an toàn |
| **Shadow Exchange** | hạng S | Đổi chỗ với bóng, cooldown theo khoảng cách |
| **Monarch's Domain** | hạng S | Aura +50% sức mạnh quân đoàn, 20s, cooldown dài |
| **Gate Creation** | Vương Giả | Tự mở Vết Nứt tới dungeon đã clear (fast travel có chủ đích) |
| **Thừa Kế** | Vương Giả | Dùng phiên bản yếu của kỹ năng boss đã thu phục (rotating kit — endgame build diversity đỉnh cao) |

---

## 6. Ngân sách thiết kế (chống bành trướng)

- Tổng active skill người chơi: **≤ 24** (4 ô + 1 class cùng lúc → chọn 5/24 là quyết định build hằng ngày).
- Passive node: ~180. Keystone: 9. Rune: ~40 loại lúc 1.0.
- Mọi skill mới phải trả lời: *"Nó thay combo/rotation nào? Nó khắc build nào? Build nào khắc nó?"* — không trả lời được 3 câu → không vào game.

---

## 7. Rủi ro & Câu hỏi mở

1. **Rune RNG gây cay cú?** → Bột Rune + rune định hướng (chọn phe) giảm; theo dõi tỷ lệ "20 giờ không ra rune mục tiêu" trong test.
2. **Skill tree 180 nút có quá tải người mới?** → Gốc chung chỉ 6 nút bắt buộc hiểu; cây mở dần theo hạng.
3. **Keystone đánh đổi có bị "lựa hiển nhiên"?** → Mỗi keystone phải có ít nhất một encounter trong game nơi nó *yếu hơn không chọn* — thiết kế boss kiểm chứng (tài liệu 09).

---

## 8. Bổ sung v3.0 — bộ kỹ năng chiến đấu sâu nhưng đọc được

Năm ô trang bị (4 active + 1 class) là trần thao tác đồng thời; thư viện kỹ năng có thể lớn hơn nhưng mỗi skill phải thuộc một trong bốn vai trò: mở giao tranh, né/phản đòn, kiểm soát không gian, hoặc payoff combo. Một skill mới thay thế được một quyết định cũ mới xứng đáng vào pool.

Thêm ba mẫu bắt buộc cho 1.0: **Bước Hư Ảnh** (chuỗi dash/không thể bị chọn trong khoảnh khắc, nhưng không gây sát thương miễn phí), **Nhãn Giới Tĩnh Lặng** (làm chậm cục bộ, xem 05.11), và **Cưỡi Bóng** (mount skill theo chủng loài). Chúng dùng ý tưởng action-RPG chung nhưng có animation, luật, tên gọi và counterplay gốc UMBRA. Monarch's Domain được chuẩn hóa: aura tròn, 12 giây cơ bản, phạm vi/hiệu ứng/đồng hồ hiển thị rõ; bóng trong vùng nhận buff theo vai trò, kẻ địch không bị xóa cơ chế.
