# 14 — CÔNG THỨC TOÁN HỌC (MATHEMATICAL FORMULAS)

> **Chủ sở hữu:** Systems Designer + Balance Designer
> **Đây là NGUỒN CHÂN LÝ DUY NHẤT về con số.** Mọi tài liệu khác trích dẫn; mọi thay đổi phải qua review balance + cập nhật simulation.
> Quy ước: `L` = level ngườichơi · `La` = level mục tiêu (quái) · `⌊x⌋` = floor · `clamp(x,a,b)` = giới hạn trong [a,b].
> **v2.0:** thêm 14.12 (Thể Lực), 14.13 (Tinh Thần địch), 14.14 (Kính Trọng), 14.15 (Thuê Hunter), 14.16 (Giá bán rác); hoàn thiện bảng bonus 14.6.

---

## 14.1. Chỉ số cơ bản của ngườichơi

| Đại lượng | Công thức |
|---|---|
| HP tối đa | `HP = 20 + VIT·6 + L·2` |
| Mana tối đa | `MP = 20 + INT·8 + L·1` |
| Hồi HP (ngoài combat) | `VIT·0.05 HP/giây` (cap 5 HP/s) |
| Hồi Mana | `(2 + INT·0.15) MP/giây`, +50% khi không giao tranh 5s |
| Sát thương cơ bản cận chiến | `Base = WeaponBase + STR·1.2` |
| Crit chance | `5% + PER·0.25%`, trần 60% |
| Crit damage | `150% + PER·0.5%`, trần 250% |
| Tốc độ chạy | `+0.15%/điểm AGI`, trần +25% |
| i-frame dodge | `0.25s + AGI·0.001s`, trần 0.4s |

## 14.2. Đường cong EXP

EXP cần để lên từ level `L` sang `L+1`:

```
EXP(L) = ⌊ 60 · L^1.85 + 25·L ⌋          (1 ≤ L < 100)
EXP_prestige(P) = ⌊ 50000 · (1 + P·0.35) ⌋  (P = cấp Vượt Ngôi ≥ 1)
```

EXP nhận từ quái:

```
EXP_quái = EXP_base(quái) · (1 + 0.06·(La − L))     nếu La > L
         · max(0.3, 1 − 0.08·(L − La))              nếu La < L  (farm quái yếu giảm hiệu quả)
```

Bảng mẫu (kiểm chứng nhịp ở tài liệu 03.2.3):

| L | EXP cần | Tích lũy |
|---|---|---|
| 5 | ≈ 1.200 | ≈ 3.100 |
| 20 | ≈ 16.000 | ≈ 120.000 |
| 40 | ≈ 56.000 | ≈ 800.000 |
| 70 | ≈ 158.000 | ≈ 4.100.000 |
| 99 | ≈ 296.000 | ≈ 10.900.000 |

## 14.3. Diminishing returns chỉ số (sau soft cap 100 điểm tự do)

```
Hiệu quả điểm(x) = x                            nếu x ≤ 100
                 = 100 + (x−100)^0.75           nếu x > 100
```

Mọi ảnh hưởng tuyến tính theo chỉ số (14.1) tính trên *hiệu quả điểm*, không tính trên điểm thô.

## 14.4. Công thức sát thương

```
DMG_gốc   = (Base + Bonus_skill) · Mult_skill · Mult_combo
DMG_hệ    = DMG_gốc · (1 + AffinityBonus) · (1 − Kháng_mục_tiêu) · Hệ_số_yếu_khắc (1.25 nếu khắc; 0.75 nếu bị kháng)
Giảm giáp = Armor / (Armor + 50 + 10·L_tấn_công)          (trần hiệu dụng 75%)
DMG_cuối  = DMG_hệ · (1 − Giảm giáp) · CritMult(nếu crit) · Rand(0.95, 1.05)
```

