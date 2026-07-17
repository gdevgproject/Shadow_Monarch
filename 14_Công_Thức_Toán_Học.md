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
| Kháng trạng thái nền | `Res_base = min(0.35, VIT_e·0.003)`; trang bị/node cộng thêm nhưng trần hiệu dụng 75% |
| Tự giải độc ngoài combat | `Detox = 1 stack/6s` nếu `VIT_e ≥ 40`; mỗi +30 VIT_e giảm 1s, trần 1 stack/2s |

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
ΔL       = L − La
P_1      = clamp(0.35 + 0.03·ΔL + 0.15·(UyQuyền / YêuCầu(cấp bóng)) − γ(cấp bóng), 0.05, 0.85)
P_2      = clamp(P_1 + 0.15, 0.20, 0.95)
P_3      = clamp(P_1 + 0.35, 0.40, 0.99)       (mục tiêu thường/elite)
P_3_boss = 1.00                                  (boss định danh đủ điều kiện)
P_hiệp_sĩ_huyết_sắt = [0.00, 0.00, 1.00]         (nghi lễ kịch bản hóa)
Mana_hiệp_sĩ_huyết_sắt = [0, 0, ManaArise_tier]  (hai phong ấn không đốt tài nguyên)
```

`P_n` là xác suất hiển thị **trước** lần thử n; các lần sau không bị phạt vì thất bại. Mục tiêu không-boss thất bại cả ba lần rơi Mảnh Bóng theo bảng loot; boss có Capture Contract dùng pity lần 3.

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
                 Bonus_hạng: F:+0 · E:+3 · D:+6 · C:+10 · B:+20 · A:+35 · S:+60 · S+:+80 · QG:+100 · VG:+200
Triệu hồi đồng thờI = clamp( 2 + ⌊L/10⌋ + Bonus_triệu_hồi , 2 , 40 )
                 Bonus_triệu_hồi: F:+0 · E:+0 · D:+1 · C:+2 · B:+4 · A:+7 · S:+12 · S+:+15 · QG:+18 · VG:+26
Mana triệu hồi = Σ(ManaGọi(cấp i) · HệSố_đội_hình) một lần khi gọi; không có Mana duy trì/giây.
                 ManaGọi: Thường 6 · Tinh Nhuệ 10 · Kỵ Sĩ 16 · Tinh Kỵ 24 · Chỉ Huy 34 · Tướng Quân 48 · Nguyên Soái 68
                 HệSố_đội_hình = 0.75 khi gọi preset đầy đủ, 1.00 khi gọi lẻ; thu hồi/garnison = 0 Mana.
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
Thuế_tài_sản     : phí rank-up = 1000 · (chỉ_số_hạng)^2 vàng   (F=1 · E=2 · D=3 · C=4 · B=5 · A=6 · S=7 · S+=8)
Loot_diminish(k) = Loot_gốc · max(0.25, 1 − 0.25·(k−3))  (k = lần farm cùng loại gate/ngày, k>3)
```

## 14.9. Power Budget (PB)

```
PB_self = (STR_e + AGI_e + VIT_e + INT_e + PER_e) · 1.0
        + Σ(Điểm_kỹ_năng · Trọng_số_kỹ_năng)
        + Σ(GearScore)                                       (keystone/rarity/affix đã chuẩn hóa)
PB_legion = Σ(ShadowScore_i · trạng_thái_i · role_fit_i)
PB = PB_self + min(0.60 · PB_self, PB_legion)                (đóng góp quân đoàn có trần, không tự tham chiếu)
```

`PB_yêu_cầu` của nội dung được gán tay theo hạng: F=12 · E=20 · D=45 · C=90 · B=180 · A=350 · S=700 · S+=950 · QG=1200 · VG=2000+ (hiệu chỉnh qua simulation).

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
    10 · chỉ_số_hạng_ngườichơi                           (F=0 · E=1 · D=2 · C=3 · B=4 · A=5 · S=6 · S+=7 · QG=8 · VG=9)
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

## 14.17. Thăng Giới & Thế Giới Song Song — mới v3.0

