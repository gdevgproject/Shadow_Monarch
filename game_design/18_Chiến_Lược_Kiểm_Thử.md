# 18 — CHIẾN LƯỢC KIỂM THỬ (TESTING STRATEGY)

> **Chủ sở hữu:** QA Lead
> **Triết lý:** Một mod sống nhiều năm không thể test tay mãi. Tự động hóa *mọi thứ đo được*, để con người test *mọi thứ cảm được*.

---

## 1. Kim tự tháp kiểm thử

```
          ▲  Playtest cảm xúc (con người)
         ╱ ╲   — combat feel, attachment, flow
        ╱   ╲
       ╱ Tích ╲  Kịch bản end-to-end (bot trong game)
      ╱  hợp   ╲  — clear một gate, hoàn thành Job Change
     ╱──────────╲
    ║ Simulation║  Cân bằng headless (không render)
    ║  10k trận ║  — TTK, PB, tỷ lệ Arise, economy drift
    ║────────────║
    ║ Unit test  ║  — công thức 14.x, migration, codec
    ╚════════════╝
```

## 2. Tầng 1 — Unit test

- **Công thức:** mọi công thức tài liệu 14 có test vector (đầu vào → đầu ra kỳ vọng), kể cả biên (clamp, trần, diminishing returns).
- **Data:** mọi JSON trong repo qua schema validation CI; codec từ chối đúng file lỗi.
- **Migration:** mở save mẫu của mọi phiên bản hỗ trợ → nâng cấp → kiểm tra dữ liệu bảo toàn (đặc biệt quân đoàn).
- **Dodge M1-05:** vector AGI kiểm tra i-frame cap, Focus/Fatigue cost và hồi; packet test từ chối hướng không hợp lệ, spectator/vehicle/Fatigue 100/Focus thiếu; timing test phân biệt normal và Né Chuẩn Xác; migration v3→v4 giữ state cũ và khởi tạo resource đúng.

## 3. Tầng 2 — Simulation cân bằng (headless)

- Mô phỏng combat không render: build chuẩn theo PB đấu với mọi gate/boss → đo **TTK thực tế vs mục tiêu 13.5**, tỷ lệ thắng kỳ vọng.
- Simulation economy: 10.000 "ngày chơi ảo" → đo tích lũy 4 loại tiền, phát hiện lạm phát hoặc bí tắc (mục tiêu faucet/sink ≈ 0.85).
- Simulation Arise: phân phối xác suất theo level — đảm bảo không có "vùng chết" (tỷ lệ 5% kéo dài hàng chục level).
- Chạy trong CI mỗi lần đổi công thức/bảng số; kết quả đính kèm PR.

## 4. Tầng 3 — Kịch bản trong game (bot)

Dùng game-test framework của nền tảng: bot thực hiện kịch bản end-to-end trong world test:

1. Thức Tỉnh → hoàn thành nhiệm vụ rèn luyện → tự nhận Khế Ước Rèn Luyện → chọn thử thách → sống sót Vùng Thử Luyện.
2. Vào gate hạng D → clear **mọi objective** → rời Gate → Gate đóng → Tàn Tích tồn tại.
3. Job Change hoàn chỉnh → Arise thành công một bóng → bóng tuân lệnh cơ bản.
4. Dungeon Break kích hoạt → dọn → Field Dungeon không tồn tại; bỏ mặc → Field Dungeon hình thành.
5. Save/reload giữa trận boss → trạng thái khôi phục đúng.

## 5. Tầng 4 — Playtest cảm xúc (con người)

- **Nhịp:** mỗi milestone 2 đợt, mỗi đợt 5–8 người (hồ sơ khác nhau: vanilla player, ARPG player, builder).
- **Phương pháp:** chơi tự do 2 giờ → phỏng vấn theo thước đo tài liệu 01.6 (kể tên 3 bóng? chủ động tìm trận khó? vẫn đào đá/xây nhà?).
- **Telemetry nội bộ (offline, local-only):** tỷ lệ chọn build, nơi chết nhiều, quest bỏ dở — dữ liệu lưu local của tester, không gửi đâu cả.

## 6. Hiệu năng & regression

- Benchmark scene chuẩn mỗi build (tài liệu 17.3) — chậm hơn baseline >5% = chặn merge.
- Soak test: world chạy 72 giờ ảo với event xoay vòng → kiểm tra rò bộ nhớ, entity sót, save phình.