Posture damage: `PDMG = DMG_gốc · k_posture(vũ khí)` — đòn nặng k=1.5, đòn nhẹ k=0.5, parry thành công k=3.

## 14.5. Tỷ lệ Trỗi Dậy (Arise)

```
ΔL      = L − La
P_base  = 0.35
P       = clamp( P_base + 0.03·ΔL + 0.15·(UyQuyền / YêuCầu(cấp bóng)) − γ(cấp bóng) − 0.10·lần_thử_thứ , 0.05 , 0.95 )
```

| Cấp bóng mục tiêu | γ | Yêu Cầu Uy Quyền |
|---|---|---|
| Thường/Tinh Nhuệ | 0.00 | 10 |
| Kỵ Sĩ/Tinh Kỵ | 0.10 | 40 |
| Chỉ Huy/Tướng Quân | 0.25 | 100 |
| Nguyên Soái | 0.40 | 250 |
| Boss (lần giết đầu) | γ riêng − 0.20 (bonus) | theo cấp tương đương |

## 14.6. Sức chứa, triệu hồi & tái sinh quân đoàn (hoàn thiện v2.0)

```
Sở hữu tối đa   = 10 + ⌊INT·0.8⌋ + UyQuyền·0.5 + Bonus_hạng
                 Bonus_hạng: E:+0 · D:+5 · C:+10 · B:+20 · A:+35 · S:+60 · QG:+100 · VG:+200
Triệu hồi đồng thờI = clamp( 2 + ⌊L/10⌋ + Bonus_triệu_hồi , 2 , 40 )
                 Bonus_triệu_hồi: E:+0 · D:+1 · C:+2 · B:+4 · A:+7 · S:+12 · QG:+18 · VG:+26
Mana duy trì   = Σ(ChiPhí_duy_trì(cấp i)) / giây ; hết mana → bóng tan về (không chết)
```

**Tái sinh bóng (Quân Đoàn Bất Tử, tài liệu 04.7):**

```
Chi phí tái triệu hồi TỨC THờI = ManaCơBản(cấp) · (1 + 0.5·n)     n = số lần tái triệu hồi cùng bóng trong 60s (trần n=3 → ×2.5)
   ManaCơBản: Thường 15 · Tinh Nhuệ 25 · Kỵ Sĩ 45 · Tinh Kỵ 70 · Chỉ Huy 110 · Tướng Quân 170 · Nguyên Soái 260 · Đại Nguyên Soái 400
Tái sinh CHẬM (miễn phí)       = Thường 60s · Tinh Nhuệ 90s · Kỵ Sĩ 150s · Tinh Kỵ 240s · Chỉ Huy 420s · Tướng Quân 600s · Nguyên Soái 900s
   Keystone "Đạo Quân Bất Tử": chia 3 thờI gian hồi chậm; sát thương bóng −15%.
Bóng tái triệu hồi tức thờI trở lại với 60% HP.
```

## 14.7. TTK mục tiêu → HP quái thiết kế

```
HP_quái = DPS_chuẩn(PB_yêu_cầu) · TTK_mục_tiêu(loại)
```

với `DPS_chuẩn(PB) = 8 · PB^1.1` (hiệu chỉnh bằng simulation 18.4). Bảng TTK: tài liệu 13.5.

## 14.8. Điều tiết kinh tế

```
Giá_mua_lặp(n)   = Giá_gốc · (1 + 0.5·n)            (n = số lần mua cùng mặt hàng trong ngày)
Phí_reforge      = Phí_cơ_bản(hạng) · (1 + 0.15·số_lần_reforge_món_đó)
Thuế_tài_sản     : phí rank-up = 1000 · (chỉ_số_hạng)^2 vàng   (E=1 … S=6)
Loot_diminish(k) = Loot_gốc · max(0.25, 1 − 0.25·(k−3))  (k = lần farm cùng loại gate/ngày, k>3)
```

## 14.9. Power Budget (PB)

