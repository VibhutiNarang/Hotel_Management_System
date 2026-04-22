package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoomRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Random;

@Controller
public class LoginController {

    private final UserRepository repo;
    private final RoomRepository roomRepo;
    private final Random random = new Random();

    public LoginController(UserRepository repo, RoomRepository roomRepo) {
        this.repo = repo;
        this.roomRepo = roomRepo;
    }

    private static final String STYLE = """
            <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background: linear-gradient(135deg, #fff0f5, #ffe6f0);
                margin: 0;
            }
            .container {
                background: white;
                padding: 30px;
                border-radius: 15px;
                width: 90%;
                max-width: 1000px;
                margin: 40px auto;
                text-align:center;
            }
            .cards {
                display:flex;
                gap:20px;
                flex-wrap:wrap;
                justify-content:center;
            }
            .card {
                padding:15px;
                border-radius:10px;
                background: white;
                width:250px;
                box-shadow:0 5px 15px rgba(0,0,0,0.1);
            }
            .card img {
                width:100%;
                border-radius:10px;
            }
            .btn {
                padding:10px;
                margin:5px;
                border-radius:8px;
                color:white;
                text-decoration:none;
                display:inline-block;
            }
            .btn-primary { background:#6f84f1; }
            .btn-success { background:#28a745; }
            .btn-danger { background:red; }
            .btn-dark { background:black; }
            .back-btn {
                margin-top:20px;
                display:inline-block;
            }
            </style>
            """;

    // ===================== HOME =====================
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "<html><head>" + STYLE + "</head><body>" +

                "<div class='container'>" +
                "<h1>🏨 Multi-Hotel Management System</h1>" +

                "<div class='cards'>" +

                hotelCard("Radisson",
                        "https://images.trvl-media.com/lodging/2000000/1530000/1528800/1528788/6f594bec.jpg")
                +
                hotelCard("Taj",
                        "https://images.trvl-media.com/lodging/20000000/19460000/19458500/19458493/8cf0d468.jpg")
                +
                hotelCard("Lalit", "https://www.thelalit.com/wp-content/uploads/2024/11/pool-mumbai-632x421.jpg") +

