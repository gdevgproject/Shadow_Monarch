# UMBRA Compatibility Smoke Checklist

Tài liệu này cung cấp ma trận kiểm thử tương thích thủ công (manual compatibility check) cho UMBRA mod với các mod hiệu năng và đồ họa phổ biến trong hệ sinh thái Fabric.

## 1. Môi trường kiểm thử chuẩn (Baseline Environment)

| Thành phần | Phiên bản yêu cầu | Ghi chú |
|---|---|---|
| Minecraft | `26.2` | Phiên bản Java Edition ổn định |
| Fabric Loader | `0.19.3` | Baseline loader |
| Fabric API | `0.154.2+26.2` | Baseline Fabric API |
| Java Runtime | `25` | Yêu cầu Java 25+ |

---

## 2. Ma trận tương thích hiệu năng & đồ họa (Performance & Visual Matrix)

Thực hiện các bước kiểm tra thủ công với các tổ hợp mod sau:

### Preset A: Vanilla thuần (Chỉ Fabric Loader + Fabric API + UMBRA)
- `[ ]` Game khởi động thành công tới màn hình Title Screen.
- `[ ]` Tạo world mới chế độ Creative Flat.
- `[ ]` Mở debug HUD (Overlay) và kiểm tra các thông số TPS/MSPT hiển thị đầy đủ và không bị che khuất.
- `[ ]` Đóng/mở game, kiểm tra save file được tạo thành công dưới thư mục `saves`.

### Preset B: Sodium (Tăng hiệu năng)
- `[ ]` Thêm Sodium vào thư mục `mods`.
- `[ ]` Khởi động game thành công.
- `[ ]` Load world test hiện có, mở debug HUD.
- `[ ]` Thay đổi cấu hình Video Settings trong Sodium (Low -> High preset), kiểm tra không bị giật lag hay crash.

### Preset C: Sodium + Iris (Shader Pack)
- `[ ]` Thêm Iris và một Shader Pack nhẹ (ví dụ: Complementary Reimagined hoặc Sildurs Vibrant).
- `[ ]` Khởi động game, kích hoạt Shader Pack.
- `[ ]` Kiểm tra font chữ, màu sắc và độ mờ của debug HUD khi render dưới hiệu ứng shader (không bị nhấp nháy, không bị lỗi tương phản).
- `[ ]` Kiểm tra log file không xuất hiện lỗi OpenGL/Blaze3D.

---

## 3. Quy trình quét xung đột phím (Keybind Conflict Scan)

Trước khi phát hành, thực hiện quét xung đột phím trong game:
1. Vào **Options** -> **Controls** -> **Key Binds**.
2. Tìm các phím tắt của UMBRA (ví dụ: các phím kích hoạt Stance, Dodge, gọi quân đoàn bóng).
3. Đảm bảo phím mặc định của UMBRA không bị gán trùng màu đỏ với các phím vanilla di chuyển/hành động quan trọng (WASD, Space, Shift, E, Q).
4. Thực hiện remap phím sang phím bất kỳ và kiểm tra xem tính năng của UMBRA có nhận đúng phím mới hay không.

---

## 4. Dedicated Server Smoke Test

Kiểm tra tính độc lập của logic Server:
1. Chạy server phát triển thông qua lệnh:
   ```powershell
   .\gradlew.bat runServer
   ```
2. Kết nối bằng client Minecraft, chạy thử các lệnh kiểm tra và đảm bảo Server xử lý đúng logic mà không bị crash do cố gắng gọi các class Client-only (đặc biệt là HUD/VFX classes).