```
PB = (STR_e + AGI_e + VIT_e + INT_e + PER_e) · 1.0          (chỉ số hiệu dụng)
   + Σ(Điểm_kỹ_năng · Trọng_số_kỹ_năng)                      (keystone trọng số cao)
   + Σ(GearScore)                                            (theo rarity + affix)
   + min(0.6, LegionRatio) · PB · Trạng_thái_quân_đoàn      (quân đoàn, trần 60%)
```

`PB_yêu_cầu` của nội dung được gán tay theo hạng: E=20 · D=45 · C=90 · B=180 · A=350 · S=700 · QG=1200 · VG=2000+ (hiệu chỉnh qua simulation).

## 14.10. Prestige (Vượt Ngôi)

```
Bonus_tổng(P) = 1 + 0.10 · ln(1 + P)        (log-scale: P=10 → +24%; P=100 → +46%)
```

Không cộng dồn tuyến tính bất kỳ chỉ số nào khác ở prestige ngoài Bonus_tổng — chống power creep mất kiểm soát.

## 14.11. Adaptive Difficulty

```
Hệ_số_thích_ứng = clamp( 1 + 0.10·tanh( (Hiệu_suất_kỳ_vọng − Hiệu_suất_thực) / σ ) , 0.90 , 1.10 )
```

Áp dụng cho: tốc độ telegraph, tần suất quái phụ. Không áp dụng cho HP/sát thương nền. Cửa sổ đo: 20 trận gần nhất, cập nhật mỗi 5 trận.

## 14.12. Thể Lực (Fatigue) — mới v2.0 (tài liệu 05.3)

```
THANG: 0 → 100 (Kiệt Sức)
TĂNG (chỉ trong/gần combat):
  · Kỹ năng bộc phá (dash/burst/tốc biến/execute/gầm): +3 mỗi lần
  · Giao tranh liên tục: +1 mỗi 60 giây
  · Nhận đòn nặng (≥ 25% HP): +2
  · Arise giữa combat: +2 mỗi lần thử
GIẢM:
  · Ngoài combat ở Tổ Ấm/lửa trại: −2 mỗi 10 giây
  · Ngủ (giường vanilla): về 0
  · Bình Thể Lực: −30 · Nước Thánh: −60 (giá theo bậc mua, 14.8)
  · Level Up: về 0 (kèm HP/Mana 100%)
NGƯỠNG:
  · 60–84  Mệt:      −10% hồi Mana, −5% tốc độ đánh
  · 85–99  Rã RờI:   −25% hồi Mana, −10% sát thương, kỹ năng bộc phá tốn +50% Focus
  · 100    Kiệt Sức: Slowness I, khóa kỹ năng bộc phá, bóng Tán Vỡ chỉ hồi chậm
```

Ràng buộc thiết kế kiểm chứng bằng simulation: một "ngày săn" trung bình (2–3 gate + boss) sinh ~55–75 Fatigue → ngườichơi *chọn* giữa về ngủ sớm hay mua bình đẩy tiếp. Không bao giờ tăng ngoài combat.

## 14.13. Tinh Thần địch (Morale) — mới v2.0 (tài liệu 07.7)

```
TinhThần_đội (0–100), khởi tạo 100, cập nhật mỗi giây bởi Squad Brain:
  Δ = −25·(tỷ_lệ_quân_đã_mất) 
      −15 nếu chỉ huy/healer đội đã chết
      −20·(chênh_hạng_ngườichơi − hạng_quái, nếu > 0, trần −40)   (Uy Áp Chúa Tể)
      −10 mỗi lần trúng Gầm Kinh Hồn / hiệu ứng fear
      +5 mỗi elite còn sống · +10 nếu boss cùng khu vực còn sống
      · Hệ số tính cách: Lãnh Đạm ×0 (miễn) · Nhút Nhát ×1.5 · Cuồng Chiến ×0.6
NGƯỠNG: ≥60 Quyết Chiến · 30–59 Dao Động · 10–29 Hoảng Sợ · <10 Vỡ Trận
Không áp dụng Uy Áp cho: boss, mini-boss, elite, nội dung đúng hạng ngườichơi, Construct, Undead (miễn fear).
```