## 7. Tiêu chí "Done" của một tính năng

- [ ] Unit/simulation liên quan xanh.
- [ ] Kịch bản bot (nếu thuộc 5 kịch bản lõi) xanh.
- [ ] Benchmark không regression.
- [ ] Đã có ít nhất một người *không phải tác giả* chơi thử và ghi nhận cảm xúc đúng mục tiêu.
- [ ] Tài liệu chuyên môn tương ứng đã cập nhật.

## 8. Rủi ro & Quyết định vận hành

1. **Simulation lệch thực tế (bot không né đòn như người)?** → Simulation đo *sàn* cân bằng; playtest đo *trần*. Hai tầng không thay thế nhau.
2. **Chi phí duy trì bot khi MC cập nhật?** → Kịch bản bot viết trên lớp hành động trừu tượng ("đi tới", "đánh"), không phụ thuộc API cụ thể.
3. **Playtest cảm xúc khó tuyển người?** → Duy trì cộng đồng tester nhỏ, thưởng bằng tên trong credits + quyền xem roadmap sớm.

---

## 9. Bổ sung v4.0 — ma trận regression bắt buộc

Thêm test vector cho ba lần Arise (xác suất tăng, boss lần 3 = 100%, Hiệp Sĩ Huyết Sắt `[0,0,1]`), timer linh hồn trong/ngoài Gate, Gate không đóng trước objective, và trần hai Gate mỗi vùng. Simulation phải chạy commander/solo/hybrid ở từng rank F→S+ và từng Stratum để chặn power creep quân đoàn.

Kịch bản bot mới: gọi/thu hồi bóng không trừ mana theo giây; đặt garnison hộ tống Em/Mẹ; quay lại Thế Giới Gốc sau tầng song song không đổi block; AI boss bảo vệ healer/boss mà vẫn có counterplay. **Từ P7+**, dungeon nước thêm đường thở/thoát vào bộ regression. Playtest cảm xúc thêm câu hỏi: người chơi có biết vì sao điểm Tiềm Năng không undo không, có thấy boss capture công bằng không, có dám quay lại thế giới cũ để tận hưởng sức mạnh không.

## 10. Bổ sung v4.0 — Gate/compatibility/content gate không được bỏ qua

**Test trạng thái Gate (unit + integration):**

| Tình huống | Kết quả bắt buộc |
|---|---|
| Hạ boss nhưng còn lõi/cứu hộ | `IN_PROGRESS`; rời Gate → `OPEN`, không `CLOSED` |
| Clear hết objective, còn đứng trong Gate | `CLEARED_AWAITING_EXIT`; loot và Arise vẫn dùng được |
| Clear hết rồi rời Gate | `CLOSED`, sinh Tàn Tích đúng một lần, cleanup pool đúng một lần |
| Rời dở dang, reload world, quay lại | objective/quái đã chết/loot/Soul Echo còn đúng; boss sống reset đầy HP/pha 1 |
| Deadline lúc đang trong Gate | `BREAK_PENDING`, không teleport/xóa entity; khi Gate trống mới `BROKEN` |
| Soul Echo boss/elite/unique | không mất khi rời Gate chưa clear; không bị gộp bởi cap; UI warning trước exit-close |
| Hai Gate ở Stratum A và hai ở Stratum B | hợp lệ; gate thứ ba cùng Stratum bị queue, không spawn lén |

**Compatibility gate mỗi release:** tạo world mới và world đã migrate; chạy vanilla Fabric, Fabric API bắt buộc, Sodium, Sodium+Iris shader tối thiểu, và không có mod tùy chọn. Kiểm Gate/Domain/Arise/HUD ở preset thấp-cao; thêm dungeon nước vào ma trận từ P7+; kiểm attach/detach optional integrations; chạy dedicated-server smoke test Java 25. Không gắn nhãn “hỗ trợ” cho tổ hợp chưa chạy qua bảng này.

**Content greenlight:** mỗi faction, boss, skill, item set hoặc mount mới phải qua card 26.6 và test “khác biệt quyết định”: tester phải chỉ ra một lựa chọn combat/đội hình/đường đi mới mà nội dung tạo ra. Nếu chỉ đổi mesh, HP hoặc màu, nó không được tính là content combat mới và không vào milestone chính.
