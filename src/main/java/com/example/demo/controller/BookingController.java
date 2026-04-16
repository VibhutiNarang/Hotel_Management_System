package com.example.demo.controller;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    private final BookingRepository repo;

    public BookingController(BookingRepository repo) {
        this.repo = repo;
    }

    // SAVE BOOKING
    @PostMapping("/book")
    public String book(@RequestParam String name,
                       @RequestParam String room,
                       @RequestParam String hotel,
                       @RequestParam String mobile) {

        Booking b = new Booking();
        b.setName(name);
        b.setRoom(room);

        // optional fields if you add in model later
        // b.setHotel(hotel);
        // b.setMobile(mobile);

        repo.save(b);

        return """
        <html>
        <body style='font-family:sans-serif; text-align:center; padding:50px;'>
            <div style='background:white; padding:30px; border-radius:10px; display:inline-block; box-shadow:0 5px 15px rgba(0,0,0,0.1);'>
                <h2 style='color:green;'>✅ Booking Successful!</h2>
                <p>Thank you for choosing us.</p>
                <a href='/'>🏠 Home</a>
            </div>
        </body>
        </html>
        """;
    }

    // VIEW ALL BOOKINGS (ADMIN)
    @GetMapping("/allBookings")
    public String getAll() {

        StringBuilder data = new StringBuilder("""
        <html>
        <body style='font-family:sans-serif; padding:30px; background:#f4f6f9;'>
        <h2>📋 All Bookings</h2>
        """);

        for (Booking b : repo.findAll()) {
            data.append("<div style='background:white; padding:15px; margin:10px; border-radius:10px;'>")
                .append("<b>").append(b.getName()).append("</b>")
                .append("<br>Room: ").append(b.getRoom())
                .append("</div>");
        }

        data.append("<br><a href='/admin/dashboard'>⬅ Back</a>");
        data.append("</body></html>");

        return data.toString();
    }
}