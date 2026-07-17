# 22 — NGUYÊN TẮC PHÁT TRIỂN (DEVELOPMENT PRINCIPLES)

> **Chủ sở hữu:** Toàn team — đây là "hiến chương" của dự án.
> Khi tranh luận bế tắc, quay về đây. Thứ tự nguyên tắc là thứ tự ưu tiên.

---

## Nguyên tắc 1 — Cảm xúc trước, tính năng sau
Không thêm hệ thống vì "hay ho". Mỗi hệ thống phải chỉ ra nó khuếch đại cảm xúc nào trong Kim Tự Tháp (00.3) và bằng chứng nào cho thấy nó làm được. Không chỉ ra được → không làm.

## Nguyên tắc 2 — Minecraft trước, mod sau
Người chơi cài UMBRA để chơi *Minecraft hay hơn*, không phải để chơi một game khác mượn vỏ Minecraft. Khi một cơ chế xung đột với thói quen vanilla (đào, xây, farm, redstone), cơ chế đó phải nhường hoặc tự thiết kế lại cho hòa.

## Nguyên tắc 3 — Một cơ chế độc quyền thắng mướI cơ chế chung chung
Làm **Trỗi Dậy** sâu sắc nhất có thể trước khi nghĩ đến hệ thống thứ hai. Độ sâu của trái tim game quyết định độ cao của cả dự án.

## Nguyên tắc 4 — Tiến trình phải nhìn thấy được
Con số, thông báo, quân đoàn đứng sau lưng, thế giới đổi thái độ. Sức mạnh tăng mà không ai thấy — kể cả chính người chơi — thì không tồn tại.

## Nguyên tắc 5 — Thất bại là nội dung
Chết, phạt, Arise hỏng, bỏ lỡ gate — mọi thất bại phải *cho ra* một trải nghiệm hoặc một tài nguyên. Không có fail-state vô nghĩa.

## Nguyên tắc 6 — Quyết định thật, không đáp án đúng
Mọi lựa chọn (stat, build, bóng, formation, gate) phải có ít nhất một tình huống tỏa sáng và một tình huống trả giá. Phát hiện "đáp án duy nhất" = bug cân bằng.

## Nguyên tắc 7 — Tôn trọng thời gian người chơi
Không FOMO, không time-gate thời gian thực, không mất tiến trình vì lỗi kỹ thuật, checkpoint hợp lý, grind luôn sinh ra quyết định mới. Người chơi chơi 15 phút hay 5 giờ đều có tiến bộ.

## Nguyên tắc 8 — Hiệu năng là tính năng
Ngân sách tick được bảo vệ như bảo vệ gameplay. Tính năng đẹp nhưng vượt ngân sách = tính năng chưa xong.

## Nguyên tắc 9 — Data-driven, config tôn trọng
Nội dung sống bằng dữ liệu; hành vi xâm lấn tắt được bằng config; không hardcode thứ người khác muốn đổi.

## Nguyên tắc 10 — Fantasy coherence hơn cân bằng tuyệt đối
Người chơi tha thứ cho một build hơi mạnh nếu nó *đúng cảm giác*; họ không tha thứ cho thứ nhạt nhẽo dù cân bằng hoàn hảo. Cân bằng phục vụ trải nghiệm, không ngược lại — nhưng power creep luôn được *quản trị chủ động* (13.4).

## Nguyên tắc 11 — Giết con cưng của bạn
Tính năng yêu thích của một cá nhân vẫn bị cắt nếu dữ liệu playtest nói nó không hoạt động. Không ai sở hữu tính năng; cả team sở hữu trải nghiệm.

## Nguyên tắc 12 — Vòng lặp có điểm tốt nghiệp
UMBRA có *cái kết* (đánh bại Kiến Trúc Sư) và sau đó là *tự do có chủ đích* (Tháp Vô Định, collection, New Game+). Ta thiết kế để người chơi **muốn** ở lại — không thiết kế để họ **không rời được**. Đó là ranh giới giữa thỏa mãn và bóc lột, và dự án này đứng ở phía nào.

---

### Quy trình áp dụng
- Review thiết kế mới: đối chiếu 12 nguyên tắc theo thứ tự — vi phạm nguyên tắc cao hơn không được cứu bởi nguyên tắc thấp hơn.
- Nguyên tắc có thể *sửa*, bằng đề xuất ADR (16.6) được cả team thông qua — không bao giờ vi phạm âm thầm.