```
Tầng giới W = 0, 1, 2, ...; W=0 là Thế Giới Gốc.
PB_mục_tiêu(W) = PB_QG · (1 + 0.22·W)      (W ≤ 5), sau đó tăng 0.12/W.
Tăng số HP/DMG tối đa từ tầng = 35% ngân sách tầng.
Tối thiểu 65% ngân sách tầng phải là: mutator, đội hình, layout, AI, mục tiêu phụ hoặc reward collection.
Gate gần người chơi: tier_effective ∈ [tier_player − 2, tier_player + 2] với trọng số 0.85.
Gate vượt biên: trọng số 0.15, phải có cảnh báo/scout/đường rút.
Gate hoạt động tối đa mỗi World Stratum đang hoạt động = 2; Stratum không hoạt động tạm dừng timer/event.
```

## 14.18. Chỉ số combat có trần cứng đọc được — mới v3.0

```
Crit chance ≤ 60% · Crit damage ≤ 250% · Move speed bonus ≤ 25% · Dodge i-frame ≤ 0.40s.
Kháng hiệu dụng ≤ 75%; phần kháng vượt trần đổi thành kháng trạng thái/giảm thời lượng theo tỉ lệ 0.25.
Điểm chỉ số vượt soft-cap vẫn ghi nhận và tăng ảnh hưởng phụ; không bị mất điểm.
```

## 14.19. Focus, dodge và Perception Readout — mới v3.0

```
Focus_max = 100
Focus_hồi = 18 + min(12, 0.12·AGI_e) / giây ngoài animation khóa;
            giảm 35% khi đang nhận sát thương liên tục.
Chi phí: dodge 25 · parry 18 · dash skill theo dữ liệu (20–40).
Focus không hồi trong 0.35s sau khi tiêu; không bị Fatigue khóa, nhưng Fatigue 85+ tăng chi phí bộc phá theo 14.12.
PER tiers: 0 = phe + threat icon · 25 = level/role · 60 = weakness/status/telegraph · 100 = resist/trait/escort/true Gate clue.
```

Mọi giá trị Focus/Readout là config/datapack override được trong giới hạn cap; không skill hay stat nào được giảm dodge xuống 0 Focus hoặc vượt i-frame cap.

## 14.20. Monarch's Domain — mới v3.0

```
Duration = 12s · Cooldown = 90s · Radius = 12 block (tăng qua node/gear, trần 18).
Buff theo role trong vùng: Tank +15% posture/guard · DPS +12% damage · Support +20% heal/buff potency.
Tổng đóng góp quân đoàn vẫn bị cap PB_legion của 14.9; Domain không cộng chồng vô hạn, không biến boss thành mục tiêu đứng yên.
```

## 14.21. Gate lifecycle & Soul Echo — chuẩn v4.0

```text
objectives_complete = ∀ objective ∈ required_objectives: objective.state == COMPLETE
gate_close_allowed  = objectives_complete ∧ valid_participant_count_inside == 0 ∧ exit_event_after_clear

IN_PROGRESS + leave + ¬objectives_complete  → OPEN              (persist progress)
IN_PROGRESS + objectives_complete             → CLEARED_AWAITING_EXIT
CLEARED_AWAITING_EXIT + last_valid_exit       → CLOSED
OPEN/IN_PROGRESS + deadline + inside > 0      → BREAK_PENDING
BREAK_PENDING + inside == 0 + ¬gate_close_allowed → BROKEN
```

`exit_event_after_clear` là sự kiện rời instance, không phải timer và không phải boss chết. Quái thường đã hạ không respawn khi rời sớm; boss/elite **còn sống** hồi đầy và trở về pha 1 sau 10 giây Gate trống. Điều này giữ tiến độ khám phá nhưng chặn việc rút ra để bào HP.

```text
FreshCorpse_ttl_outside = 120s
FreshCorpse_ttl_inside  = 120s, then serialize → SoulEcho
SoulEcho_expiry         = Gate.CLOSED or Gate.BROKEN
SoulEcho_cap_per_gate   = 128 eligible targets (boss/elite/unique protected first)
```

`SoulEcho_cap_per_gate` là rào chắn hiệu năng, không phải tước phần thưởng: khi chạm trần, quái thường thấp nhất được gộp thành **Mảnh Bóng bảo lưu** theo tỷ lệ hiển thị trước; boss/elite/unique không bao giờ bị gộp. UI hiển thị số Echo và cảnh báo đóng Gate ở 25.

---

## Phụ lục: quy trình thay đổi công thức

1. Mở issue balance kèm lý do + dự đoán ảnh hưởng.
2. Chạy simulation trước/sau (18.4), đính kèm kết quả.
3. Review bởi Balance Designer + một designer khác hệ thống bị ảnh hưởng.
4. Ghi vào changelog thiết kế; cập nhật bảng mẫu trong file này nếu cần.
