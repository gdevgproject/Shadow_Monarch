# UMBRA — Quy ước cho mọi coding agent

## Mục tiêu làm việc

UMBRA là một Fabric **modular monolith**, không phải tập hợp tính năng được code độc lập. Mỗi thay đổi phải bảo toàn trải nghiệm Minecraft sandbox, server-authoritative, data-driven và ngân sách hiệu năng đã cam kết.

## Thứ bậc nguồn chân lý

Khi tài liệu mâu thuẫn, áp dụng đúng thứ tự trong `game_design/27_Ma_Trận_Truy_Vết_Yêu_Cầu_Và_Sẵn_Sàng_Code.md`:

1. `00_README.md`, phần quyết định chuẩn v3/v4.
2. `14_Công_Thức_Toán_Học.md`.
3. `15_Mô_Hình_Dữ_Liệu.md`.
4. `16_Kiến_Trúc_Kỹ_Thuật_TDD.md`.
5. Tài liệu gameplay của hệ thống liên quan.
6. Test, compatibility, roadmap và content process.

Không được chọn phương án “tiện code hơn” nếu nó khác nguồn chân lý. Nếu cần đổi quyết định cấp 1–4, dừng phần mở rộng, viết ADR và cập nhật tài liệu cùng thay đổi.

## Cách nhận và hoàn thành ticket

- Chỉ nhận một ticket có phạm vi rõ ràng trong `game_design/29_Backlog_M0_Đến_M2.md` hoặc backlog được duyệt sau này.
- Trước khi sửa: đọc các tài liệu nguồn được ticket dẫn chiếu và báo lại assumption/rủi ro có thể chặn ticket.
- Mỗi PR/commit ghi ticket ID, requirement ID (`Rxx` nếu có), test đã chạy và bằng chứng kiểm tra trong game.
- Một ticket chỉ thay đổi một hành vi quan sát được hoặc một hợp đồng kỹ thuật. Không gộp “tiện tay” gameplay khác.
- Không thay đổi schema save, công thức, transaction Gate hay packet mà không có migration/test tương ứng.
- Không đưa asset, tên, lore, moveset hoặc biểu đạt nhận diện của IP khác vào dự án. Áp dụng card của tài liệu 26.

## Ranh giới kỹ thuật

- Một JAR Fabric; bounded context là module logic, **không** là microservice.
- Context chỉ gọi xuống `umbra-core`; trao đổi ngang qua interface/service và event đã định nghĩa.
- Server quyết định state/gameplay. Client chỉ gửi intent hợp lệ và render state đồng bộ.
- `GateLifecycleService` là owner duy nhất của transition Gate. Không UI, mixin hay event listener nào tự đóng/dọn Gate.
- Không raw OpenGL, không hook render pipeline của Sodium/Iris, không thêm dependency bắt buộc ngoài Fabric API nếu chưa có ADR.

## Definition of Done tối thiểu

1. Build và test liên quan xanh.
2. Validator/migration/benchmark không regression nếu ticket chạm các vùng đó.
3. Có bước kiểm tra manual trong `runClient` hoặc GameTest khi hành vi nhìn thấy được.
4. Cập nhật tài liệu/schema/ADR khi hợp đồng thay đổi.
5. Không có thay đổi ngoài phạm vi ticket.