                "</div></div></body></html>";
    }

    private String hotelCard(String name, String img) {
        return "<div class='card'>" +
                "<img src='" + img + "'>" +
                "<h3>Hotel " + name + "</h3>" +
                "<a class='btn btn-success' href='/customer/dashboard?hotel=" + name + "'>Guest</a>" +
                "<a class='btn btn-primary' href='/login?hotel=" + name + "'>Admin</a>" +
                "</div>";
    }

    // ===================== CUSTOMER =====================
    @GetMapping("/customer/dashboard")
    @ResponseBody
    public String customer(@RequestParam String hotel) {

        List<Room> rooms = roomRepo.findByHotelAndIsAvailableTrue(hotel);

        String html = "<html><head>" + STYLE + "</head><body><div class='container'>";
        html += "<h1>Welcome to " + hotel + "</h1><div class='cards'>";

        for (Room r : rooms) {

            String img;

            if (r.getType().equalsIgnoreCase("Suite")) {
                img = "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800";
            } else if (r.getType().equalsIgnoreCase("Presidential")) {
                img = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=800";
            } else if (r.getType().equalsIgnoreCase("Deluxe")) {
                img = "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=800";
            } else {
                img = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800";
            }

            html += "<div class='card'>" +
                    "<img src='" + img + "'>" +
                    "<h3>" + r.getType() + "</h3>" +
                    "<p>₹" + r.getPrice() + "</p>" +

                    "<a class='btn btn-primary' href='/customer/facilities'>Facilities</a>" +
                    "<a class='btn btn-success' href='/customer/book-form?id=" + r.getId() +
                    "&hotel=" + hotel + "'>Book</a>" +

                    "<a class='btn btn-dark' href='/customer/bill?id=" + r.getId() + "'>Bill</a>" +
                    "<a class='btn btn-primary' href='/customer/checkin?id=" + r.getId() + "'>Check-In</a>" +
                    "<a class='btn btn-danger' href='/customer/checkout?id=" + r.getId() + "'>Check-Out</a>" +
                    "<a class='btn btn-success' href='/customer/feedback?id=" + r.getId() + "'>Feedback</a>" +

                    "</div>";
        }

        html += "</div><a class='btn btn-dark back-btn' href='/'>Back</a>";
        return html + "</div></body></html>";
    }

    // ===================== BOOK =====================
    @GetMapping("/customer/book-form")
    @ResponseBody
    public String bookForm(@RequestParam Long id, @RequestParam String hotel) {

        return "<html><head>" + STYLE + "</head><body><div class='container'>" +

                "<h2>Guest Check-In</h2>" +

                "<form method='post' action='/customer/book'>" +

                "<input type='hidden' name='id' value='" + id + "'>" +
                "<input type='hidden' name='hotel' value='" + hotel + "'>" +

                "<input name='name' placeholder='Full Name'><br>" +
                "<input name='phone' placeholder='Phone Number'><br>" +
                "<input name='aadhaar' placeholder='Aadhaar Number'><br>" +

                "<select name='roomType'>" +
                "<option>Suite</option>" +
                "<option>Deluxe</option>" +
                "</select><br>" +

                "<label>Breakfast:</label>" +
                "<select name='breakfast'>" +
                "<option value='yes'>Yes</option>" +
                "<option value='no'>No</option>" +
                "</select><br><br>" +

                "<button class='btn btn-success'>Confirm Booking</button>" +
                "</form>" +

                "<a class='btn btn-dark back-btn' href='/customer/dashboard?hotel=" + hotel + "'>Back</a>" +

                "</div></body></html>";
    }

    @PostMapping("/customer/book")
    public RedirectView book(@RequestParam Long id,
            @RequestParam String hotel,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String aadhaar,
            @RequestParam String roomType,
            @RequestParam String breakfast) {

        Room r = roomRepo.findById(id).orElse(null);

        if (r != null && r.isAvailable()) {
            r.setAvailable(false);
            r.setCustomerName(name);
            r.setPhone(phone);
            r.setAadhaar(aadhaar);

            r.setBreakfast("yes".equalsIgnoreCase(breakfast));
            r.setPaymentStatus("PENDING");
            r.setCheckedIn(false);
            r.setCheckedOut(false);

            roomRepo.save(r);
        }

        return new RedirectView("/customer/dashboard?hotel=" + hotel);
    }

    // ===================== BILL =====================
    @GetMapping("/customer/bill")
    @ResponseBody
    public String viewBill(@RequestParam Long id) {

        Room r = roomRepo.findById(id).orElse(null);
        if (r == null)
            return "No Bill Found";

        double total = r.getPrice();
        if (r.isBreakfast())
            total += 500;

        return "<html><head>" + STYLE + "</head><body><div class='container'>" +
                "<h2>Bill Details</h2>" +
                "<p>Name: " + r.getCustomerName() + "</p>" +
                "<p>Room: " + r.getType() + "</p>" +
                "<p>Base Price: ₹" + r.getPrice() + "</p>" +
                "<p>Breakfast: " + (r.isBreakfast() ? "₹500" : "Not Included") + "</p>" +
                "<h3>Total: ₹" + total + "</h3>" +
                "<p>Status: " + r.getPaymentStatus() + "</p>" +

                ("PENDING".equalsIgnoreCase(r.getPaymentStatus())
                        ? "<a class='btn btn-success' href='/customer/pay?id=" + r.getId() + "'>Pay Now</a>"
                        : "")
                +

                "<br><a class='btn btn-dark back-btn' href='/'>Back</a>" +
                "</div></body></html>";
    }

    @GetMapping("/customer/pay")
    public RedirectView pay(@RequestParam Long id) {
        Room r = roomRepo.findById(id).orElse(null);
        if (r != null) {
            r.setPaymentStatus("PAID");
            roomRepo.save(r);
        }
        return new RedirectView("/customer/bill?id=" + id);
    }

    // ===================== CHECK-IN / CHECK-OUT =====================
    @GetMapping("/customer/checkin")
    public RedirectView checkin(@RequestParam Long id) {
        Room r = roomRepo.findById(id).orElse(null);
        if (r != null) {
            r.setCheckedIn(true);
            roomRepo.save(r);
            return new RedirectView("/customer/dashboard?hotel=" + r.getHotel());
        }
        return new RedirectView("/");
    }

    @GetMapping("/customer/checkout")
    public RedirectView customerCheckout(@RequestParam Long id) {
        Room r = roomRepo.findById(id).orElse(null);
        if (r != null) {
            r.setCheckedOut(true);
            r.setAvailable(true);

            // RESET DATA
            r.setCustomerName(null);
            r.setPhone(null);
            r.setAadhaar(null);
            r.setBreakfast(false);
            r.setPaymentStatus(null);
            r.setCheckedIn(false);

            roomRepo.save(r);
            return new RedirectView("/customer/dashboard?hotel=" + r.getHotel());
        }
        return new RedirectView("/");
    }

    // ===================== FEEDBACK =====================
    @GetMapping("/customer/feedback")
    @ResponseBody
    public String feedbackForm(@RequestParam Long id) {
        return "<html><head>" + STYLE + "</head><body><div class='container'>" +
                "<h2>Give Feedback</h2>" +
                "<form method='post' action='/customer/feedback'>" +
                "<input type='hidden' name='id' value='" + id + "'>" +
                "<textarea name='feedback' placeholder='Write here...'></textarea><br><br>" +
                "<button class='btn btn-success'>Submit</button>" +
                "</form></div></body></html>";
    }

    @PostMapping("/customer/feedback")
    public RedirectView saveFeedback(@RequestParam Long id,
            @RequestParam String feedback) {

        Room r = roomRepo.findById(id).orElse(null);
        if (r != null) {
            r.setFeedback(feedback);
            roomRepo.save(r);
        }

        return new RedirectView("/");
    }

    // ===================== FACILITIES =====================
    @GetMapping("/customer/facilities")
    @ResponseBody
    public String facilities() {
        return "<html><head>" + STYLE + "</head><body><div class='container'>" +

                "<h2>Hotel Facilities</h2>" +
                "<ul>" +
                "<li>🏊 Swimming Pool</li>" +
                "<li>🍽️ Restaurant</li>" +
                "<li>📶 Free WiFi</li>" +
                "<li>🅿️ Parking</li>" +
                "<li>💪 Gym</li>" +
                "</ul>" +

                "<a class='btn btn-dark back-btn' href='/'>Back</a>" +
                "</div></body></html>";
    }

    // ===================== LOGIN =====================
    @GetMapping("/login")
    @ResponseBody
    public String loginPage(@RequestParam String hotel) {
        return "<html><head>" + STYLE + "</head><body><div class='container'>" +
                "<h2>Admin Login - " + hotel + "</h2>" +

                "<form method='post' action='/login'>" +
                "<input type='hidden' name='hotel' value='" + hotel + "'>" +
                "<input name='username'><br>" +
                "<input type='password' name='password'><br>" +
                "<button class='btn btn-primary'>Login</button>" +
                "</form>" +

                "<a class='btn btn-dark back-btn' href='/'>Back</a>" +
                "</div></body></html>";
    }

    @PostMapping("/login")
    public Object login(@RequestParam String hotel,
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        User user = repo.findByUsernameAndPasswordAndHotel(username, password, hotel);

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            session.setAttribute("user", user);
            return new RedirectView("/admin/dashboard");
        }

        return "Invalid Login";
    }

    // ===================== ADMIN =====================
    @GetMapping("/admin/dashboard")
    @ResponseBody
    public String adminDash(HttpSession session) {

        User u = (User) session.getAttribute("user");
        if (u == null)
            return "Access Denied";

        List<Room> rooms = roomRepo.findByHotel(u.getHotel());

        String html = "<html><head>" + STYLE + "</head><body><div class='container'>";
        html += "<h1>" + u.getHotel() + " Admin</h1>";

        html += "<a class='btn btn-success' href='/admin/add-room-form'>Add Room</a>";
        html += "<a class='btn btn-dark' href='/logout'>Logout</a><br><br>";

        for (Room r : rooms) {

            String status = r.isAvailable() ? "Available ✅" : "Occupied ❌";

            html += "<p>" +
                    r.getRoomNumber() + " - " + r.getType() +
                    " (" + status + ")" +

                    (!r.isAvailable()
                            ? " <a class='btn btn-danger' href='/admin/check-out?id=" + r.getId() + "'>Check-out</a>"
                            : "")
                    +

                    "</p>";
        }

        return html + "</div></body></html>";
    }

    @GetMapping("/admin/check-out")
    public RedirectView checkout(@RequestParam Long id) {
        Room r = roomRepo.findById(id).orElse(null);
        if (r != null) {
            r.setAvailable(true);
            roomRepo.save(r);
        }
        return new RedirectView("/admin/dashboard");
    }

    // ===================== ADD ROOM =====================
    @GetMapping("/admin/add-room-form")
    @ResponseBody
    public String form() {
        return "<html><head>" + STYLE + "</head><body><div class='container'>" +
                "<form method='post' action='/admin/add-room'>" +
                "<input name='roomNumber' placeholder='Room No'><br>" +
                "<select name='type'>\r\n" + //
                "    <option>Suite</option>\r\n" + //
                "    <option>Deluxe</option>\r\n" + //
                "    <option>Presidential</option>\r\n" + //
                "</select><br>" +
                "<button class='btn btn-success'>Add</button>" +
                "</form>" +
                "<a class='btn btn-dark back-btn' href='/admin/dashboard'>Back</a>" +
                "</div></body></html>";
    }

    @PostMapping("/admin/add-room")
    public RedirectView addRoom(@RequestParam String roomNumber,
            @RequestParam String type,
            HttpSession session) {

        User u = (User) session.getAttribute("user");
        if (u == null)
            return new RedirectView("/");

        Room r = new Room();
        r.setRoomNumber(roomNumber);
        r.setType(type);
        r.setPrice(6000 + random.nextInt(8000));
        r.setAvailable(true);
        r.setHotel(u.getHotel());

        roomRepo.save(r);

        return new RedirectView("/admin/dashboard");
    }

    // ===================== LOGOUT =====================
    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }
}