## 14.14. Kính Trọng (Respect) — mới v2.0 (tài liệu 10.2.3)

```
KínhTrọng(npc) = clamp(
    10 · (chỉ_số_hạng_ngườichơi − 1)                    (E=0 … VG=7)
  + Σ(điểm_việc_làm_được_chứng_kiến · phân_rã_thờI_gian)  (dẹp Break +30, giết boss gần làng +20, tặng quà +5; phân rã −10%/tuần game)
  + Σ(điểm_tin_đồn · 0.5)                                 (lan truyền chậm, phóng đại ±20%)
  + Điểm_tương_tác_riêng(npc)                             (trade, quest riêng)
, −50 , 200 )
BẬC:  ≤ −10 Khinh Thường · −9…29 Thờ Ơ · 30–79 Tôn Trọng · 80–149 Kính Nể · ≥150 Sùng Kính
Hiệu ứng giá: Khinh Thường ×1.5 · Thờ Ơ ×1.1 · Tôn Trọng ×1.0 · Kính Nể ×0.85 · Sùng Kính ×0.7
```

## 14.15. Thuê Hunter (Hire) — mới v2.0 (tài liệu 10.2.5)

```
Phí thuê/1 gate = 50 · (chỉ_số_hạng_hunter)^2 · (1 + 0.5 nếu hunter cùng hạng cao nhất có thể thuê)
   chỉ_số_hạng: F=0.5 · E=1 · D=2 · C=3 · B=4 · A=5 (không thuê được S/S★)
Điều kiện: hạng_hunter ≤ hạng_ngườichơi − 1
Chia loot: hunter nhận 15% + 5%·chỉ_số_hạng_hunter giá trị loot rờI (không lấy vật phẩm định danh, không lấy xác Arise)
Giới hạn: không vào Red Gate / Vực Tháp / nội dung vượt hạng họ.
```

## 14.16. Giá bán "Rác ra Vàng" — mới v2.0 (tài liệu 12.2)

```
Giá_bán(item, k) = Giá_tầng(item) · max(0.2, 1 − 0.15·(k − 4)) · Bonus_ngày(item)
   k = số stack đã bán cùng tầng trong ngày game (diminishing sau stack thứ 4)
   Bonus_ngày = ×1.5 nếu item thuộc "danh mục thu mua" xoay vòng của ngày hôm đó
TỶ LỆ MUA:BÁN mặc định = 4 : 1
TẦNG GIÁ (quy đổi /stack 64, hiệu chỉnh bằng simulation):
   Rác (thịt thối, xương, dây, đá cuội, đất):        6 vàng
   Phổ Thông (da, len, lông, nông sản, gỗ):         12 vàng
   Quý (sắt, vàng vanilla, redstone, lapis):        40 vàng
   Rất Quý (kim cương, netherite scrap, ngọc ender): 160 vàng
   Loot gate theo rarity: Thường 8 · Tốt 20 · Hiếm 60 · Sử Thi 200 (Huyền Thoại/Thần Thoại: không bán)
MỤC TIÊU KIỂM CHỨNG (simulation 18.3): thu nhập từ bán rác ≤ 25% tổng thu nhập Vàng của ngườichơi chủ động.
```

---

## Phụ lục: quy trình thay đổi công thức

1. Mở issue balance kèm lý do + dự đoán ảnh hưởng.
2. Chạy simulation trước/sau (18.4), đính kèm kết quả.
3. Review bởi Balance Designer + một designer khác hệ thống bị ảnh hưởng.
4. Ghi vào changelog thiết kế; cập nhật bảng mẫu trong file này nếu cần.
